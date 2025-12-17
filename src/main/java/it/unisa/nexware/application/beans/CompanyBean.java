package it.unisa.nexware.application.beans;

import it.unisa.nexware.application.enums.AccountStatus;

public class CompanyBean {

    // Costruttori
    public CompanyBean() {
        id = 0;
        username = email = telephone =
                vat = companyName = companyAddress = "";
    }

    public CompanyBean(int id, String username, String email, String telephone, String vat,
                       String companyName, String companyAddress, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telephone = telephone;
        this.vat = vat;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.status = status;
    }

    public CompanyBean(String username, String email, String telephone, String vat,
                       String companyName, String companyAddress, AccountStatus status) {
        id = 0;
        this.username = username;
        this.email = email;
        this.telephone = telephone;
        this.vat = vat;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.status = status;
    }

    // Metodi di accesso
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getVat() {
        return vat;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public AccountStatus getStatus() {
        return status;
    }

    // Metodi modificatori
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    // Attributi
    private int id;
    private String username;
    private String email;
    private String telephone;
    private String vat;
    private String companyName;
    private String companyAddress;
    private AccountStatus status;
}
