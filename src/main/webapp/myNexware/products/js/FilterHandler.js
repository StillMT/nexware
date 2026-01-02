const filterForm = document.getElementById('filter-form');
const startDateInput = document.getElementById('start-date');
const endDateInput = document.getElementById('end-date');
const statusFilterInput = document.getElementById('status-filter');
const searchQueryInput = document.getElementById('search-query');

filterForm.addEventListener('submit', (e) => {
    if (new Date(startDateInput.value) > today || new Date(endDateInput.value) > today ) {
        showPopup(
            "Attenzione",
            "Una o tutte e due le date sono invalide"
        );
        e.preventDefault();
        return;
    }

    if (startDateInput.value === startDate && endDateInput.value === endDate &&
        statusFilterInput.value === statusFilter && searchQueryInput.value === searchQuery)
        e.preventDefault();
});

statusFilterInput.addEventListener('change', () => {
    filterForm.submit();
});

if (dbErr === true)
    showPopup(
        "Attenzione",
        "Errore durante la comunicazione con il database, riprova più tardi."
    );

const pMex = document.querySelector('.product-message');
if (pMex)
    setTimeout(() => pMex.classList.add('hide'), 2500);