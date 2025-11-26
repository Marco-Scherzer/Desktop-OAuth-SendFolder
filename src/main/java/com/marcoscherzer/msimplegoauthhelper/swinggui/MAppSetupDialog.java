package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.marcoscherzer.msimplegoauthhelper.MGWebApis;
import com.marcoscherzer.msimplegoauthmailservice.MMailAdressFormatException;
import com.marcoscherzer.msimplekeystore.MPasswordComplexityException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.marcoscherzer.msimplegoauthmailservice.MSimpleMailerUtil.checkMailAddress;
import static com.marcoscherzer.msimplekeystore.MSimpleKeystoreUtil.checkPasswordComplexity;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppSetupDialog {

    private String[] result;
    private Exception capturedException;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     *   * uready
     */
    public final String[] showAndWait() throws InterruptedException, InvocationTargetException, Exception {
        Runnable task = () -> {
            try {
                result = buildAndShowDialog();
            } catch (Exception e) {
                capturedException = e;
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeAndWait(task);
        }

        if (capturedException != null) {
            throw capturedException;
        }
        return result;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String[] buildAndShowDialog() {
        // Eingabefelder mit Platzhaltertext
        JTextField fromField = new JTextField("Account email address");
        JTextField toField   = new JTextField("Recipient email address");
        JPasswordField pwField = new JPasswordField("Program startup password");
        JTextField jsonPathField = new JTextField("client_secret.json file");
        jsonPathField.setEditable(false);

        // FileChooser-Button direkt neben dem JSON-Feld
        JButton chooseJsonButton = new JButton("Choose...");
        chooseJsonButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select client_secret.json file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
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

        // Abstandszeile
        JLabel spacer = new JLabel(" ");

        // Placeholder-Verhalten
        addPlaceholderBehavior(fromField, "Email address");
        addPlaceholderBehavior(toField, "WebApi Scopes");
        addPlaceholderBehavior(pwField, "Program startup password");

        // Validierungen
        addMailValidation(fromField);
        addMailValidation(toField);
        addPasswordValidation(pwField);

        // Elternpanel für alle Zeilen
        JPanel rowsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rp = new GridBagConstraints();
        rp.gridx = 0; rp.gridy = 0; rp.weightx = 1.0; rp.fill = GridBagConstraints.HORIZONTAL;
        rp.insets = new Insets(5,5,5,5);

        // Labels fett und kleiner
        Font baseFont = fromField.getFont();
        Font labelFont = baseFont.deriveFont(Font.BOLD, baseFont.getSize() - 2f);

        // Row 1: JSON Label
        JPanel jsonLabelRow = new JPanel(new GridBagLayout());
        GridBagConstraints l1 = new GridBagConstraints();
        l1.gridx = 0; l1.gridy = 0; l1.weightx = 1.0; l1.fill = GridBagConstraints.HORIZONTAL;
        JLabel jsonLabel = new JLabel("JSON file:");
        jsonLabel.setFont(labelFont);
        jsonLabelRow.add(jsonLabel, l1);
        rowsPanel.add(jsonLabelRow, rp);
        rp.gridy++;

        // Row 1: JSON Field + Button
        JPanel jsonRow = new JPanel(new GridBagLayout());
        GridBagConstraints r1 = new GridBagConstraints();
        r1.insets = new Insets(0,0,0,0);
        r1.fill = GridBagConstraints.HORIZONTAL;
        r1.gridx = 0; r1.gridy = 0; r1.weightx = 1.0;
        jsonRow.add(jsonPathField, r1);
        r1.gridx = 1; r1.gridy = 0; r1.weightx = 0;
        jsonRow.add(chooseJsonButton, r1);
        rowsPanel.add(jsonRow, rp);
        rp.gridy++;

        // Row 2: Sender Label
        JPanel senderLabelRow = new JPanel(new GridBagLayout());
        GridBagConstraints l2 = new GridBagConstraints();
        l2.gridx = 0; l2.gridy = 0; l2.weightx = 1.0; l2.fill = GridBagConstraints.HORIZONTAL;
        JLabel senderLabel = new JLabel("Email address:");
        senderLabel.setFont(labelFont);
        senderLabelRow.add(senderLabel, l2);
        rowsPanel.add(senderLabelRow, rp);
        rp.gridy++;

        // Row 2: Sender Field
        JPanel senderRow = new JPanel(new GridBagLayout());
        GridBagConstraints r2 = new GridBagConstraints();
        r2.gridx = 0; r2.gridy = 0; r2.weightx = 1.0; r2.fill = GridBagConstraints.HORIZONTAL;
        senderRow.add(fromField, r2);
        rowsPanel.add(senderRow, rp);
        rp.gridy++;

        // Row 3: Recipient Label
        JPanel recipientLabelRow = new JPanel(new GridBagLayout());
        GridBagConstraints l3 = new GridBagConstraints();
        l3.gridx = 0; l3.gridy = 0; l3.weightx = 1.0; l3.fill = GridBagConstraints.HORIZONTAL;
        JButton webApiScopesAddButton = new JButton("Add WebApi Scopes");
        webApiScopesAddButton.setFont(labelFont);
        recipientLabelRow.add(webApiScopesAddButton, l3);
        rowsPanel.add(recipientLabelRow, rp);
        rp.gridy++;

        webApiScopesAddButton.addActionListener(e -> {
            Set<String> apiNames = MGWebApis.getAllWebApiNames();
            MButtonOverviewDialog dlg = new MButtonOverviewDialog("Web-API Scopes",
                    apiNames.toArray(new String[0]));
            try {
                Set<String> chosen = dlg.showAndWait();
                if (chosen != null) {
                    System.out.println("Ausgewählt: " + chosen);//unready
                } else {
                    System.out.println("Abgebrochen");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Row 3: Recipient Field
        JPanel recipientRow = new JPanel(new GridBagLayout());
        GridBagConstraints r3 = new GridBagConstraints();
        r3.gridx = 0; r3.gridy = 0; r3.weightx = 1.0; r3.fill = GridBagConstraints.HORIZONTAL;
        recipientRow.add(toField, r3);
        rowsPanel.add(recipientRow, rp);
        rp.gridy++;

        // Row 4: Password Label
        JPanel passwordLabelRow = new JPanel(new GridBagLayout());
        GridBagConstraints l4 = new GridBagConstraints();
        l4.gridx = 0; l4.gridy = 0; l4.weightx = 1.0; l4.fill = GridBagConstraints.HORIZONTAL;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabelRow.add(passwordLabel, l4);
        rowsPanel.add(passwordLabelRow, rp);
        rp.gridy++;

        // Row 4: Password Field
        JPanel passwordRow = new JPanel(new GridBagLayout());
        GridBagConstraints r4 = new GridBagConstraints();
        r4.gridx = 0; r4.gridy = 0; r4.weightx = 1.0; r4.fill = GridBagConstraints.HORIZONTAL;
        passwordRow.add(pwField, r4);
        rowsPanel.add(passwordRow, rp);
        rp.gridy++;

        // Dialog-Inhalt
        Object[] message = {
                heading, null,
                infoLabel, null,
                spacer, null,
                rowsPanel
        };

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
            String pw   = new String(pwField.getPassword());
            String jsonPath = jsonPathField.getText().trim();

            // Validierungen
            List<String> errors = new ArrayList<>();
            try { checkMailAddress(from); } catch (MMailAdressFormatException e) { errors.add("Email address invalid: " + e.getMessage()); }
            //try { checkMailAddress(to); } catch (MMailAdressFormatException e) { errors.add("Recipient email invalid: " + e.getMessage()); }
            try { checkPasswordComplexity(pw, 15, true, true, true); } catch (MPasswordComplexityException e) { errors.add("Password invalid: " + e.getMessage()); }
            if (jsonPath.equals("") || jsonPath.equals("client_secret.json file")) {
                errors.add("client_secret.json file not selected.");
            }

            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Invalid format(s):\n");
                for (String err : errors) sb.append("* ").append(err).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return new MAppSetupDialog().buildAndShowDialog();
            }

            return new String[]{from, to, pw, jsonPath};
        } else {
            return null;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void addPlaceholderBehavior(final JTextComponent field, final String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK); // normale Textfarbe
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void addMailValidation(JTextField field) {
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
    private static void addPasswordValidation(JPasswordField field) {
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

}


