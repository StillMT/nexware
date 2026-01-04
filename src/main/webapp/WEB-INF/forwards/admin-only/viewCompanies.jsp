<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.CompanyBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="it.unisa.nexware.application.enums.AccountStatus" %>

<!DOCTYPE html>
<html>
<% final String pageTitle = "Gestione Compagnie"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
  <div class="list-wrapper">

    <div class="list-wrapper-header">
      <span class="title">Compagnie Registrate</span>
    </div>

    <div class="list">
      <div class="list-row header">
        <span>Ragione Sociale</span>
        <span>Username</span>
        <span>Email</span>
        <span>Telefono</span>
        <span>P.IVA</span>
        <span>Indirizzo</span>
        <span>Data Iscriz.</span>
        <span>Stato</span>
        <span>Azioni</span>
      </div>

      <%
        List<CompanyBean> companies = (List<CompanyBean>) request.getAttribute("companies");
        if (companies != null && !companies.isEmpty()) {
          for (CompanyBean c : companies) {
      %>
      <div class="list-row" id="row-<%= c.getId() %>">
        <span><strong><%= c.getCompanyName() != null ? c.getCompanyName() : "N/D" %></strong></span>
        <span><%= c.getUsername() %></span>
        <span><%= c.getEmail() %></span>
        <span><%= c.getTelephone() %></span>
        <span><%= c.getVat() %></span>
        <span><%= c.getCompanyAddress() != null ? c.getCompanyAddress() : "-" %></span>
        <span><%= c.getSingupTime() != null ? FieldValidator.formatDateTime(c.getSingupTime()) : "-" %></span>
          <span>
            <span class="status-pill <%= c.getStatus().name().toLowerCase() %>" id="status-<%= c.getId() %>">
            <%= c.getStatus().getString() %>
            </span>
          </span>
          <span>
            <button type="button" class="action-button nor" onclick="updateCompanyStatus(<%= c.getId() %>, '<%= AccountStatus.NORMAL %>')">
                    Sblocca
            </button>
            <button type="button" class="action-button lim" onclick="updateCompanyStatus(<%= c.getId() %>, '<%= AccountStatus.LIMITED %>')">
                    Limita
            </button>
            <button type="button" class="action-button ban" onclick="updateCompanyStatus(<%= c.getId() %>, '<%= AccountStatus.BANNED %>')">
                  Banna
            </button>
          </span>
      </div>
      <%      }
      } else {
      %>
      <div class="empty-list">
        Non ci sono compagnie registrate al momento.
      </div>
      <% } %>
    </div>
  </div>

  <%@ include file="/WEB-INF/includes/popup.jspf" %>

</main>

<script>
  const statusLabels = {
    <% for(AccountStatus as : AccountStatus.values()) { %>
    "<%= as.name() %>": "<%= as.getString() %>",
    <% } %>
  };
</script>
<script src="js/CompanyManager.js"></script>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>