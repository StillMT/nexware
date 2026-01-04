package it.unisa.nexware.application.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class DisplayHelper {

    private static final Map<String, String> KEYWORD_ICONS = new LinkedHashMap<>();

    private static final String[] GENERIC_ICONS = {
            "fa-folder",
            "fa-tag",
            "fa-cube",
            "fa-bookmark",
            "fa-star",
            "fa-circle"
    };

    static {

        KEYWORD_ICONS.put("sicurezza", "fa-lock");
        KEYWORD_ICONS.put("security", "fa-lock");
        KEYWORD_ICONS.put("antivirus", "fa-bug");
        KEYWORD_ICONS.put("cyber", "fa-user-secret");


        KEYWORD_ICONS.put("crm", "fa-users");
        KEYWORD_ICONS.put("dati", "fa-chart-pie");
        KEYWORD_ICONS.put("finanza", "fa-chart-line");
        KEYWORD_ICONS.put("marketing", "fa-bullhorn");
        KEYWORD_ICONS.put("sales", "fa-money-bill-alt");

        KEYWORD_ICONS.put("ufficio", "fa-briefcase");
        KEYWORD_ICONS.put("produttivit", "fa-check-square");


        KEYWORD_ICONS.put("cloud", "fa-cloud");
        KEYWORD_ICONS.put("sviluppo", "fa-code");
        KEYWORD_ICONS.put("ai", "fa-brain");
        KEYWORD_ICONS.put("artificiale", "fa-brain");
        KEYWORD_ICONS.put("iot", "fa-wifi");
        KEYWORD_ICONS.put("network", "fa-sitemap");
        KEYWORD_ICONS.put("hardware", "fa-microchip");


        KEYWORD_ICONS.put("foto", "fa-camera");
        KEYWORD_ICONS.put("design", "fa-paint-brush");
        KEYWORD_ICONS.put("gam", "fa-gamepad");
        KEYWORD_ICONS.put("salute", "fa-medkit");
        KEYWORD_ICONS.put("istruzione", "fa-graduation-cap");
        KEYWORD_ICONS.put("formazione", "fa-graduation-cap");
    }

    public static String getIconForCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return GENERIC_ICONS[0];
        }
        String normalized = categoryName.toLowerCase();

        for (Map.Entry<String, String> entry : KEYWORD_ICONS.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        int hash = Math.abs(categoryName.hashCode());
        int index = hash % GENERIC_ICONS.length;

        return GENERIC_ICONS[index];
    }
}