package it.unisa.nexware.application.utils;

import it.unisa.nexware.application.beans.AdminBean;
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

    public static void setAdminSessionToLogin(HttpSession s, AdminBean a) {
        s.setAttribute("admin", a);
    }

    public static boolean isLogged(HttpServletRequest req) {
        return req.getSession().getAttribute("company") != null;
    }

    public static boolean isAdmin(HttpServletRequest req) {
        return req.getSession().getAttribute("admin") != null;
    }
}