package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import com.marcoscherzer.msimplegooglemailer.MOutgoingMail;

import javax.swing.*;
import java.awt.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * MiniGui unready
 */
public abstract class MMiniGui {
    private JFrame consentFrame;
    private JLabel counterLabel;

    protected void MMiniGui(MOutgoingMail mail) {
        SwingUtilities.invokeLater(() -> {
            consentFrame = new JFrame("OAuthFileSendFolder for GoogleMail (Prototype, preAlpha 0.1) - Send Mail");
            consentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            consentFrame.setAlwaysOnTop(true);
            consentFrame.setLocationRelativeTo(null);

            // Innerer Container mit Padding
            JPanel innerPanel = new JPanel();
            innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

            // 1. Zeile: Betreff
            JTextField subjectField = new JTextField("Betreff: " + mail.getSubject());
            subjectField.setEditable(true);
            subjectField.setMaximumSize(new Dimension(Integer.MAX_VALUE, subjectField.getPreferredSize().height));
            innerPanel.add(subjectField);
            innerPanel.add(Box.createVerticalStrut(10));

            // 2. Zeile: TextArea für Mailtext
            JTextArea messageArea = new JTextArea();
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(messageArea);
            scrollPane.setPreferredSize(new Dimension(580, 200));
            scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            innerPanel.add(scrollPane);
            innerPanel.add(Box.createVerticalStrut(10));

            // 3. Zeile: Counter + Buttons
            counterLabel = new JLabel("Attachments: 0", SwingConstants.CENTER);
            JButton sendButton = new JButton("Senden");
            JButton cancelButton = new JButton("Abbrechen");

            sendButton.addActionListener(e -> {
                mail.appendMessageText(messageArea.getText());
                onSendPermitted(mail);   // abstrakte Methode
                consentFrame.dispose();
            });

            cancelButton.addActionListener(e -> {
                onSendCanceled();        // abstrakte Methode
                consentFrame.dispose();
            });

            JPanel bottomRow = new JPanel(new GridLayout(1, 3, 10, 0));
            bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            bottomRow.add(counterLabel);
            bottomRow.add(sendButton);
            bottomRow.add(cancelButton);

            innerPanel.add(bottomRow);

            consentFrame.add(innerPanel);
            consentFrame.setSize(600, 400);
            consentFrame.setVisible(true);
        });
    }

    protected abstract void onSendPermitted(MOutgoingMail mail);

    protected abstract void onSendCanceled();
}
