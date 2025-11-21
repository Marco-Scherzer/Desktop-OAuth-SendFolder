package com.marcoscherzer.msimplegoauthmailerapplication.util;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleDialog {
    private final JComponent content;

    private String title = "";
    private int optionType = JOptionPane.DEFAULT_OPTION;
    private int messageType = JOptionPane.INFORMATION_MESSAGE;
    private Icon icon = null;
    private Object[] options = null;
    private Object initialButtonValue = null;
    private JOptionPane pane;
    private JDialog dialog;
    private int result = JOptionPane.CLOSED_OPTION;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleDialog(JComponent content) {
        this.content = content;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setOptionType(int optionType) {
        this.optionType = optionType;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setMessageType(int messageType) {
        this.messageType = messageType;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setOptions(Object[] options) {
        this.options = options;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleDialog setInitialButtonValue(Object initialButtonValue) {
        this.initialButtonValue = initialButtonValue;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final int pressButton(int index) {
        if (pane != null && dialog != null && options != null && index >= 0 && index < options.length) {
            pane.setValue(options[index]);   // simuliert Klick
            result = index;                  // Ergebnis setzen
            dialog.dispose();                // Dialog schließen
        }
        return result;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final int showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            pane = new JOptionPane(
                    content,
                    messageType,
                    optionType,
                    icon,
                    options,
                    initialButtonValue
            );

            dialog = pane.createDialog(title);
            dialog.setModal(true);
            dialog.setVisible(true);

            Object selectedValue = pane.getValue();
            if (selectedValue == null) {
                result = JOptionPane.CLOSED_OPTION;
            } else {
                result = Arrays.asList(options).indexOf(selectedValue);
            }
        };

        SwingUtilities.invokeAndWait(task);
        return result;
    }
}















