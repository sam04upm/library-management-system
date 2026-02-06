public class InputValidator {

    public static boolean validateISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        // ISBN-10 or ISBN-13 format
        return isbn.matches("^[0-9]{10}$|^[0-9]{13}$");
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Malaysian phone format (10–11 digits)
        return phone.matches("^[0-9]{10,11}$");
    }

    public static boolean validateNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
