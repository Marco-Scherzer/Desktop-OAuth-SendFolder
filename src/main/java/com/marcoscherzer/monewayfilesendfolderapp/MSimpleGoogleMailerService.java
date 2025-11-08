package com.marcoscherzer.monewayfilesendfolderapp;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;
import com.marcoscherzer.msimplegooglemailer.MSimpleKeystore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.marcoscherzer.monewayfilesendfolderapp.MUtil.createFolderLink;
import static com.marcoscherzer.monewayfilesendfolderapp.MUtil.createPathIfNotExists;
import static com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailerUtil.checkMailAddress;


/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailerService {

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir+"\\mail";
    private static String clientAndPathUUID;
    private static MFileWatcher watcher;
    private static Path sentFolder;
    private static Path outFolder;
    private static String fromAdress;
    private static String toAdress;


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        try {
            System.setErr(System.out);
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean keystoreFileExists = Files.exists(keystorePath);

            String pw = MUtil.promptPassword(!keystoreFileExists ? "Please set a password: " : "Please enter your password: ");

            System.out.println();
            MSimpleGoogleMailer.setClientKeystoreDir(userDir);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);
            MSimpleKeystore store = mailer.getKeystore();

            if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                String fromAddress = readMailAddressInput("sender");
                String toAddress = readMailAddressInput("receiver");
                store.add("fromAddress", fromAddress);
                store.add("toAddress", toAddress);
            }

            clientAndPathUUID = store.get("clientId");

            Path outPath = Paths.get(basePath, clientAndPathUUID);
            Path sentPath = Paths.get(basePath, clientAndPathUUID + "-sent");
            outFolder = createPathIfNotExists(outPath, "Out folder");
            sentFolder = createPathIfNotExists(sentPath, "Sent folder");

            fromAdress = store.get("fromAddress");
            toAdress = store.get("toAddress");


            /**
             *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
             */
            watcher = new MFileWatcher() {
                @Override
                protected void onFileChangedAndUnlocked(Path file) {
                    boolean sent = false;
                    try {
                        String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        MOutgoingMail mail = new MOutgoingMail(fromAdress, toAdress, clientAndPathUUID + ", sendTime " + sendTime)
                                .appendMessageText("Backup " + clientAndPathUUID + "\\" + file.getFileName())
                                .addAttachment(file.toString());

                        mailer.send(mail);
                        System.out.println("Backup sent: " + file.getFileName());
                        sent = true;
                    } catch (Exception sendExc) {
                        System.err.println("Error while sending: " + sendExc.getMessage());
                    }

                    if (sent) {
                        try {
                            Path targetFile = sentFolder.resolve(file.getFileName());
                            Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("File moved to: " + targetFile);
                        } catch (Exception moveExc) {
                            System.err.println("Error while moving the file: " + moveExc.getMessage());
                        }
                    }
                }
            };

            watcher.startWatching(outFolder);
            createFolderLink(outFolder.toString(), "Outgoing Things");
            createFolderLink(sentFolder.toString(), "Sent Things");
            printConfiguration(fromAdress, toAdress, basePath, clientAndPathUUID, clientAndPathUUID + "-sent");

        } catch (Exception exc) {
            //exc.printStackTrace();
            exit(1);
        }
    }

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
        public static String readMailAddressInput(String addressDescription)  {
            Scanner scanner = new Scanner(System.in);
                String out = null;
                while (out == null) {
                    System.out.print("Enter "+ addressDescription +" email address (or type 'exit' to cancel): ");
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Aborted by user.");
                        exit(0);
                    }
                    try {
                        out = checkMailAddress(input);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please try again.");
                    }
                }
            return out;
        }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(int code) {
        try {
            if (watcher != null) watcher.shutdown();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        System.out.println("Program terminated. Exit code: " + code);
        System.exit(code);
    }

    /**
     * @author Marco Scherzer
     * Copyright © Marco Scherzer. All rights reserved.
     */
    private static void printConfiguration(String fromAddress, String toAddress, String basePath, String outFolder, String sentFolder) {
        System.out.println(
                "\n==========================================================================" +
                        "\n                     MOneWayFileSendFolder for GoogleMail" +
                        "\n  (A little spontaneous Mini Project focusing on simplicity and security)" +
                        "\n" +
                        "\n  Author   : Marco Scherzer" +
                        "\n  Copyright: © Marco Scherzer. All rights reserved." +
                        "\n==========================================================================" +
                        "\n  Welcome Mail Sender!" +
                        "\n" +
                        "\n  Base Path:" +
                        "\n  " + basePath +
                        "\n" +
                        "\n  Outgoing Folder: " + outFolder +
                        "\n  Sent Folder: " + sentFolder +
                        "\n" +
                        "\n  Sender Address   : " + fromAddress +
                        "\n  Receiver Address : " + toAddress +
                        "\n" +
                        "\n  New files added to the 'Outgoing Things' Folder on your Desktop " +
                        "\n  will be automatically sent via email." +
                        "\n  After sending, they will be moved to the 'Sent Things' folder." +
                        "\n=========================================================================="
        );

    }






}







