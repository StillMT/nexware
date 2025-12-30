package it.unisa.nexware.application.utils;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.storage.dao.CartDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionSetter {

    // Costruttore
    private SessionSetter() {}

    // Metodi
    public static void setSessionToLogin(HttpSession s, CompanyBean cm) {
        s.setAttribute("company", cm);
    }

    public static boolean isLogged(HttpServletRequest req) {
        return req.getSession().getAttribute("company") != null;
    }
}