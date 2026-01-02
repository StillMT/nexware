package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.AccountStatus;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.application.utils.FieldValidator;
import it.unisa.nexware.application.dto.SessionMessage;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/myNexware/products/productManager/")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,    // 2MB Limite per RAM
        maxFileSize = 1024 * 1024 * 50,         // 50MB per file
        maxRequestSize = 1024 * 1024 * 100      // 100MB per richiesta
)
public class ProductManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("a");
        if (!"add".equals(action) && !"edit".equals(action)) {
            request.setAttribute("err", "NO_ACT");
            request.getRequestDispatcher("/WEB-INF/forwards/productManagerDash.jsp").forward(request, response);
            return;
        }

        HttpSession s = request.getSession();
        CompanyBean loggedCompany = (CompanyBean) s.getAttribute("company");
        String commit = request.getParameter("commit");
        String err = "";

        ProductBean p = null;
        boolean isAdd = false;
        boolean proceedToValidation = false;

        switch (action) {
            case "add":
                isAdd = true;

                AccountStatus cs = loggedCompany.getStatus();
                if (cs == null)
                    err = "INV_PARAM";
                else if (cs == AccountStatus.LIMITED)
                    err = "LIMITED_COM";
                else if (cs == AccountStatus.LIMITED_INFO)
                    err = "NO_INFO_COM";
                else
                    proceedToValidation = "true".equals(commit);
                break;

            case "edit":
                int pId = FieldValidator.idValidate(request.getParameter("p"));
                if (pId <= 0)
                    err = "INV_ID";
                else {
                    p = ProductDAO.doGetProductById(pId, true);
                    if (p == null)
                        err = "ERR_DB";
                    else if (p.getId() == 0)
                        err = "PD_NOT_FOUND";
                    else if (p.getIdCompany() != loggedCompany.getId())
                        err = "PD_COMPANY_MISMATCH";
                    else
                        proceedToValidation = "true".equals(commit);
                }
                break;
        }

        if (err.isEmpty() && proceedToValidation) {
            if (isAdd) {
                p = new ProductBean();
                p.setIdCompany(loggedCompany.getId());
            }

            if (!isAdd && "true".equals(request.getParameter("delete"))) {
                p.setStatus(ProductStatus.CANCELED);

                if (!ProductDAO.doEditProduct(p))
                    err = "ERR_DB";
                else {
                    s.setAttribute("queryProduct", new SessionMessage(p.getName()));
                    response.sendRedirect("/myNexware/products/");
                    return;
                }
            } else {
                String newName = request.getParameter("name");
                String newDescription = request.getParameter("description");
                String newCategoryStr = request.getParameter("category");
                String newStatusStr = request.getParameter("status");
                String newPriceStr = request.getParameter("price");
                String newStockStr = request.getParameter("stock");

                if (!(FieldValidator.productNameValidate(newName) && FieldValidator.productDescValidate(newDescription)
                        && FieldValidator.productCategoryValidate(newCategoryStr) && FieldValidator.productPriceValidate(newPriceStr)
                        && FieldValidator.productStockValidate(newStockStr)))
                    err = "INV_PARAM";
                else {
                    int newCategoryId = FieldValidator.idValidate(newCategoryStr);
                    BigDecimal newPrice = new BigDecimal(newPriceStr);
                    int newStock = Integer.parseInt(newStockStr);
                    ProductStatus newStatus = ProductStatus.fromString(newStatusStr);

                    if (CategoryDAO.doGetCategoryById(newCategoryId).isEmpty())
                        err = "INV_PARAM";
                    else if (newStatus == null || newStatus == ProductStatus.CANCELED)
                        err = "INV_PARAM";
                    else {
                        boolean changesDetected = false;

                        if (isAdd || !p.getName().equals(newName)) {
                            if (ProductDAO.doCheckProductNameAvailability(newName)) {
                                p.setName(newName);
                                changesDetected = true;
                            } else
                                err = "INV_PARAM";
                        }

                        if (err.isEmpty()) {
                            if (!newDescription.equals(p.getDescription())) { p.setDescription(newDescription); changesDetected = true; }
                            if (p.getIdCategory() != newCategoryId) { p.setIdCategory(newCategoryId); changesDetected = true; }
                            if (p.getStatus() != newStatus) { p.setStatus(newStatus); changesDetected = true; }
                            if (p.getPrice() == null || p.getPrice().compareTo(newPrice) != 0) { p.setPrice(newPrice); changesDetected = true; }
                            if (p.getStock() != newStock) { p.setStock(newStock); changesDetected = true; }

                            if (changesDetected) {
                                boolean result = false;
                                if (isAdd) {
                                    int id = ProductDAO.doSaveProduct(p);

                                    if (id != 0) {
                                        p.setId(id);
                                        result = true;
                                    }
                                }
                                else
                                    result = ProductDAO.doEditProduct(p);

                                if (!result)
                                    err = "ERR_DB";
                                else if (isAdd)
                                        s.setAttribute("isAdded", "true");
                            }

                            if (err.isEmpty()) {
                                if (!changesDetected)
                                    s.setAttribute("queryProduct", new SessionMessage(p.getName()));

                                request.setAttribute("product", p);
                                request.setAttribute("filesUploadForward", Boolean.TRUE);
                                request.getRequestDispatcher("/myNexware/products/productFileServlet").forward(request, response);

                                return;
                            }
                        }
                    }
                }
            }
        }

        String forwardUrl;

        request.setAttribute("product", p);
        request.setAttribute("err", err);
        request.setAttribute("cats", CategoryDAO.doGetCatList());
        if (!isAdd) {
            request.setAttribute("productForward", "/WEB-INF/forwards/productManagerDash.jsp");
            request.setAttribute("filesToo", "true");

            forwardUrl = "/catalogue/imgs/getFileList";
        } else
            forwardUrl = "/WEB-INF/forwards/productManagerDash.jsp";
        request.getRequestDispatcher(forwardUrl).forward(request, response);
    }
}