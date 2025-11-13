package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailer;
import com.marcoscherzer.msimplegooglemailer.MSimpleKeystore;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createFolderLink;
import static com.marcoscherzer.oauthfilesendfolderforgooglemail.MUtil.createPathIfNotExists;
import static com.marcoscherzer.msimplegooglemailer.MSimpleGoogleMailerUtil.checkMailAddress;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleGoogleMailerService {

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir+"\\mail";
    private static MFileNameWatcher outgoingDesktopLinkWatcher;
    private static MFileNameWatcher sentDesktopLinkWatcher;
    private static boolean askConsent = true;
    private static Path notSentFolder;
    private static Path sentFolder;
    private static Path outFolder;
    private static String clientAndPathUUID;
    private static MConsentOutgoingMailWatcher watcher;
    private static String fromAddress;
    private static String toAddress;

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
            MSimpleGoogleMailer mailer = new MSimpleGoogleMailer("BackupMailer", pw, false);
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
            Path notSentPath = Paths.get(basePath, clientAndPathUUID + "-notSent");

            outFolder = createPathIfNotExists(outPath, "Out folder");
            sentFolder = createPathIfNotExists(sentPath, "Sent folder");
            notSentFolder = createPathIfNotExists(notSentPath, "NotSent folder");

            fromAddress = store.get("fromAddress");
            toAddress = store.get("toAddress");

            watcher = new MConsentOutgoingMailWatcher(outFolder, sentFolder, notSentFolder, mailer, fromAddress, toAddress, clientAndPathUUID, askConsent) {
                private JFrame consentFrame;
                private JLabel counterLabel;

                @Override
                protected void onAskForConsent(MOutgoingMail mail,
                                               Consumer<MOutgoingMail> onSendPermitted,
                                               Runnable onSendCanceled) {
                    SwingUtilities.invokeLater(() -> {
                        consentFrame = new JFrame("Send Mail");
                        consentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        consentFrame.setAlwaysOnTop(true);
                        consentFrame.setLocationRelativeTo(null);

                        // Neues Label für die Betreffzeile
                        JLabel subjectLabel = new JLabel("Betreff: " + mail.getSubject());

                        counterLabel = new JLabel("Attachments: 0");

                        JButton sendButton = new JButton("Senden");
                        JButton cancelButton = new JButton("Abbrechen");

                        sendButton.addActionListener(e -> {
                            onSendPermitted.accept(mail);
                            userConsentActive = false;
                            consentFrame.dispose();
                        });

                        cancelButton.addActionListener(e -> {
                            onSendCanceled.run();
                            userConsentActive = false;
                            consentFrame.dispose();
                        });

                        // Panel mit vertikalem Layout
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.add(subjectLabel);
                        panel.add(counterLabel);

                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(sendButton);
                        buttonPanel.add(cancelButton);

                        panel.add(buttonPanel);

                        consentFrame.add(panel);
                        consentFrame.setSize(400, 150);   // feste Größe
                        consentFrame.setVisible(true);
                    });
                }

                @Override
                protected void onNewAttachmentAdded(int size) {
                    if (counterLabel != null) {
                        counterLabel.setText("Attachments: " + size);
                    }

                }
            };

            watcher.startWatching();

            // Startup-Scan: verarbeite liegengebliebene Dateien
            File[] files = outFolder.toFile().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        watcher.onFileChangedAndUnlocked(f.toPath());
                    }
                }
            }

            createDesktopLinksAndStartWatchingNames();

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
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void  createDesktopLinksAndStartWatchingNames() throws Exception {
        Path absoluteOutgoingLinkPath = createFolderLink(outFolder.toString(), "Outgoing Things");
        Path absoluteSentLinkPath = createFolderLink(sentFolder.toString(), "Sent Things");
        Path absoluteNotSentLinkPath = createFolderLink(notSentFolder.toString(), "NotSent Things");

        if (absoluteOutgoingLinkPath != null || absoluteSentLinkPath != null || absoluteNotSentLinkPath != null) {

            outgoingDesktopLinkWatcher = new MFileNameWatcher(absoluteOutgoingLinkPath) {
                @Override
                protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                    System.out.println("Security integrity violated. Desktop Folder Link \"" + absoluteOutgoingLinkPath + "\" changed. Shutting down.");
                    exit(0);
                }
            };

            sentDesktopLinkWatcher = new MFileNameWatcher(absoluteSentLinkPath) {
                @Override
                protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                    System.out.println("Security integrity violated. Desktop Folder Link \"" + absoluteSentLinkPath + "\" changed. Shutting down.");
                    exit(0);
                }
            };

            MFileNameWatcher notSentDesktopLinkWatcher = new MFileNameWatcher(absoluteNotSentLinkPath) {
                @Override
                protected void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName) {
                    System.out.println("Security integrity violated. Desktop Folder Link \"" + absoluteNotSentLinkPath + "\" changed. Shutting down.");
                    exit(0);
                }
            };

            outgoingDesktopLinkWatcher.startWatching();
            System.out.println("Monitoring \"" + absoluteOutgoingLinkPath + "\" for name integrity violations...");
            sentDesktopLinkWatcher.startWatching();
            System.out.println("Monitoring \"" + absoluteSentLinkPath + "\" for name integrity violations...");
            notSentDesktopLinkWatcher.startWatching();
            System.out.println("Monitoring \"" + absoluteNotSentLinkPath + "\" for name integrity violations...");
        } else {
            System.out.println("No Desktop links could be created. Please create links manually");
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

}







