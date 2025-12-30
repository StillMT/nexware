package it.unisa.nexware.application.servlets;

import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import it.unisa.nexware.application.beans.CategoryBean;
import it.unisa.nexware.application.beans.ProductBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/catalogue/")
public class CatalogueServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<CategoryBean> categories = CategoryDAO.doGetCatList();
        request.setAttribute("catList", categories);

        String categoryFilter = request.getParameter("category-filter");
        String searchQuery = request.getParameter("q");

        List<ProductBean> products;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            products = ProductDAO.doSearchByName(searchQuery);
            request.setAttribute("searchQuery", searchQuery);
        } else if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.equals("ALL")) {
            try {
                int catId = Integer.parseInt(categoryFilter);
                products = ProductDAO.doGetProductsByCategory(catId);
            } catch (NumberFormatException e) {
                products = ProductDAO.doGetCatalogueProducts();
                categoryFilter = "ALL";
            }
        } else {
            products = ProductDAO.doGetCatalogueProducts();
            categoryFilter = "ALL";
        }

        request.setAttribute("pList", products);
        request.setAttribute("categoryFilter", categoryFilter);

        request.getRequestDispatcher("/WEB-INF/forwards/viewCatalogue.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}