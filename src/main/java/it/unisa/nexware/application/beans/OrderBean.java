package it.unisa.nexware.application.beans;

import it.unisa.nexware.application.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderBean {

    private int id;
    private int idCompany;
    private String orderNr;
    private BigDecimal totalPrice;
    private OrderStatus state;
    private LocalDate date;
    private String halfCardNumber;
    private List<OrderedProductBean> products;

    public OrderBean(int id, int idCompany, String orderNr, BigDecimal totalPrice,
                     OrderStatus state, LocalDate date, String halfCardNumber) {
        this.id = id;
        this.idCompany = idCompany;
        this.orderNr = orderNr;
        this.totalPrice = totalPrice != null ? totalPrice : BigDecimal.ZERO;
        this.state = state != null ? state : OrderStatus.WAITING;
        this.date = date != null ? date : LocalDate.now();
        this.halfCardNumber = halfCardNumber != null ? halfCardNumber : "";
        this.products = new ArrayList<>();
    }

    public OrderBean(int id, int idCompany, String orderNr,
                     OrderStatus state, LocalDate date, String halfCardNumber) {
        this(id, idCompany, orderNr, BigDecimal.ZERO, state, date, halfCardNumber);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCompany() { return idCompany; }
    public void setIdCompany(int idCompany) { this.idCompany = idCompany; }

    public String getOrderNr() { return orderNr; }
    public void setOrderNr(String orderNr) { this.orderNr = orderNr; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public OrderStatus getState() { return state; }
    public void setState(OrderStatus state) { this.state = state; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getHalfCardNumber() { return halfCardNumber; }
    public void setHalfCardNumber(String halfCardNumber) { this.halfCardNumber = halfCardNumber; }

    public List<OrderedProductBean> getProducts() { return products; }
    public void setProducts(List<OrderedProductBean> products) { this.products = products; }
}