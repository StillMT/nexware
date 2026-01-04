function updateCompanyStatus(companyId, newStatus) {
    const statusLabel = statusLabels[newStatus] || newStatus;

    if(!confirm("Sei sicuro di voler cambiare lo stato in " + statusLabel + "?")) return;

    const url = '/myNexware/admin/companies/updateStatus';
    const params = new URLSearchParams();
    params.append('id', companyId);
    params.append('status', newStatus);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: params
    })
        .then(response => {
            if (response.ok)
                return response.json();
            throw new Error("Errore di rete");
        })
        .then(data => {
            if (data.result === true) {
                const badge = document.getElementById('status-' + companyId);
                badge.className = 'status-pill ' + newStatus.toLowerCase();
                badge.innerText = statusLabels[newStatus];
            } else {
                showPopup("Attenzione", "Errore durante l'aggiornamento dello stato.");
            }
        })
        .catch(_ => {
            showPopup("Attenzione", "Errore di connessione o del server.");
        });
}