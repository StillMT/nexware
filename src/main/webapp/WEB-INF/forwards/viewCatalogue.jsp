<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.beans.CategoryBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>

<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("pList");
    List<CategoryBean> categories = (List<CategoryBean>) request.getAttribute("catList");
    String categoryFilter = (String) request.getAttribute("categoryFilter");

    if (categoryFilter == null) categoryFilter = "ALL";
%>

<!DOCTYPE html>
<html>
<% final String pageTitle = "Catalogo"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
    <div class="catalogue-container">

        <div class="category-bar">
            <a href="${pageContext.request.contextPath}/catalogue/"
               class="category-btn <%= "ALL".equals(categoryFilter) ? "active" : "" %>">
                Tutte
            </a>

            <%
                if (categories != null) {
                    for (CategoryBean c : categories) {
                        boolean isActive = String.valueOf(c.getId()).equals(categoryFilter);
            %>
            <a href="${pageContext.request.contextPath}/catalogue/?category-filter=<%= c.getId() %>"
               class="category-btn <%= isActive ? "active" : "" %>">
                <%= c.getName() %>
            </a>
            <%
                    }
                }
            %>
        </div>

        <div class="product-grid">
            <%
                if (products != null && !products.isEmpty()) {
                    for (ProductBean p : products) {
            %>
            <div class="product-card">
                <a href="${pageContext.request.contextPath}/catalogue/view/?p=<%= p.getId() %>"
                   class="product-link">

                    <img src="${pageContext.request.contextPath}/catalogue/imgs/<%= p.getId() %>/1"
                         alt="<%= p.getName() %>"
                         class="product-img">

                    <div class="product-info">
                        <h3 class="product-name"><%= p.getName() %></h3>
                        <p class="product-company"><%= p.getCompanyName() %></p>
                        <p class="product-price">
                            <%= FieldValidator.formatEuroPrice(p.getPrice()) %>
                        </p>

                        <div class="stock-status">
                            <% if (p.getStock() == 0) { %>
                            <span class="stock-out">Esaurito</span>
                            <% } else if (p.getStock() <= 10) { %>
                            <span class="stock-low">
                                            Disponibili: <%= p.getStock() %>
                                        </span>
                            <% } else { %>
                            <span class="stock-ok">Disponibile</span>
                            <% } %>
                        </div>
                    </div>
                </a>
            </div>
            <%
                }
            } else {
            %>
            <div class="no-products">
                Nessun prodotto trovato.
            </div>
            <%
                }
            %>
        </div>

    </div>
</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>