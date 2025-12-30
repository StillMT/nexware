package it.unisa.nexware.application.beans;

import java.math.BigDecimal;

public class OrderedProductBean {
    private int id;
    private int orderId;
    private ProductBean product;
    private BigDecimal price;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public ProductBean getProduct() { return product; }
    public void setProduct(ProductBean product) { this.product = product; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}