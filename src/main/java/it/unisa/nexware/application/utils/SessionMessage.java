package it.unisa.nexware.application.utils;

import java.time.LocalDateTime;

public class SessionMessage {

    // Costruttore
    public SessionMessage(String message) {
        this.message = message;
        dTime = LocalDateTime.now();
    }

    // Metodi di accesso
    public String getMessage() {
        return message;
    }

    public LocalDateTime getTime() {
        return dTime;
    }

    // Attributi
    private String message;
    private LocalDateTime dTime;
}
