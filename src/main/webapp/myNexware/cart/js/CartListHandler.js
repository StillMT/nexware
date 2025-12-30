const loadingBlocker = document.getElementById('blocker');
const removeButtons = document.querySelectorAll('.item-remove');

removeButtons.forEach(removeButton => removeButton.addEventListener('click', async () => {
    const id = removeButton.dataset.id;
    const itemParent = removeButton.closest('.item');

    toggleBlocker();

    try {
        const response = await fetch('/myNexware/cart/removeProduct?p=' + encodeURI(id));
        const data = await response.json();

        if (data.result === true) {
            itemParent.remove();
            updateCartState();
        } else
            location.reload();
    } catch (ex) {
        showPopup(
            "Attenzione",
            "Errore durante la comunicazione con il database, riprova più tardi."
        );
    }

    setTimeout(() => toggleBlocker(), 500);
}));

function toggleBlocker() {
    loadingBlocker.style.display = loadingBlocker.style.display === 'block' ? 'none' : 'block';
}

function updateCartState() {
    const items = document.querySelectorAll('.item');
    const cartDetails = document.querySelector('.cart-details');

    const fees = items.length > 0 ? parseFloat(cartDetails.dataset.fees) : 0;

    let subtotal = 0;

    items.forEach(item => {
        const priceElement = item.querySelector('.item-price');
        if (priceElement && priceElement.dataset.price)
            subtotal += parseFloat(priceElement.dataset.price);
    });

    const total = subtotal + fees;

    const summaryPrices = document.querySelectorAll('.cart-summary .price');
    if (summaryPrices.length >= 3) {
        summaryPrices[0].innerHTML = formatCurrency(subtotal);
        summaryPrices[1].innerHTML = formatCurrency(fees);
        summaryPrices[2].innerHTML = formatCurrency(total);
    }

    const count = items.length;
    const title = document.querySelector('.cart-title');
    if (title) {
        title.innerText = `Carrello (${count} articol${count === 1 ? 'o' : 'i'})`;
    }

    if (count === 0) {
        const noItemsDiv = document.querySelector('.no-items');
        if (noItemsDiv)
            noItemsDiv.style.display = 'block';
    }
}

function formatCurrency(amount) {
    return '€&nbsp;' + amount.toLocaleString('it-IT', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}