package it.unisa.nexware.application.beans;

public class ReportBean {

    // Costruttori
    public ReportBean() {
        id = 0;
        companyName = email = object = description = "";
    }

    public ReportBean(String companyName, String email, String object, String description) {
        this.companyName = companyName;
        this.email = email;
        this.object = object;
        this.description = description;
    }

    public ReportBean(int id, String companyName, String email, String object, String description) {
        this(companyName, email, object, description);
        this.id = id;
    }

    // Metodi di accesso
    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getObject() {
        return object;
    }

    public String getDescription() {
        return description;
    }

    // Attributi
    private int id;
    private String companyName;
    private String email;
    private String object;
    private String description;
}


