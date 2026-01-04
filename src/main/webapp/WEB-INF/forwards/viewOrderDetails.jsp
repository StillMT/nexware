<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.unisa.nexware.application.beans.OrderBean" %>
<%@ page import="it.unisa.nexware.application.beans.OrderedProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="it.unisa.nexware.application.enums.ProductStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%
    OrderBean order = (OrderBean) request.getAttribute("order");
    List<OrderedProductBean> items = (List<OrderedProductBean>) request.getAttribute("items");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <% final String pageTitle = "Dettaglio Ordine #" + (order != null ? order.getOrderNr() : ""); %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <link rel="icon" href="data:,">

    <link rel="stylesheet" type="text/css" href="<%= contextPath %>/myNexware/detailsOrders/styles/main.css">
</head>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
    <div class="order-details-wrapper">

        <div class="back-button-wrapper">
            <a href="<%= contextPath %>/myNexware/orders/" class="action-button">
                <i class="fa-solid fa-arrow-left"></i> Torna agli ordini
            </a>
        </div>

        <% if (order != null) { %>
        <h2>Dettagli Ordine #<%= order.getOrderNr() %></h2>

        <div class="order-info">
            <p><strong>Data:</strong> <%= FieldValidator.formatDate(order.getDate()) %></p>
            <p>
                <strong>Stato:</strong>
                <span class="status-pill <%= order.getState().name().toLowerCase() %>">
                        <%= order.getState().getString() %>
                </span>
            </p>
            <p><strong>Totale ordine:</strong> <%= FieldValidator.formatEuroPrice(order.getTotalPrice()) %></p>
            <p><strong>Pagamento:</strong> •••• •••• •••• <%= order.getHalfCardNumber() %></p>
        </div>

        <hr>

        <h3>Prodotti acquistati</h3>

        <% if (items != null && !items.isEmpty()) { %>
        <div class="products-list">
            <%
                for (OrderedProductBean item : items) {
                    boolean isAvailable = (item.getProduct().getStatus() == ProductStatus.ACTIVE);
            %>
            <div class="product-row <%= !isAvailable ? "not-available" : "" %>">
                <div class="product-left">
                    <div class="img-container">
                        <% if (isAvailable) { %>

                        <img src="<%= contextPath %>/catalogue/imgs/<%= item.getProduct().getId() %>/1"
                             alt="<%= item.getProduct().getName() %>">
                        <% } else { %>
                        <div class="img-placeholder" style="display:flex; align-items:center; justify-content:center; width:64px; height:64px; background:#eee; border-radius:8px;">
                            <i class="fa-solid fa-box-archive" style="color:#ccc;"></i>
                        </div>
                        <% } %>
                    </div>

                    <div class="product-info-text">
                        <% if (isAvailable) { %>
                        <a href="<%= contextPath %>/catalogue/view/?p=<%= item.getProduct().getId() %>"
                           class="product-name product-link">
                            <%= item.getProduct().getName() %>
                        </a>
                        <% } else { %>
                        <span class="status-warning">Prodotto non disponibile </span>
                        <% } %>
                    </div>
                </div>

                <span class="product-price">
                    <%= FieldValidator.formatEuroPrice(item.getPrice()) %>
                </span>
            </div>
            <% } %>
        </div>

        <hr>

        <div class="total-summary">
            <p class="total-order">
                <strong>Totale Finale:</strong>
                <span class="price-highlight"><%= FieldValidator.formatEuroPrice(order.getTotalPrice()) %></span>
            </p>
        </div>

        <% } else { %>
        <p>Nessun prodotto trovato per questo ordine.</p>
        <% } %>

        <% } else { %>
        <p>Ordine non trovato.</p>
        <% } %>

    </div>
</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>