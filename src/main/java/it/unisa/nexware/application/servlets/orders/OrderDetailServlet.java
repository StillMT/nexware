package it.unisa.nexware.application.servlets.orders;

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
import java.util.List;

@WebServlet("/myNexware/orders/view/*")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/myNexware/orders/");
            return;
        }


        String orderNr = pathInfo.substring(1);


        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }



        OrderBean order = OrderDAO.getOrderByNumber(orderNr, company.getId());

        if (order == null) {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        List<OrderedProductBean> items = OrderedProductDAO.getByOrderId(order.getId());


        request.setAttribute("order", order);
        request.setAttribute("items", items);

        request.getRequestDispatcher("/WEB-INF/forwards/viewOrderDetails.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}