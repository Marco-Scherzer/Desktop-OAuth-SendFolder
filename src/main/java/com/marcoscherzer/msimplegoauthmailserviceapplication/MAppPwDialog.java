package com.marcoscherzer.msimplegoauthmailserviceapplication;

import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Copyright Marco Scherzer, All rights reserved
 */
public final class MAppPwDialog {

    private Consumer<String> okHandler;

    /**
     * Copyright Marco Scherzer, All rights reserved
     */
    public MAppPwDialog setOkHandler(Consumer<String> handler) {
        this.okHandler = handler;
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     */
    public void showAndWait() throws InterruptedException, InvocationTargetException {
        JPasswordField pwField = new JPasswordField("testTesttest-123");
        pwField.setColumns(20);
        MSimpleDialog dialog = new MSimpleDialog(pwField, 400, 200)
                .setTitle("Enter Password")
                .setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        dialog.addButton("OK", e -> {
            if (okHandler != null) {
                String password = new String(pwField.getPassword());
                okHandler.accept(password);
            }
        }, true); // Dialog schließen nach OK
        dialog.addButton("Cancel", e -> {
            MMain.exit(null, 0);
        }, true);
        dialog.showAndWait();
    }
}



