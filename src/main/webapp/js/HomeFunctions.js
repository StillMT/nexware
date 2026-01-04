document.addEventListener("DOMContentLoaded", function () {
    init3DTiltEffect();
});

function init3DTiltEffect() {
    const container = document.querySelector(".banner-img-container");
    const img = container ? container.querySelector("img") : null;

    if (!container || !img) return;
    container.addEventListener("mousemove", (e) => {
        const rect = container.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;

        const rotateX = ((y - centerY) / 20) * -1;
        const rotateY = (x - centerX) / 20;

        img.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(1.05)`;
    });

    container.addEventListener("mouseenter", () => {
        img.style.transition = "none";
    });


    container.addEventListener("mouseleave", () => {
        img.style.transition = "transform 0.5s ease";
        img.style.transform = "perspective(1000px) rotateX(0deg) rotateY(0deg) scale(1)";
    });
}