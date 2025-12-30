const summary = document.querySelector('.cart-summary');
const container = document.querySelector('.cart-details');

let currentY = 0;
const speed = 0.08;

function updatePosition() {
    if (!summary || !container) return;

    const rootFontSize = parseFloat(getComputedStyle(document.documentElement).fontSize);
    const gap = 10 * rootFontSize;

    const containerRect = container.getBoundingClientRect();
    const summaryHeight = summary.offsetHeight;
    const containerHeight = container.offsetHeight;

    let targetY = -containerRect.top + gap;

    if (targetY < 0) {
        targetY = 0;
    } else if (targetY > containerHeight - summaryHeight) {
        targetY = containerHeight - summaryHeight;
    }

    currentY += (targetY - currentY) * speed;

    if (Math.abs(targetY - currentY) < 0.1) {
        currentY = targetY;
    }

    summary.style.transform = `translate3d(0, ${currentY}px, 0)`;

    requestAnimationFrame(updatePosition);
}

requestAnimationFrame(updatePosition);