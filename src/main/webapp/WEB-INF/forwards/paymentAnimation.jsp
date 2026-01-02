<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Pagamento in corso"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="payment-wrapper">
                <div class="payment-header">
                    <span><%= request.getAttribute("cardOwner") %></span>
                    <span>
                        ••••&nbsp;••••&nbsp;••••&nbsp;<%= request.getAttribute("cardNr") %>
                        <i class="fa-solid fa-spinner fa-spin"></i>
                    </span>
                </div>

                <div class="payment-body">
                    <img src="imgs/card.png" />
                    <span>Elaborazione della transazione.</span>
                </div>
            </div>
        </main>

        <script src="js/PaymentRedirecter.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>