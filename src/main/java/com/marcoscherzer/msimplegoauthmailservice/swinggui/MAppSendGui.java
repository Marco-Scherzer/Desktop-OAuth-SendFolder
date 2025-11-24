package com.marcoscherzer.msimplegoauthmailservice.swinggui;

import com.marcoscherzer.msimplegoauthmailservice.MOutgoingMail;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.marcoscherzer.msimplegoauthmailserviceapplication.core.MAttachmentWatcher;

import javax.swing.*;
import java.awt.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppSendGui implements MAttachmentWatcher.MConsentQuestioner {
    private JFrame consentFrame;
    private JLabel counterLabel;
    private boolean showingPrefs = false;
    private Boolean result;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MAppSendGui(MOutgoingMail mail) {
        this(mail, 900, 600, 16); // Defaultwerte: Breite 900, Höhe 600, Schriftgröße 16
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */

    public MAppSendGui(MOutgoingMail mail, int windowWidth, int windowHeight, int fontSize) {
        Runnable task = () -> {
            consentFrame = new JFrame("OAuthFileSendFolder for Gmail (Prototype, preAlpha 0.1) - Send Mail");
            consentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            consentFrame.setAlwaysOnTop(true);
            consentFrame.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel basePanel = new JPanel(cardLayout);

            // ---------------- Hauptansicht ----------------
            JPanel innerPanel = new JPanel();
            innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

            JPanel fieldsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel senderLabel = new JLabel("Sender:");
            gbc.gridx = 0; gbc.gridy = 0;
            fieldsPanel.add(senderLabel, gbc);

            JTextField senderField = new JTextField(mail.getFrom());
            senderField.setEditable(false);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
            fieldsPanel.add(senderField, gbc);

            JLabel receiverLabel = new JLabel("Recipient:");
            gbc.gridx = 0; gbc.gridy = 1;
            fieldsPanel.add(receiverLabel, gbc);

            JTextField receiverField = new JTextField(mail.getTo());
            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
            fieldsPanel.add(receiverField, gbc);

            JLabel subjectLabel = new JLabel("Subject:");
            gbc.gridx = 0; gbc.gridy = 2;
            fieldsPanel.add(subjectLabel, gbc);

            JTextField subjectField = new JTextField(mail.getSubject());
            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
            fieldsPanel.add(subjectField, gbc);

            innerPanel.add(fieldsPanel);
            innerPanel.add(Box.createVerticalStrut(10));

            // TextArea für Mail Body
            StringBuilder initialText = new StringBuilder("\n\n\n\n\nAttachments:\n");
            for (String attachment : mail.getAttachments()) {
                String fileName = new java.io.File(attachment).getName();
                initialText.append("- ").append(fileName).append("\n");
            }

            JTextArea messageArea = new JTextArea(initialText.toString());
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(messageArea);
            scrollPane.setPreferredSize(new Dimension(800, 400));
            innerPanel.add(scrollPane);
            innerPanel.add(Box.createVerticalStrut(10));

            // Counter + Buttons
            counterLabel = new JLabel("Attachments: " + mail.getAttachments().length, SwingConstants.CENTER);
            JButton sendButton = new JButton("Send");
            JButton cancelButton = new JButton("Cancel");

            sendButton.addActionListener(e -> {
                mail.setTo(receiverField.getText());
                mail.setSubject(subjectField.getText());
                mail.appendMessageText(messageArea.getText());
                consentFrame.dispose();
                result = true;
            });

            cancelButton.addActionListener(e -> {
                consentFrame.dispose();
                result = false;
            });

            JPanel bottomRow = new JPanel(new GridLayout(1, 3, 10, 0));
            bottomRow.add(counterLabel);
            bottomRow.add(sendButton);
            bottomRow.add(cancelButton);
            innerPanel.add(bottomRow);

            // ---------------- Preferences Ansicht ----------------
            JPanel prefsPanel = new JPanel();
            prefsPanel.setLayout(new BoxLayout(prefsPanel, BoxLayout.Y_AXIS));
            prefsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            prefsPanel.add(new JLabel("Preferences / Einstellungen hier"));

            basePanel.add(innerPanel, "MAIN");
            basePanel.add(prefsPanel, "PREFS");

            // Menüleiste mit Zahnrad-Icon
            JMenuBar menuBar = new JMenuBar();
            JMenu preferencesMenu = new JMenu();
            preferencesMenu.setToolTipText("Preferences");
            preferencesMenu.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("gear.svg"));

            preferencesMenu.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (showingPrefs) {
                        cardLayout.show(basePanel, "MAIN");
                        showingPrefs = false;
                    } else {
                        cardLayout.show(basePanel, "PREFS");
                        showingPrefs = true;
                    }
                }
            });

            menuBar.add(preferencesMenu);
            consentFrame.setJMenuBar(menuBar);
            consentFrame.add(basePanel);
            consentFrame.setSize(windowWidth, windowHeight);
            consentFrame.setLocationRelativeTo(null);
            consentFrame.setVisible(true);
        };
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }


    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final Boolean getResult() {
        return result;
    }
}














