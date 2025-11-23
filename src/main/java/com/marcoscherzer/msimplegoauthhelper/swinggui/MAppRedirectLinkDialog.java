package com.marcoscherzer.msimplegoauthhelper.swinggui;
import com.marcoscherzer.msimplegoauthmailserviceapplication.MMain;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MMutableBoolean;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleHtmlTextPanel;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * Copyright Marco Scherzer, All rights reserved
 * unready
 */
public final class MAppRedirectLinkDialog {

    private MSimpleDialog dialog;

    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public MAppRedirectLinkDialog() {}

    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public final void showAndWait(String oAuthLink, MMutableBoolean continueOAuthOrNot) throws InterruptedException, InvocationTargetException {
        MSimpleHtmlTextPanel htmlPanel = new MSimpleHtmlTextPanel();
        dialog = new MSimpleDialog(htmlPanel, 700, 400)
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

                    } catch (Exception exc) {
                        continueOAuthOrNot.set(false);
                        MMain.exit(exc, 0);
                    }
                }, false) // Dialog bleibt offen
                .addButton("Cancel", e -> {
                    continueOAuthOrNot.set(false);
                    MMain.exit(null, 0);
                }, true); // Dialog schließt

        htmlPanel.setDialogSize(700, 400)
                .addText("Additional authentication needed.", "white", 14, "none")
                .addText("Please open the following address in your browser:", "white", 14, "none")
                .addHyperlink(oAuthLink, oAuthLink, "#87CEEB", 11, "underline",
                        e -> dialog.pressButton(0)) // Klick auf Link simuliert "OK"
                .create();
        dialog.showAndWait();
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public void hide() {
        //f.hideOverlay();
        dialog.getUIComponent().setVisible(false);
        dialog.getUIComponent().dispose();
    }
}




