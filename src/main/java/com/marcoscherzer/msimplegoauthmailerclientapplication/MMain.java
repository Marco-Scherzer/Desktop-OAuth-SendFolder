package com.marcoscherzer.msimplegoauthmailerclientapplication;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegoauthmailer.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private static String[] arg;
    private static TrayIcon trayIcon;

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

            String pw=null;
            MSimpleMailerKeystore store = null;
            try {
                logFrame = new MAppLoggingArea(true);
                setupTrayIcon();
                if (isDbg()) logFrame.getLogFrame().setVisible(true);// sonst nur im tray sichtbar
                Path keystorePath = Paths.get(userDir, "mystore.p12");
                boolean setup = !Files.exists(keystorePath);
                if (setup) {
                    System.out.println("showing setup dialog");
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Setup started. \nInfo: Use the SystemTray Icon to view log Information", TrayIcon.MessageType.INFO);
                    String[] setupedValues = new MAppSetupDialog(true).createAndShowDialog();
                    String from = setupedValues[0];
                    String to = setupedValues[1];
                    pw = new MAppSetupDialog(true).createAndShowDialog()[2];
                    store = new MSimpleMailerKeystore(pw,userDir);
                    store.getKeyStore().put("fromAddress", from);
                    store.getKeyStore().put("toAddress", to);
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Setup completed.", TrayIcon.MessageType.INFO);
                    System.out.println("setup completed");
                } else {
                    System.out.println("showing password dialog");
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Password required.\n", TrayIcon.MessageType.INFO);
                    pw = new MAppPwDialog().createAndShowDialog();
                    if(pw == null) exit(null,1);
                    else{
                        System.out.println("Access-level 1 granted: Application");
                        trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Access-level 1 granted: Application\n", TrayIcon.MessageType.INFO);
                    }
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
                        trayIcon.displayMessage("OAuth Desktop FileSend Folder", "To send mail drag files onto its dolphin icon on the desktop or click it.", TrayIcon.MessageType.INFO);
                    } catch (Throwable exc) {
                        System.err.println(exc.getMessage());
                        exit(exc, 1);
                    }
                }

                @Override
                protected final void onStartOAuth(String oAuthLink) {
                    System.out.println("Additional authentification needed " + oAuthLink);
                    trayIcon.displayMessage("OAuth Desktop FileSend Folder", "Additional authentification needed\n", TrayIcon.MessageType.INFO);

                }

                @Override
                protected final void onOAuthFailure(Throwable exc) {
                    System.err.println(exc.getMessage());
                    exit(exc, 1);
                }

            };
          //  mailer.startOAuth();
        }
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
        trayIcon = new TrayIcon(image, "OAuth Desktop FileSend Folder", traymenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void createMessageDialogAndWait(String message, String title){
         JOptionPane.showMessageDialog(null,message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void exit(Throwable exception,int code) {
        if(isDbg() && exception!=null){exception.printStackTrace();}
        try {
            if(mailer != null && mailer.isInDoNotPersistOAuthTokenMode()) mailer.revokeOAuthTokenFromServer();
            if (watcher != null) watcher.shutdown();
            if(sentDesktopLinkWatcher != null ) sentDesktopLinkWatcher.shutdown();
            if(notSentDesktopLinkWatcher != null ) notSentDesktopLinkWatcher.shutdown();
        } catch (Exception exc) {
            System.err.println(exception.getMessage());
            if(isDbg()) exc.printStackTrace();
        }
        System.out.println("Exiting program with code " + code);
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







