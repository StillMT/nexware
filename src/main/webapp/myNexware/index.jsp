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

                        <!-- I MIEI ANNUNCI -->
                        <a class="card"
                           href="${pageContext.request.contextPath}/myNexware/products">
                            I miei annunci
                        </a>

                        <!-- I MIEI ORDINI -->
                        <a class="card"
                           href="${pageContext.request.contextPath}/myNexware/orders">
                            I miei ordini
                        </a>

                        <!-- IL MIO PROFILO -->
                        <a class="card"
                           href="${pageContext.request.contextPath}/myNexware/profile">
                            Il mio profilo
                        </a>

                        <!-- LOGOUT -->
                        <a class="card logout"
                           href="${pageContext.request.contextPath}/myNexware/logout">
                            Logout
                        </a>

                    </div>
                </div>


            </main>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>
