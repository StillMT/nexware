package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/myNexware/products/")
public class OwnProductViewerServlet extends HttpServlet {

    // Attributi
    boolean post  = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate today = LocalDate.now();

        String startDate = "";
        String endDate = "";
        String statusFilter = "";

        if (post) {
            startDate = request.getParameter("start-date");
            endDate = request.getParameter("end-date");
            statusFilter = request.getParameter("status-filter");
        }

        if (!post || !FieldValidator.dateValidate(startDate) || !FieldValidator.dateValidate(endDate) ||
                (!statusFilter.equals("ALL") && ProductStatus.fromString(statusFilter) == null)) {
            startDate = today.minusDays(90).toString();
            endDate = today.toString();
            statusFilter = "ALL";
        }

        String searchQuery = request.getParameter("search-query");
        if (searchQuery == null)
            searchQuery = "";

        CompanyBean cm = (CompanyBean) request.getSession().getAttribute("company");
        request.setAttribute("products", ProductDAO.doGetProductsByCompany(cm,
                searchQuery, startDate, endDate, statusFilter));
        request.setAttribute("category-list", CategoryDAO.doGetCatList());

        request.setAttribute("today",  today.toString());
        request.setAttribute("start-date",  startDate);
        request.setAttribute("end-date",   endDate);
        request.setAttribute("status-filter",  statusFilter);
        request.setAttribute("search-query",  searchQuery);
        request.getRequestDispatcher("/myNexware/products/viewProducts.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        post = true;
        doGet(request, response);
    }
}
