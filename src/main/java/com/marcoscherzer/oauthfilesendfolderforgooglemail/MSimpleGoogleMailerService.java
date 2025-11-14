package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;
import com.marcoscherzer.msimplegooglemailer.MSimpleKeystore;

import java.nio.file.*;
import java.util.*;

import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderDesktopLink;
import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createPathIfNotExists;
import static com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailerUtil.checkMailAddress;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * unready
 */
public final class MSimpleGoogleMailerService {

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir+"\\mail";
    private static boolean askConsent = true;
    private static Path notSentFolder;
    private static Path sentFolder;
    private static String clientAndPathUUID;
    private static MAttachmentWatcher watcher;
    private static String fromAddress;
    private static String toAddress;
    private static MSimpleGoogleMailer mailer;
    private static MFileNameWatcher notSentDesktopLinkWatcher;
    private static MFileNameWatcher sentDesktopLinkWatcher;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
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
            //MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, false); //dbg
            mailer = new MSimpleGoogleMailer("BackupMailer", pw, true);//false because of dbg
            MSimpleKeystore store = mailer.getKeystore();

            if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                String fromAddress = readMailAddressInput("sender");
                String toAddress = readMailAddressInput("receiver");
                store.add("fromAddress", fromAddress);
                store.add("toAddress", toAddress);
            }

            clientAndPathUUID = store.get("clientId");

            sentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-sent"), "Sent folder");
            notSentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-notSent"), "NotSent folder");

            fromAddress = store.get("fromAddress");
            toAddress = store.get("toAddress");

            MAttachmentWatcher m = watcher = new MAttachmentWatcher(sentFolder, notSentFolder, mailer, fromAddress, toAddress, clientAndPathUUID) {
                @Override
                public final MConsentQuestionResult askForConsent(MOutgoingMail mail) {
                    return () -> true;
                }
            }.startServer();


            sentDesktopLinkWatcher     = createAndWatchFolderDesktopLink(sentFolder.toString(), "Sent Things", "Sent");
            notSentDesktopLinkWatcher = createAndWatchFolderDesktopLink(notSentFolder.toString(), "NotSent Things", "NotSent");

            printConfiguration(fromAddress, toAddress, basePath, clientAndPathUUID, clientAndPathUUID + "-sent");

            System.out.println("To end the Program please press a key");
            new Scanner(System.in).nextLine();
            exit(0);
        } catch (Exception exc) {
            exc.printStackTrace();
            exit(1);
        }
    }

    /**
     * @author Marco Scherzer
     * Copyright © Marco Scherzer. All rights reserved.
     */
    private static void printConfiguration(String fromAddress, String toAddress, String basePath, String outFolder, String sentFolder) {
        System.out.println(
                "\n==========================================================================" +
                        "\n                     OAuth FileSendFolder for GoogleMail" +
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

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(int code) {
        try {
            if(mailer.isInDoNotPersistOAuthTokenMode()) mailer.revokeOAuthTokenFromServer();
            if (watcher != null) watcher.shutdown();
            if(sentDesktopLinkWatcher != null ) sentDesktopLinkWatcher.shutdown();
            if(notSentDesktopLinkWatcher != null ) notSentDesktopLinkWatcher.shutdown();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        System.out.println("Program terminated. Exit code: " + code);
        System.exit(code);
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
    private static MFileNameWatcher createAndWatchFolderDesktopLink(String folderPath, String linkName, String label) throws Exception {
        Path linkPath = createFolderDesktopLink(folderPath, linkName);
        if (linkPath == null) {
            System.out.println("Desktop link for '" + linkName + "' (" + label + ") could not be created. Please create it manually.");
            return null;
        }

        MFileNameWatcher watcher = new MFileNameWatcher(linkPath) {
            @Override
            protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                System.out.println("Security integrity violated. Desktop Folder Link \""
                        + linkPath + "\" (" + label + ") changed. Shutting down.");
                exit(0);
            }
        };

        watcher.startWatching();
        System.out.println("Monitoring \"" + linkPath + "\" (" + label + ") for name integrity violations...");
        return watcher;
    }


}







