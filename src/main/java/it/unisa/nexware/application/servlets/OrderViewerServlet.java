package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.OrderDAO;
import it.unisa.nexware.storage.dao.OrderedProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        List<OrderBean> orders = OrderDAO.doGetOrdersByCompany(company, searchQuery, startDate, endDate, statusFilter);


        Map<Integer, BigDecimal> orderTotals = new HashMap<>();
        for (OrderBean o : orders) {
            List<OrderedProductBean> items = OrderedProductDAO.getByOrderId(o.getId());
            BigDecimal total = BigDecimal.ZERO;
            for (OrderedProductBean item : items) {
                if (item.getPrice() != null) total = total.add(item.getPrice());
            }
            orderTotals.put(o.getId(), total);
        }

        request.setAttribute("orders", orders);
        request.setAttribute("orderTotals", orderTotals);
        request.setAttribute("today", today.toString());
        request.setAttribute("start-date", startDate);
        request.setAttribute("end-date", endDate);
        request.setAttribute("status-filter", statusFilter);
        request.setAttribute("search-query", searchQuery);

        request.getRequestDispatcher("/myNexware/orders/viewOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}