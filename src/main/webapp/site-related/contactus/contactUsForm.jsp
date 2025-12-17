<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    Object r = request.getAttribute("result");
    String result = "";
    if (r != null)
        result = r.toString();
%>

<!DOCTYPE html>
<html lang="it">
<% final String pageTitle = "Contattaci"; %>
<%@ include file="/WEB-INF/includes/head.jspf" %>

<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="main-cont">

    <section class="contact-page">

        <h2 class="page-title">Contattaci</h2>

        <div class="contact-layout">

            <div class="contact-container">

                <h3>Inviaci un messaggio</h3>

                <form method="post" id="contact-form">
                    <div class="row-inputs">
                        <div class="input-group">
                            <label for="name" class="required">Nome azienda</label>
                            <input type="text" id="name" name="name" placeholder="S.r.l" required />
                            <div class="form-error" id="nameError">
                                Nome non valido
                            </div>
                        </div>

                        <div class="input-group">
                            <label for="email" class="required">Email</label>
                            <input type="email" id="email" name="email" placeholder="Email" required />
                            <div class="form-error" id="emailError">
                                Email non valida
                            </div>
                        </div>
                    </div>


                    <div class="input-group">
                        <label for="object" class="required">Oggetto</label>
                        <input type="text" id="object" name="object" placeholder="Oggetto" required />
                        <div class="form-error" id="objectError">
                            Oggetto non valido
                        </div>
                    </div>


                    <div class="input-group">
                        <label for="description" class="required">Descrizione</label>
                        <textarea id="description" name="description" rows="7" placeholder="Messaggio" required></textarea>
                        <div class="form-error" id="messageError">
                            Messaggio non valido
                        </div>
                    </div>

                    <input type="submit" value="Invia richiesta" />

                </form>
            </div>


            <aside class="contact-info-wrapper">

                <h3>Informazioni di contatto</h3>

                <p> <i class="fa-solid fa-location-dot"></i> <strong>Indirizzo:</strong><br>
                    Via Roma 123, Milano, Italia
                </p>

                <p><i class="fa-solid fa-phone"></i> <strong>Telefono:</strong><br>
                    +39 02 1234567
                </p>

                <p> <i class="fa-solid fa-envelope"></i> <strong>Email:</strong><br>
                    info@nexware.com
                </p>

                <p> <i class="fa-solid fa-clock"></i> <strong>Orari:</strong><br>
                    Lun–Ven 9:00–18:00
                </p>

                <div class="map-container">
                    <iframe
                            src="https://maps.google.com/maps?q=Milano&t=&z=13&ie=UTF8&iwloc=&output=embed"
                            loading="lazy">
                    </iframe>
                </div>

            </aside>

        </div>
    </section>

    <%@ include file="/WEB-INF/includes/popup.jspf" %>

</main>

<script>
    const resultStatus = "<%= result %>";
</script>
<script src="js/ResultViewer.js"></script>
<script src="js/ContactFormValidator.js"></script>


<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
