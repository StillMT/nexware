package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CompanyDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/myNexware/profile/")
public class ProfileServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/myNexware/profile/viewProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        CompanyBean currentCompany = (CompanyBean) session.getAttribute("company");

        if (currentCompany == null) {
            response.sendRedirect(request.getContextPath() + "/myNexware/login/index.jsp");
            return;
        }

        if ("updateProfile".equals(request.getParameter("action")))
            handleUpdateProfile(request, response, session, currentCompany);
        else
            doGet(request, response);

    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session, CompanyBean company) throws IOException, ServletException {

        String newEmail = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String newPhone = request.getParameter("telephone") != null ? request.getParameter("telephone").trim() : "";
        String newCompanyName = request.getParameter("company_name") != null ? request.getParameter("company_name").trim() : "";
        String newVat = request.getParameter("vat") != null ? request.getParameter("vat").trim() : "";
        String newAddress = request.getParameter("registered_office") != null ? request.getParameter("registered_office").trim() : "";

        String errorMessage = null;

        if (!newEmail.equalsIgnoreCase(company.getEmail())) {
            if (newEmail.isEmpty()) errorMessage = "L'email è obbligatoria.";
            else if (!FieldValidator.emailValidate(newEmail)) errorMessage = "Formato Email non valido.";
            else if (!CompanyDAO.doCheckEmailAvailability(newEmail)) errorMessage = "Email già utilizzata da un altro account.";
        }

        if (errorMessage == null && !newPhone.equals(company.getTelephone())) {
            if (newPhone.isEmpty()) errorMessage = "Il telefono è obbligatorio.";
            else if (!FieldValidator.phoneValidate(newPhone)) errorMessage = "Formato Telefono non valido.";
            else if (!CompanyDAO.doCheckPhoneAvailability(newPhone)) errorMessage = "Numero di telefono già registrato.";
        }

        if (errorMessage == null && !newCompanyName.equalsIgnoreCase(company.getCompanyName())) {
            if (newCompanyName.length() < 2) errorMessage = "Nome azienda troppo corto.";
            else if (!CompanyDAO.doCheckCompanyNameAvailability(newCompanyName)) errorMessage = "Nome azienda già esistente.";
        }

        if (errorMessage == null && !newVat.equals(company.getVat())) {
            if (!FieldValidator.vatValidate(newVat)) errorMessage = "Partita IVA non valida.";
            else if (!CompanyDAO.doCheckVATAvailability(newVat)) errorMessage = "Partita IVA già presente nel sistema.";
        }

        if (errorMessage == null && !newAddress.equalsIgnoreCase(company.getCompanyAddress())) {
            if (newAddress.length() < 5) errorMessage = "Indirizzo non valido (troppo corto).";
        }

        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/myNexware/profile/viewProfile.jsp").forward(request, response);
            return;
        }

        String oldEmail = company.getEmail();
        String oldPhone = company.getTelephone();
        String oldName = company.getCompanyName();
        String oldVat = company.getVat();
        String oldAddress = company.getCompanyAddress();

        company.setEmail(newEmail);
        company.setTelephone(newPhone);
        company.setCompanyName(newCompanyName);
        company.setVat(newVat);
        company.setCompanyAddress(newAddress);

        if (CompanyDAO.doUpdateCompany(company)) {
            session.setAttribute("company", company);
            response.sendRedirect(request.getContextPath() + "/myNexware/profile/?msg=success");
        } else {
            company.setEmail(oldEmail);
            company.setTelephone(oldPhone);
            company.setCompanyName(oldName);
            company.setVat(oldVat);
            company.setCompanyAddress(oldAddress);

            request.setAttribute("error", "Errore tecnico durante il salvataggio. Riprova più tardi.");
            request.getRequestDispatcher("/myNexware/profile/viewProfile.jsp").forward(request, response);
        }
    }
}