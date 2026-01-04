<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="it.unisa.nexware.application.utils.SessionSetter" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    ProductBean p = (ProductBean) request.getAttribute("product");
    String err = request.getAttribute("error").toString();
%>

<!DOCTYPE html>
<html>
    <% final String pageTitle = err.isEmpty() ? p.getName() : "Errore"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <%
                if (err.isEmpty()) {
                    int imgsCount = (int) request.getAttribute("productImgsCount");
            %>

            <div class="product-wrapper">
                <% if (loggedCompany != null && loggedCompany.getId() == p.getIdCompany()) { %>
                <div class="own-product-actions">
                    <span><i class="fa-solid fa-check"></i>Prodotto di tua proprietà</span>
                    <span class="actions">
                        <form action="${pageContext.request.contextPath}/myNexware/products/" method="post">
                            <input type="hidden" name="search-query" value="<%= p.getName() %>" />
                            <button>Visualizza nella lista</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/myNexware/products/productManager/" method="post">
                            <input type="hidden" name="a" value="edit" />
                            <input type="hidden" name="p" value="<%= p.getId() %>" />
                            <button>Modifica prodotto</button>
                        </form>
                    </span>
                </div>
                <% } %>

                <div class="product-view">
                    <div class="slideshow-wrapper">
                        <div class="slideshow-imgs">
                            <div class="slides-container" id="slidesContainer">
                                <% for (int i = 1; i <= imgsCount || i == 1; i++) { %>
                                <div class="slide"><img src="/catalogue/imgs/<%= p.getId() %>/<%= i %>" /></div>
                                <% } %>
                            </div>
                            <% if (imgsCount > 1) { %>
                            <a class="prev">❮</a>
                            <a class="next">❯</a>
                            <% } %>
                        </div>
                        <div class="dots">
                            <% if (imgsCount > 0) {
                                for (int i = 1; i <= imgsCount; i++) { %>
                            <span class="dot" onclick="currentSlide(<%= i %>)"></span>
                            <% } } else { %>
                            <span class="dot"></span>
                            <% } %>
                        </div>
                    </div>

                    <div class="product-details">
                        <div class="p-header">
                            <span class="product-name"><%= p.getName() %></span>
                            <%
                                String cn = p.getCompanyName();
                                if (cn != null && !cn.isBlank()) {
                            %>
                                <span class="vendor-name">Produttore: <%= cn %></span>
                            <% } %>
                        </div>

                        <hr class="separator" />

                        <span class="category">
                            Categoria:
                            <a href="${pageContext.request.contextPath}/catalogue/?category-filter=<%= p.getIdCategory() %>">
                                <%= request.getAttribute("catName") %>
                            </a>
                        </span>

                        <span class="price"><%= FieldValidator.formatEuroPrice(p.getPrice()) %></span>
                        <span class="stock <%= p.getStock() <= 10 ? "last" : "" %>">
                            Disponibilit&agrave;: <%= p.getStock() %>
                            <%= p.getStock() <= 10 ? "(Ultimi pezzi)" : "" %>
                        </span>

                        <% if (!SessionSetter.isAdmin(request)) { %>
                        <div class="add-to-cart-wrapper">
                            <a href="${pageContext.request.contextPath}/myNexware/cart/addProduct?p=<%= p.getId() %>">
                                <span class="add-to-cart">Aggiungi al carrello</span>
                            </a>
                        </div>
                        <% } %>
                    </div>
                </div>

                <div class="product-description">
                    <span>Descrizione</span>
                    <span><%= p.getDescription() %></span>
                </div>
            </div>

            <% } else { %>
            <div class="error">
                <%
                    String mess = "";

                    switch (err) {
                        case "INV_ID":
                            mess = "ID prodotto invalido.";
                            break;

                        case "DB_ERR":
                            mess = "Errore interno al database, riprova più tardi.";
                            break;

                        case "PD_NOT_FOUND":
                            mess = "Prodotto non trovato.";
                            break;
                    }
                %>
                <%= mess %>
            </div>
            <% } %>
        </main>

        <script src="${pageContext.request.contextPath}/catalogue/view/js/SlideshowHandler.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>