package com.marcoscherzer.msimplegoauthmailerclientapplication;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegoauthmailer.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkPasswordComplexity;
import static com.marcoscherzer.msimplegoauthmailerclientapplication.MUtil.createFolderDesktopLink;
import static com.marcoscherzer.msimplegoauthmailerclientapplication.MUtil.createPathIfNotExists;
import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkMailAddress;

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

    private static JTextArea logArea;
    private static JFrame logFrame;
    private static PrintStream originalOut;
    private static PrintStream originalErr;
    private static String[] arg;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public static void main(String[] args) {
        arg=new String[]{"-debug"};
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

                //originalOut = System.out;
                //originalErr = System.err;
                setupLogging();
                if(arg[0]!=null && arg[0].equals("-debug")) logFrame.setVisible(true);//dbg

                Path keystorePath = Paths.get(userDir, "mystore.p12");
                boolean keystoreFileExists = Files.exists(keystorePath);
                String pw;
                if (!keystoreFileExists) {
                    System.out.println("showing setup dialog");
                    pw = showSetupDialog(true)[2];
                }
                else {
                    System.out.println("showing password dialog");
                    pw = showPasswordDialog();
                }
                MSimpleMailer.setClientKeystoreDir(userDir);
                mailer = new MSimpleMailer("BackupMailer", pw, false);
                MSimpleKeystore store = mailer.getKeystore();

                if (!store.containsAllNonNullKeys("fromAddress", "toAddress")) {
                    String[] setupedValues = showSetupDialog(false);
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
                        "Client Secret Error:\n" + exc.getMessage()+" Setup will be restarted on next launch.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                exit(1);
            } catch (MPasswordIntegrityException exc) {
                JOptionPane.showMessageDialog(
                        null,
                        "Client Integrity Error:\n" + exc.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                exit(1);
            } catch (Exception exc) {
                if(arg[0]!=null && arg[0].equals("-debug")) {exc.printStackTrace();}
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
        logFrame = new JFrame("Log Output");
        logFrame.add(new JScrollPane(logArea));
        logFrame.pack();
        logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        OutputStream outStream = new OutputStream() {
            @Override
            public void write(byte[] b, int off, int len) {
                final String text = new String(b, off, len);
                SwingUtilities.invokeLater(() -> {
                    logArea.append(text);
                    logArea.setCaretPosition(logArea.getDocument().getLength());
                });
            }

            @Override
            public void write(int b) {
                write(new byte[]{(byte)b}, 0, 1);
            }
        };

        PrintStream ps = new PrintStream(outStream, true);
        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    private static void setupTrayIcon() throws Exception {
        PopupMenu traymenu = new PopupMenu();

        MenuItem showLogItem = new MenuItem("Show Log");
        showLogItem.addActionListener((ActionEvent e) -> {
            if (!logFrame.isVisible()) {
                logFrame.setVisible(true);
                logFrame.setState(JFrame.NORMAL);
                logFrame.toFront();
            } else {
                logFrame.setVisible(false);
            }
        });
        traymenu.add(showLogItem);

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
    private static String[] showSetupDialog(boolean setPw) {
        JTextField fromField = new JTextField(); fromField.setText("m.scherzer@hotmail.com");
        JTextField toField = new JTextField(); toField.setText("m.scherzer@hotmail.com");
        JPasswordField pwField = new JPasswordField(); pwField.setText("testTesttest-123)");

        JLabel heading = new JLabel("OAuth FileSendFolder Setup");
        JLabel infoLabel = new JLabel("(Requires an email account (tested with Gmail) and a clientSecret.json file provided by Google)\n\n");
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


        fromField.getDocument().addDocumentListener(new DocumentListener() {
            private void validate() {
                try {
                    checkMailAddress(fromField.getText().trim());
                    fromField.setBorder(UIManager.getBorder("TextField.border"));
                } catch (IllegalArgumentException ex) {
                    fromField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }
        });

        toField.getDocument().addDocumentListener(new DocumentListener() {
            private void validate() {
                try {
                    checkMailAddress(toField.getText().trim());
                    toField.setBorder(UIManager.getBorder("TextField.border"));
                } catch (IllegalArgumentException ex) {
                    toField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }
        });

        if (setPw) {
            pwField.getDocument().addDocumentListener(new DocumentListener() {
                private void validate() {
                    try {
                        checkPasswordComplexity(new String(pwField.getPassword()), 15, true, true, true);
                        pwField.setBorder(UIManager.getBorder("TextField.border"));
                    } catch (IllegalArgumentException ex) {
                        pwField.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                    }
                }
                public void insertUpdate(DocumentEvent e) { validate(); }
                public void removeUpdate(DocumentEvent e) { validate(); }
                public void changedUpdate(DocumentEvent e) { validate(); }
            });
        }

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
            String from = fromField.getText().trim();
            String to   = toField.getText().trim();
            String pw   = setPw ? new String(pwField.getPassword()) : null;

            // finale Sicherung beim Submit
            List<String> errors = new ArrayList<>();

            try {
                checkMailAddress(from);
            } catch (IllegalArgumentException e) {
                errors.add("Sender email invalid: " + e.getMessage());
            }

            try {
                checkMailAddress(to);
            } catch (IllegalArgumentException e) {
                errors.add("Recipient email invalid: " + e.getMessage());
            }

            if (setPw) {
                try {
                    checkPasswordComplexity(pw, 15, true, true, true);
                } catch (IllegalArgumentException e) {
                    errors.add("Password invalid: " + e.getMessage());
                }
            }

            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Invalid format(s):\n");
                for (String err : errors) {
                    sb.append("* ").append(err).append("\n");
                }
                JOptionPane.showMessageDialog(
                        null,
                        sb.toString(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return showSetupDialog(setPw); // Dialog erneut öffnen
            }

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







