package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer
 * @copyright Marco Scherzer, All rights reserved
 */
public final class MHtmlDialog {
    private final JPanel panel;
    private String result;

    MHtmlDialog(JPanel panel){
         this.panel = panel;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            int option = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            result = (option == JOptionPane.OK_OPTION) ? "OK" : null;
        };

        SwingUtilities.invokeAndWait(task);
        return result;
    }
}














