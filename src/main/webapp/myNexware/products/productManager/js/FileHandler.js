const form = document.getElementById("product-form");
const inputImages = document.getElementById("input-images");
const imagesWrapper = document.getElementById("images-previewer");
const inputFiles = document.getElementById("input-files");
const filesWrapper = document.getElementById("files-previewer");

let newImages = [];
let newFiles = [];

function initExistingImages(productId, count) {
    for (let i = 1; i <= count; i++) {
        const imageUrl = `/catalogue/imgs/${productId}/${i}`;
        const previewItem = createPreviewItem(imageUrl, i.toString(), true);
        imagesWrapper.appendChild(previewItem);
    }
}

function initExistingFiles(existingFilesList) {
    existingFilesList.forEach(fileName => {
        const item = createFileItem(fileName, true);
        filesWrapper.appendChild(item);
    });
}

inputImages.addEventListener("change", () => {
    Array.from(inputImages.files).forEach(file => {
        if (!file.type.startsWith('image/')) return;

        if (newImages.some(f => f.name === file.name)) {
            showPopup("Attenzione", "L'immagine '" + file.name + "' è già stata aggiunta!");

            return;
        }

        newImages.push(file);

        const reader = new FileReader();
        reader.onload = function(e) {
            const previewItem = createPreviewItem(e.target.result, file, false);
            imagesWrapper.appendChild(previewItem);
        }
        reader.readAsDataURL(file);
    });
    inputImages.value = "";
});

function createPreviewItem(src, fileOrIndex, isExisting) {
    const previewItem = document.createElement('div');
    previewItem.className = "preview-item";

    const img = document.createElement('img');
    img.src = src;

    const trashBinSpan = document.createElement("span");
    trashBinSpan.className = "delete-item";
    trashBinSpan.innerHTML = '<i class="fa-solid fa-trash"></i>';

    if (isExisting) {
        const keptInput = document.createElement("input");
        keptInput.type = "hidden";
        keptInput.name = "keptImages";
        keptInput.value = fileOrIndex;
        previewItem.appendChild(keptInput);
    }

    trashBinSpan.addEventListener("click", () => {
        if (!isExisting) {
            const index = newImages.indexOf(fileOrIndex);
            if (index > -1) newImages.splice(index, 1);
        }
        previewItem.remove();
    });

    previewItem.appendChild(img);
    previewItem.appendChild(trashBinSpan);
    return previewItem;
}

inputFiles.addEventListener("change", () => {
    Array.from(inputFiles.files).forEach(file => {

        if (newFiles.some(f => f.name === file.name)) {
            showPopup("Attenzione", "Il file '" + file.name + "' è già presente nei nuovi caricamenti!");
            return;
        }

        let isDuplicateOld = false;
        const existingInputs = document.querySelectorAll('input[name="keptFiles"]');
        existingInputs.forEach(input => {
            if (input.value === file.name) isDuplicateOld = true;
        });

        if (isDuplicateOld) {
            showPopup("Attenzione", "Il file '" + file.name + "' è già presente sul server!");

            return;
        }

        newFiles.push(file);
        const item = createFileItem(file.name, false, file);
        filesWrapper.appendChild(item);
    });
    inputFiles.value = "";
});

function createFileItem(fileName, isExisting, fileObj = null) {
    const item = document.createElement("div");
    item.className = "list-item";

    const nameContainer = document.createElement("span");
    nameContainer.className = "file-name";
    nameContainer.innerText = fileName;

    const trash = document.createElement("i");
    trash.className = "fa-solid fa-trash delete-item";

    if (isExisting) {
        const keptInput = document.createElement("input");
        keptInput.type = "hidden";
        keptInput.name = "keptFiles";
        keptInput.value = fileName;
        item.appendChild(keptInput);
    }

    trash.addEventListener("click", () => {
        if (!isExisting && fileObj) {
            const index = newFiles.indexOf(fileObj);
            if (index > -1) newFiles.splice(index, 1);
        }
        item.remove();
    });

    item.appendChild(nameContainer);
    item.appendChild(trash);

    return item;
}

form.addEventListener("submit", () => {
    const imagesDataTransfer = new DataTransfer();
    newImages.forEach(file => imagesDataTransfer.items.add(file));
    inputImages.files = imagesDataTransfer.files;

    const filesDataTransfer = new DataTransfer();
    newFiles.forEach(file => filesDataTransfer.items.add(file));
    inputFiles.files = filesDataTransfer.files;
});

if(typeof imgsCount !== 'undefined' && imgsCount > 0)
    initExistingImages(productId, imgsCount);

if(typeof fileList !== 'undefined' && fileList.length > 0)
    initExistingFiles(fileList);