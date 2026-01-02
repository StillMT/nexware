package it.unisa.nexware.application.servlets.orders;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/myNexware/orders/")
public class OrderViewerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LocalDate today = LocalDate.now();
        String startDate = request.getParameter("start-date");
        String endDate = request.getParameter("end-date");
        String statusFilter = request.getParameter("status-filter");
        String searchQuery = request.getParameter("search-query");

        if (searchQuery == null) searchQuery = "";


        if (!FieldValidator.dateValidate(startDate) || !FieldValidator.dateValidate(endDate) ||
                (statusFilter != null && !statusFilter.equals("ALL") && OrderStatus.fromString(statusFilter) == null)) {
            startDate = today.minusDays(90).toString();
            endDate = today.toString();
            statusFilter = "ALL";
        }

        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        List<OrderBean> orders = OrderDAO.doGetOrdersByCompany(company, searchQuery, startDate, endDate, statusFilter);


        request.setAttribute("orders", orders);
        request.setAttribute("today", today.toString());
        request.setAttribute("start-date", startDate);
        request.setAttribute("end-date", endDate);
        request.setAttribute("status-filter", statusFilter);
        request.setAttribute("search-query", searchQuery);

        request.getRequestDispatcher("/WEB-INF/forwards/viewOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String orderNr = request.getParameter("orderNr");
        String newStatusStr = request.getParameter("newStatus");


        if (orderNr != null && newStatusStr != null) {
            try {
                OrderStatus newStatus = OrderStatus.valueOf(newStatusStr);


                OrderDAO.updateOrderStatus(orderNr, newStatus);

            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }


            response.sendRedirect(request.getContextPath() + "/myNexware/orders/");
        } else {

            doGet(request, response);
        }
    }
}