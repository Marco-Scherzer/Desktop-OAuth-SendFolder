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
                        dialog.getUIComponent().setVisible(false);

                    } catch (Exception exc) {
                        continueOAuthOrNot.set(false);
                        onException(exc);
                    }
                }, false) // Dialog bleibt offen
                .addButton("Cancel", e -> {
                    continueOAuthOrNot.set(false);
                    dispose();
                    onCanceled();
                }, true); // Dialog schließt

        htmlPanel.setDialogSize(700, 400)
                .addText("Additional authentication needed.", "white", 14, "none")
                .addText("Please open the following address in your browser:", "white", 14, "none")
                .addHyperlink(oAuthLink, oAuthLink, "#87CEEB", 11, "underline",
                        e -> dialog.pressButton(0)) // Klick auf Link simuliert "OK"
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

    /**
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public final void dispose() {
        dialog.getUIComponent().setVisible(false);
        dialog.getUIComponent().dispose();
        if(loginOverlay != null) loginOverlay.dispose();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onException(Exception exc);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onCanceled();

}




