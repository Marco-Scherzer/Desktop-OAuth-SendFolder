package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.marcoscherzer.msimplegoauthmailserviceapplication.MMain;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * impl start
 * unready
 */
public final class MButtonOverviewDialog {

    private final String title;
    private final String[] options;
    private final Set<String> selected = new HashSet<>();
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public MButtonOverviewDialog(String title, String... options) {
        this.title = title;
        this.options = options;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public Set<String> showAndWait() throws InterruptedException, InvocationTargetException {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Toggle-Buttons für jede Option
        List<JToggleButton> toggleButtons = new ArrayList<>();
        for (String opt : options) {
            JToggleButton toggle = new JToggleButton(opt);
            toggleButtons.add(toggle);
            contentPanel.add(toggle);
        }

        MSimpleDialog dialog = new MSimpleDialog(contentPanel, 400, 600)
                .setTitle(title)
                .setIcon(UIManager.getIcon("OptionPane.informationIcon"));

        // OK-Button: sammelt alle ausgewählten Optionen
        dialog.addButton("OK", e -> {
            selected.clear();
            for (JToggleButton btn : toggleButtons) {
                if (btn.isSelected()) {
                    selected.add(btn.getText());
                }
            }
        }, true);

        // Cancel-Button: beendet ohne Auswahl
        dialog.addButton("Cancel", e -> {
            selected.clear();
            MMain.exit(null, 0);
        }, true);

        dialog.showAndWait();

        return selected.isEmpty() ? null : new HashSet<>(selected);
    }

    /**
     * Test-Main
     * unready
     */
    public static void main(String[] args) throws Exception {
        String[] testOptions = new String[50];
        for (int i = 0; i < 50; i++) {
            testOptions[i] = "Option " + (i + 1);
        }

        MButtonOverviewDialog dlg = new MButtonOverviewDialog("Test 50 Buttons", testOptions);
        Set<String> chosen = dlg.showAndWait();

        if (chosen != null) {
            System.out.println("Ausgewählt: " + chosen);
        } else {
            System.out.println("Abgebrochen");
        }
    }
}



