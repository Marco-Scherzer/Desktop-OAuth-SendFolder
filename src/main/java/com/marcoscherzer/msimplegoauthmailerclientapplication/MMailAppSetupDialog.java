package com.marcoscherzer.msimplegoauthmailerclientapplication;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkMailAddress;
import static com.marcoscherzer.msimplegoauthmailer.MSimpleMailerUtil.checkPasswordComplexity;
import static com.marcoscherzer.msimplegoauthmailerclientapplication.MUtil.createTwoPartLabel;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MMailAppSetupDialog {

    private final boolean setPw;
    private String[] result;

    public MMailAppSetupDialog(boolean setPw) {
        this.setPw = setPw;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String[] showDialog() throws InterruptedException, InvocationTargetException {
        try {
            SwingUtilities.invokeAndWait(() -> {
                result = buildAndShowDialog();
            });
        } catch (Exception exc) {
            throw exc;
        }
        return result;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String[] buildAndShowDialog() throws InterruptedException, InvocationTargetException {
        JTextField fromField = new JTextField("m.scherzer@hotmail.com");
        JTextField toField   = new JTextField("m.scherzer@hotmail.com");
        JPasswordField pwField = new JPasswordField("testTesttest-123)");

        JLabel heading   = new JLabel("OAuth FileSendFolder Setup");
        JLabel infoLabel = new JLabel("(Requires an email account (tested with Gmail) and a clientSecret.json file provided by Google)\n\n");
        JLabel label0    = new JLabel("\n");
        JLabel label1    = new JLabel("Email address:");
        JLabel label2    = new JLabel("Default recipient address:");
        JPanel label3    = createTwoPartLabel("Program startup password:",
                "(Do not use any account or email account password!)",
                16, 11);

        heading.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 23f));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Validierung für Felder
        addMailValidation(fromField);
        addMailValidation(toField);
        if (setPw) addPasswordValidation(pwField);

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

            List<String> errors = new ArrayList<>();
            try { checkMailAddress(from); } catch (IllegalArgumentException e) { errors.add("Sender email invalid: " + e.getMessage()); }
            try { checkMailAddress(to);   } catch (IllegalArgumentException e) { errors.add("Recipient email invalid: " + e.getMessage()); }
            if (setPw) {
                try { checkPasswordComplexity(pw, 15, true, true, true); }
                catch (IllegalArgumentException e) { errors.add("Password invalid: " + e.getMessage()); }
            }

            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Invalid format(s):\n");
                for (String err : errors) sb.append("* ").append(err).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return new MMailAppSetupDialog(setPw).showDialog(); // Dialog erneut öffnen
            }
            return setPw ? new String[]{from, to, pw} : new String[]{from, to};
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
                } catch (IllegalArgumentException ex) {
                    field.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }
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
                } catch (IllegalArgumentException ex) {
                    field.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                }
            }
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }
        });
    }
}

