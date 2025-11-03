package com.marcoscherzer.msimplegooglemailer;


/**
 * © Marco Scherzer – Alle Rechte vorbehalten
 */

import java.io.Console;
import java.util.Scanner;

public class MConsoleUtil {

    public static final char[] promptPassword(String promptText) {
        Console console = System.console();
        if (console != null) {
            return console.readPassword(promptText);
        } else {
            System.out.print(promptText);
            return new Scanner(System.in).nextLine().toCharArray();
        }
    }
}
