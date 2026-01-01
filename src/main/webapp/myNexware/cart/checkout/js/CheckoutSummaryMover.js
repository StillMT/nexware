const summary = document.querySelector('.checkout-summary');
const container = document.querySelector('.side-details');

let currentY = 0;
const speed = 0.08;

function updatePosition() {
    if (!summary || !container) return;

    const rootFontSize = parseFloat(getComputedStyle(document.documentElement).fontSize);
    const gap = 10 * rootFontSize;

    const containerRect = container.getBoundingClientRect();
    const summaryHeight = summary.offsetHeight;
    const containerHeight = container.offsetHeight;
    const startOffset = summary.offsetTop;

    let targetY = gap - (containerRect.top + startOffset);
    const maxTranslate = containerHeight - summaryHeight - startOffset;

    if (targetY < 0)
        targetY = 0;
    else if (targetY > maxTranslate)
        targetY = maxTranslate;

    currentY += (targetY - currentY) * speed;

    if (Math.abs(targetY - currentY) < 0.1)
        currentY = targetY;

    summary.style.transform = `translate3d(0, ${currentY}px, 0)`;

    requestAnimationFrame(updatePosition);
}

requestAnimationFrame(updatePosition);