document.addEventListener("DOMContentLoaded", () => {
    const btnAdd = document.getElementById("btnAdd");
    const inputName = document.getElementById("newCatName");

    btnAdd.addEventListener("click", () => addCategory(inputName.value));

    inputName.addEventListener("keypress", (e) => {
        if (e.key === "Enter")
            addCategory(inputName.value);
    });
});

function addCategory(name) {
    const cleanName = name.trim();

    if (cleanName.length === 0) {
        showPopup("Errore", "Il nome della categoria non può essere vuoto.");
        return;
    }

    const url = `/myNexware/admin/categories/editCat/?action=add&catName=${encodeURIComponent(cleanName)}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error("Errore di rete");
            return response.json();
        })
        .then(data => {
            if (data.result && data.id) {
                document.getElementById("newCatName").value = "";

                const grid = document.getElementById("catGrid");
                const emptyMsg = grid.querySelector(".empty-msg");
                if (emptyMsg) emptyMsg.remove();

                const newCatDiv = document.createElement("div");
                newCatDiv.className = "cat";
                newCatDiv.id = `cat-${data.id}`;
                newCatDiv.innerHTML = `
                    <span title="${cleanName}">${cleanName}</span>
                    <button class="btn-delete" onclick="removeCategory(${data.id})" title="Elimina">
                        &times;
                    </button>
                `;

                grid.appendChild(newCatDiv);
            } else
                showPopup("Errore", "Impossibile aggiungere la categoria.");
        })
        .catch(err => {
            console.error(err);
            showPopup("Errore", "Errore di comunicazione con il database.");
        });
}

function removeCategory(id) {
    if (!confirm("Sei sicuro di voler eliminare questa categoria?"))
        return;

    const url = `/myNexware/admin/categories/editCat/?action=rem&catId=${id}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error("Errore HTTP");
            return response.json();
        })
        .then(data => {
            if (data.result) {
                const el = document.getElementById(`cat-${id}`);
                if (el)
                    el.remove();

                const grid = document.getElementById("catGrid");
                if (grid.children.length === 0)
                    grid.innerHTML = '<p class="empty-msg">Nessuna categoria presente.</p>';
            } else
                showPopup("Errore", "Impossibile eliminare la categoria, è possibile che sia associata ancora a dei prodotti.");
        })
        .catch(err => {
            console.error(err);
            showPopup("Errore", "Errore durante l'eliminazione.");
        });
}