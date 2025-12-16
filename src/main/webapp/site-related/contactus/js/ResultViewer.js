switch (resultStatus) {
    case "OK":
        showPopup(
            "Attenzione",
            "Report inviato con successo!"
        );
        break;

    case "ERR":
        showPopup(
            "Attenzione",
            "Errore riscontrato durante l'invio del report."
        );
        break;
}