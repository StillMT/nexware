async function confirmOrder(orderNr, buttonElement) {


    const url = buttonElement.getAttribute('data-url');
    buttonElement.disabled = true;

    const originalText = buttonElement.innerText;
    buttonElement.innerText = "...";

    try {
        const params = new URLSearchParams();
        params.append('orderNr', orderNr);
        params.append('newStatus', 'DELIVERED');

        const response = await fetch(url, {
            method: 'POST',
            body: params,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

        if (response.ok) {
            const data = await response.json();
            if (data.result === true) {

                updateUI(buttonElement);
            } else {
                console.error("Errore server");
                buttonElement.disabled = false;
                buttonElement.innerText = originalText;
            }
        }
    } catch (e) {
        console.error("Errore di rete:", e);
        buttonElement.disabled = false;
        buttonElement.innerText = originalText;
    }
}

function updateUI(buttonElement) {
    const row = buttonElement.closest('.list-row');
    const statusPill = row.querySelector('.status-pill');


    statusPill.innerText = 'Consegnato';
    statusPill.className = 'status-pill delivered';


    buttonElement.style.opacity = '0';
    setTimeout(() => buttonElement.remove(), 300);
}