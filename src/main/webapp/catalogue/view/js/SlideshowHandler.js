let slideIndex = 0;

function showSlides(n) {
    const container = document.getElementById("slidesContainer");
    const slides = document.querySelectorAll(".slide");
    const dots = document.getElementsByClassName("dot");

    if (n >= slides.length) slideIndex = 0;
    else if (n < 0) slideIndex = slides.length - 1;
    else slideIndex = n;

    const offset = -slideIndex * 100;
    container.style.transform = `translateX(${offset}%)`;

    for (let i = 0; i < dots.length; i++)
        dots[i].classList.remove("active");

    if (dots[slideIndex])
        dots[slideIndex].classList.add("active");
}

function plusSlides(n) {
    showSlides(slideIndex + n);
}

function currentSlide(n) {
    showSlides(n - 1);
}

document.querySelector(".prev")?.addEventListener("click", () => plusSlides(-1));
document.querySelector(".next")?.addEventListener("click", () => plusSlides(1));

showSlides(slideIndex);