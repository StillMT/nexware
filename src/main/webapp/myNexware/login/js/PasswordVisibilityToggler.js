const visibilityElement = document.getElementById("password_view_key");
const pswField = document.getElementById("password_login");

const fullEye = "fa-solid fa-eye";
const crossedEye = "fa-solid fa-eye-slash";

visibilityElement.addEventListener("click", () => {
    if (visibilityElement.className === fullEye) {
        visibilityElement.className = crossedEye;
        pswField.type = "text";
    } else {
        visibilityElement.className = fullEye;
        pswField.type = "password";
    }
});