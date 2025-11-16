package com.marcoscherzer.msimplegooglemailer;

import java.util.regex.Pattern;


/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGMailerUtil {


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void checkPasswordComplexity(String password, int minLength, boolean requireUppercase, boolean requireDigit, boolean requireSpecial) throws IllegalArgumentException {
        StringBuilder requirements = new StringBuilder("Password must meet the following requirements:");
        requirements.append("\n- Minimum length: ").append(minLength).append(" characters");
        if (requireUppercase) requirements.append("\n- At least one uppercase letter");
        if (requireDigit) requirements.append("\n- At least one digit");
        if (requireSpecial) requirements.append("\n- At least one special character");

        if (password == null || password.isBlank()) {
            System.err.println(requirements.toString());
            throw new IllegalArgumentException(requirements.toString());
        }

        if (password.length() < minLength) {
            System.err.println(requirements.toString());
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            System.err.println(requirements.toString());
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireDigit && !password.matches(".*\\d.*")) {
            System.err.println(requirements.toString());
            throw new IllegalArgumentException(requirements.toString());
        }

        if (requireSpecial && !password.matches(".*[^a-zA-Z0-9].*")) {
            System.err.println(requirements.toString());
            throw new IllegalArgumentException(requirements.toString());
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final String checkMailAddress(String email) {
        if (email == null || email.isBlank()) {
            System.err.println("Error: Sender address must not be empty.");
            throw new IllegalArgumentException("Sender address must not be empty.");
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) {
            System.err.println("Error: Invalid email format:" + email);
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return email;
    }
}
