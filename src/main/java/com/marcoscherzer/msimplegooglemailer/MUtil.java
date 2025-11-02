package com.marcoscherzer.msimplegooglemailer;


import java.util.regex.Pattern;

/**
 * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MUtil {

    /**
     * @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void checkMailAddress(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Absenderadresse darf nicht leer sein.");
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Ungültiges Email-Format: " + email);
        }
    }
}
