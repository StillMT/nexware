package it.unisa.nexware.application.enums;

public enum AccountStatus {
    NORMAL,
    LIMITED_INFO,
    LIMITED,
    BANNED;

    public String getString() {
        switch(this) {
            case NORMAL:
                return "Normale";

            case LIMITED_INFO:
                return "Info mancanti";

            case LIMITED:
                return "Limitato";

            case BANNED:
                return "Bannato";
        }

        return "";
    }

    public static AccountStatus fromString(String str) {
        try {
            return AccountStatus.valueOf(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}