<!DOCTYPE html>
<html>
    <% final String pageTitle = "Area admin"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">

            <div class="cards-wrapper">
                <h1>Area Admin</h1>

                <div class="menu">
                    <a class="card" href="reports/">
                        <i class="fa-solid fa-flag"></i>
                        <span class="card-text">Report</span>
                    </a>
                    <a class="card" href="categories/">
                        <i class="fa-solid fa-list"></i>
                        <span class="card-text">Categorie</span>
                    </a>
                    <a class="card" href="companies/">
                        <i class="fa-solid fa-user-group"></i>
                        <span class="card-text">Utenti</span>
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