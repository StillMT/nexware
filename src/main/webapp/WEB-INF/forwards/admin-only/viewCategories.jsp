<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.CategoryBean" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Manager categorie"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <% List<CategoryBean> cats = (List<CategoryBean>) request.getAttribute("catList"); %>

            <div class="grid-wrapper">
                <span class="title">Manager categorie</span>

                <div class="add-bar">
                    <input type="text" id="newCatName" placeholder="Nome nuova categoria..." maxlength="50">
                    <button class="btn-primary" id="btnAdd">Aggiungi</button>
                </div>

                <div class="cat-grid" id="catGrid">
                    <% if (cats != null && !cats.isEmpty()) {
                        for (CategoryBean c : cats) { %>
                    <div class="cat" id="cat-<%= c.getId() %>">
                        <span title="<%= c.getName() %>"><%= c.getName() %></span>
                        <button class="btn-delete" onclick="removeCategory(<%= c.getId() %>)" title="Elimina">
                            &times; </button>
                    </div>
                    <%     }
                    } else { %>
                    <p class="empty-msg">Nessuna categoria presente.</p>
                    <% } %>
                </div>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

        </main>

        <script>
            const isEmpty = <%= cats == null || cats.isEmpty() %>;
        </script>
        <script src="js/CategoryManager.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>