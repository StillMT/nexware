const form = document.getElementById("contact-form");

const name = document.getElementById("name");
const email = document.getElementById("email");
const object = document.getElementById("object");
const description = document.getElementById("description");

function showError(errorId, field) {
    const el = document.getElementById(errorId);
    if (el) el.style.display = "block";
    if (field) field.classList.add("input-error");
}

function hideError(errorId, field) {
    const el = document.getElementById(errorId);
    if (el) el.style.display = "none";
    if (field) field.classList.remove("input-error");
}

const regexName = /^[a-zA-Z0-9\s.'-]{2,}$/;
const regexEmail = /^(?=.{1,254}$)[a-zA-Z0-9](?!.*?[.]{2})[a-zA-Z0-9._%+-]{0,63}@[a-zA-Z0-9](?!.*--)[a-zA-Z0-9.-]{0,253}\.[a-zA-Z]{2,}$/;



name.addEventListener("input", () => {
    if (!regexName.test(name.value.trim())) {
        showError("nameError", name);
    } else {
        hideError("nameError", name);
    }
});

email.addEventListener("input", () => {
    if (!regexEmail.test(email.value.trim())) {
        showError("emailError", email);
    } else {
        hideError("emailError", email);
    }
});

object.addEventListener("input", () => {
    if (object.value.trim().length < 3 || object.value.length > 50) {
        showError("objectError", object);
    } else {
        hideError("objectError", object);
    }
});

description.addEventListener("input", () => {
    if (description.value.trim().length < 10 || description.value.length > 500) {
        showError("messageError", description);
    } else {
        hideError("messageError", description);
    }
});

form.addEventListener("submit", function (e) {

    let isValid = true;

    if (!regexName.test(name.value.trim())) {
        showError("nameError", name);
        isValid = false;
    }

    if (!regexEmail.test(email.value.trim())) {
        showError("emailError", email);
        isValid = false;
    }

    if (object.value.trim().length < 3 || object.value.length > 50) {
        showError("objectError", object);
        isValid = false;
    }

    if (description.value.trim().length < 10 || description.value.length > 500) {
        showError("messageError", description);
        isValid = false;
    }

    if (!isValid) {
        e.preventDefault();
    }
});