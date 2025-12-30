package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.storage.dao.OrderDAO;
import it.unisa.nexware.storage.dao.OrderedProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/myNexware/orders/view/*")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String pathInfo = request.getPathInfo(); // es: /12
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/myNexware/orders/");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        OrderBean order = OrderDAO.getOrderById(orderId, company.getId());
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        List<OrderedProductBean> items = OrderedProductDAO.getByOrderId(orderId);


        BigDecimal total = BigDecimal.ZERO;
        if (items != null) {
            for (OrderedProductBean item : items) {
                if (item.getPrice() != null) {
                    total = total.add(item.getPrice());
                }
            }
        }


        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.setAttribute("calculatedTotal", total);


        request.getRequestDispatcher("/myNexware/orders/viewOrderDetails.jsp")
                .forward(request, response);
    }
}