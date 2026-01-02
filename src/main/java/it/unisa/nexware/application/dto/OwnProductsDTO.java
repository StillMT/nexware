package it.unisa.nexware.application.dto;

import it.unisa.nexware.application.beans.CategoryBean;
import it.unisa.nexware.application.beans.ProductBean;

import java.util.List;

public class OwnProductsDTO {

    // Costruttore
    public OwnProductsDTO(List<ProductBean> products, List<CategoryBean> categories) {
        this.products = products;
        this.categories = categories;
    }

    // Metodi di accesso
    public List<ProductBean> getProducts() {
        return products;
    }

    public List<CategoryBean> getCategories() {
        return categories;
    }

    // Attributi
    private List<ProductBean> products;
    private List<CategoryBean> categories;
}
