package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.marcoscherzer.msimplegoauthhelper.MMutableBoolean;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleHtmlTextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * Copyright Marco Scherzer, All rights reserved
 * unready
 */
public abstract class MAuthRedirectLinkDialog {

    private MSimpleDialog dialog;
    private MSpinnerOverlayFrame loginOverlay;
    private JCheckBox dontShowAgainCheckBox;

    public final void showAndWait(String oAuthLink, MMutableBoolean continueOAuthOrNot)
            throws InterruptedException, InvocationTargetException {

        MSimpleHtmlTextPanel htmlPanel = new MSimpleHtmlTextPanel();
        JScrollPane scrollPane = new JScrollPane(htmlPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Label + Checkbox nebeneinander in einem nested GridBag
        dontShowAgainCheckBox = new JCheckBox();
        JLabel dontShowLabel = new JLabel("Do not show this dialog in the future");

        JPanel checkRow = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCheck = new GridBagConstraints();
        gbcCheck.gridy = 0;

        // Label rechtsbündig
        gbcCheck.gridx = 0;
        gbcCheck.weightx = 1.0;
        gbcCheck.anchor = GridBagConstraints.EAST;
        checkRow.add(dontShowLabel, gbcCheck);

        // Checkbox direkt daneben
        gbcCheck.gridx = 1;
        gbcCheck.weightx = 0.0;
        gbcCheck.anchor = GridBagConstraints.WEST;
        checkRow.add(dontShowAgainCheckBox, gbcCheck);

        // Hauptcontainer mit GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.80; // 80% für ScrollPane
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(scrollPane, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.05; // 5% für Checkbox-Zeile
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0); // Abstand nach oben
        contentPanel.add(checkRow, gbc);

        dialog = new MSimpleDialog(contentPanel, 700, 400)
                .setTitle("OAuth login")
                .setMessageType(JOptionPane.INFORMATION_MESSAGE)
                .setIcon(null)
                .addButton("OK", e -> {
                    try {
                        continueOAuthOrNot.set(true);
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(URI.create(oAuthLink));
                        } else {
                            System.out.println("Browser can not be started: Please open url manually: " + oAuthLink);
                        }
                        dialog.getUIComponent().setVisible(false);

                        if (dontShowAgainCheckBox.isSelected()) {
                            onDontShowAgainSelected();
                        }

                    } catch (Exception exc) {
                        continueOAuthOrNot.set(false);
                        onException(exc);
                    }
                }, false)
                .addButton("Cancel", e -> {
                    continueOAuthOrNot.set(false);
                    dispose();
                    onCanceled();
                }, true);

        htmlPanel.setDialogSize(700, 400)
                .addText("Additional authentication needed.", "white", 14, "none")
                .addText("Please open the following address in your browser:", "white", 14, "none")
                .addHyperlink(oAuthLink, oAuthLink, "#87CEEB", 11, "underline",
                        e -> dialog.pressButton(0))
                .create();

        dialog.showAndWait();
        dialog.getUIComponent().setVisible(false);

        loginOverlay = new MSpinnerOverlayFrame();
        loginOverlay.setMouseHandler(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.getUIComponent().setVisible(true);
            }
        });
        loginOverlay.setVisible(true);
    }

    public final void dispose() {
        dialog.getUIComponent().setVisible(false);
        dialog.getUIComponent().dispose();
        if (loginOverlay != null) loginOverlay.dispose();
    }

    protected abstract void onException(Exception exc);

    protected abstract void onCanceled();

    protected abstract void onDontShowAgainSelected();
}









