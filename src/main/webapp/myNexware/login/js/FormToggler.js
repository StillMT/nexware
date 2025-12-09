const loginFormWrapper = document.getElementById("login-form");
const registerFormWrapper = document.getElementById("register-form");

const loginToggler = document.getElementById("login-toggler");
const registerToggler = document.getElementById("register-toggler");

loginToggler.addEventListener("click", () => {
    loginFormWrapper.style.display = "none";
    registerFormWrapper.style.display = "";
});

registerToggler.addEventListener("click", () => viewRegister());

function viewRegister() {
    loginFormWrapper.style.display = "";
    registerFormWrapper.style.display = "none";
}

const addInfoCheckbox = document.getElementById("add_info");
const addInfoInputs = document.querySelectorAll(".add_info_field");

addInfoCheckbox.addEventListener("change", () =>
    addInfoInputs.forEach(input => {
        let cl = input.classList;

        if (addInfoCheckbox.checked)
            cl.add("visible");
        else
            cl.remove("visible");
    })
);