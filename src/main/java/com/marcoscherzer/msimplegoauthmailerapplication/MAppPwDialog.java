package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppPwDialog {

    private MAppPwDialog() { }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final String createAndShowDialog() throws InterruptedException, InvocationTargetException {
        final AtomicReference<String> resultRef = new AtomicReference<>();

        Runnable task = () -> {
            JPasswordField pwField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    null,
                    pwField,
                    "Enter Password",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION) {
                resultRef.set(new String(pwField.getPassword()));
            } else {
                resultRef.set(null);
            }
        };

        SwingUtilities.invokeAndWait(task);
        return resultRef.get();
    }
}

