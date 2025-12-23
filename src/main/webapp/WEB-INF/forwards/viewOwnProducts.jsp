<%!
    public String getCatNameById(int id, List<CategoryBean> cats) {
        for (CategoryBean cat : cats)
            if (cat.getId() == id)
                return cat.getName();

        return null;
    }
%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="it.unisa.nexware.application.enums.ProductStatus" %>
<%@ page import="it.unisa.nexware.application.beans.CategoryBean" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String today = request.getAttribute("today").toString();

    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    List<CategoryBean> cats = (List<CategoryBean>) request.getAttribute("category-list");
    String startDate = request.getAttribute("start-date").toString();
    String endDate = request.getAttribute("end-date").toString();
    String statusFilter = request.getAttribute("status-filter").toString();
    String searchQuery = request.getAttribute("search-query").toString();
%>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "I miei Prodotti"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="list-wrapper">
                <div class="list-wrapper-header">
                    <span class="title">I miei prodotti</span>
                    <form action="productManager/" method="post">
                        <input type="hidden" name="a" value="add" />
                        <button class="add-product">Aggiungi prodotto</button>
                    </form>
                </div>

                <%
                    if (s.getAttribute("queryProduct") != null) {
                        s.removeAttribute("queryProduct");

                        String message = "Prodotto ";
                        if ("true".equals(s.getAttribute("isAdded")))
                            message += "inserito";
                        else
                            message += "modificato";
                        message += " correttamente.";
                %>
                <div class="product-message">
                    <i class="fa-solid fa-check"></i>
                    <%= message %>
                </div>
                <% } %>

                <form id="filter-form" method="POST">
                    <div class="list-filter">
                        <div class="filter">
                            <span class="span-separator">Filtro</span>
                            <input type="date" id="start-date" name="start-date" value="<%= startDate %>" max="<%= today %>" />
                            <span class="span-separator">-</span>
                            <input type="date" id="end-date" name="end-date" value="<%= endDate %>" max="<%= today %>" />
                        </div>

                        <div class="filter">
                            <span class="span-separator">Stato</span>
                            <select name="status-filter" id="status-filter">
                                <option value="ALL" <%= statusFilter.equals("ALL") ? "selected" : "" %>>Tutti</option>
                                <% for (ProductStatus ps : ProductStatus.values()) { %>

                                <option value="<%= ps.name() %>" <%= statusFilter.equals(ps.name()) ? "selected" : "" %>>
                                    <%= ps.getString() %>
                                </option>

                                <% } %>
                            </select>
                        </div>

                        <div class="filter">
                            <label for="search-query"><i class="fa-solid fa-magnifying-glass" id="search-icon-filter"></i></label>
                            <input type="search" id="search-query" name="search-query" placeholder="Cerca..." autocomplete="off" />
                        </div>

                        <input type="submit" class="filter-button" value="Filtra" />
                    </div>
                </form>

                <div class="list">
                    <div class="list-row header">
                        <span>Prodotto</span>
                        <span>Nome</span>
                        <span>Categoria</span>
                        <span>Data creazione</span>
                        <span>Data ultima modifica</span>
                        <span>Stato</span>
                        <span>Prezzo</span>
                        <span>Stock licenze</span>
                        <span>Azioni</span>
                    </div>

                    <%
                        if (products != null && !products.isEmpty()) {
                            for (ProductBean p : products) {
                    %>
                    <div class="list-row">
                        <span><img src="/catalogue/imgs/<%= p.getId() %>/1" /></span>
                        <span><%= p.getName() %></span>
                        <span><%= getCatNameById(p.getIdCategory(), cats) %></span>
                        <span><%= FieldValidator.formatDateTime(p.getCreationDate()) %></span>
                        <span><%= FieldValidator.formatDateTime(p.getUpdateDate()) %></span>
                        <span>
                            <span class="status-pill <%= p.getStatus().name().toLowerCase() %>"><%= p.getStatus().getString() %></span>
                        </span>
                        <span><%= FieldValidator.formatEuroPrice(p.getPrice()) %></span>
                        <span><%= p.getStock() %> lic.</span>
                        <span>
                            <% if (p.getStatus() != ProductStatus.CANCELED) { %>
                                <a href="${pageContext.request.contextPath}/catalogue/view?p=<%= p.getId() %>" target="_blank"><span class="action-button">Visualizza</span></a>
                            <% } %>
                            <form action="productManager/" method="post" class="modify-form">
                                <input type="hidden" name="a" value="edit" />
                                <input type="hidden" name="p" value="<%= p.getId() %>" />
                                <button class="action-button">Modifica</button>
                            </form>
                        </span>
                    </div>
                    <% }
                    } else { %>

                    <div class="empty-list">
                        Mi dispiace ma non sono stati trovati prodotti con i filtri usati, prova a modificarli.
                    </div>

                    <% } %>
                </div>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

        </main>

        <script>
            const today = new Date("<%= today %>");
            const startDate = "<%= startDate %>";
            const endDate = "<%= endDate %>";
            const statusFilter = "<%= statusFilter %>";
            const searchQuery = "<%= searchQuery %>";
        </script>
        <script src="../../myNexware/products/js/FilterHandler.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>