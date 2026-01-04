package it.unisa.nexware.application.servlets.admin;

import it.unisa.nexware.application.enums.AccountStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CompanyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/myNexware/admin/companies/", "/myNexware/admin/companies/updateStatus"})
public class CompanyManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("companies", CompanyDAO.doGetAllCompanies());
        request.getRequestDispatcher("/WEB-INF/forwards/admin-only/viewCompanies.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.endsWith("/updateStatus")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String idStr = request.getParameter("id");
            String statusStr = request.getParameter("status");

            if (idStr == null || statusStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = FieldValidator.idValidate(idStr);
            if (id <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            AccountStatus status = AccountStatus.fromString(statusStr);
            if (status == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.getWriter().write("{\"result\": " + CompanyDAO.doUpdateStatus(id, status) + "}");
        } else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}