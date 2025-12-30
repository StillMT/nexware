<%@ page import="it.unisa.nexware.application.beans.OrderBean" %>
<%@ page import="it.unisa.nexware.application.enums.OrderStatus" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    List<OrderBean> orders = (List<OrderBean>) request.getAttribute("orders");
    Map<Integer, BigDecimal> orderTotals = (Map<Integer, BigDecimal>) request.getAttribute("orderTotals");
    String today = (String) request.getAttribute("today");
    String startDate = (String) request.getAttribute("start-date");
    String endDate = (String) request.getAttribute("end-date");
    String statusFilter = (String) request.getAttribute("status-filter");
    String searchQuery = (String) request.getAttribute("search-query");
%>

<!DOCTYPE html>
<html>
<% final String pageTitle = "I miei ordini"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
    <div class="list-wrapper">

        <div class="list-wrapper-header">
            <span class="title">I miei ordini</span>
        </div>


        <form id="filter-form" method="POST">
            <div class="list-filter">
                <div class="filter">
                    <span class="span-separator">Filtro</span>
                    <input type="date" name="start-date" value="<%= startDate %>" max="<%= today %>"/>
                    <span class="span-separator">-</span>
                    <input type="date" name="end-date" value="<%= endDate %>" max="<%= today %>"/>
                </div>

                <div class="filter">
                    <span class="span-separator">Stato</span>
                    <select name="status-filter">
                        <option value="ALL" <%= "ALL".equals(statusFilter) ? "selected" : "" %>>Tutti</option>
                        <% for (OrderStatus os : OrderStatus.values()) { %>
                        <option value="<%= os.name() %>" <%= os.name().equals(statusFilter) ? "selected" : "" %>>
                            <%= os.getString() %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <div class="filter">
                    <label for="search-query"><i class="fa-solid fa-magnifying-glass"></i></label>
                    <input type="search" id="search-query" name="search-query" placeholder="Cerca numero ordine..."
                           value="<%= searchQuery %>" autocomplete="off"/>
                </div>

                <input type="submit" class="filter-button" value="Filtra"/>
            </div>
        </form>


        <div class="list">
            <div class="list-row header">
                <span>N. Ordine</span>
                <span>Data</span>
                <span>Totale</span>
                <span>Stato</span>
                <span>Dettagli</span>
            </div>

            <% if (orders != null && !orders.isEmpty()) {
                int count = 0;
                for (OrderBean o : orders) {
                    count++;
                    BigDecimal total = (orderTotals != null) ? orderTotals.get(o.getId()) : null;
            %>
            <div class="list-row<%= count == orders.size() ? " final" : "" %>">
                <span><%= o.getOrderNr() %></span>
                <span><%= FieldValidator.formatDate(o.getDate()) %></span>
                <span><%= (total != null) ? FieldValidator.formatEuroPrice(total) : "-" %></span>
                <span>
                    <span class="status-pill <%= o.getState().name().toLowerCase() %>">
                        <%= o.getState().getString() %>
                    </span>
                </span>
                <span>
                    <a href="<%= request.getContextPath() %>/myNexware/orders/view/<%= o.getId() %>">
                        <span class="action-button">Dettagli</span>
                    </a>
                </span>
            </div>
            <% } } else { %>
            <div class="empty-list">
                Non sono stati trovati ordini con i filtri selezionati.
            </div>
            <% } %>
        </div>

    </div>
</main>

<script>
    const today = "<%= today %>";
    const startDate = "<%= startDate %>";
    const endDate = "<%= endDate %>";
    const statusFilter = "<%= statusFilter %>";
    const searchQuery = "<%= searchQuery %>";
</script>

<script src="js/FilterHandler.js"></script>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>