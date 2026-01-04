package it.unisa.nexware.application.servlets.admin;

import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(urlPatterns = {"/myNexware/admin/categories/", "/myNexware/admin/categories/editCat/"})
public class CategoryManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/myNexware/admin/categories/":
                request.setAttribute("catList", CategoryDAO.doGetCatList());
                request.getRequestDispatcher("/WEB-INF/forwards/admin-only/viewCategories.jsp").forward(request, response);
                break;

            case "/myNexware/admin/categories/editCat/":
                JSONObject result = new JSONObject();
                result.put("result", false);

                String action = request.getParameter("action");

                if (action == null || action.isBlank() || (!action.equals("add") && !action.equals("rem")))
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                else
                    switch (action) {
                        case "add":
                            String catName = request.getParameter("catName");
                            if (catName == null || catName.isBlank())
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            else {
                                int newId = CategoryDAO.doAddCategory(catName);
                                if (newId > 0) {
                                    result.put("result", true);
                                    result.put("id", newId);
                                }
                            }
                            break;

                        case "rem":
                            int catId = FieldValidator.idValidate(request.getParameter("catId"));
                            if (catId <= 0)
                                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            else
                                result.put("result", CategoryDAO.doRemoveCategory(catId));
                            break;
                    }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(result);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
