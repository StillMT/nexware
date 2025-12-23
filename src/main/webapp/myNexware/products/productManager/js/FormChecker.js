const nameInput = document.getElementById("name");
const descInput = document.getElementById("description");
const catInput = document.getElementById("category");
const priceInput = document.getElementById("price");
const stockInput = document.getElementById("stock");

const initialName = nameInput.value;

const regexName = /^[\s\S]{5,250}$/;
const regexDesc = /^[\s\S]{30,16000}$/;
const regexCategory = /^(0|[1-9]\d*)$/;
const regexPrice = /^(0|[1-9]\d*)(\.\d{1,2})?$/;
const regexStock = /^(0|[1-9]\d*)$/;

function validateField(input, regex) {
    if (!regex.test(input.value)) {
        input.style.borderColor = "red";
        return false;
    } else {
        input.style.borderColor = "";
        return true;
    }
}

nameInput.addEventListener("input", () => {
    validateField(nameInput, regexName);
});

nameInput.addEventListener("blur", () => {
    if (validateField(nameInput, regexName) && nameInput.value !== initialName) {
        const params = new URLSearchParams();
        params.append("product-name", nameInput.value);

        fetch("/myNexware/products/check-product-name?" + params.toString())
            .then(response => response.json())
            .then(data => {
                if (data.result !== true) {
                    nameInput.style.borderColor = "red";
                }
            })
            .catch(() => {
                nameInput.style.borderColor = "red";
            });
    }
});

descInput.addEventListener("input", () => validateField(descInput, regexDesc));
catInput.addEventListener("input", () => validateField(catInput, regexCategory));
priceInput.addEventListener("input", () => validateField(priceInput, regexPrice));
stockInput.addEventListener("input", () => validateField(stockInput, regexStock));

form.addEventListener("submit", (e) => {
    e.preventDefault();

    const isNameValid = validateField(nameInput, regexName);
    const isDescValid = validateField(descInput, regexDesc);
    const isCatValid = validateField(catInput, regexCategory);
    const isPriceValid = validateField(priceInput, regexPrice);
    const isStockValid = validateField(stockInput, regexStock);

    if (!isNameValid || !isDescValid || !isCatValid || !isPriceValid || !isStockValid) {
        return;
    }

    if (nameInput.value !== initialName) {
        const params = new URLSearchParams();
        params.append("product-name", nameInput.value);

        fetch("/myNexware/products/check-product-name?" + params.toString())
            .then(response => response.json())
            .then(data => {
                if (data.result === true) {
                    form.submit();
                } else {
                    nameInput.style.borderColor = "red";
                }
            })
            .catch(() => {
                nameInput.style.borderColor = "red";
            });
    } else {
        form.submit();
    }
});

document.getElementById("delete-product-btn").addEventListener("click", () => {
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "delete"
    input.value = "true";

    form.appendChild(input);
    form.submit();
});