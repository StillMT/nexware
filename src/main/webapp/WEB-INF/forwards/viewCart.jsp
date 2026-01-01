<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.unisa.nexware.application.enums.ProductStatus" %>
<%@ page import="it.unisa.nexware.application.utils.SessionMessage" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    BigDecimal subtotal = BigDecimal.ZERO;
    BigDecimal fees = new BigDecimal("3.99");
    int pCount = products != null ? products.size() : 0;
%>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Carrello"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="cart-wrapper">
                <%
                    SessionMessage sm = (SessionMessage) s.getAttribute("addingResult");
                    if (sm != null) {
                %>
                <span class="adding-message<%= sm.getMessage().equals("false") ? " error" : "" %>">
                    <%= sm.getMessage().equals("false") ? "<i class=\"fa-solid fa-xmark\"></i>Errore durante l'aggiunta del prodotto"
                            : "<i class=\"fa-solid fa-check\"></i>Prodotto aggiunto correttamente" %>.
                </span>
                <%
                        s.removeAttribute("addingResult");
                    } else {
                %>
                    <span></span>
                <% } %>

                <span class="cart-title">Carrello (<%= pCount %> articol<%= pCount == 1 ? "o" : "i" %>)</span>

                <div class="cart-details" data-fees="<%= fees %>">
                    <div class="loading-blocker" id="blocker"></div>

                    <div class="cart-items-list">

                        <%
                            boolean oneProductValid = false;
                            boolean oneProductNotValid = false;
                            if (products != null)
                                for (ProductBean p : products) {
                                    int productId;
                                    String productName;
                                    String companyName;
                                    int stock;
                                    BigDecimal price;

                                    if (p.getStatus() == ProductStatus.HIDDEN || p.getStatus() == ProductStatus.CANCELED) {
                                        productId = stock = 0;
                                        productName = "Prodotto non disponibile";
                                        companyName = "";
                                        price = BigDecimal.ZERO;

                                        oneProductNotValid = true;
                                    } else {
                                        productId = p.getId();
                                        productName = p.getName();
                                        companyName = p.getCompanyName();
                                        stock = p.getStock();
                                        price = p.getPrice();

                                        if (stock >= 1)
                                            oneProductValid = true;
                                        else
                                            oneProductNotValid = true;
                                    }

                                    if (stock != 0)
                                        subtotal = subtotal.add(price);
                        %>

                        <div class="item">
                            <img class="product-image" src="/catalogue/imgs/<%= productId %>/1">
                            <div class="item-details">
                                <div class="item-title">
                                    <a <% if (productId != 0) { %>href="${pageContext.request.contextPath}/catalogue/view/?p=<%= productId %>"<% } %> class="item-name">
                                        <span><%= productName %></span>
                                    </a>
                                    <span class="company-item-title"><%= companyName %></span>
                                </div>
                                <% if (productId != 0) { %><span class="item-stock<%= stock <= 10 ? " low-stock" : "" %>">Disponibilit&agrave;:&nbsp;<%= stock == 0 ? "non disponibile" : stock %></span><% } %>
                                <span class="item-remove" data-id="<%= p.getId() %>">Rimuovi</span>
                            </div>
                            <% if (productId != 0) { %><div class="item-price<%= stock <= 0 ? " low-stock" : "" %>" data-price="<%= price %>"><%= FieldValidator.formatEuroPrice(price) %></div><% } %>
                        </div>

                        <% } %>

                        <div class="no-items" style="<%= pCount == 0 ? "display: flex;" : "" %>">Nessun prodotto aggiunto al carrello</div>
                    </div>

                    <div class="cart-summary">
                        <div class="labeled-price">
                            <span>Subtotale:</span>
                            <span class="price"><%= FieldValidator.formatEuroPrice(subtotal) %></span>
                        </div>
                        <div class="labeled-price">
                            <span>Commissioni:</span>
                            <span class="price"><%= FieldValidator.formatEuroPrice(subtotal.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : fees) %></span>
                        </div>
                        <hr class="separator" />
                        <div class="labeled-price">
                            <span>Totale:</span>
                            <span class="price"><%= FieldValidator.formatEuroPrice(subtotal.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : subtotal.add(fees)) %></span>
                        </div>

                        <form action="checkout/" method="post">
                            <input type="submit" value="Vai al checkout" <%= pCount == 0 || !oneProductValid ? "disabled" : "" %> />
                        </form>
                        <span class="warning-checkout" style="<%= oneProductNotValid ? "display: block;" : "" %>">I prodotti non disponibili saranno ignorati nel checkout.</span>
                    </div>
                </div>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

        </main>

        <script>
            const dbErr = <%= products == null ? "true" : "false" %>;
        </script>
        <script src="js/CartSummaryMover.js"></script>
        <script src="js/CartListHandler.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>