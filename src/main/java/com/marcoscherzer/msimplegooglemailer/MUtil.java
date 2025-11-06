package com.marcoscherzer.msimplegooglemailer;
import java.io.Console;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MUtil {

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
