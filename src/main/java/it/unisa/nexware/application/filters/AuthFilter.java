package it.unisa.nexware.application.filters;

import it.unisa.nexware.application.utils.SessionSetter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/myNexware/*")
public class AuthFilter implements Filter {

    private static final String LOGIN_SERVLET = "/myNexware/loginEndpoint";
    private static final String REGISTER_SERVLET = "/myNexware/registerEndpoint";
    private static final String RESERVED_AREA = "/myNexware";
    private static final String CART = "/myNexware/cart";
    private static final String CHECKOUT = "/myNexware/cart/checkout";
    private static final String LOGIN_PAGE = "/myNexware/login";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        String context = req.getContextPath();
        boolean logged = SessionSetter.isLogged(req);

        if (path.startsWith(context + LOGIN_PAGE) || path.startsWith(context + LOGIN_SERVLET)
                || path.startsWith(context + REGISTER_SERVLET)) {
            if (logged)
                res.sendRedirect("/");
            else
                chain.doFilter(request, response);

            return;
        }

        if (path.startsWith(context + CHECKOUT)) {
            if (logged)
                chain.doFilter(request, response);
            else
                res.sendRedirect(LOGIN_PAGE);

            return;
        }

        if (path.startsWith(context + CART)) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith(context + RESERVED_AREA)) {
            if (logged)
                chain.doFilter(request, response);
            else
                res.sendRedirect(LOGIN_PAGE);

            return;
        }

        // Default: blocca se non loggato
        if (logged)
            chain.doFilter(request, response);
        else
            res.sendRedirect(LOGIN_PAGE + "?e=NO_AUTH");
    }
}