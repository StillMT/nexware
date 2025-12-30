package it.unisa.nexware.application.enums;

public enum OrderStatus {

    WAITING,
    DELIVERED;

    public String getString() {
        switch (this) {

            case WAITING:
                return "In elaborazione";

            case DELIVERED:
                return "Consegnato";
        }

        return "";
    }

    public static OrderStatus fromString(String str) {
        try {
            return OrderStatus.valueOf(str);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
