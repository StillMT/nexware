const popup = document.getElementById("main-popup");

function showPopup(title, description) {
    document.getElementById("main-popup-title").innerHTML = title;
    document.getElementById("main-popup-description").innerHTML = description;
    popup.classList.add("show");
}

function hidePopup() {
    popup.classList.remove("show");
}