package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.NewsletterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/newsletter/")
public class NewsletterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("newsletter_email");

        if (FieldValidator.emailValidate(email)) {
            if (NewsletterDAO.doCheckEmailAvailability(email)) {
                request.setAttribute("status", "OK");
                NewsletterDAO.doInsertEmail(email);
            } else
                request.setAttribute("status", "EX");
        }

        request.getRequestDispatcher("/newsletter/viewNewsletterResult.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
