package it.unisa.nexware.application.dto;

import java.time.Duration;
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

    public boolean isValid() {
        return Duration.between(dTime, LocalDateTime.now()).toSeconds() <= 30;
    }

    // Attributi
    private String message;
    private LocalDateTime dTime;
}
