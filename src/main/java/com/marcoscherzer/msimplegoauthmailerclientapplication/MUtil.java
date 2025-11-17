package com.marcoscherzer.msimplegoauthmailerclientapplication;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import javax.swing.*;
import java.awt.*;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public final class MUtil {

    /**
     * Creates a junction on the desktop pointing to a target folder.
     * Works only on Windows and only for folders on the same volume.
     * returns the absolute path of the created link if desktop path exists. null otherwise.
     * Author: Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final Path createFolderDesktopLink(String targetFolderPath, String linkName) throws Exception {

     Path desktopPath = getDesktopPath();
            System.out.println("Creating Desktop link for \"" + targetFolderPath + "\" in Desktop folder \"" + desktopPath + "\"");

            if (desktopPath == null || !Files.exists(desktopPath)) {
                System.err.println("Desktop path is invalid");
                return null;
            }
            return createFolderLink(targetFolderPath, desktopPath, linkName);
    }


    /**
     * Creates a junction  pointing to a target folder.
     * Works only on Windows and only for folders on the same volume.
     * returns the absolute path of the created link if desktop path exists. null otherwise.
     * Author: Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final Path createFolderLink(String folderPath, Path linkPath_, String linkName) throws Exception {
        Path linkPath;
        try {

            Path targetPath = Paths.get(folderPath);
            if (!Files.isDirectory(targetPath)) {
                throw new IllegalArgumentException("Target is not a directory. Junction Links only work with folders");
            }

            String safeName = linkName.replaceAll("[^a-zA-Z0-9_\\- ]", "").trim();
            linkPath = linkPath_.resolve(safeName);

            String command = String.format("cmd /c mklink /J \"%s\" \"%s\"", linkPath.toAbsolutePath(), targetPath.toAbsolutePath());

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Desktop link created: " + linkPath.toAbsolutePath());
            } else {
                System.err.println("Failed to create desktop link (already existing ?)");
            }

        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println("Error while creating desktop link " + e.getMessage());
            throw new Exception(e);
        }
        return linkPath.toAbsolutePath();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final Path getDesktopPath() {
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
    public static final Path createPathIfNotExists(Path path, String descriptionName) throws Exception {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println(descriptionName + " created: \"" + path+"\"");
            } else {
                System.out.println(descriptionName + " already exists: \"" + path+"\"");
            }
            return path;
        } catch (IOException e) {
            throw new Exception("Error while creating directory for '" + descriptionName + "': \"" + path+"\"", e);
        }
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
    public static final String createTimeStampFileName(File file){
            String originalName = file.getName();
            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex != -1) {
                extension = originalName.substring(dotIndex);
                originalName = originalName.substring(0, dotIndex);
            }
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            return originalName + "_" + timestamp + extension;
    }


}
