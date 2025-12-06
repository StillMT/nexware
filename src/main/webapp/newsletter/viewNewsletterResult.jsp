<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String status = (String) request.getAttribute("status");
    String message = "";

    if (status != null)
        message = switch (status) {
            case "OK" -> "E-mail aggiunta correttamente alla newsletter";
            case "EX" -> "E-mail già  iscritta alla newsletter";
            default -> "Operazione non riuscita";
        };
%>

<!DOCTYPE html>
<html>
  <% final String pageTitle = "Benvenuto nella nostra newsletter"; %>
  <%@ include file="/WEB-INF/includes/head.jspf" %>

  <body>
    <%@ include file="/WEB-INF/includes/header.jspf" %>

    <main class="main-cont">
        <div class="newsletter-result">
            <p><%= message %>.</p>
        </div>
    </main>

    <%@ include file="/WEB-INF/includes/footer.jspf" %>
  </body>
</html>