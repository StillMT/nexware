package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CategoryBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<ProductBean> homeProducts = ProductDAO.doGetHomeProducts();
        List<CategoryBean> allCategories = CategoryDAO.doGetCatList();

        List<ProductBean> featuredProducts = new ArrayList<>();
        List<ProductBean> bannerProducts = new ArrayList<>();


        if (homeProducts != null && !homeProducts.isEmpty()) {
            int size = homeProducts.size();

            if (size >= 3) {
                featuredProducts = homeProducts.subList(0, 3);
            } else {
                featuredProducts = homeProducts;
            }


            if (size >= 4) {
                bannerProducts.add(homeProducts.get(3));
            }

            else if (size > 0) {
                bannerProducts.add(homeProducts.get(0));
            }
        }


        List<CategoryBean> homeCategories = new ArrayList<>();
        if(allCategories != null && !allCategories.isEmpty()) {
            List<CategoryBean> shuffledCategories = new ArrayList<>(allCategories);
            Collections.shuffle(shuffledCategories);
            homeCategories = shuffledCategories.stream()
                    .limit(4)
                    .collect(Collectors.toList());
        }


        request.setAttribute("bannerProducts", bannerProducts);
        request.setAttribute("featuredProducts", featuredProducts);
        request.setAttribute("categories", homeCategories);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/forwards/viewHome.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}