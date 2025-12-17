document.addEventListener("DOMContentLoaded", () => {
    forceResetState();
});


window.addEventListener("pageshow", (event) => {
    if (event.persisted) {
        forceResetState();
    }
});

function forceResetState() {
    const form = document.getElementById('profileForm');
    if (form) {
        form.reset();
    }
    cancelEditing();
}

function enableEditing() {
    const inputs = document.querySelectorAll('.editable-field');

    inputs.forEach(input => {
        input.removeAttribute('readonly');
        input.removeAttribute('tabindex');
    });


    if (inputs.length > 0) inputs[0].focus();


    const btnEdit = document.getElementById('btnEdit');
    const btnCancel = document.getElementById('btnCancel');
    const btnSave = document.getElementById('btnSave');

    if (btnEdit) btnEdit.style.display = 'none';
    if (btnCancel) btnCancel.style.display = 'block';
    if (btnSave) btnSave.style.display = 'block';
}

function cancelEditing() {
    const inputs = document.querySelectorAll('.editable-field');
    const form = document.getElementById('profileForm');
    if (form) form.reset();
    inputs.forEach(input => {
        input.setAttribute('readonly', 'true');
        input.setAttribute('tabindex', '-1');


        input.classList.remove("input-error");
        input.blur();
    });


    const wrappers = document.querySelectorAll(".data-value-wrapper");
    wrappers.forEach(wrapper => wrapper.classList.remove("wrapper-error"));


    const btnEdit = document.getElementById('btnEdit');
    const btnCancel = document.getElementById('btnCancel');
    const btnSave = document.getElementById('btnSave');

    if (btnEdit) btnEdit.style.display = 'block';
    if (btnCancel) btnCancel.style.display = 'none';
    if (btnSave) btnSave.style.display = 'none';
}