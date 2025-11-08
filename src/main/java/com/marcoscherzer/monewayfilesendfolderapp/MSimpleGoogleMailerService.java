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

import static com.marcoscherzer.monewayfilesendfolderapp.MUtil.createFolderLink;
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
            args = new String[]{"m.scherzer@outlook.com", "m.scherzer@outlook.com"};
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean initialized = Files.exists(keystorePath);
            boolean argsLengthOK = args.length == 2;

            if (!initialized && !argsLengthOK) throw new Exception("Error: On first start, you must setup the fromAddress and toAddress:\njava -jar MSendBackupMail [From-Email-Address] [To-Email-Address]");

            String pw = MUtil.promptPassword(!initialized ? "Please set a password: " : "Please enter your password: ");
            pw = "testTesttest-123";

            System.out.println();
            MSimpleGoogleMailer.setClientKeystoreDir(userDir);
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);
            MSimpleKeystore store = mailer.getKeystore();

            if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                store.add("fromAddress", checkMailAddress(args[0]));
                store.add("toAddress", checkMailAddress(args[1]));
            }

            clientAndPathUUID = store.get("clientId");

            Path outPath = Paths.get(basePath, clientAndPathUUID);
            Path sentPath = Paths.get(basePath,clientAndPathUUID + "-sent");
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
            createFolderLink(outFolder.toString(),"Outgoing Things");
            createFolderLink(sentFolder.toString(),"Sent Things");
            printConfiguration(fromAdress,toAdress,basePath,clientAndPathUUID,clientAndPathUUID + "-sent");

        } catch (Exception exc) {
            //exc.printStackTrace();
            exit(1);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final Path createPathIfNotExists(Path path, String descriptionName) throws Exception {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println(descriptionName + " created: " + path);
            } else {
                System.out.println(descriptionName + " already exists: " + path);
            }
            return path;
        } catch (IOException e) {
            throw new Exception("Error while creating directory for \"" + descriptionName + "\": " + path, e);
        }
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
                "\n==================================================================" +
                        "\n                       MOneWayFileSendFolder for GoogleMail" +
                        "\n  (A little spontaneous Mini Project focusing on simplicity and security)" +
                        "\n" +
                        "\n  Author   : Marco Scherzer" +
                        "\n  Copyright: © Marco Scherzer. All rights reserved." +
                        "\n==================================================================" +
                        "\n  Welcome Mail Sender!" +
                        "\n" +
                        "\n  Base Path:" +
                        "\n  " + basePath +
                        "\n" +
                        "\n  Outgoing Folder:" + outFolder +
                        "\n  Sent Folder:" + sentFolder +
                        "\n" +
                        "\n  Sender Address   : " + fromAddress +
                        "\n  Receiver Address : " + toAddress +
                        "\n" +
                        "\n  New files added to the path will be automatically sent via email." +
                        "\n  After sending, they will be moved to the 'sent' folder." +
                        "\n=================================================================="
        );

    }






}







