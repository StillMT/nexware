<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <% final String pageTitle = "Entra nel nostro fantastico sito!"; %>
    <%@ include file="/WEB-INF/includes/head.jspf" %>

    <body>
        <%@ include file="/WEB-INF/includes/header.jspf" %>

        <main class="main-cont">
            <div class="form-wrapper" id="login-form">
                <span class="form-title">Login</span>

                <form action="${pageContext.request.contextPath}/myNexware/loginEndpoint" method="post" class="login-form">
                    <div class="form-row">
                        <label for="username_login">Username</label>
                        <span>
                            <label for="username_login"><i class="fa-solid fa-user"></i></label>
                            <input type="text" name="username" id="username_login" placeholder="Username" />
                        </span>
                    </div>
                    <div class="form-row">
                        <label for="password_login">Password</label>
                        <span>
                            <label for="password_login"><i class="fa-solid fa-key"></i></label>
                            <input type="password" name="password" id="password_login" placeholder="Password" />
                            <i class="fa-solid fa-eye" id="password_view_key"></i>
                        </span>
                    </div>

                    <div class="form-row final">
                        <span class="underline">Password dimenticata?</span>
                        <span>
                            <input type="checkbox" id="remember_me" name="remember_me" />
                            <label for="remember_me">Ricordami</label>
                        </span>
                    </div>

                    <input type="submit" value="Login" />
                    <span class="not-registered" id="login-toggler">Non hai ancora un account? Iscriviti!</span>
                </form>
            </div>

            <div class="form-wrapper" id="register-form" style="display: none">
                <span class="form-title">Registrazione</span>

                <form action="${pageContext.request.contextPath}/myNexware/registerEndpoint" method="post" class="login-form">
                    <div class="form-row">
                        <label for="username_register">Username</label>
                        <span>
                            <label for="username_register"><i class="fa-solid fa-user"></i></label>
                            <input type="text" name="username" id="username_register" placeholder="Username" min="3" maxlength="16" />
                        </span>

                        <div class="form-error" id="usernameAvailabilityError">
                            Username gi&agrave; registrato
                        </div>
                        <div class="form-error" id="usernameError">
                            L'username deve essere lungo tra i 3 e i 16 caratteri e può contenere solo lettere, numeri, trattini e trattini bassi
                        </div>
                    </div>
                    <div class="form-row">
                        <label for="password_register">Password</label>
                        <span>
                            <label for="password_register"><i class="fa-solid fa-key"></i></label>
                            <input type="password" name="password" id="password_register" placeholder="Password" />
                        </span>

                        <div class="form-error" id="passwordError">
                            La password deve avere minimo 8 caratteri, massimo 20 caratteri ed almeno un carattere speciale (senza spazi)
                        </div>
                    </div>
                    <div class="form-row">
                        <label for="rep_password_register">Ripeti password</label>
                        <span>
                            <label for="rep_password_register"><i class="fa-solid fa-key"></i></label>
                            <input type="password" name="rep-password" id="rep_password_register" placeholder="Ripeti password" />
                        </span>

                        <div class="form-error" id="repPasswordError">
                            Le password non coincidono
                        </div>
                    </div>
                    <div class="form-row">
                        <label for="email">Email</label>
                        <span>
                            <label for="email"><i class="fa-solid fa-at"></i></label>
                            <input type="email" name="email" id="email" placeholder="example@example.com" />
                        </span>

                        <div class="form-error" id="emailAvailabilityError">
                            Email gi&agrave; registrata
                        </div>
                        <div class="form-error" id="emailError">
                            Email non valida
                        </div>
                    </div>
                    <div class="form-row">
                        <label for="telephone">Telefono</label>
                        <span>
                            <label for="telephone"><i class="fa-solid fa-phone"></i></label>
                            <input type="tel" name="telephone" id="telephone" placeholder="02 00000 000" />
                        </span>

                        <div class="form-error" id="telephoneAvailabilityError">
                            Numero di telefono gi&agrave; registrato
                        </div>
                        <div class="form-error" id="telephoneError">
                            Numero di telefono invalido
                        </div>
                    </div>

                    <div class="form-row add_info_field">
                        <label for="vat">Partita IVA</label>
                        <span>
                            <label for="vat"><i class="fa-solid fa-file-invoice-dollar"></i></label>
                            <input type="text" name="vat" id="vat" placeholder="00000000000" maxlength="11" />

                            <span id="validate_vat" class="loading">Valida</span>
                        </span>

                        <div class="form-error" id="vat-not-valid">
                            Partita IVA non valida
                        </div>
                        <div class="form-error" id="vat-already-registered">
                            Partita IVA gi&agrave; registrata
                        </div>
                        <div class="form-error" id="vat-not-found" style="color: #0b4f70">
                            Partita IVA non trovata nei registri UE. Puoi comunque registrarti
                        </div>
                    </div>
                    <div class="form-row add_info_field">
                        <label for="company_name">Nome azienda</label>
                        <span>
                            <label for="company_name"><i class="fa-solid fa-building"></i></label>
                            <input type="text" name="company_name" id="company_name" placeholder="S.r.l." maxlength="255" />
                        </span>

                        <div class="form-error" id="companyNameAvailabilityError">
                            Nome azienda gi&agrave; registrato
                        </div>
                    </div>
                    <div class="form-row add_info_field">
                        <label for="registered_office">Sede legale</label>
                        <span>
                            <label for="registered_office"><i class="fa-solid fa-map-location-dot"></i></label>
                            <input type="text" name="registered_office" id="registered_office" placeholder="Via Roma, 58, Milano (MI) 20099" maxlength="255" />
                        </span>
                    </div>

                    <div class="form-row">
                        <div>
                            <input type="checkbox" id="add_info" name="add_info" value="true" />
                            <label for="add_info">Fornisci informazioni facoltative per l'uso completo dell'e-commerce.</label>
                        </div>
                    </div>

                    <input type="submit" value="Registrati" />

                    <span class="not-registered" id="register-toggler">Hai gi&agrave; un account? Accedi!</span>
                </form>
            </div>

            <%@ include file="/WEB-INF/includes/popup.jspf" %>

        </main>

        <script>
            <% String e = request.getParameter("e"); %>

            const error = "<%= e != null ? e : "" %>";
        </script>

        <script src="js/PasswordVisibilityToggler.js"></script>
        <script src="js/FormToggler.js"></script>
        <script src="js/FormValidator.js"></script>

        <%@ include file="/WEB-INF/includes/footer.jspf" %>
    </body>
</html>