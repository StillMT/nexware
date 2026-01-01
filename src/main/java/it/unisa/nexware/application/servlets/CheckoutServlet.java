package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.application.utils.SessionMessage;
import it.unisa.nexware.storage.dao.CartDAO;
import it.unisa.nexware.storage.dao.OrderDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/myNexware/cart/checkout/", "/myNexware/cart/checkout/proceedToPay/"})
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        CompanyBean company = (CompanyBean) s.getAttribute("company");

        switch(request.getServletPath()) {
            case "/myNexware/cart/checkout/":
                request.setAttribute("products", ProductDAO.doGetCheckoutProducts(company));
                request.getRequestDispatcher("/WEB-INF/forwards/viewCheckout.jsp").forward(request, response);
                break;

            case "/myNexware/cart/checkout/proceedToPay/":
                String cardOwner = request.getParameter("card-owner");
                if (!FieldValidator.cardOwnerValidate(cardOwner)) {
                    sendError(s, response, "INV_CARD_OW");
                    return;
                }

                String cardNum = request.getParameter("card-number");
                if (!FieldValidator.cardValidate(cardNum, request.getParameter("card-cvv"), request.getParameter("card-expiry"))) {
                    sendError(s, response, "INV_CARD");
                    return;
                }

                BigDecimal jspCalculatedTotal;
                try {
                    jspCalculatedTotal = new BigDecimal(request.getParameter("calculatedTotal"));
                } catch (NumberFormatException _) {
                    sendError(s, response, "INV_TOTAL");
                    return;
                }

                List<ProductBean> products = ProductDAO.doGetCheckoutProducts(company);
                if (products == null) {
                    sendError(s, response, "DB_ERR");
                    return;
                } else if (products.isEmpty()) {
                    response.sendRedirect("/myNexware/cart/");
                    return;
                }

                if (jspCalculatedTotal.compareTo(getTotal(products)) != 0) {
                    sendError(s, response, "INV_TOTAL");
                    return;
                }

                String card4nr = cardNum.substring(cardNum.length() - 4);
                if (OrderDAO.doCreateOrder(company, generateOrderNumber(company), card4nr, jspCalculatedTotal, products)) {
                    CartDAO.doRemoveCheckoutedProducts(company, products);
                    s.setAttribute("orderMessage", new SessionMessage("OK"));

                    request.setAttribute("cardOwner", cardOwner);
                    request.setAttribute("cardNr", card4nr);
                    request.getRequestDispatcher("/WEB-INF/forwards/paymentAnimation.jsp").forward(request, response);
                }
                else {
                    sendError(s, response, "DB_ERR");
                    return;
                }
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void sendError(HttpSession s, HttpServletResponse response, String message) throws IOException {
        s.setAttribute("checkoutError", new SessionMessage(message));
        response.sendRedirect("/myNexware/cart/checkout/");
    }

    private BigDecimal getTotal(List<ProductBean> products) {
        BigDecimal total = BigDecimal.ZERO;

        for (ProductBean product : products)
            total = total.add(product.getPrice());

        return total.add(new BigDecimal("3.99"));
    }

    private String generateOrderNumber(CompanyBean c) {
        long timestamp = System.currentTimeMillis() % 1000;
        int randomPart1 = (int) (Math.random() * 10000000);

        return String.format("%03d-%07d-%07d", timestamp, c.getId() % 10000000, randomPart1);
    }
}