package it.unisa.nexware.application.servlets.orders;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.storage.dao.OrderDAO;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/myNexware/orders/confirmer-order")
public class OrderConfirmerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject result = new JSONObject();
        result.put("result", false);


        String orderNr = request.getParameter("orderNr");
        String newStatusStr = request.getParameter("newStatus");


        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");

        if (orderNr != null && newStatusStr != null && company != null) {
            try {
                OrderStatus newStatus = OrderStatus.valueOf(newStatusStr);


                boolean success = OrderDAO.updateOrderStatus(orderNr, company.getId(), newStatus);

                result.put("result", success);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (company == null) {
            result.put("error", "Sessione scaduta");
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(result.toString());
    }
}