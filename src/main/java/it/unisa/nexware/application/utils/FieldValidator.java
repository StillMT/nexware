package it.unisa.nexware.application.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class FieldValidator {

    // Costruttore
    private FieldValidator() {}

    // Metodi formatting
    public static String formatEuroPrice(BigDecimal price) {
        return String.format("€%,.2f", price);
    }

    public static String formatDateTime(LocalDateTime dt) {
        return String.format("%02d", dt.getDayOfMonth()) + "/" + String.format("%02d", dt.getMonthValue()) +
                "/" + dt.getYear() + " " + String.format("%02d", dt.getHour()) + ":" + String.format("%02d", dt.getMinute());
    }

    // Metodi validate
    public static int idValidate(String id) {
        int pId;

        try {
            pId = Integer.parseInt(id);
        } catch (NumberFormatException | NullPointerException _) { pId = 0; }

        return pId;
    }

    // Company
    public static boolean repPswValidate(String psw, String repPsw) {
        return repPsw.equals(psw);
    }

    public static boolean usernameValidate(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches() && !containsBadWord(username);
    }

    public static boolean passwordValidate(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean emailValidate(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean phoneValidate(String phone) {
        return phone != null && TELEPHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean dateValidate(String date) {
        return date != null && DATE_PATTERN.matcher(date).matches();
    }

    public static boolean containsBadWord(String input) {
        if (input == null)
            return false;

        String normalized = input.toLowerCase()
                .replace("3", "e")
                .replace("0", "o")
                .replace("1", "i")
                .replace("!", "i")
                .replace("$", "s")
                .replace("@", "a");

        for (String word : BAD_WORDS)
            if (normalized.contains(word))
                return true;

        return false;
    }

    public static boolean vatValidate(String vat) {
        if (vat == null || !VAT_PATTERN.matcher(vat).matches())
            return false;

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int digit = Character.getNumericValue(vat.charAt(i));

            if ((i % 2) == 0)
                sum += digit;
            else {
                int temp = digit * 2;
                if (temp > 9)
                    temp = temp - 9;

                sum += temp;
            }
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        int lastDigit = Character.getNumericValue(vat.charAt(10));

        return checkDigit == lastDigit;
    }

    public static boolean companyNameValidate(String companyName) {
        return companyName != null && COMPANY_NAME_PATTERN.matcher(companyName).matches();
    }

    // Product
    public static boolean productNameValidate(String pName) {
        return pName != null && P_NAME_PATTERN.matcher(pName).matches() && !containsBadWord(pName);
    }

    public static boolean productDescValidate(String pDesc) {
        return pDesc != null && P_DESC_PATTERN.matcher(pDesc).matches() && !containsBadWord(pDesc);
    }

    public static boolean productCategoryValidate(String pCat) {
        return pCat != null && P_CATEGORY_PATTERN.matcher(pCat).matches();
    }

    public static boolean productPriceValidate(String pPrice) {
        return pPrice != null && P_PRICE_PATTERN.matcher(pPrice).matches();
    }

    public static boolean productStockValidate(String pStock) {
        return pStock != null && P_STOCK_PATTERN.matcher(pStock).matches();
    }

    // Attributi

    // Product
    private static final Pattern P_NAME_PATTERN = Pattern.compile("^[\\s\\S]{5,250}$");
    private static final Pattern P_DESC_PATTERN = Pattern.compile("^[\\s\\S]{30,16000}$");
    private static final Pattern P_CATEGORY_PATTERN = Pattern.compile("^(0|[1-9]\\d*)$");
    private static final Pattern P_PRICE_PATTERN = Pattern.compile("^(0|[1-9]\\d*)(\\.\\d{1,2})?$");
    private static final Pattern P_STOCK_PATTERN = P_CATEGORY_PATTERN;

    // Company
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,16}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[!@#$%^&*(),.?\":{}|<>_])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>_]{8,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,254}$)[a-zA-Z0-9](?!.*?[.]{2})[a-zA-Z0-9._%+-]{0,63}@[a-zA-Z0-9](?!.*--)[a-zA-Z0-9.-]{0,253}\\.[a-zA-Z]{2,}$");
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^(?:\\+39|0039)?(?:3\\d{9}|0\\d{8,10})$");
    private static final Pattern VAT_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern COMPANY_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9 .,'&()-]{1,255}$");

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private static final Set<String> BAD_WORDS = new HashSet<>(Arrays.asList(
            "ciao", "ciao2"
    ));
}
