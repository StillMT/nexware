package it.unisa.nexware.application.servlets.orders;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.storage.dao.OrderDAO;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/myNexware/orders/view/*")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/myNexware/orders/");
            return;
        }
        String orderNr = pathInfo.substring(1);

        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");
        if (company == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Connection con = null;
        try {
            con = DriverManagerConnectionPool.getConnection();

            OrderBean order = OrderDAO.getOrderByNumber(con, orderNr, company.getId());

            if (order != null) {

                request.setAttribute("order", order);

                request.setAttribute("items", order.getProducts());

                request.getRequestDispatcher("/WEB-INF/forwards/viewOrderDetails.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, null, null);
        }
    }
}
