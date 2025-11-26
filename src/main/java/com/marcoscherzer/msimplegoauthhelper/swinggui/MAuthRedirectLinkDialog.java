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

    // --- Fenstergrößen ---
    private static final int DIALOG_WIDTH  = 700;
    private static final int DIALOG_HEIGHT = 400;

    // --- Höhenanteile ---
    private static final double SCROLLPANE_HEIGHT_FACTOR = 0.95;
    private static final double CHECKROW_HEIGHT_FACTOR   = 0.05;

    // --- Schriftgrößen ---
    private static final double FONT_SIZE_FACTOR      = 0.035;
    private static final double FONT_SIZE_LINK_FACTOR = 0.030;

    // --- Padding für Top-Row (ScrollPane) ---
    private static final double TOP_ROW_PADDING_VERTICAL_FACTOR = 0.02;
    private static final double TOP_ROW_PADDING_LEFT_FACTOR     = 0.02;
    private static final double TOP_ROW_PADDING_RIGHT_FACTOR    = 0.02;

    // --- Padding für Checkbox-Row (äußere Zeile) ---
    private static final double CHECK_ROW_PADDING_VERTICAL_FACTOR = 0.025;
    private static final double CHECK_ROW_PADDING_LEFT_FACTOR     = 0.02;
    private static final double CHECK_ROW_PADDING_RIGHT_FACTOR    = 0.02;

    // --- Interne Paddings für Label in Checkbox-Row ---
    private static final double LABEL_PADDING_VERTICAL_FACTOR = 0.015;
    private static final double LABEL_PADDING_LEFT_FACTOR     = 0.03;
    private static final double LABEL_PADDING_RIGHT_FACTOR    = 0.00;

    // --- Interne Paddings für Checkbox in Checkbox-Row ---
    private static final double CHECKBOX_PADDING_VERTICAL_FACTOR = 0.015;
    private static final double CHECKBOX_PADDING_LEFT_FACTOR     = 0.02;
    private static final double CHECKBOX_PADDING_RIGHT_FACTOR    = 0.02;

    private MSimpleDialog dialog;
    private MSpinnerOverlayFrame loginOverlay;
    private JCheckBox dontShowAgainCheckBox;
    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public final void showAndWait(String oAuthLink, MMutableBoolean continueOAuthOrNot)
            throws InterruptedException, InvocationTargetException {

        // Schriftgrößen berechnen
        int fontSize     = (int)(DIALOG_HEIGHT * FONT_SIZE_FACTOR);
        int fontSizeLink = (int)(DIALOG_HEIGHT * FONT_SIZE_LINK_FACTOR);

        // Padding berechnen
        int topRowPaddingVertical   = (int)(DIALOG_HEIGHT * TOP_ROW_PADDING_VERTICAL_FACTOR);
        int topRowPaddingLeft       = (int)(DIALOG_WIDTH  * TOP_ROW_PADDING_LEFT_FACTOR);
        int topRowPaddingRight      = (int)(DIALOG_WIDTH  * TOP_ROW_PADDING_RIGHT_FACTOR);

        int checkRowPaddingVertical = (int)(DIALOG_HEIGHT * CHECK_ROW_PADDING_VERTICAL_FACTOR);
        int checkRowPaddingLeft     = (int)(DIALOG_WIDTH  * CHECK_ROW_PADDING_LEFT_FACTOR);
        int checkRowPaddingRight    = (int)(DIALOG_WIDTH  * CHECK_ROW_PADDING_RIGHT_FACTOR);

        int labelPaddingVertical    = (int)(DIALOG_HEIGHT * LABEL_PADDING_VERTICAL_FACTOR);
        int labelPaddingLeft        = (int)(DIALOG_WIDTH  * LABEL_PADDING_LEFT_FACTOR);
        int labelPaddingRight       = (int)(DIALOG_WIDTH  * LABEL_PADDING_RIGHT_FACTOR);

        int checkboxPaddingVertical = (int)(DIALOG_HEIGHT * CHECKBOX_PADDING_VERTICAL_FACTOR);
        int checkboxPaddingLeft     = (int)(DIALOG_WIDTH  * CHECKBOX_PADDING_LEFT_FACTOR);
        int checkboxPaddingRight    = (int)(DIALOG_WIDTH  * CHECKBOX_PADDING_RIGHT_FACTOR);

        // HTML Panel
        MSimpleHtmlTextPanel htmlPanel = new MSimpleHtmlTextPanel();
        JScrollPane scrollPane = new JScrollPane(htmlPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Label + Checkbox nebeneinander in einem nested GridBag
        dontShowAgainCheckBox = new JCheckBox();
        JLabel dontShowLabel = new JLabel("Do not show this dialog in the future");
        dontShowLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

        JPanel checkRow = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCheck = new GridBagConstraints();
        gbcCheck.gridy = 0;

        // Label rechtsbündig mit eigenem Padding
        gbcCheck.gridx = 0;
        gbcCheck.weightx = 1.0;
        gbcCheck.anchor = GridBagConstraints.EAST;
        gbcCheck.insets = new Insets(labelPaddingVertical, labelPaddingLeft,
                labelPaddingVertical, labelPaddingRight);
        checkRow.add(dontShowLabel, gbcCheck);

        // Checkbox direkt daneben mit eigenem Padding
        gbcCheck.gridx = 1;
        gbcCheck.weightx = 0.0;
        gbcCheck.anchor = GridBagConstraints.WEST;
        gbcCheck.insets = new Insets(checkboxPaddingVertical, checkboxPaddingLeft,
                checkboxPaddingVertical, checkboxPaddingRight);
        checkRow.add(dontShowAgainCheckBox, gbcCheck);

        // Hauptcontainer mit GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = SCROLLPANE_HEIGHT_FACTOR; // 80% für ScrollPane
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(topRowPaddingVertical, topRowPaddingLeft,
                topRowPaddingVertical, topRowPaddingRight);
        contentPanel.add(scrollPane, gbc);

        gbc.gridy = 1;
        gbc.weighty = CHECKROW_HEIGHT_FACTOR; // 5% für Checkbox-Zeile
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(checkRowPaddingVertical, checkRowPaddingLeft,
                checkRowPaddingVertical, checkRowPaddingRight);
        contentPanel.add(checkRow, gbc);

        dialog = new MSimpleDialog(contentPanel, DIALOG_WIDTH, DIALOG_HEIGHT)
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

        htmlPanel.setDialogSize(DIALOG_WIDTH, DIALOG_HEIGHT)
                .addText("Additional authentication needed.", "white", fontSize, "none")
                .addText("Please open the following address in your browser:", "white", fontSize, "none")
                .addHyperlink(oAuthLink, oAuthLink, "#87CEEB", fontSizeLink, "underline",
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
    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    protected abstract void onException(Exception exc);
    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    protected abstract void onCanceled();
    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    protected abstract void onDontShowAgainSelected();
}












