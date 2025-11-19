package com.marcoscherzer.msimplegoauthmailerapplication;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegoauthmailer.*;
import com.marcoscherzer.msimplegoauthmailerapplication.core.MAttachmentWatcher;
import com.marcoscherzer.msimplekeystore.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;

import static com.marcoscherzer.msimplegoauthmailerapplication.MUtil.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MMain {

    private static MAttachmentWatcher watcher;
    private static MSimpleMailer mailer;
    private static MAppLoggingArea logFrame;
    private static boolean isDbg;
    private static TrayIcon trayIcon;
    private static final String userDir = System.getProperty("user.dir");
    private static final String keystorePath = userDir+"\\mystore.p12";
    private static final String mailFoldersPath = userDir + "\\mail";
    private static String trayIconPathWithinResourcesFolder = "/5.png";

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void main(String[] args) {
        isDbg = (args != null && args[0]!=null && args[0].equals("-debug"));
        isDbg = true; //dbg
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

            MSimpleMailerKeystore store = null;
            try {
                logFrame = new MAppLoggingArea();
                setupTrayIcon();
                if (isDbg) logFrame.getLogFrame().setVisible(true);// sonst nur im tray sichtbar
                boolean setup = !Files.exists(Paths.get(keystorePath));
                if (setup) {
                    System.out.println("showing setup dialog");
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder",
                            "Setup started." + (isDbg ? "\nInfo: Use the SystemTray Icon to view log Information" : ""),
                            TrayIcon.MessageType.INFO);
                    String[] setupedValues = new MAppSetupDialog().showAndWait();
                    if (setupedValues == null) exit(null, 1); // canceled
                    String from = setupedValues[0];
                    String to = setupedValues[1];
                    String pw = setupedValues[2];
                    String clientSecretPath = setupedValues[3];

                    // Keystore erstellen mit ausgewähltem client_secret.json
                    store = new MSimpleMailerKeystore(pw, clientSecretPath, keystorePath);
                    store.getKeyStore().put("fromAddress", from);
                    store.getKeyStore().put("toAddress", to);

                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Setup completed.", TrayIcon.MessageType.INFO);
                    System.out.println("setup completed");
                } else {
                    System.out.println("showing password dialog");
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Password required.\n", TrayIcon.MessageType.INFO);
                    String pw = new MAppPwDialog().showAndWait();
                    if(pw == null) exit(null,1);//canceled
                    store = new MSimpleMailerKeystore(pw,"", keystorePath);
                    System.out.println("Access-level 1 granted: Application");
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Access-level 1 granted: Application\n", TrayIcon.MessageType.INFO);
                }
            } catch (Exception exc){
                System.err.println(exc.getMessage());
                createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                exit(exc,1);
            } catch (MKeystoreException exc) {
                System.err.println(exc.getMessage());
                createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                exit(exc,1);
            } catch (MPasswordIntegrityException exc) {
                System.err.println(exc.getMessage());
                createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                exit(exc,1);
            } catch (MPasswordComplexityException exc) {
                System.err.println(exc.getMessage());
                createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                exit(exc,1);
            } catch (MClientSecretException exc) {
                System.err.println(exc.getMessage());
                createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
                exit(exc,1);
            }

        if(store!=null) {
            mailer = new MSimpleMailer(store, "BackupMailer", true) {
                @Override
                protected final void onOAuthSucceeded() {
                    try {
                        MSimpleKeystore store = mailer.getKeystore();
                        String clientAndPathUUID = store.get("clientId");
                        Path sentFolder = createPathIfNotExists(Paths.get(mailFoldersPath, clientAndPathUUID + "-sent"), "Sent folder");
                        Path unsentFolder = createPathIfNotExists(Paths.get(mailFoldersPath, clientAndPathUUID + "-notSent"), "NotSent folder");

                        String fromAddress = store.get("fromAddress");
                        String toAddress = store.get("toAddress");

                        watcher = new MAttachmentWatcher(sentFolder, unsentFolder, mailer, fromAddress, toAddress, clientAndPathUUID) {
                            @Override
                            public final MConsentQuestioner askForConsent(MOutgoingMail mail) {
                                return new MMiniGui(mail, 900, 600, 16);
                            }
                        }.startServer();

                        Path linkPath = createFolderDesktopLink(sentFolder.toString(), "Sent Things");
                        if (linkPath == null) {System.out.println("Desktop link for '" + linkPath + " could not be created. Please create it manually.");};
                             linkPath = createFolderDesktopLink(unsentFolder.toString(), "Unsent Things");
                        if (linkPath == null) {System.out.println("Desktop link for '" + linkPath + " could not be created. Please create it manually.");};

                        printConfiguration(fromAddress, toAddress, mailFoldersPath, clientAndPathUUID, clientAndPathUUID + "-sent");
                        trayIcon.displayMessage("OAuth Desktop FileSend Folder", "To send mail drag files onto its dolphin icon on the desktop or click it.", TrayIcon.MessageType.INFO);
                    } catch (Throwable exc) {
                        System.err.println(exc.getMessage());
                        exit(exc, 1);
                    }
                }

                @Override
                protected final void onStartOAuth(String oAuthLink) {
                    System.out.println("Additional authentification needed " + oAuthLink);
                    try {
                        new MLinkDialog()
                                .setDialogSize(700,  350)
                                .setHyperlinkListener(MLinkDialog.createDefaultHyperlinkListener())
                                .addText("Additional authentification needed.", "black", 18, "none")
                                .addText("Please open the following address in your browser:", "black", 14, "none")
                                .addHyperlink(oAuthLink, oAuthLink, "blue", 14, "underline")
                                .showAndWait();
                    } catch (Exception exc) {
                        System.err.println(exc.getMessage());
                        exit(exc, 1);
                    }
                    trayIcon.displayMessage(
                            "OAuth Desktop FileSend Folder",
                            "Additional authentification needed\n",
                            TrayIcon.MessageType.INFO
                    );
                }

                @Override
                protected final void onOAuthFailure(Throwable exc) {
                    System.err.println(exc.getMessage());
                    exit(exc, 1);
                }

            };
            mailer.startOAuth();
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
        Image image = ImageIO.read(MMain.class.getResourceAsStream(trayIconPathWithinResourcesFolder));
        trayIcon = new TrayIcon(image, "OAuth Desktop FileSend Folder", traymenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void createMessageDialogAndWait(String message, String title){
         JOptionPane.showMessageDialog(null,message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(Throwable exception,int code) {
        if(isDbg && exception!=null){exception.printStackTrace();}
        try {
            if(mailer != null && mailer.isInDoNotPersistOAuthTokenMode()) mailer.revokeOAuthTokenFromServer();
            if (watcher != null) watcher.shutdown();
        } catch (Exception exc) {
            System.err.println(exception.getMessage());
            if(isDbg) exc.printStackTrace();
        }
        System.out.println("Exiting program with code " + code);
        System.exit(code);
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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

}







