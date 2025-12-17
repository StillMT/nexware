document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("profileForm");
    const btnCancel = document.getElementById("btnCancel");
    const btnSave = document.getElementById("btnSave");
    const btnEdit = document.getElementById("btnEdit");


    const elEmail = document.querySelector('input[name="email"]');
    const elPhone = document.querySelector('input[name="telephone"]');
    const elCompanyName = document.querySelector('input[name="company_name"]');
    const elVat = document.querySelector('input[name="vat"]');
    const elAddress = document.querySelector('input[name="registered_office"]');


    let originalEmail = elEmail ? elEmail.value.trim() : "";
    let originalPhone = elPhone ? elPhone.value.trim() : "";
    let originalCompanyName = elCompanyName ? elCompanyName.value.trim() : "";
    let originalVat = elVat ? elVat.value.trim() : "";
    let originalAddress = elAddress ? elAddress.value.trim() : "";

    function updateOriginals() {
        originalEmail = elEmail.value.trim();
        originalPhone = elPhone.value.trim();
        originalCompanyName = elCompanyName.value.trim();
        originalVat = elVat.value.trim();
        originalAddress = elAddress.value.trim();
    }


    function checkChanges() {
        const curEmail = elEmail.value.trim();
        const curPhone = elPhone.value.trim();
        const curName = elCompanyName.value.trim();
        const curVat = elVat.value.trim();
        const curAddress = elAddress.value.trim();

        const hasChanged = (
            curEmail !== originalEmail ||
            curPhone !== originalPhone ||
            curName !== originalCompanyName ||
            curVat !== originalVat ||
            curAddress !== originalAddress
        );

        if (hasChanged) {
            btnSave.disabled = false;
        } else {
            btnSave.disabled = true;
        }
    }

    const allInputs = [elEmail, elPhone, elCompanyName, elVat, elAddress];
    allInputs.forEach(el => {
        if(el) el.addEventListener('input', checkChanges);
    });

    if (btnEdit) {
        btnEdit.addEventListener("click", () => {
            updateOriginals();
            checkChanges();
        });
    }

    const RX_EMAIL = /^(?=.{1,254}$)[a-zA-Z0-9](?!.*?[.]{2})[a-zA-Z0-9._%+-]{0,63}@[a-zA-Z0-9](?!.*--)[a-zA-Z0-9.-]{0,253}\.[a-zA-Z]{2,}$/;
    const RX_PHONE = /^(?:\+39|0039)?(?:3\d{9}|0\d{8,10})$/;

    function validateVATCheckSum(vat) {
        if (!vat || !/^\d{11}$/.test(vat)) return false;
        let sum = 0;
        for (let i = 0; i < 10; i++) {
            let digit = parseInt(vat.charAt(i), 10);
            if (i % 2 === 0) sum += digit;
            else {
                let temp = digit * 2;
                if (temp > 9) temp -= 9;
                sum += temp;
            }
        }
        const checkDigit = (10 - (sum % 10)) % 10;
        const lastDigit = parseInt(vat.charAt(10), 10);
        return checkDigit === lastDigit;
    }


    function setError(element, isError) {
        const wrapper = element.closest(".data-value-wrapper");
        if (isError) {
            if (wrapper) wrapper.classList.add("wrapper-error");
            element.classList.add("input-error");
        } else {
            if (wrapper) wrapper.classList.remove("wrapper-error");
            element.classList.remove("input-error");
        }
        return !isError;
    }


    async function checkAvailability(endpoint, paramName, value, element) {
        try {
            const url = `${contextPath}${endpoint}?${paramName}=${encodeURIComponent(value)}`;
            const response = await fetch(url);
            const data = await response.json();

            if (data.result === false) return setError(element, true);
            else return setError(element, false);
        } catch (e) {
            console.error("Errore server:", e);
            return setError(element, false);
        }
    }


    async function validateEmail() {
        const val = elEmail.value.trim();
        if (!RX_EMAIL.test(val)) return setError(elEmail, true);
        if (val !== originalEmail) {
            return await checkAvailability("/check-email", "email", val, elEmail);
        }
        return setError(elEmail, false);
    }

    async function validatePhone() {
        const val = elPhone.value.trim();
        if (!RX_PHONE.test(val)) return setError(elPhone, true);
        if (val !== originalPhone) {
            return await checkAvailability("/check-phone", "phone", val, elPhone);
        }
        return setError(elPhone, false);
    }

    async function validateCompanyName() {
        const val = elCompanyName.value.trim();
        if (val.length < 2) return setError(elCompanyName, true);
        if (val !== originalCompanyName) {
            return await checkAvailability("/check-company_name", "companyName", val, elCompanyName);
        }
        return setError(elCompanyName, false);
    }

    async function validateVat() {
        const val = elVat.value.trim();
        if (!validateVATCheckSum(val)) return setError(elVat, true);
        if (val !== originalVat) {
            try {
                const url = `${contextPath}/get-vat-details?vat=${encodeURIComponent(val)}`;
                const response = await fetch(url);
                const data = await response.json();
                if (data.available === false) return setError(elVat, true);
                return setError(elVat, false);
            } catch (e) { console.error(e); }
        }
        return setError(elVat, false);
    }

    function validateAddress() {
        const val = elAddress.value.trim();
        const isValid = val.length > 5;
        return setError(elAddress, !isValid);
    }


    if (btnCancel) {
        btnCancel.addEventListener("click", () => {
            const inputs = document.querySelectorAll(".profile-input");
            inputs.forEach(input => input.classList.remove("input-error"));
            const wrappers = document.querySelectorAll(".data-value-wrapper");
            wrappers.forEach(wrapper => wrapper.classList.remove("wrapper-error"));

            form.reset();
            btnSave.disabled = true;
        });
    }


    if (elEmail) elEmail.addEventListener("blur", validateEmail);
    if (elPhone) elPhone.addEventListener("blur", validatePhone);
    if (elVat) elVat.addEventListener("blur", validateVat);
    if (elCompanyName) elCompanyName.addEventListener("blur", validateCompanyName);
    if (elAddress) elAddress.addEventListener("blur", validateAddress);


    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            if (btnSave.disabled) return;

            const submitBtn = document.getElementById("btnSave");


            submitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Conferma Modifiche';
            submitBtn.disabled = true;

            const isEmailValid = await validateEmail();
            const isPhoneValid = await validatePhone();
            const isVatValid = await validateVat();
            const isCompanyValid = await validateCompanyName();
            const isAddressValid = validateAddress();

            if (isEmailValid && isPhoneValid && isVatValid && isCompanyValid && isAddressValid) {
                form.submit();
            } else {

                submitBtn.innerHTML = '<i class="fa-solid fa-check"></i> Conferma Modifiche';
                submitBtn.disabled = false;

                if (typeof showPopup === "function") {
                    showPopup("Attenzione", "Dati non validi o già presenti.");
                } else {
                    alert("Dati non validi.");
                }
            }
        });
    }
});