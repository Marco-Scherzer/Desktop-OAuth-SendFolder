package com.marcoscherzer.msimplegoauthmailerclientapplication;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegoauthmailer.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;

import static com.marcoscherzer.msimplegoauthmailerclientapplication.MUtil.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * unready
 */
public final class MMain {

    private static final String userDir = System.getProperty("user.dir");
    private static final String basePath = userDir + "\\mail";
    private static Path notSentFolder;
    private static Path sentFolder;
    private static String clientAndPathUUID;
    private static MAttachmentWatcher watcher;
    private static String fromAddress;
    private static String toAddress;
    private static MSimpleMailer mailer;
    private static MFileNameWatcher notSentDesktopLinkWatcher;
    private static MFileNameWatcher sentDesktopLinkWatcher;

    private static MAppLoggingArea logFrame;
    private static PrintStream originalOut;
    private static PrintStream originalErr;
    private static String[] arg;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public static void main(String[] args) {
         arg = new String[]{"-debug"};
        /*
            FlatLightLaf.setup(), FlatDarkLaf.setup(), FlatIntelliJLaf.setup(), FlatDarculaLaf.setup(),
            FlatArcDarkIJTheme.setup(), FlatArcIJTheme.setup(), FlatArcOrangeIJTheme.setup(),
            FlatArcDarkOrangeIJTheme.setup(), FlatCarbonIJTheme.setup(), FlatCyanLightIJTheme.setup(),
            FlatGrayIJTheme.setup(), FlatGrayDarkIJTheme.setup(), FlatHiberbeeDarkIJTheme.setup(),
            FlatHighContrastIJTheme.setup(), FlatMonokaiProIJTheme.setup(), FlatSolarizedLightIJTheme.setup(),
            FlatSolarizedDarkIJTheme.setup(), FlatDraculaIJTheme.setup()
        */

            FlatCarbonIJTheme.setup();
            UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 16));

            final String[] pw_ = new String[1];
            try {
                logFrame = new MAppLoggingArea(true);
                //if (isDbg()) logFrame.getLogFrame().setVisible(true);// sonst nur im tray sichtbar

                Path keystorePath = Paths.get(userDir, "mystore.p12");
                boolean keystoreFileExists = Files.exists(keystorePath);
                if (!keystoreFileExists) {
                    System.out.println("showing setup dialog");
                    pw_[0] = new MAppSetupDialog(true).createAndShowDialog(true)[2];
                    System.out.println("setup completed");
                } else {
                    System.out.println("showing password dialog");
                    pw_[0] = new MAppPwDialog().createAndShowDialog(true);
                    System.out.println("password valid");
                }

            } catch (Exception exc){ exit(exc,1); }
            String pw = pw_[0];

            MSimpleMailer.setClientKeystoreDir(userDir);
            mailer = new MSimpleMailer("BackupMailer", pw, false) {
                @Override
                protected void onInitializeSucceeded() {
                   try {
                       MSimpleKeystore store = mailer.getKeystore();
                       if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                           String[] setupedValues = new MAppSetupDialog(false).createAndShowDialog(true);
                           String from = setupedValues[0];
                           String to = setupedValues[1];
                           store.add("fromAddress", from);
                           store.add("toAddress", to);
                       }
                       clientAndPathUUID = store.get("clientId");
                       sentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-sent"), "Sent folder");
                       notSentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-notSent"), "NotSent folder");

                       fromAddress = store.get("fromAddress");
                       toAddress = store.get("toAddress");

                       watcher = new MAttachmentWatcher(sentFolder, notSentFolder, mailer, fromAddress, toAddress, clientAndPathUUID) {
                           @Override
                           public final MConsentQuestioner askForConsent(MOutgoingMail mail) {
                               return new MMiniGui(mail, 900, 600, 16);
                           }
                       }.startServer();

                       sentDesktopLinkWatcher = createAndWatchFolderDesktopLink(sentFolder.toString(), "Sent Things", "Sent");
                       notSentDesktopLinkWatcher = createAndWatchFolderDesktopLink(notSentFolder.toString(), "NotSent Things", "NotSent");

                       printConfiguration(fromAddress, toAddress, basePath, clientAndPathUUID, clientAndPathUUID + "-sent");

                       setupTrayIcon();
                   } catch (Throwable exc){ exit(exc,1);}
                }

                @Override
                protected final void onCommonInitializationFailure(Throwable exc) { exit(exc,1);}

                @Override
                protected final void onClientSecretInitalizationFailure(MClientSecretException exc) {
                    createMessageDialogAndWait("Client Secret Error:\n" + exc.getMessage() + " Setup will be restarted on next launch.", "Error");
                    exit(exc,1);
                }

                @Override
                protected final void onPasswordIntegrityFailure(MPasswordIntegrityException exc) {
                    createMessageDialogAndWait("Client Integrity Error:\n" + exc.getMessage(), "Error");
                    exit(exc,1);
                }
            };
            mailer.startInitialization();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void createMessageDialogAndWait(String message, String title){
        SwingUtilities.invokeLater(() -> { JOptionPane.showMessageDialog(null,message, title, JOptionPane.ERROR_MESSAGE);});
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(Throwable exception,int code) {
        System.err.println(exception.getMessage());
        if(isDbg() && exception!=null){exception.printStackTrace();}
        try {
            if(mailer != null && mailer.isInDoNotPersistOAuthTokenMode()) mailer.revokeOAuthTokenFromServer();
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
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static boolean isDbg(){
       return (arg != null && arg[0]!=null && arg[0].equals("-debug"));
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void setupTrayIcon() throws Exception {
        PopupMenu traymenu = new PopupMenu();

        MenuItem showLogItem = new MenuItem("Show Log");
        showLogItem.addActionListener((ActionEvent e) -> {
            if (!logFrame.getLogFrame().isVisible()) {
                logFrame.getLogArea().setVisible(true);
                logFrame.getLogFrame().setState(JFrame.NORMAL);
                logFrame.getLogFrame().toFront();
            } else {
                logFrame.getLogFrame().setVisible(false);
            }
        });
        traymenu.add(showLogItem);

        MenuItem exitItem = new MenuItem("Close Program");
        exitItem.addActionListener((ActionEvent e) -> {
            System.out.println("Program closed via Tray-Icon");
            exit(null,0);
        });
        traymenu.add(exitItem);

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported!");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = ImageIO.read(MMain.class.getResourceAsStream("/5.png"));
        TrayIcon trayIcon = new TrayIcon(image, "OAuth Desktop FileSend Folder", traymenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);

        trayIcon.displayMessage("OAuth Desktop FileSend Folder",
                "OAuth Desktop FileSend Folder started in your system tray.\n" +
                        "To send mail drag files onto its dolphin icon on the desktop or click it.",
                TrayIcon.MessageType.INFO);
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void printConfiguration(String fromAddress, String toAddress, String basePath, String outFolder, String sentFolder) {
        System.out.println(
                "\n==========================================================================" +
                        "\n                     OAuth FileSendFolder" +
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
                exit(null,0);
            }
        };

        watcher.startWatching();
        System.out.println("Monitoring \"" + linkPath + "\" (" + label + ") for name integrity violations...");
        return watcher;
    }


}







