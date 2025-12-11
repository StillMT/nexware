package it.unisa.nexware.application.enums;

public enum ProductStatus {
    ACTIVE,
    HIDDEN,
    CANCELED;

    public String getString() {
        switch(this) {
            case ACTIVE:
                return "Attivo";

            case HIDDEN:
                return "Nascosto";

            case CANCELED:
                return "Cancellato";
        }

        return "";
    }

    public static ProductStatus fromString(String str) {
        try {
            return ProductStatus.valueOf(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}