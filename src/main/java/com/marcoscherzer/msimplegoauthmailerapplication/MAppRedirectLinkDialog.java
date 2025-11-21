package com.marcoscherzer.msimplegoauthmailerapplication;

import com.marcoscherzer.msimplegoauthmailerapplication.util.MSimpleDialog;
import com.marcoscherzer.msimplegoauthmailerapplication.util.MSimpleHtmlTextPanel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppRedirectLinkDialog {

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final int showAndWait(String oAuthLink) throws InterruptedException, InvocationTargetException {
        MSimpleHtmlTextPanel htmlPanel = new MSimpleHtmlTextPanel();

        MSimpleDialog dialog = new MSimpleDialog(htmlPanel)
                .setTitle("OAuth login")
                .setMessageType(JOptionPane.INFORMATION_MESSAGE)
                .setOptionType(JOptionPane.DEFAULT_OPTION)
                .setOptions(new Object[]{"OK", "Cancel"})
                .setInitialButtonValue("OK");

        htmlPanel.setDialogSize(700, 350)
                .addText("Additional authentication needed.", "white", 18, "none")
                .addText("Please open the following address in your browser:", "white", 14, "none")
                .addHyperlink(oAuthLink, oAuthLink, "yellow", 14, "underline",
                        e -> { dialog.pressButton(0); }) // Klick auf Link simuliert "OK"
                .create();

        return dialog.showAndWait();
    }
}



