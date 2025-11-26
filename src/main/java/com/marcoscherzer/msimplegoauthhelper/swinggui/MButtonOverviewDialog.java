package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.marcoscherzer.msimplegoauthmailserviceapplication.MMain;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MButtonOverviewDialog {

    private final String title;
    private final String[] options;
    private String selected;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MButtonOverviewDialog(String title, String... options) {
        this.title = title;
        this.options = options;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String showAndWait() throws InterruptedException, InvocationTargetException {
        MSimpleDialog dialog = new MSimpleDialog(new JPanel(), 400, 300)
                .setTitle(title)
                .setIcon(UIManager.getIcon("OptionPane.informationIcon"));

        for (String opt : options) {
            dialog.addButton(opt, e -> {
                selected = opt;
            }, true);
        }

        dialog.addButton("Cancel", e -> {
            selected = null;
            MMain.exit(null, 0);
        }, true);

        dialog.showAndWait();
        return selected;
    }

}

