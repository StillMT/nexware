<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.beans.CategoryBean" %>
<%@ page import="it.unisa.nexware.application.utils.DisplayHelper" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>

<%
    String pageTitle = "Home";

    List<ProductBean> bannerList = (List<ProductBean>) request.getAttribute("bannerProducts");
    List<ProductBean> featuredProducts = (List<ProductBean>) request.getAttribute("featuredProducts");
    List<CategoryBean> categories = (List<CategoryBean>) request.getAttribute("categories");
%>

<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>

<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">

    <div class="banner-wrapper" id="bannerSliderContainer">
        <%
            if (bannerList != null && !bannerList.isEmpty()) {
                ProductBean p = bannerList.get(0);
        %>
        <div class="banner-content banner-slide active-slide">
            <div class="banner-info">
                <span class="brand-label">Potrebbe piacerti</span>
                <h1 class="banner-title"><%= p.getName() %></h1>
                <p class="banner-desc"><%= p.getDescription() %></p>
                <span class="banner-price"><%= FieldValidator.formatEuroPrice(p.getPrice()) %></span>

                <a href="${pageContext.request.contextPath}/catalogue/view/?p=<%= p.getId() %>" class="btn-primary-home">
                    Acquista Ora
                </a>
            </div>

            <div class="banner-img-container">
                <img src="${pageContext.request.contextPath}/catalogue/imgs/<%= p.getId() %>/1"
                     alt="<%= p.getName() %>"
                     class="slide-3d-img">
            </div>
        </div>
        <% } else { %>
        <div class="banner-content banner-slide active-slide">
            <div class="banner-info">
                <h1 class="banner-title">Benvenuto in NexWare</h1>
                <p class="banner-desc">Il catalogo software professionale per il tuo business.</p>
                <a href="${pageContext.request.contextPath}/catalogue" class="btn-primary-home">Vai al Catalogo</a>
            </div>
        </div>
        <% } %>
    </div>

    <div class="content-wrapper">
        <h2 class="section-header">Nuovi Arrivi</h2>

        <% if (featuredProducts != null && !featuredProducts.isEmpty()) { %>
        <div class="items-grid">
            <% for (ProductBean p : featuredProducts) { %>
            <article class="item-card">

                <div class="item-img-container">
                    <img src="${pageContext.request.contextPath}/catalogue/imgs/<%= p.getId() %>/1"
                         alt="<%= p.getName() %>" loading="lazy">
                </div>

                <div class="item-details">
                    <a href="${pageContext.request.contextPath}/catalogue/view/?p=<%= p.getId() %>" class="item-name-link">
                        <%= p.getName() %>
                    </a>
                    <span class="item-price"><%= FieldValidator.formatEuroPrice(p.getPrice()) %></span>
                </div>
                <div class="item-actions">
                    <a href="${pageContext.request.contextPath}/catalogue/view/?p=<%= p.getId() %>" class="btn-action cart">
                        Acquista ora <i class="fa-solid fa-chevron-right"></i>
                    </a>
                </div>
            </article>
            <% } %>
        </div>
        <% } else { %>
        <p class="empty-msg">Nessun prodotto disponibile al momento.</p>
        <% } %>
    </div>

    <div class="content-wrapper bg-grey">
        <h2 class="section-header">Esplora Categorie</h2>
        <div class="categories-container">
            <%
                if (categories != null) {
                    for (CategoryBean c : categories) {
            %>
            <a href="${pageContext.request.contextPath}/catalogue/?category-filter=<%= c.getId() %>" class="category-box">
                <i class="fa-solid <%= DisplayHelper.getIconForCategory(c.getName()) %>"></i>
                <span class="category-name"><%= c.getName() %></span>
            </a>
            <%
                    }
                }
            %>
        </div>
    </div>

    <div class="info-wrapper">
        <h2 class="section-header">La Nostra Missione</h2>
        <p>NexWare Inc. offre solo il meglio del software professionale.</p>
        <a href="${pageContext.request.contextPath}/site-related/aboutus" class="btn-outline">Scopri di più</a>
    </div>

</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
<script src="${pageContext.request.contextPath}/js/HomeFunctions.js"></script>

</body>
</html>