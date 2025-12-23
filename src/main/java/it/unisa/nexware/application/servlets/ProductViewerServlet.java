package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/catalogue/view/")
public class ProductViewerServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CompanyBean company = (CompanyBean) request.getSession().getAttribute("company");

        String error = "";
        ProductBean p = new ProductBean();

        int pId = FieldValidator.idValidate(request.getParameter("p"));
        if (pId <= 0)
            error = "INV_ID";

        if (error.isEmpty()) {
            p = ProductDAO.doGetProductById(pId, false);

            if (p == null)
                error = "DB_ERR";
            else if (p.getId() == 0 ||
                    (p.getStatus() == ProductStatus.HIDDEN && (company == null || p.getIdCompany() != company.getId())))
                error = "PD_NOT_FOUND";
        }

        request.setAttribute("product", p);
        request.setAttribute("error", error);

        String forwardUrl;

        if (error.isEmpty()) {
            forwardUrl = "/catalogue/imgs/getFileList";

            request.setAttribute("productForward", "/WEB-INF/forwards/viewProduct.jsp");
            request.setAttribute("catName", CategoryDAO.doGetCategoryById(p.getIdCategory()));
        } else
            forwardUrl = "/WEB-INF/forwards/viewProduct.jsp";

        request.getRequestDispatcher(forwardUrl).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
