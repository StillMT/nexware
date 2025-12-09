package it.unisa.nexware.application.enums;

public enum AccountStatus {
    NORMAL,
    LIMITED_INFO,
    LIMITED,
    BANNED;

    public static AccountStatus fromDb(String dbValue) {
        return AccountStatus.valueOf(dbValue.toUpperCase());
    }
}
