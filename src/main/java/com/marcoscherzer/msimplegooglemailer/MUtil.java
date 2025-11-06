package com.marcoscherzer.msimplegooglemailer;
import java.io.Console;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MUtil {
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Prüft die Passwortkomplexität anhand konfigurierbarer Kriterien.
     */
    public static final String promptPassword(String promptText) {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword("%s", promptText);
            return new String(passwordChars);
        } else {
            System.out.print(promptText);
            return new Scanner(System.in).nextLine();
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Prüft die Passwortkomplexität anhand konfigurierbarer Kriterien.
     */
    public static void checkPasswordComplexity(String password, int minLength, boolean requireUppercase, boolean requireDigit, boolean requireSpecial) throws IllegalArgumentException {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty.");
        }

        StringBuilder requirements = new StringBuilder("Password must meet the following requirements:");
        requirements.append("\n- Minimum length: ").append(minLength).append(" characters");

        if (requireUppercase) requirements.append("\n- At least one uppercase letter");
        if (requireDigit) requirements.append("\n- At least one digit");
        if (requireSpecial) requirements.append("\n- At least one special character");

        if (password.length() < minLength) {
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireDigit && !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireSpecial && !password.matches(".*[^a-zA-Z0-9].*")) {
            throw new IllegalArgumentException(requirements.toString());
        }
    }

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final String checkMailAddress(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Absenderadresse darf nicht leer sein.");
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Ungültiges Email-Format: " + email);
        }
        return email;
    }
}