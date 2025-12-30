<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.CategoryBean" %>
<%@ page import="it.unisa.nexware.application.enums.ProductStatus" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    boolean isAdding = "add".equals(request.getParameter("a"));
    ProductBean p = (ProductBean) request.getAttribute("product");
    List<CategoryBean> cats = (List<CategoryBean>) request.getAttribute("cats");
    String err = request.getAttribute("err").toString();
    if (err == null)
        err = "";
%>

<!DOCTYPE html>
<html>
<% final String pageTitle = (isAdding ? "Aggiungi" : "Modifica") + " prodotto"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <% if (err.isEmpty()) { %>

            <div class="p-manager-wrapper">
                <div class="p-manager-header">
                    <span class="title"><%= pageTitle %></span>
                    <hr />
                </div>

                <form id="product-form" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="a" value="<%= request.getParameter("a") %>" />
                    <input type="hidden" name="p" value="<%= request.getParameter("p") %>" />
                    <input type="hidden" name="commit" value="true" />

                    <div class="p-manager-form-inputs">
                        <div class="p-manager-details">
                            <span class="title">Dettagli prodotto</span>

                            <div class="input-row double">
                                <label for="name">Nome prodotto</label>
                                <label for="category">Categoria</label>
                                <input type="text" name="name" id="name" value="<%= p != null ? p.getName() : "" %>" />
                                <select name="category" id="category">
                                    <%
                                        if (cats != null)
                                            for (CategoryBean cat : cats) {
                                    %>
                                    <option value="<%= cat.getId() %>" <%= p != null && cat.getId() == p.getIdCategory() ? "selected" : "" %>><%= cat.getName() %></option>
                                    <% } %>
                                </select>
                            </div>

                            <div class="input-row">
                                <label for="description">Descrizione lunga</label>
                                <textarea name="description" id="description"><%= p != null ? p.getDescription() : "" %></textarea>
                            </div>

                            <div class="input-row">
                                <label>Visibilit&agrave;</label>
                                <span>
                                    <input type="radio" name="status" id="radio-active" value="ACTIVE" <%= p != null ? (p.getStatus() == ProductStatus.ACTIVE ? "checked" : "") : "checked" %> />
                                    <label for="radio-active">Pubblico</label>
                                    <input type="radio" name="status" id="radio-hidden" value="HIDDEN" <%= p != null && p.getStatus() == ProductStatus.HIDDEN ? "checked" : "" %> />
                                    <label for="radio-hidden">Nascosto</label>
                                </span>
                            </div>

                            <div class="input-row double even">
                                <label for="price">Prezzo</label>
                                <label for="stock">Stock</label>
                                <input type="number" name="price" id="price" min="0.01" step="0.01" value="<%= p != null ? p.getPrice() : "" %>" />
                                <input type="number" name="stock" id="stock" min="0" step="1" value="<%= p != null ? p.getStock() : "" %>" />
                            </div>
                        </div>

                        <div class="p-manager-files-wrapper">
                            <div class="p-manager-imgs">
                                <span class="title">Immagini prodotto</span>
                                <input type="file" name="images" id="input-images" accept="image/*" />
                                <div id="images-previewer" class="preview-wrapper"></div>
                            </div>
                            <div class="p-manager-files">
                                <span class="title">File/licenza</span>
                                <input type="file" name="files" id="input-files" />
                                <div id="files-previewer" class="files-list-wrapper"></div>
                            </div>
                        </div>
                    </div>

                    <div class="p-manager-form-controls">
                        <span onclick="history.back()" class="cancel">Annulla</span>
                        <input type="submit" value="Salva prodotto">

                        <% if ("edit".equals(request.getParameter("a"))) { %>
                        <span class="delete" id="delete-product-btn">Elimina prodotto</span>
                        <% } %>
                    </div>
                </form>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

            <% } else { %>
            <div class="error">
                <%
                    String mess = "";

                    switch (err) {
                        case "NO_ACT":
                            mess = "Azione non valida. Riprova dalla dashboard.";
                            break;

                        case "LIMITED_COM":
                            mess = "Il tuo account è limitato. Contatta l'assistenza per aggiungere prodotti.";
                            break;

                        case "NO_INFO_COM":
                            mess = "Devi completare il profilo aziendale prima di poter aggiungere prodotti.";
                            break;

                        case "INV_ID":
                            mess = "ID prodotto non valido.";
                            break;

                        case "PD_NOT_FOUND":
                            mess = "Prodotto non trovato. Potrebbe essere stato eliminato.";
                            break;

                        case "PD_COMPANY_MISMATCH":
                            mess = "Non hai i permessi necessari per modificare questo prodotto.";
                            break;

                        case "INV_PARAM":
                            mess = "Dati non validi o nome prodotto già esistente. Controlla i campi evidenziati.";
                            break;

                        case "ERR_DB":
                            mess = "Errore interno al database durante il salvataggio. Riprova più tardi.";
                            break;
                    }
                %>
                <%= mess %>
            </div>
            <% } %>
        </main>

        <%
            Object imgCnt = request.getAttribute("productImgsCount");
            Object fileList = request.getAttribute("filesList");
            String pId = (p != null) ? String.valueOf(p.getId()) : "0";
        %>
        <script>
            const imgsCount = <%= imgCnt != null ? imgCnt.toString() : "0" %>;
            const fileList = JSON.parse('<%= fileList != null ? fileList.toString().replace("\"", "\\\"") : "[]" %>');
            const productId = "<%= pId %>";
        </script>
        <script src="js/FileHandler.js"></script>
        <script src="js/FormChecker.js"></script>
        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>