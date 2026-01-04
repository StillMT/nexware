<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ReportBean" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Reports"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="list-wrapper">
                <span class="title">Reports</span>
                <div class="report-list">
                    <div class="row header">
                        <span>ID</span>
                        <span>Nome azienda</span>
                        <span>E-mail</span>
                        <span>Oggetto</span>
                        <span>Descrizione</span>
                    </div>

                    <%
                        List<ReportBean> reports = (List<ReportBean>) request.getAttribute("reports");
                        if (reports != null && !reports.isEmpty()) {
                            for (ReportBean r : reports) {
                    %>
                    <div class="row">
                        <span><%= r.getId() %></span>
                        <span title="<%= r.getCompanyName() %>"><%= r.getCompanyName() %></span>
                        <span title="<%= r.getEmail() %>"><%= r.getEmail() %></span>
                        <span title="<%= r.getObject() %>"><%= r.getObject() %></span>
                        <span title="<%= r.getDescription() %>"><%= r.getDescription() %></span>
                    </div>
                    <%
                            }
                        } else {
                    %>
                    <div class="no-report">Nessun report trovato nel database.</div>
                    <% } %>
                </div>
            </div>
        </main>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>