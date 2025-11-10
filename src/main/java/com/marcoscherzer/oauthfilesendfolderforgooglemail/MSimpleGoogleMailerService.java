package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;
import com.marcoscherzer.msimplegooglemailer.MSimpleKeystore;

import javax.swing.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderLink;
import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createPathIfNotExists;
import static com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailerUtil.checkMailAddress;


/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailerService {

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir+"\\mail";
    private static String clientAndPathUUID;
    private static MFolderWatcher watcher;
    private static Path sentFolder;
    private static Path outFolder;
    private static String fromAdress;
    private static String toAdress;
    private static MFileNameWatcher outgoingDesktopLinkWatcher;
    private static MFileNameWatcher sentDesktopLinkWatcher;


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        try {
            System.setErr(System.out);
            Path keystorePath = Paths.get(userDir, "mystore.p12");
            boolean keystoreFileExists = Files.exists(keystorePath);

            String pw = MUtil.promptPassword(!keystoreFileExists ? "Please set a password: " : "Please enter your password: ");
            pw = "testTesttest-123";
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
             * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
             */
            watcher = new MFolderWatcher(outFolder) {
                private long t0 = 0; // Initialwert: kein Versand bisher

                @Override
                protected void onFileChangedAndUnlocked(Path file) {
                    ArrayList<MOutgoingMail> toSendMails = new ArrayList<>();
                    boolean askConsent = true; // Benutzerabfrage aktiv?
                    long consentDelayMillis = 3000; // Zeitgrenze für Alert

                    long t = System.currentTimeMillis();
                    String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    MOutgoingMail mail = new MOutgoingMail(fromAdress, toAdress, clientAndPathUUID + ", sendTime " + sendTime)
                            .appendMessageText("Backup " + clientAndPathUUID + "\\" + file.getFileName())
                            .addAttachment(file.toString());
                    toSendMails.add(mail);

                    boolean sendNow = true;
                    if (askConsent && (t - t0 > consentDelayMillis)) {
                        sendNow = showSendAlert();
                        t0 = System.currentTimeMillis(); // Zeitstempel aktualisieren nach Alert
                    }

                    if (!askConsent || sendNow) {
                        try {
                            for (MOutgoingMail ml : toSendMails) {
                                mailer.send(ml);
                            }
                            System.out.println("Mails sent: " + file.getFileName());
                            toSendMails.clear();
                            try {
                                Path targetFile = sentFolder.resolve(file.getFileName());
                                Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("File moved to: " + targetFile);
                            } catch (Exception moveExc) {
                                System.err.println("Error while moving the file: " + moveExc.getMessage());
                            }
                        } catch (Exception sendExc) {
                            System.err.println("Error while sending: " + sendExc.getMessage());
                        }
                    }
                }
            };


            watcher.startWatching();
            Path absoluteOutgoingLinkPath = createFolderLink(outFolder.toString(), "Outgoing Things");
            Path absoluteSentLinkPath = createFolderLink(sentFolder.toString(), "Sent Things");
            if(absoluteOutgoingLinkPath != null || absoluteSentLinkPath != null){

                outgoingDesktopLinkWatcher = new MFileNameWatcher(absoluteOutgoingLinkPath) {
                    @Override
                    protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                        System.out.println("Security integrity violated. Desktop Folder Link \""+absoluteSentLinkPath+"\" changed. Shutting down.");
                        exit(0);
                    }
                };

                sentDesktopLinkWatcher = new MFileNameWatcher(absoluteSentLinkPath) {
                    @Override
                    protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                        System.out.println("Security integrity violated. Desktop Folder Link \""+absoluteSentLinkPath+"\" changed. Shutting down.");
                        exit(0);
                    }
                };

                outgoingDesktopLinkWatcher.startWatching();
                System.out.println("Monitoring \"" + absoluteOutgoingLinkPath + "\" for name integrity violations...");;
                sentDesktopLinkWatcher.startWatching();
                System.out.println("Monitoring \""+absoluteSentLinkPath+"\" for name integrity violations...");
            } else System.out.println("No Desktop links could be created. Please create links manually"); //ask next startup for linknames


            printConfiguration(fromAdress, toAdress, basePath, clientAndPathUUID, clientAndPathUUID + "-sent");
            System.out.println("To end the Program please press a key");
            new Scanner(System.in).nextLine();
            exit(0);
        } catch (Exception exc) {
            exc.printStackTrace();
            exit(1);
        }
    }

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean showSendAlert() {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Do you want to send the backup mail now?",
                "Send Mail",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
        public static final String readMailAddressInput(String addressDescription)  {
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
            if(outgoingDesktopLinkWatcher != null ) outgoingDesktopLinkWatcher.shutdown();
            if(sentDesktopLinkWatcher != null ) sentDesktopLinkWatcher.shutdown();
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
                        "\n                     OneWay FileSendFolder for GoogleMail" +
                        "\n  (A little spontaneous Mini Project focusing on simplicity and security)" +
                        "\n" +
                        "\n  Author   : Marco Scherzer" +
                        "\n  Copyright: Marco Scherzer. All rights reserved." +
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







