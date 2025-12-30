<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.unisa.nexware.application.beans.OrderBean" %>
<%@ page import="it.unisa.nexware.application.beans.OrderedProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%
    OrderBean order = (OrderBean) request.getAttribute("order");
    List<OrderedProductBean> items =
            (List<OrderedProductBean>) request.getAttribute("items");
    BigDecimal calculatedTotal =
            (BigDecimal) request.getAttribute("calculatedTotal");
%>

<!DOCTYPE html>
<html>
<% final String pageTitle = "Dettaglio Ordine"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<link rel="stylesheet"
      href="<%= request.getContextPath() %>/myNexware/orders/styles/order-details.css">

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
    <div class="order-details-wrapper">


        <div class="back-button-wrapper">
            <a href="<%= request.getContextPath() %>/myNexware/orders/"
               class="action-button">
                ← Torna agli ordini
            </a>
        </div>

        <h2>Dettagli Ordine #<%= order.getOrderNr() %></h2>


        <div class="order-info">
            <p>
                <strong>Data:</strong>
                <%= FieldValidator.formatDate(order.getDate()) %>
            </p>

            <p>
                <strong>Stato:</strong>
                <span class="status-pill <%= order.getState().name().toLowerCase() %>">
                    <%= order.getState().getString() %>
                </span>
            </p>

            <p>
                <strong>Totale ordine:</strong>
                <%= FieldValidator.formatEuroPrice(calculatedTotal) %>
            </p>

            <p>
                <strong>Carta:</strong>
                **** **** **** <%= order.getHalfCardNumber() %>
            </p>
        </div>

        <hr>

        <h3>Prodotti acquistati</h3>

        <% if (items != null && !items.isEmpty()) { %>
        <div class="products-list">

            <% for (OrderedProductBean item : items) { %>
            <div class="product-row">

                <div class="product-left">

                    <img
                            src="http://localhost/catalogue/imgs/<%= item.getProduct().getId() %>/1"
                            alt="<%= item.getProduct().getName() %>">


                    <a
                            href="http://localhost/catalogue/view/?p=<%= item.getProduct().getId() %>"
                            class="product-name product-link">
                        <%= item.getProduct().getName() %>
                    </a>
                </div>


                <span class="product-price">
                            <%= FieldValidator.formatEuroPrice(item.getPrice()) %>
                        </span>

            </div>
            <% } %>

        </div>

        <hr>

        <p class="total-order">
            <strong>Totale calcolato:</strong>
            <%= FieldValidator.formatEuroPrice(calculatedTotal) %>
        </p>

        <% } else { %>
        <p>Nessun prodotto trovato per questo ordine.</p>
        <% } %>

    </div>
</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>