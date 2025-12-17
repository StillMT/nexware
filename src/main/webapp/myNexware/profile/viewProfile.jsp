<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html>
<% final String pageTitle = "Area Personale"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">
    <div class="form-wrapper">
        <form id="profileForm" action="${pageContext.request.contextPath}/myNexware/profile/" method="POST" autocomplete="off">
            <input type="hidden" name="action" value="updateProfile">
            <span class="form-title">Dati Aziendali e Contatto</span>

            <div class="profile-row">
                <label><i class="fa-solid fa-user"></i> Username</label>
                <span class="data-value-wrapper locked">
                    <input type="text" class="profile-input locked-always" value="<%= loggedCompany.getUsername() %>" readonly tabindex="-1">
                </span>
            </div>

            <div class="profile-row">
                <label><i class="fa-solid fa-at"></i> Email</label>
                <span class="data-value-wrapper">
                    <input type="email" class="profile-input editable-field" name="email" value="<%= loggedCompany.getEmail() %>" readonly tabindex="-1">
                </span>
            </div>

            <div class="profile-row">
                <label><i class="fa-solid fa-phone"></i> Telefono</label>
                <span class="data-value-wrapper">
                    <input type="tel" class="profile-input editable-field" name="telephone" value="<%= loggedCompany.getTelephone() %>" readonly tabindex="-1">
                </span>
            </div>

            <hr class="profile-divider">

            <span class="form-title">Informazioni Legali</span>

            <div class="profile-row">
                <label><i class="fa-solid fa-building"></i> Nome Azienda</label>
                <span class="data-value-wrapper">
                    <input type="text" class="profile-input editable-field" name="company_name" value="<%= loggedCompany.getCompanyName() %>" readonly tabindex="-1">
                </span>
            </div>

            <div class="profile-row">
                <label><i class="fa-solid fa-file-invoice-dollar"></i> Partita IVA (VAT)</label>
                <span class="data-value-wrapper">
                    <input type="text" class="profile-input editable-field" name="vat" value="<%= loggedCompany.getVat() %>" readonly maxlength="11" tabindex="-1">
                </span>
            </div>

            <div class="profile-row">
                <label><i class="fa-solid fa-map-location-dot"></i> Sede Legale</label>
                <span class="data-value-wrapper">
                    <input type="text" class="profile-input editable-field" name="registered_office" value="<%= loggedCompany.getCompanyAddress() %>" readonly tabindex="-1">
                </span>
            </div>

            <div class="action-buttons">
                <button type="button" id="btnEdit" class="btn-primary" onclick="enableEditing()">
                    <i class="fa-solid fa-pen"></i> Modifica Profilo
                </button>

                <button type="button" id="btnCancel" onclick="cancelEditing()">
                    <i class="fa-solid fa-xmark"></i> Annulla
                </button>

                <button type="submit" id="btnSave">
                    <i class="fa-solid fa-check"></i> Conferma Modifiche
                </button>
            </div>
        </form>
    </div>

    <%@ include file="/WEB-INF/includes/popup.jspf" %>

</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>

<script src="${pageContext.request.contextPath}/myNexware/profile/js/profileFunctions.js"></script>

<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>

<script src="${pageContext.request.contextPath}/myNexware/profile/js/RegexFilter.js"></script>

</body>
</html>