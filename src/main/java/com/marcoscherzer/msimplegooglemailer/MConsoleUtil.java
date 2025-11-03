/**
 * © Marco Scherzer – Alle Rechte vorbehalten
 */
package com.marcoscherzer.msimplegooglemailer;

import java.io.Console;
import java.util.Scanner;

public class MConsoleUtil {

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
}

