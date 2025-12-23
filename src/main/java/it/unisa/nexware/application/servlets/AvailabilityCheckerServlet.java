package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CompanyDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet(urlPatterns = {"/check-username", "/check-email", "/check-phone", "/get-vat-details", "/check-company_name",
        "/myNexware/products/check-product-name"})
public class AvailabilityCheckerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject result = new JSONObject();
        result.put("result", false);

        switch (request.getServletPath()) {
            case "/get-vat-details":
                final String EU_REST = "https://ec.europa.eu/taxation_customs/vies/rest-api/ms/IT/vat/";

                String vatNumber = request.getParameter("vat");
                if  (FieldValidator.vatValidate(vatNumber))
                    try (HttpClient httpClient = HttpClient.newBuilder().build()) {

                        HttpRequest RestRequest = HttpRequest.newBuilder()
                                .uri(URI.create(EU_REST + vatNumber)).GET().build();

                        HttpResponse<String> RestResponse = httpClient.send(
                                RestRequest, HttpResponse.BodyHandlers.ofString());

                        try {
                            JSONObject vatDetails = new JSONObject(RestResponse.body());

                            if (vatDetails.getBoolean("isValid")) {
                                result.put("eu_valid", true);

                                result.put("companyName", vatDetails.getString("name"));
                                result.put("companyAddress", vatDetails.getString("address")
                                        .replace('\n', ',').trim());
                            }
                            else
                                result.put("eu_valid", false);

                            result.put("available", CompanyDAO.doCheckVATAvailability(vatNumber));
                            result.put("result", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                else {
                    result.put("valid", false);
                    result.put("result", true);
                }
                break;

            case "/check-company_name":
                String companyName = request.getParameter("companyName");
                if (companyName != null)
                    if (companyName.isBlank())
                        result.put("result", true);
                    else
                        result.put("result", CompanyDAO.doCheckCompanyNameAvailability(companyName));
                break;

            case "/check-username":
                String username = request.getParameter("username");
                if (FieldValidator.usernameValidate(username))
                    result.put("result", CompanyDAO.doCheckUsernameAvailability(username));
                break;

            case "/check-email":
                String email = request.getParameter("email");
                if (FieldValidator.emailValidate(email))
                    result.put("result", CompanyDAO.doCheckEmailAvailability(email));
                break;

            case "/check-phone":
                String phone = request.getParameter("phone");
                if (FieldValidator.phoneValidate(phone))
                    result.put("result", CompanyDAO.doCheckPhoneAvailability(phone));
                break;

            case "/myNexware/products/check-product-name":
                String pName = request.getParameter("product-name");
                if (FieldValidator.productNameValidate(pName))
                    result.put("result", ProductDAO.doCheckProductNameAvailability(pName));
                break;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
