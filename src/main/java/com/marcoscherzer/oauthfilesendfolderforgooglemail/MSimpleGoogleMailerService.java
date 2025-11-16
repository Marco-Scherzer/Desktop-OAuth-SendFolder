package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegooglemailer.MClientSecretException;
import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;
import com.marcoscherzer.msimplegooglemailer.MSimpleGMailer;
import com.marcoscherzer.msimplegooglemailer.MSimpleKeystore;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;
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
    private static final String basePath = userDir + "\\mail";
    private static Path notSentFolder;
    private static Path sentFolder;
    private static String clientAndPathUUID;
    private static MAttachmentWatcher watcher;
    private static String fromAddress;
    private static String toAddress;
    private static MSimpleGMailer mailer;
    private static MFileNameWatcher notSentDesktopLinkWatcher;
    private static MFileNameWatcher sentDesktopLinkWatcher;

    private static JTextArea logArea;
    private static JFrame logFrame;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
       /*
                FlatLightLaf.setup(), FlatDarkLaf.setup(), FlatIntelliJLaf.setup(), FlatDarculaLaf.setup(),
                FlatArcDarkIJTheme.setup(), FlatArcIJTheme.setup(), FlatArcOrangeIJTheme.setup(),
                FlatArcDarkOrangeIJTheme.setup(), FlatCarbonIJTheme.setup(), FlatCyanLightIJTheme.setup(),
                FlatGrayIJTheme.setup(), FlatGrayDarkIJTheme.setup(), FlatHiberbeeDarkIJTheme.setup(),
                FlatHighContrastIJTheme.setup(), FlatMonokaiProIJTheme.setup(), FlatSolarizedLightIJTheme.setup(),
                FlatSolarizedDarkIJTheme.setup(), FlatDraculaIJTheme.setup()
           */

                try {
                    FlatCarbonIJTheme.setup();
                } catch (Exception exc) {
                    System.out.println("UI theme not supported");
                }
                UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 16));


                Path keystorePath = Paths.get(userDir, "mystore.p12");
                boolean keystoreFileExists = Files.exists(keystorePath);
                String pw;
                if (!keystoreFileExists) { pw = showSetupDialog(true)[0]; } else { pw = showPasswordDialog();}
                MSimpleGMailer.setClientKeystoreDir(userDir);

                mailer = new MSimpleGMailer("BackupMailer", pw, false);
                MSimpleKeystore store = mailer.getKeystore();

                if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                    String[] setupedValues = showSetupDialog(false);
                    String from = setupedValues[0];
                    String to = setupedValues[1];
                    store.add("fromAddress", from);
                    store.add("toAddress", to);
                }

                setupLogging();
                openMainWindow();
                clientAndPathUUID = store.get("clientId");
                sentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-sent"), "Sent folder");
                notSentFolder = createPathIfNotExists(Paths.get(basePath, clientAndPathUUID + "-notSent"), "NotSent folder");

                fromAddress = store.get("fromAddress");
                toAddress = store.get("toAddress");

                watcher = new MAttachmentWatcher(sentFolder, notSentFolder, mailer, fromAddress, toAddress, clientAndPathUUID) {
                    @Override
                    public final MConsentQuestioner askForConsent(MOutgoingMail mail) {
                        return new MMiniGui(mail,900, 600, 16);
                    }
                }.startServer();

                sentDesktopLinkWatcher = createAndWatchFolderDesktopLink(sentFolder.toString(), "Sent Things", "Sent");
                notSentDesktopLinkWatcher = createAndWatchFolderDesktopLink(notSentFolder.toString(), "NotSent Things", "NotSent");

                printConfiguration(fromAddress, toAddress, basePath, clientAndPathUUID, clientAndPathUUID + "-sent");

                setupTrayIcon();

            } catch (MClientSecretException exc) {
            JOptionPane.showMessageDialog(
                    null,
                    "Client Secret Error:\n" + exc.getMessage()+" Setup will be restarted next time.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            exit(1);
            } catch (Exception exc) {
                //exc.printStackTrace();
                exit(1);
            }
        });
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void setupLogging() {
        logArea = new JTextArea(20, 80);
        logArea.setEditable(false);
        logFrame = new JFrame("OAuth FileSendFolder Log");
        logFrame.add(new JScrollPane(logArea));
        logFrame.pack();
        logFrame.setVisible(false); // Anfangs unsichtbar

        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                logArea.append(String.valueOf((char) b));
            }
        });
        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static String[] showSetupDialog(boolean setPw) {
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JPasswordField pwField = new JPasswordField();

        JLabel heading = new JLabel("OAuth FileSendFolder Setup");
        JLabel infoLabel = new JLabel("(This Program requires a Gmail address and a clientSecret.json file)\n\n");
        JLabel label0 = new JLabel("\n");
        JLabel label1 = new JLabel("Email address:");
        JLabel label2 = new JLabel("Default recipient address:");
        JPanel label3 = createTwoPartLabel("Program startup password:", "(Do not use any account or email account password!)", 16, 11);

        heading.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 23f));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        label0.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        label1.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        label2.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        label3.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));

        Object[] message = {
                heading, null,
                infoLabel, null,
                label0, null,
                label1, fromField,
                label2, toField,
                label3, pwField,
                "\n"
        };
        Object[] message2 = {
                heading, null,
                infoLabel, null,
                label0, null,
                label1, fromField,
                label2, toField,
                "\n"
        };

        int option = JOptionPane.showConfirmDialog(
                null,
                setPw ? message : message2,
                "",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            // Werte einsammeln
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String pw = setPw ? new String(pwField.getPassword()) : null;

            // Rückgabe als String[]
            return setPw ? new String[]{from, to, pw} : new String[]{from, to};
        } else {
            System.exit(0);
            return null;
        }
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static JPanel createTwoPartLabel(String mainText, String hintText, float mainFontSize, float hintFontSize) {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel labelMain = new JLabel(mainText);
        labelMain.setFont(labelMain.getFont().deriveFont(Font.BOLD, mainFontSize));
        labelMain.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelHint = new JLabel(hintText);
        labelHint.setFont(labelHint.getFont().deriveFont(Font.PLAIN, hintFontSize));
        labelHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelPanel.add(labelMain);
        labelPanel.add(labelHint);

        return labelPanel;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static String showPasswordDialog() {
        JPasswordField pwField = new JPasswordField();
        pwField.setText("testTesttest-123");
        int option = JOptionPane.showConfirmDialog(null, pwField, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return new String(pwField.getPassword());
        } else {
            System.exit(0);
            return null;
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void setupTrayIcon() throws Exception {
        PopupMenu traymenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Close Program");
        exitItem.addActionListener((ActionEvent e) -> {
            System.out.println("Program closed via Tray-Icon");
            System.exit(0);
        });
        traymenu.add(exitItem);

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported!");
            return;
        }
        SystemTray tray = SystemTray.getSystemTray();
        Image image = ImageIO.read(MSimpleGoogleMailerService.class.getResourceAsStream("/5.png"));
        TrayIcon trayIcon = new TrayIcon(image, "OAuth Desktop FileSend Folder for GoogleMail", traymenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        trayIcon.displayMessage("OAuth Desktop FileSend Folder for GoogleMail",
                "OAuth Desktop FileSend Folder for GoogleMail started in your system tray.\n" +
                        "To send mail drag files onto its dolphin icon on the desktop or click it.",
                TrayIcon.MessageType.INFO);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void openMainWindow() {
        logFrame.setSize(600, 400);
        logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logFrame.setVisible(true); // erst jetzt sichtbar machen
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
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







