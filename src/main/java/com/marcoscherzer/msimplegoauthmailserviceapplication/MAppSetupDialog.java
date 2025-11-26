package com.marcoscherzer.msimplegoauthmailserviceapplication;

import com.marcoscherzer.msimplegoauthmailservice.MMailAdressFormatException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import static com.marcoscherzer.msimplegoauthmailservice.MSimpleMailerUtil.checkMailAddress;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppSetupDialog {

    private String[] result;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     *   * uready
     */
    public final String[] showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> { result = buildAndShowDialog(); };

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeAndWait(task);
        }

        return result;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String[] buildAndShowDialog() {
        // Eingabefelder mit Platzhaltertext
        JTextField fromField = new JTextField("Sender email address");
        JTextField toField   = new JTextField("Recipient email address");

        // Überschrift und Unterüberschrift
        JLabel heading = new JLabel("OAuth FileSendFolder Setup");
        JLabel infoLabel = new JLabel("(Requires an email account (tested with Gmail))\n\n");

        heading.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 23f));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Abstandszeile
        JLabel spacer = new JLabel(" ");

        // Placeholder-Verhalten
        addPlaceholderBehavior(fromField, "Sender email address");
        addPlaceholderBehavior(toField, "Recipient email address");

        // Validierungen
        addMailValidation(fromField);
        addMailValidation(toField);

        // Elternpanel für alle Zeilen
        JPanel rowsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rp = new GridBagConstraints();
        rp.gridx = 0; rp.gridy = 0; rp.weightx = 1.0; rp.fill = GridBagConstraints.HORIZONTAL;
        rp.insets = new Insets(5,5,5,5);

        // Labels fett und kleiner
        Font baseFont = fromField.getFont();
        Font labelFont = baseFont.deriveFont(Font.BOLD, baseFont.getSize() - 2f);

        // Row 2: Sender Label
        JPanel senderLabelRow = new JPanel(new GridBagLayout());
        GridBagConstraints l2 = new GridBagConstraints();
        l2.gridx = 0; l2.gridy = 0; l2.weightx = 1.0; l2.fill = GridBagConstraints.HORIZONTAL;
        JLabel senderLabel = new JLabel("Sender:");
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
        JLabel recipientLabel = new JLabel("Recipient:");
        recipientLabel.setFont(labelFont);
        recipientLabelRow.add(recipientLabel, l3);
        rowsPanel.add(recipientLabelRow, rp);
        rp.gridy++;

        // Row 3: Recipient Field
        JPanel recipientRow = new JPanel(new GridBagLayout());
        GridBagConstraints r3 = new GridBagConstraints();
        r3.gridx = 0; r3.gridy = 0; r3.weightx = 1.0; r3.fill = GridBagConstraints.HORIZONTAL;
        recipientRow.add(toField, r3);
        rowsPanel.add(recipientRow, rp);
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

            // Validierungen
            List<String> errors = new ArrayList<>();
            try { checkMailAddress(from); } catch (MMailAdressFormatException e) { errors.add("Sender email invalid: " + e.getMessage()); }
            try { checkMailAddress(to); } catch (MMailAdressFormatException e) { errors.add("Recipient email invalid: " + e.getMessage()); }
            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Invalid format(s):\n");
                for (String err : errors) sb.append("* ").append(err).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return new MAppSetupDialog().buildAndShowDialog();
            }

            return new String[]{from, to};
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


}
