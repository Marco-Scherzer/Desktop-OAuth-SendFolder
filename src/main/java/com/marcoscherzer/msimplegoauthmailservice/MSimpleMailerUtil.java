package com.marcoscherzer.msimplegoauthmailservice;

import java.util.regex.Pattern;


/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleMailerUtil {



    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final String checkMailAddress(String email) throws MMailAdressFormatException {
        if (email == null || email.equals("")) {
            System.err.println("Error: Sender address must not be empty.");
            throw new MMailAdressFormatException("Sender address must not be empty.");
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) {
            System.err.println("Error: Invalid email format:" + email);
            throw new MMailAdressFormatException("Invalid email format: " + email);
        }
        return email;
    }
}
