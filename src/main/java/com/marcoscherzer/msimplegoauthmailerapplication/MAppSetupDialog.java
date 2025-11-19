package com.marcoscherzer.msimplegoauthmailerapplication;

import com.marcoscherzer.msimplegoauthmailer.MMailAdressFormatException;
import com.marcoscherzer.msimplekeystore.MPasswordComplexityException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkMailAddress;
import static com.marcoscherzer.msimplekeystore.MSimpleKeystoreUtil.checkPasswordComplexity;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppSetupDialog {

    private final boolean setPw;
    private String[] result;
    private Exception capturedException;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MAppSetupDialog(boolean setPasswordDialogMode) {
        this.setPw = setPasswordDialogMode;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String[] createAndShowDialog() throws InterruptedException, InvocationTargetException, Exception {
        Runnable task = () -> {
            try {
                result = buildAndShowDialog();
            } catch (Exception e) {
                capturedException = e;
            }
        };
            SwingUtilities.invokeAndWait(task);
            if (capturedException != null) {
                throw capturedException;
            }
            return result;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String[] buildAndShowDialog() {
        // Eingabefelder mit Platzhaltertext
        JTextField fromField = new JTextField("Sender email address");
        JTextField toField   = new JTextField("Recipient email address");
        JPasswordField pwField = new JPasswordField("Program startup password");
        JTextField jsonPathField = new JTextField("client_secret.json file");
        jsonPathField.setEditable(false);

        // FileChooser-Button direkt daneben
        JButton chooseJsonButton = new JButton("Choose...");
        JPanel jsonPanel = new JPanel(new BorderLayout(5,0));
        jsonPanel.add(jsonPathField, BorderLayout.CENTER);
        jsonPanel.add(chooseJsonButton, BorderLayout.EAST);

        chooseJsonButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select client_secret.json file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                jsonPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Überschrift und Unterüberschrift
        JLabel heading = new JLabel("OAuth FileSendFolder Setup");
        JLabel infoLabel = new JLabel("(Requires an email account (tested with Gmail) and a clientSecret.json file provided by Google)\n\n");

        heading.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 23f));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Validierungen
        addMailValidation(fromField);
        addMailValidation(toField);
        if (setPw) addPasswordValidation(pwField);

        // Dialog-Inhalt: nur Überschrift, Unterüberschrift, Textfelder
        Object[] message = setPw
                ? new Object[]{heading, null, infoLabel, null, jsonPanel, fromField, toField, pwField}
                : new Object[]{heading, null, infoLabel, null, jsonPanel, fromField, toField};

        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                "",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String from = fromField.getText().trim();
            String to   = toField.getText().trim();
            String pw   = setPw ? new String(pwField.getPassword()) : null;
            String jsonPath = jsonPathField.getText().trim();

            // Validierungen
            List<String> errors = new ArrayList<>();
            try { checkMailAddress(from); } catch (MMailAdressFormatException e) { errors.add("Sender email invalid: " + e.getMessage()); }
            try { checkMailAddress(to); } catch (MMailAdressFormatException e) { errors.add("Recipient email invalid: " + e.getMessage()); }
            if (setPw) {
                try { checkPasswordComplexity(pw, 15, true, true, true); } catch (MPasswordComplexityException e) { errors.add("Password invalid: " + e.getMessage()); }
            }
            if (jsonPath.isBlank() || jsonPath.equals("client_secret.json file")) {
                errors.add("client_secret.json file not selected.");
            }

            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Invalid format(s):\n");
                for (String err : errors) sb.append("* ").append(err).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return new MAppSetupDialog(setPw).buildAndShowDialog();
            }

            return setPw ? new String[]{from, to, pw, jsonPath} : new String[]{from, to, jsonPath};
        } else {
            return null;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void addMailValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void validate() {
                try {
                    checkMailAddress(field.getText().trim());
                    field.setBorder(UIManager.getBorder("TextField.border"));
                } catch (MMailAdressFormatException exc) {
                    field.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }

            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            public void changedUpdate(DocumentEvent e) {
                validate();
            }
        });
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void addPasswordValidation(JPasswordField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void validate() {
                try {
                    checkPasswordComplexity(new String(field.getPassword()), 15, true, true, true);
                    field.setBorder(UIManager.getBorder("TextField.border"));
                } catch (MPasswordComplexityException ex) {
                    field.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }

            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            public void changedUpdate(DocumentEvent e) {
                validate();
            }
        });
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final JPanel createTwoPartLabel(String mainText, String hintText, float mainFontSize, float hintFontSize) {
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

}


