package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppPwDialog {

    private String result;

    public MAppPwDialog() { }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            JPasswordField pwField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    null,
                    pwField,
                    "Enter Password",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION) {
                result = new String(pwField.getPassword());
            } else {
                result = null;
            }
        };
        SwingUtilities.invokeAndWait(task);
        return result;
    }
}
