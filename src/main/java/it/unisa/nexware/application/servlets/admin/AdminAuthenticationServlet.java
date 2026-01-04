package it.unisa.nexware.application.servlets.admin;

import it.unisa.nexware.application.beans.AdminBean;
import it.unisa.nexware.application.dto.SessionMessage;
import it.unisa.nexware.application.utils.SessionSetter;
import it.unisa.nexware.storage.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/myNexware/admin/login/", "/myNexware/admin/login/loginEndpoint"})
public class AdminAuthenticationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/myNexware/admin/login/loginEndpoint")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getSession().setAttribute("adminLogin", new SessionMessage("OK"));
        response.sendRedirect("/myNexware/login/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/myNexware/admin/login/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        AdminBean a = AdminDAO.doLoginAdmin(username, password);
        if (a == null || a.getUsername().isEmpty() || a.getUsername().isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        SessionSetter.setAdminSessionToLogin(request.getSession(), a);
        response.sendRedirect("/myNexware/admin/");
    }
}
