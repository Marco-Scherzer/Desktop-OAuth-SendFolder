package com.marcoscherzer.msimplekeystore;

public class MSimpleKeystoreUtil {

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void checkPasswordComplexity(String password, int minLength, boolean requireUppercase, boolean requireDigit, boolean requireSpecial) throws MPasswordComplexityException {
        StringBuilder requirements = new StringBuilder("Password must meet the following requirements:");
        requirements.append("\n- Minimum length: ").append(minLength).append(" characters");
        if (requireUppercase) requirements.append("\n- At least one uppercase letter");
        if (requireDigit) requirements.append("\n- At least one digit");
        if (requireSpecial) requirements.append("\n- At least one special character");

        if (password == null || password.equals("")) {
            System.err.println(requirements.toString());
            throw new MPasswordComplexityException(requirements.toString());
        }

        if (password.length() < minLength) {
            System.err.println(requirements.toString());
            throw new MPasswordComplexityException(requirements.toString());
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            System.err.println(requirements.toString());
            throw new MPasswordComplexityException(requirements.toString());
        }

        if (requireDigit && !password.matches(".*\\d.*")) {
            System.err.println(requirements.toString());
            throw new MPasswordComplexityException(requirements.toString());
        }

        if (requireSpecial && !password.matches(".*[^a-zA-Z0-9].*")) {
            System.err.println(requirements.toString());
            throw new MPasswordComplexityException(requirements.toString());
        }
    }
}
