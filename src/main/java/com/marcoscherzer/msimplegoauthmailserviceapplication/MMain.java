package com.marcoscherzer.msimplegoauthmailserviceapplication;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.gmail.GmailScopes;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthHelper;
import com.marcoscherzer.msimplegoauthhelper.MSimpleOAuthKeystore;
import com.marcoscherzer.msimplegoauthhelper.swinggui.MAppRedirectLinkDialog;
import com.marcoscherzer.msimplegoauthhelper.swinggui.MSpinnerOverlayFrame;
import com.marcoscherzer.msimplegoauthmailservice.MMailService;
import com.marcoscherzer.msimplegoauthmailservice.MOutgoingMail;
import com.marcoscherzer.msimplegoauthmailservice.swinggui.MAppSendGui;
import com.marcoscherzer.msimplegoauthmailserviceapplication.core.MAttachmentWatcher;
import com.marcoscherzer.msimplekeystore.MSimpleKeystore;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static com.marcoscherzer.msimplegoauthmailserviceapplication.MAuthFlow_SwingGui.createMessageDialogAndWait;
import static com.marcoscherzer.msimplegoauthmailserviceapplication.util.MUtil.createFolderDesktopLink;
import static com.marcoscherzer.msimplegoauthmailserviceapplication.util.MUtil.createPathIfNotExists;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * uready
 */
public final class MMain {

    private static MAttachmentWatcher watcher;
    private static MSimpleOAuthHelper oAuthHelper;
    private static MAppLoggingArea logFrame;
    private static boolean isDbg;
    private static TrayIcon trayIcon;
    private static final String userDir = System.getProperty("user.dir");
    private static final String keystorePath = userDir+"\\mystore.p12";
    private static final String mailFoldersPath = userDir + "\\mail";
    private static String trayIconPathWithinResourcesFolder = "/5.png";
    private static MSimpleOAuthKeystore store;
    private static MAppRedirectLinkDialog appRedirectLinkDialog;
    private static MSpinnerOverlayFrame loginOverlay;


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * uready
     */
    public static final void main(String[] args) {
   try{
        isDbg = (args != null && args.length > 0 && args[0]!=null && args[0].equals("-debug"));
        //isDbg = true; //dbg

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

            logFrame = new MAppLoggingArea();

            setupTrayIcon();
            if (isDbg) logFrame.getLogFrame().setVisible(true);// sonst nur im tray sichtbar

       trayIcon.displayMessage("OAuth Desktop FileSend Folder",
               "Setup started." + (isDbg ? "\nInfo: Use the SystemTray Icon to view log Information" : ""),
               TrayIcon.MessageType.INFO);

       MAuthFlow_SwingGui flow = new MAuthFlow_SwingGui(keystorePath){

           @Override
           protected void statusMsg(String message) {
               trayIcon.displayMessage("OAuth Desktop FileSend Folder", message,TrayIcon.MessageType.INFO);
           }

           @Override
           protected void initializeServices(Credential credential, String applicationName) throws Exception {
               MMailService mailService = new MMailService(credential, applicationName);

               MSimpleKeystore store = oAuthHelper.getKeystore();
               String clientAndPathUUID = store.get("clientId");
               Path sentFolder = createPathIfNotExists(Paths.get(mailFoldersPath, clientAndPathUUID + "-sent"), "Sent folder");
               Path unsentFolder = createPathIfNotExists(Paths.get(mailFoldersPath, clientAndPathUUID + "-notSent"), "NotSent folder");

               String fromAddress = store.get("fromAddress");
               String toAddress = store.get("toAddress");

               watcher = new MAttachmentWatcher(sentFolder, unsentFolder, mailService, fromAddress, toAddress, clientAndPathUUID) {
                   @Override
                   public final MConsentQuestioner askForConsent(MOutgoingMail mail) {
                       return new MAppSendGui(mail, 900, 600, 16);
                   }
               }.startServer();

               Path linkPath = createFolderDesktopLink(sentFolder.toString(), "Sent Things");
               if (linkPath == null) {System.out.println("Desktop link for '" + linkPath + " could not be created. Please create it manually.");};
               linkPath = createFolderDesktopLink(unsentFolder.toString(), "Unsent Things");
               if (linkPath == null) {System.out.println("Desktop link for '" + linkPath + " could not be created. Please create it manually.");};

               printConfiguration(fromAddress, toAddress, mailFoldersPath, clientAndPathUUID, clientAndPathUUID + "-sent");
               trayIcon.displayMessage("OAuth Desktop FileSend Folder", "To send mail drag files onto its dolphin icon on the desktop or click it.", TrayIcon.MessageType.INFO);
           }
       };
       flow.swingGuiAuthFlow(Collections.singletonList(GmailScopes.GMAIL_SEND),true);

    } catch (Exception exc) {
        System.err.println(exc.getMessage());
        exit(exc, 1);
    }
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void setupTrayIcon(){
        System.out.println("setting up Tray-Icon");
        try {
            if (!SystemTray.isSupported()) {
                throw new Exception("Error: System Tray not supported.");
            }

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
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(MMain.class.getResourceAsStream(trayIconPathWithinResourcesFolder));
            trayIcon = new TrayIcon(image, "OAuth Desktop FileSend Folder", traymenu);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            createMessageDialogAndWait("Error:\n" + exc.getMessage(),"Error");
            exit(exc,1);
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void exit(Throwable exception,int code) {
        if(isDbg && exception!=null){exception.printStackTrace();}
        try {
            if(oAuthHelper != null && oAuthHelper.isInDoNotPersistOAuthTokenMode()) oAuthHelper.revokeOAuthTokenFromServer();
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







