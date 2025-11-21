package com.marcoscherzer.msimplegoauthmailerapplication;

import com.marcoscherzer.msimplegoauthmailerapplication.util.MSimpleDialog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppPwDialog {

    private String result;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String showAndWait() throws InterruptedException, InvocationTargetException {
        JPasswordField pwField = new JPasswordField();

        MSimpleDialog dialog = new MSimpleDialog(pwField)
                .setTitle("Enter Password")
                .setOptionType(JOptionPane.OK_CANCEL_OPTION)
                .setMessageType(JOptionPane.QUESTION_MESSAGE)
                .setOptions(new Object[]{"OK", "Cancel"})
                .setInitialButtonValue("OK");

        int option = dialog.showAndWait();

        if (option == 0) { // Index 0 = "OK"
            result = new String(pwField.getPassword());
        } else {
            result = null;
        }

        return result;
    }
}

