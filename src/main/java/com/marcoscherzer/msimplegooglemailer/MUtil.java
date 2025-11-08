package com.marcoscherzer.msimplegooglemailer;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MUtil {

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public static void createFolderLink(String targetFolderPath, String linkName) {
            try {
                Path desktopPath= getDesktopPathViaPowerShell();
                System.out.println(desktopPath);
                if (desktopPath == null || !Files.exists(desktopPath)) {
                    System.err.println("Desktop path is invalid.");
                    return;
                }

                // Sonderzeichen aus dem Linknamen entfernen
                String safeName = linkName.replaceAll("[^a-zA-Z0-9_\\- ]", "").trim();

                Path linkFile = desktopPath.resolve(safeName + ".url");
                String content =
                        "[InternetShortcut]\n" +
                                "URL=file:///" + targetFolderPath.replace("\\", "/") + "\n" +
                                "IconIndex=0\n";

                Files.write(linkFile, content.getBytes());
                System.out.println("Link created: " + linkFile.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating folder link: " + e.getMessage());
                e.printStackTrace();
            }
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
        public static Path getDesktopPathViaPowerShell() {
            try {
                ProcessBuilder builder = new ProcessBuilder(
                        "powershell",
                        "-NoProfile",
                        "-Command",
                        "[Environment]::GetFolderPath('Desktop')"
                );
                builder.redirectErrorStream(true);
                Process process = builder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        Path desktopPath = Paths.get(line.trim());
                        System.out.println("Resolved desktop path: " + desktopPath);
                        return desktopPath;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error executing PowerShell: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
