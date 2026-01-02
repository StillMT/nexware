package it.unisa.nexware.application.facades;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.dto.OwnProductsDTO;
import it.unisa.nexware.storage.dao.CategoryDAO;
import it.unisa.nexware.storage.dao.ProductDAO;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class OwnProductsFacade {

    public OwnProductsDTO getProductsData(CompanyBean c, String query, String startDate, String endDate, String status) {
        return new OwnProductsDTO(ProductDAO.doGetProductsByCompany(c, query, startDate, endDate, status),
                CategoryDAO.doGetCatList()
        );
    }

}