package it.unisa.nexware.application.beans;

import it.unisa.nexware.application.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductBean {

    // Costruttori
    public ProductBean() {
        id = idCategory = stock = 0;
        name = description = "";
        creationDate = updateDate = LocalDateTime.now();
        status = ProductStatus.CANCELED;
        price = BigDecimal.ZERO;
    }

    public ProductBean(int id, String name, String description, int idCategory,
                       LocalDateTime creationDate, LocalDateTime updateDate, ProductStatus status,
                       BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.idCategory = idCategory;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.status = status;
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

    // Attributi
    private int id;
    private String name;
    private String description;
    private int idCategory;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private ProductStatus status;
    private BigDecimal price;
    private int stock;
}
