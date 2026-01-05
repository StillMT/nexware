package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.enums.AccountStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.application.utils.SessionSetter;
import it.unisa.nexware.storage.dao.CompanyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/myNexware/loginEndpoint", "/myNexware/registerEndpoint"})
public class AuthenticationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Servlet protetta da AuthFilter

        String servletPath = request.getServletPath();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        switch (servletPath) {
            case "/myNexware/loginEndpoint":
                if (!FieldValidator.usernameValidate(username) || !FieldValidator.passwordValidate(password)) {
                    response.sendRedirect("/myNexware/login?e=NOT_VALID");
                    return;
                }

                CompanyBean loggedCompany = CompanyDAO.doLoginCompany(username, password);
                if (loggedCompany == null) {
                    response.sendRedirect("/myNexware/login?e=ERR_DB");
                    return;
                } else if (loggedCompany.getId() == 0) {
                    response.sendRedirect("/myNexware/login?e=INC_U_P");
                    return;
                }

                SessionSetter.setSessionToLogin(request.getSession(), loggedCompany);
                response.sendRedirect("/");
                break;

            case "/myNexware/registerEndpoint":
                String repPassword = request.getParameter("rep-password");
                String email = request.getParameter("email");
                String telephone = request.getParameter("telephone");
                String vat = request.getParameter("vat");
                String companyName = request.getParameter("company_name");
                String registeredOffice = request.getParameter("registered_office");
                boolean additionalInfo = Boolean.parseBoolean(request.getParameter("add_info"));

                if (!checkRegisterParams(username, password, repPassword, email, telephone, vat, companyName, additionalInfo)) {
                    response.sendRedirect("/myNexware/login?e=NOT_VALID_R");
                    return;
                }

                CompanyBean company = new CompanyBean(username, email, telephone, vat, companyName,
                        registeredOffice, additionalInfo && vat != null ? AccountStatus.NORMAL : AccountStatus.LIMITED_INFO);
                int cmId = CompanyDAO.doRegisterCompany(company, password);
                if (cmId > 0) {
                    company.setId(cmId);
                    SessionSetter.setSessionToLogin(request.getSession(), company);
                    response.sendRedirect("/");
                } else
                    response.sendRedirect("/myNexware/login?e=ERR_DB_R");
                break;
        }
    }

    private boolean checkRegisterParams(String username, String password, String repPassword, String email,
                                        String telephone, String vat, String companyName, boolean additionalInfo) {
        if (!FieldValidator.usernameValidate(username) || !CompanyDAO.doCheckUsernameAvailability(username))
            return false;

        if (!FieldValidator.passwordValidate(password))
            return false;

        if (!FieldValidator.repPswValidate(password, repPassword))
            return false;

        if (!FieldValidator.emailValidate(email) || !CompanyDAO.doCheckEmailAvailability(email))
            return false;

        if (!FieldValidator.phoneValidate(telephone))
            return false;

        if (additionalInfo && companyName != null && !companyName.isBlank() && !CompanyDAO.doCheckCompanyNameAvailability(companyName))
            return false;

        return !additionalInfo || (vat != null && !vat.isBlank() && FieldValidator.vatValidate(vat) && CompanyDAO.doCheckVATAvailability(vat));
    }
}