package it.unisa.nexware.application.beans;

import it.unisa.nexware.application.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductBean {

    // Costruttori
    public ProductBean() {
        id = idCategory = idCompany = stock = 0;
        name = description = "";
        creationDate = updateDate = LocalDateTime.MIN;
        status = ProductStatus.CANCELED;
        price = BigDecimal.ZERO;
    }

    public ProductBean(int id, String name, BigDecimal price, int stock, String companyName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.companyName = companyName;
    }

    public ProductBean(int id, String name, ProductStatus status, BigDecimal price, int stock, String companyName) {
        this(id, name, price, stock, companyName);
        this.status = status;
    }

    private ProductBean(int id, String name, String description, int idCategory,
                        ProductStatus status, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.idCategory = idCategory;
        this.status = status;
        this.price = price;
        this.stock = stock;
    }

    public ProductBean(int id, String name, String description, int idCategory,
                       LocalDateTime creationDate, LocalDateTime updateDate, ProductStatus status,
                       BigDecimal price, int stock) {
        this(id, name, description, idCategory, status, price, stock);
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public ProductBean(int id, String name, String description, int idCategory, int idCompany,
                       ProductStatus status, BigDecimal price, int stock, String companyName) {
        this(id, name, description, idCategory, status, price, stock);
        this.idCompany = idCompany;
        this.companyName = companyName;
    }

    public ProductBean(int id, String name, int idCategory, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.idCategory = idCategory;
        this.price = price;
        this.stock = stock;
    }


    // Metodi di accesso
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getCompanyName() {
        return companyName;
    }

    // Metodi modificatori
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    // Attributi
    private int id;
    private String name;
    private String description;
    private int idCategory;
    private int idCompany;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private ProductStatus status;
    private BigDecimal price;
    private int stock;

    private String companyName;
}
