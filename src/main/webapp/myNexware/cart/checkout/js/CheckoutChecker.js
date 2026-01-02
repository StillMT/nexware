const checkoutForm = document.getElementById('checkout-form');
const cardInput = document.getElementById("card-number");
const expiryInput = document.getElementById("card-expiry");
const cvvInput = document.getElementById("card-cvv");
const cardOwnerInput = document.getElementById("card-owner");

const ownerRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ]+(?:\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)+$/;

cardOwnerInput.addEventListener("input", () => {
    if (cardOwnerInput.value.length > 0 && !ownerRegex.test(value))
        cardOwnerInput.style.borderColor = "red";
    else
        cardOwnerInput.style.borderColor = "";
});

cardInput.addEventListener("input", (e) => {
    let value = e.target.value.replace(/\D/g, "");
    let pattern = [4, 4, 4, 4];

    if (value.startsWith("34") || value.startsWith("37"))
        pattern = [4, 6, 5];

    let formatted = "";
    let currentPos = 0;
    for (let size of pattern) {
        if (value.length > currentPos)
            formatted += value.substring(currentPos, currentPos + size) + " ";
        currentPos += size;
    }
    e.target.value = formatted.trim();

    const rawValue = e.target.value.replace(/\s/g, "");
    if (rawValue.length >= 13 && checkLuhn(rawValue))
        e.target.style.borderColor = "";
    else if (rawValue.length > 0)
        e.target.style.borderColor = "red";
});

expiryInput.addEventListener("input", (e) => {
    let value = e.target.value.replace(/\D/g, "");
    if (value.length >= 2)
        e.target.value = value.substring(0, 2) + "/" + value.substring(2, 4);
    else
        e.target.value = value;

    const exp = e.target.value;
    if (exp.length === 5) {
        const [month, year] = exp.split("/").map(n => parseInt(n));
        const now = new Date();
        const currentYear = parseInt(now.getFullYear().toString().slice(-2));
        const currentMonth = now.getMonth() + 1;
        const isExpValid = month >= 1 && month <= 12 && (year > currentYear || (year === currentYear && month >= currentMonth));
        e.target.style.borderColor = isExpValid ? "" : "red";
    }
});

cvvInput.addEventListener("input", (e) => {
    let value = e.target.value.replace(/\D/g, "");
    e.target.value = value;
    e.target.style.borderColor = (value.length >= 3) ? "" : "red";
});

function checkLuhn(number) {
    let sum = 0;
    let shouldDouble = false;
    for (let i = number.length - 1; i >= 0; i--) {
        let digit = parseInt(number.charAt(i));
        if (shouldDouble) {
            digit *= 2;
            if (digit > 9)
                digit -= 9;
        }
        sum += digit;
        shouldDouble = !shouldDouble;
    }
    return (sum % 10) === 0;
}

window.isPaymentValid = function() {
    const num = cardInput.value.replace(/\s/g, "");
    const exp = expiryInput.value;
    const cvv = cvvInput.value;
    const owner = cardOwnerInput.value;

    const [month, year] = exp.split("/").map(n => parseInt(n));
    const now = new Date();
    const currentYear = parseInt(now.getFullYear().toString().slice(-2));
    const currentMonth = now.getMonth() + 1;

    const isExpValid = exp.length === 5 && month >= 1 && month <= 12 &&
        (year > currentYear || (year === currentYear && month >= currentMonth));

    const isLuhnValid = num.length >= 13 && checkLuhn(num);
    const isCvvValid = cvv.length >= 3;
    const isOwnerValid = ownerRegex.test(owner);

    cardInput.style.borderColor = isLuhnValid ? "" : "red";
    expiryInput.style.borderColor = isExpValid ? "" : "red";
    cvvInput.style.borderColor = isCvvValid ? "" : "red";
    cardOwnerInput.style.borderColor = isOwnerValid ? "" : "red";

    return isLuhnValid && isExpValid && isCvvValid && isOwnerValid;
};

checkoutForm.addEventListener('submit', (e) => {
    if (!window.isPaymentValid()) {
        e.preventDefault();
        showPopup(
            "Attenzione",
            "Controlla i dati inseriti riguardanti la carta."
        );
    }
});

switch (err) {
    case "INV_CARD_OW":
        showPopup(
            "Attenzione",
            "Intestatario carta invalido."
        );
        break;

    case "INV_CARD":
        showPopup(
            "Attenzione",
            "Controlla i dati del metodo di pagamento."
        );
        break;

    case "INV_TOTAL":
        showPopup(
            "Attenzione",
            "Incongruenza dei prezzi, riprova."
        );
        break;

    case "DB_ERR":
        showPopup(
            "Attenzione",
            "Stiamo riscontrando problemi con il database, riprova più tardi."
        );
        break;
}