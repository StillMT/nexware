const loadingBlocker = document.getElementById('blocker');
const removeButtons = document.querySelectorAll('.item-remove');
const checkoutButton = document.querySelector('.cart-summary input[type="submit"]');
const warningCheckout = document.querySelector('.warning-checkout');

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
        } else if (data.result === false)
            showPopup(
                "Attenzione",
                "Errore di sincronizzazione con il database, ricarica la pagina e riprova."
            );
    } catch (ex) {
        showPopup(
            "Attenzione",
            "Errore durante la comunicazione con il server, riprova più tardi."
        );
    }

    toggleBlocker();
}));

function toggleBlocker() {
    loadingBlocker.style.display = loadingBlocker.style.display === 'block' ? 'none' : 'block';
}

function updateCartState() {
    const items = document.querySelectorAll('.item');
    const cartDetails = document.querySelector('.cart-details');

    let subtotal = 0;
    let validCount = 0;

    let oneProductNotValid = false;
    items.forEach(item => {
        const priceElement = item.querySelector('.item-price');
        const stockElement = item.querySelector('.item-stock');


        const isPurchasable = priceElement &&
            stockElement &&
            !stockElement.innerText.toLowerCase().includes('non disponibile');

        if (isPurchasable && priceElement.dataset.price) {
            subtotal += parseFloat(priceElement.dataset.price);
            validCount++;
        } else
            oneProductNotValid = true;
    });

    const fees = validCount > 0 ? parseFloat(cartDetails.dataset.fees) : 0;
    const total = subtotal + fees;

    const summaryPrices = document.querySelectorAll('.cart-summary .price');
    if (summaryPrices.length >= 3) {
        summaryPrices[0].innerHTML = formatCurrency(subtotal);
        summaryPrices[1].innerHTML = formatCurrency(fees);
        summaryPrices[2].innerHTML = formatCurrency(total);
    }

    const count = items.length;
    const title = document.querySelector('.cart-title');

    if (title)
        title.innerText = `Carrello (${count} articol${count === 1 ? 'o' : 'i'})`;

    if (checkoutButton)
        checkoutButton.disabled = validCount === 0;

    if (count === 0) {
        const noItemsDiv = document.querySelector('.no-items');
        if (noItemsDiv)
            noItemsDiv.style.display = 'flex';
    }

    if (!oneProductNotValid)
        warningCheckout.style.display = '';
}

function formatCurrency(amount) {
    return '€&nbsp;' + amount.toLocaleString('it-IT', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

if (dbErr === true)
    showPopup(
        "Attenzione",
        "Errore durante la comunicazione con il database, riprova più tardi."
    );

const addingMex = document.querySelector('.adding-message');
if (addingMex)
    setTimeout(() => addingMex.classList.add('hide'), 2500);