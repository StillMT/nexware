package it.unisa.nexware.application.beans;

public class CategoryBean {

    // Costruttore
    public CategoryBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //Metodi di accesso
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Attributi
    private int id;
    private String name;
}
