package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.ReportBean;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.ReportDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/site-related/contactus/")
public class ReportsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyName = request.getParameter("name");
        String email = request.getParameter("email");
        String object = request.getParameter("object");
        String description = request.getParameter("description");

        if (checkParams(companyName, email, object, description) && ReportDAO.doInsertReport(new ReportBean(companyName, email, object, description)))
            request.setAttribute("result", "OK");
        else
            request.setAttribute("result", "ERR");

        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("contactUsForm.jsp").forward(request, response);
    }

    private boolean checkParams(String companyName, String email, String object, String description) {
        if (!FieldValidator.companyNameValidate(companyName))
            return false;

        if (!FieldValidator.emailValidate(email))
            return false;

        if (FieldValidator.containsBadWord(object) || (object != null && object.length() > 100))
            return false;

        return !FieldValidator.containsBadWord(description) && !(description != null && description.length() > 16000);
    }
}
