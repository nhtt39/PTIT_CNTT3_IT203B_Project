package util;

public class ValidationUtil {

    public static boolean isEmail(String email) {
        return email.contains("@");
    }

    public static boolean isPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}