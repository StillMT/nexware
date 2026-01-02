Perfetto! Per farle tornare in orizzontale, basta cambiare la direzione del flex-box da column a row e allineare gli elementi a sinistra. In questo modo l'icona e il testo saranno sulla stessa riga, come in una lista di pulsanti eleganti.

Ecco il codice aggiornato:

1. JSP: AreaPersonale.jsp
La struttura rimane quasi identica, ho solo aggiunto una classe per gestire meglio lo spazio del testo.

Java

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<% final String pageTitle = "Area personale"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">

    <div class="area-personale">
        <h1>Area Personale</h1>

        <div class="menu">

            <a class="card" href="${pageContext.request.contextPath}/myNexware/products">
                <i class="fa-solid fa-bullhorn"></i>
                <span class="card-text">I miei prodotti</span>
            </a>

            <a class="card" href="${pageContext.request.contextPath}/myNexware/orders">
                <i class="fa-solid fa-box-open"></i>
                <span class="card-text">I miei ordini</span>
            </a>

            <a class="card" href="${pageContext.request.contextPath}/myNexware/profile">
                <i class="fa-solid fa-user-gear"></i>
                <span class="card-text">Il mio profilo</span>
            </a>

            <a class="card logout" href="${pageContext.request.contextPath}/myNexware/logout">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span class="card-text">Logout</span>
            </a>

        </div>
    </div>

</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>