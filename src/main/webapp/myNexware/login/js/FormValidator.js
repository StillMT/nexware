const validateVatBtn = document.getElementById("validate_vat");
const vatInput = document.getElementById("vat");

const notValidVatError = document.getElementById("vat-not-valid");
const alreadyRegisteredVatError = document.getElementById("vat-already-registered");
const notFoundEUVatError = document.getElementById("vat-not-found");

const companyNameInput = document.getElementById("company_name");
const registeredOfficeInput = document.getElementById("registered_office");

validateVatBtn.addEventListener("click", async () => {
    function validateVAT(vat) {
        if (!vat || !/^\d{11}$/.test(vat)) return false;

        let sum = 0;
        for (let i = 0; i < 10; i++) {
            let digit = parseInt(vat.charAt(i), 10);

            if (i % 2 === 0)
                sum += digit;
            else {
                let temp = digit * 2;
                if (temp > 9) temp -= 9;
                sum += temp;
            }
        }

        const checkDigit = (10 - (sum % 10)) % 10;
        const lastDigit = parseInt(vat.charAt(10), 10);

        return checkDigit === lastDigit;
    }

    notValidVatError.style.display =
        alreadyRegisteredVatError.style.display =
            notFoundEUVatError.style.display =
                companyNameInput.value =
                    registeredOfficeInput.value = "";

    let vat = vatInput.value;
    if (!validateVAT(vat)) {
        notValidVatError.style.display = "block";
        return;
    }

    let error = false;
    validateVatBtn.style.cursor = "wait";
    try {
        const data = await (await fetch("/get-vat-details?vat=" + vat)).json();

        if (data.result === true) {
            if (data.eu_valid === true) {
                companyNameInput.value = data.companyName;
                registeredOfficeInput.value = data.companyAddress;

                checkCompanyNameAvailability();
            } else if (data.eu_valid === false && data.available === true)
                notFoundEUVatError.style.display = "block";
            else if (data.valid === false)
                notValidVatError.style.display = "block";

            if (data.available === false)
                alreadyRegisteredVatError.style.display = "block";
        } else
            error = true;
    } catch (e) {
        console.error(e);
        error = true;
    }

    if (error)
        showPopup(
            "Attenzione",
            "Stiamo avendo dei problemi con la validazione dell'IVA, riprova più tardi."
        );
    validateVatBtn.style.cursor = "";
});

const registerForm = registerFormWrapper.querySelector("form");

// Inputs
const elUsername = document.getElementById("username_register");
const elPassword = document.getElementById("password_register");
const elRepPassword = document.getElementById("rep_password_register");
const elEmail = document.getElementById("email");
const elPhone = document.getElementById("telephone");

// Div di errore
const errUsernameSyntax = document.getElementById("usernameError");
const errUsernameBusy = document.getElementById("usernameAvailabilityError");
const errPassword = document.getElementById("passwordError");
const errRepPassword = document.getElementById("repPasswordError");
const errEmailSyntax = document.getElementById("emailError");
const errEmailBusy = document.getElementById("emailAvailabilityError");
const errPhoneSyntax = document.getElementById("telephoneError");
const errPhoneBusy = document.getElementById("telephoneAvailabilityError");
const errCompanyNameBusy = document.getElementById("companyNameAvailabilityError");

// Regex
const RX_USERNAME = /^[a-zA-Z0-9_-]{3,16}$/;
const RX_PASSWORD = /^(?=.*[!@#$%^&*(),.?":{}|<>_])[a-zA-Z0-9!@#$%^&*(),.?":{}|<>_]{8,20}$/;
const RX_EMAIL = /^(?=.{1,254}$)[a-zA-Z0-9](?!.*?[.]{2})[a-zA-Z0-9._%+-]{0,63}@[a-zA-Z0-9](?!.*--)[a-zA-Z0-9.-]{0,253}\.[a-zA-Z]{2,}$/;
const RX_PHONE = /^(?:\+39|0039)?(?:3\d{9}|0\d{8,10})$/;

function toggleDisplay(element, shouldShow) {
    element.style.display = shouldShow ? "block" : "none";
}

function isVisible(element) {
    return element.style.display === "block";
}

// Validate
function validateUsernameSyntax() {
    const val = elUsername.value.trim();
    if (!val)
        return false;

    const isValid = RX_USERNAME.test(val);
    toggleDisplay(errUsernameSyntax, !isValid);

    return isValid;
}

function validatePasswordSyntax() {
    const val = elPassword.value;
    if (!val)
        return false;

    const isValid = RX_PASSWORD.test(val);
    toggleDisplay(errPassword, !isValid);

    return isValid;
}

function validateRepPassword() {
    const val = elRepPassword.value;
    const original = elPassword.value;
    if (!val)
        return false;

    const isValid = val === original;
    toggleDisplay(errRepPassword, !isValid);

    return isValid;
}

function validateEmailSyntax() {
    const val = elEmail.value.trim();
    if (!val)
        return false;

    const isValid = RX_EMAIL.test(val);
    toggleDisplay(errEmailSyntax, !isValid);

    return isValid;
}

function validatePhoneSyntax() {
    const val = elPhone.value.trim();
    if (!val)
        return false;

    const isValid = RX_PHONE.test(val);
    toggleDisplay(errPhoneSyntax, !isValid);

    return isValid;
}

// Availability
async function checkAvailability(url, paramName, value, errorElement) {
    toggleDisplay(errorElement, false);

    try {
        const data = await (await fetch(`${url}?${paramName}=${encodeURIComponent(value)}`)).json();

        if (data.result === false)
            toggleDisplay(errorElement, true);
    } catch (e) {
        console.error(e);
    }
}

function checkCompanyNameAvailability() {
    toggleDisplay(errCompanyNameBusy, false);
    const val = companyNameInput.value.trim();

    if (val)
        checkAvailability("/check-company_name", "companyName", val, errCompanyNameBusy);
}

// Listeners
elUsername.addEventListener("blur", () => {
    toggleDisplay(errUsernameBusy, false);
    if (validateUsernameSyntax())
        checkAvailability("/check-username", "username", elUsername.value.trim(), errUsernameBusy);
});

elPassword.addEventListener("input", () => {
    validatePasswordSyntax();
    if (elRepPassword.value)
        validateRepPassword();
});

elRepPassword.addEventListener("input", validateRepPassword);

elEmail.addEventListener("blur", () => {
    toggleDisplay(errEmailBusy, false);
    if (validateEmailSyntax())
        checkAvailability("/check-email", "email", elEmail.value.trim(), errEmailBusy);
});

elPhone.addEventListener("blur", () => {
    toggleDisplay(errPhoneBusy, false);
    if (validatePhoneSyntax())
        checkAvailability("/check-phone", "phone", elPhone.value.trim(), errPhoneBusy);
});

companyNameInput.addEventListener("blur", () => checkCompanyNameAvailability());

// Submit check
registerForm.addEventListener("submit", (e) => {
    let go = false;

    if (!validateUsernameSyntax() || !validatePasswordSyntax() || !validateRepPassword() || !validateEmailSyntax() || !validatePhoneSyntax())
        go = true;

    if (isVisible(errUsernameBusy) || isVisible(errEmailBusy) || isVisible(errPhoneBusy) || isVisible(errCompanyNameBusy))
        go = true;

    if (go) {
        e.preventDefault();
        showPopup(
            "Attenzione",
            "Alcuni campi sono invalidi o risultano già registrati. Controlla gli errori in rosso."
        );
    }
});

// Controllo Login
const loginForm = loginFormWrapper.querySelector("form");
const elUsernameLogin = document.getElementById("username_login");
const elPasswordLogin = document.getElementById("password_login");

loginForm.addEventListener("submit", (e) => {
    const userVal = elUsernameLogin.value.trim();
    const passVal = elPasswordLogin.value;

    const isUserValid = RX_USERNAME.test(userVal);
    const isPassValid = RX_PASSWORD.test(passVal);

    if (!isUserValid || !isPassValid) {
        e.preventDefault();
        showPopupInvalidField();
    }
});

function showPopupInvalidField() {
    showPopup(
        "Attenzione",
        "Le credenziali risultano essere invalide, controllale e riprova."
    );
}

function showPopupErrDB() {
    showPopup(
        "Attenzione",
        "Stiamo avendo problemi nel contattare il database, per favore riprova più tardi."
    );
}

switch (error) {
    case "NOT_VALID":
        showPopupInvalidField();
        break;

    case "ERR_DB":
        showPopupErrDB();
        break;

    case "INC_U_P":
        showPopup(
            "Attenzione",
            "L'username o la password non sono corretti."
        );
        break;

    case "NOT_VALID_R":
        viewRegister();
        showPopup(
            "Attenzione",
            "Uno o più campi sono invalidi, controllali e riprova."
        );
        break;

    case "ERR_DB_R":
        viewRegister();
        showPopupErrDB();
        break;

    case "NO_AUTH":
        showPopup(
            "Attenzione",
            "Devi prima loggarti prima di utilizzare quella risorsa."
        );
        break;
}