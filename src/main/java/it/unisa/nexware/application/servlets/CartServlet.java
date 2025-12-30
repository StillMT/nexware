package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.application.utils.SessionMessage;
import it.unisa.nexware.storage.dao.CartDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(urlPatterns = {"/myNexware/cart/", "/myNexware/cart/removeProduct", "/myNexware/cart/addProduct"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession s = request.getSession();
        CompanyBean loggedCompany = (CompanyBean) s.getAttribute("company");
        int id;

        switch (request.getServletPath()) {
            case "/myNexware/cart/":
                SessionMessage sm = (SessionMessage) s.getAttribute("addingResult");
                if (sm == null || !sm.isValid())
                    s.removeAttribute("addingResult");

                request.setAttribute("products", ProductDAO.doGetCartedProducts(loggedCompany));
                request.getRequestDispatcher("/WEB-INF/forwards/viewCart.jsp").forward(request, response);
                break;

            case "/myNexware/cart/removeProduct":
                JSONObject result = new JSONObject();
                result.put("result", false);

                id = FieldValidator.idValidate(request.getParameter("p"));
                if (id > 0)
                    result.put("result", CartDAO.doRemoveProduct(id, loggedCompany));

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(result.toString());
                break;

            case "/myNexware/cart/addProduct":
                id = FieldValidator.idValidate(request.getParameter("p"));
                if  (id > 0)
                    s.setAttribute("addingResult", new SessionMessage(String.valueOf(CartDAO.doAddProduct(id, loggedCompany))));

                response.sendRedirect("/myNexware/cart/");
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
