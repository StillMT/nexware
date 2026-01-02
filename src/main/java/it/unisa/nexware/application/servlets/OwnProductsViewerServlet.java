package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.dto.OwnProductsDTO;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.application.facades.OwnProductsFacade;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.application.dto.SessionMessage;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/myNexware/products/")
public class OwnProductsViewerServlet extends HttpServlet {

    // Attributi
    @Inject
    OwnProductsFacade ownProductsFacade;
    boolean post  = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        LocalDate today = LocalDate.now().plusDays(1);

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
        if (searchQuery == null || searchQuery.isBlank())
            searchQuery = "";

        SessionMessage sm = (SessionMessage) s.getAttribute("queryProduct");
        if (sm != null)
            if (sm.isValid())
                searchQuery = sm.getMessage();
            else
                s.removeAttribute("queryProduct");

        OwnProductsDTO pData = ownProductsFacade.getProductsData((CompanyBean) s.getAttribute("company"), searchQuery, startDate, endDate, statusFilter);
        request.setAttribute("products", pData.getProducts());
        request.setAttribute("category-list", pData.getCategories());

        request.setAttribute("today",  today.toString());
        request.setAttribute("start-date",  startDate);
        request.setAttribute("end-date",   endDate);
        request.setAttribute("status-filter",  statusFilter);
        request.setAttribute("search-query",  searchQuery);
        request.getRequestDispatcher("/WEB-INF/forwards/viewOwnProducts.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        post = true;
        doGet(request, response);
    }
}
