package it.unisa.nexware.application.beans;

public class AdminBean {

    // Costruttori
    public AdminBean() {
        username = "";
    }

    public AdminBean(String username) {
        this.username = username;
    }

    // Metodi di accesso
    public String getUsername() {
        return username;
    }

    // Metodi modificatori
    public void setUsername(String username) {
        this.username = username;
    }

    // Attributi
    private String username;
}
