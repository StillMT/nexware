<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nexware.application.beans.ProductBean" %>
<%@ page import="it.unisa.nexware.application.utils.FieldValidator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.unisa.nexware.application.utils.SessionMessage" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Checkout"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="checkout-wrapper">
                <span class="checkout-title">Checkout</span>

                <div class="checkout-body">
                    <form action="proceedToPay/" method="post" class="checkout-details" id="checkout-form">
                        <div class="product-list-wrapper">
                            <span class="title">Lista prodotti</span>

                            <%
                                List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
                                BigDecimal subtotal = BigDecimal.ZERO;
                                BigDecimal fees = BigDecimal.ZERO;
                                BigDecimal total;
                                int pCount = 0;

                                if (products != null && !products.isEmpty()) {
                                    fees = new BigDecimal("3.99");
                                    pCount = products.size();
                                    for (ProductBean p : products) {
                                        subtotal = subtotal.add(p.getPrice());
                            %>

                            <div class="product">
                                <img class="product-image" src="/catalogue/imgs/<%= p.getId() %>/1" />

                                <div class="product-details">
                                    <span class="product-name"><%= p.getName() %></span>
                                    <span class="company-item-title"><%= p.getCompanyName() %></span>
                                    <span class="product-price"><%= FieldValidator.formatEuroPrice(p.getPrice()) %></span>
                                </div>
                            </div>

                            <%
                                    }
                                } else {
                            %>
                            <div class="no-products">Nessun prodotto idoneo per il checkout.</div>
                            <% } %>
                        </div>

                        <div class="side-details">
                            <div class="payment-wrapper">
                                <span class="title">Metodo di pagamento</span>

                                <div class="payment-row">
                                    <label for="card-owner">Intestatario carta</label>
                                    <input type="text" id="card-owner" name="card-owner" placeholder="Mario Rossi" />
                                </div>

                                <div class="payment-row">
                                    <label for="card-number">Numero Carta</label>
                                    <input type="text" id="card-number" name="card-number" placeholder="0000 0000 0000 0000" inputmode="numeric">
                                </div>

                                <div class="payment-row double">
                                    <div>
                                        <label for="card-expiry">Scadenza</label>
                                        <input type="text" id="card-expiry" name="card-expiry" placeholder="MM/AA" maxlength="5">
                                    </div>
                                    <div>
                                        <label for="card-cvv">CVV</label>
                                        <input type="text" id="card-cvv" name="card-cvv" placeholder="123" maxlength="4">
                                    </div>
                                </div>
                            </div>

                            <div class="checkout-summary">
                                <span class="title">Riepilogo ordine</span>

                                <div class="labeled-price">
                                    <span class="label">Subtotale:</span>
                                    <span class="price"><%= FieldValidator.formatEuroPrice(subtotal) %></span>
                                </div>

                                <div class="labeled-price">
                                    <span class="label">Commissioni:</span>
                                    <span class="price"><%= FieldValidator.formatEuroPrice(fees) %></span>
                                </div>

                                <div class="labeled-price">
                                    <span class="label">Totale:</span>
                                    <span class="price"><%= FieldValidator.formatEuroPrice(total = subtotal.add(fees)) %></span>
                                </div>

                                <input type="hidden" name="calculatedTotal" value="<%= total %>">
                                <input type="submit" value="Paga ora" <%= pCount == 0 ? "disabled" : "" %> />
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

        </main>

        <% SessionMessage sm = (SessionMessage) s.getAttribute("checkoutError"); %>
        <script>
            const err = "<%= sm != null && sm.isValid() ? sm.getMessage() : "" %>";
        </script>
        <script src="js/CheckoutChecker.js"></script>
        <script src="js/CheckoutSummaryMover.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>