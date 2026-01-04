package it.unisa.nexware.application.filters;

import it.unisa.nexware.application.utils.SessionSetter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/myNexware/*")
public class AuthFilter implements Filter {

    // Logged/Guest
    private static final String LOGIN_SERVLET = "/myNexware/loginEndpoint";
    private static final String REGISTER_SERVLET = "/myNexware/registerEndpoint";
    private static final String RESERVED_AREA = "/myNexware";
    private static final String LOGIN_PAGE = "/myNexware/login";
    private static final String LOGOUT = "/myNexware/logout";

    // Admin
    private static final String LOGIN_ADMIN_AREA = "/myNexware/admin/login";
    private static final String ADMIN_AREA = "/myNexware/admin";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        String context = req.getContextPath();
        boolean logged = SessionSetter.isLogged(req);
        boolean admin = SessionSetter.isAdmin(req);

        if (path.startsWith(context + LOGOUT)) {
            if (logged || admin)
                chain.doFilter(req, res);
            else
                res.sendRedirect("/");

            return;
        }

        // Logged/Guest
        if (path.startsWith(context + LOGIN_PAGE) || path.startsWith(context + LOGIN_SERVLET)
                || path.startsWith(context + REGISTER_SERVLET)) {
            if (logged)
                res.sendRedirect("/");
            else if (admin)
                res.sendRedirect(ADMIN_AREA);
            else
                chain.doFilter(request, response);

            return;
        }

        if (path.equals(context + RESERVED_AREA) || path.equals(context + RESERVED_AREA + "/")) {
            if (logged)
                chain.doFilter(request, response);
            else
                res.sendRedirect(LOGIN_PAGE);

            return;
        }

        // Admin
        if (path.startsWith(context + LOGIN_ADMIN_AREA)) {
            if (admin)
                res.sendRedirect(ADMIN_AREA);
            else if (logged)
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            else
                chain.doFilter(request, response);

            return;
        }

        if (path.startsWith(context + ADMIN_AREA)) {
            if (admin)
                chain.doFilter(request, response);
            else
                res.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        // Default: blocca se non loggato
        if (logged)
            chain.doFilter(request, response);
        else if (admin)
            res.sendRedirect(ADMIN_AREA);
        else
            res.sendRedirect(LOGIN_PAGE + "?e=NO_AUTH");
    }
}