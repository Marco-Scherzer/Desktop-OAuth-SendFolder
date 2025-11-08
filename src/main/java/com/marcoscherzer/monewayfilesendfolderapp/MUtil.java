package com.marcoscherzer.monewayfilesendfolderapp;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class MUtil {
    /**
     * Creates a junction on the desktop pointing to a target folder.
     * Works only on Windows and only for folders on the same volume.
     * Author: Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void createFolderLink(String targetFolderPath, String linkName) {
        try {
            Path desktopPath = getDesktopPath();
            System.out.println("Creating Desktop link for \"" + targetFolderPath+"\" in Desktop folder " + desktopPath);
            System.out.println();

            if (desktopPath == null || !Files.exists(desktopPath)) {
                System.err.println("Desktop path is invalid");
                return;
            }

            Path targetPath = Paths.get(targetFolderPath);
            if (!Files.isDirectory(targetPath)) {
                System.err.println("Target is not a directory. Junction Links only work with folders");
                return;
            }

            String safeName = linkName.replaceAll("[^a-zA-Z0-9_\\- ]", "").trim();
            Path linkPath = desktopPath.resolve(safeName);

            String command = String.format("cmd /c mklink /J \"%s\" \"%s\"", linkPath.toAbsolutePath(), targetPath.toAbsolutePath());

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Desktop link created: " + linkPath.toAbsolutePath());
            } else {
                System.err.println("Failed to create desktop link (already existing ?)");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error while creating desktop link " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static Path getDesktopPath() {
        String rawPath = Advapi32Util.registryGetStringValue(
                WinReg.HKEY_CURRENT_USER,
                "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders",
                "Desktop"
        );
        rawPath = rawPath.replace("%USERPROFILE%", System.getenv("USERPROFILE"));
        return Paths.get(rawPath);
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

}
