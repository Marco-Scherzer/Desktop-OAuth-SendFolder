package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.marcoscherzer.msimplegoauthmailserviceapplication.MMain;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Marco Scherzer
 * Copyright Marco Scherzer, All rights reserved
 * unready
 */
public final class MButtonOverviewDialog {

    private final String title;
    private final String[] options;
    private final Set<String> selected = new HashSet<>();
    /**
     * @author Marco Scherzer
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public MButtonOverviewDialog(String title, String... options) {
        this.title = title;
        this.options = options;
    }

    /**
     * @author Marco Scherzer
     * Copyright Marco Scherzer, All rights reserved
     * unready
     */
    public Set<String> showAndWait() throws InterruptedException, InvocationTargetException {
        // Anzahl Spalten (z. B. 5)
        int columns = 5;
        JPanel contentPanel = new JPanel(new GridLayout(0, columns, 10, 10));

        // Toggle-Buttons erzeugen und größte Textbreite ermitteln
        java.util.List<JToggleButton> toggleButtons = new ArrayList<>();
        FontMetrics fm = new JButton().getFontMetrics(new JButton().getFont());
        int maxWidth = 0;
        int maxHeight = 0;

        for (String opt : options) {
            JToggleButton toggle = new JToggleButton(opt);
            Dimension d = toggle.getPreferredSize();

            // Textbreite exakt messen, plus etwas Padding
            int textWidth = fm.stringWidth(opt) + 20;
            maxWidth = Math.max(maxWidth, textWidth);
            maxHeight = Math.max(maxHeight, d.height);

            toggleButtons.add(toggle);
        }

        // Einheitliche Größe setzen (alle Buttons gleich breit wie längster Text)
        Dimension uniformSize = new Dimension(maxWidth, maxHeight);
        for (JToggleButton btn : toggleButtons) {
            btn.setPreferredSize(uniformSize);
            contentPanel.add(btn);
        }

        MSimpleDialog dialog = new MSimpleDialog(contentPanel, 900, 600)
                .setTitle(title)
                .setIcon(UIManager.getIcon("OptionPane.informationIcon"));

        dialog.addButton("OK", e -> {
            selected.clear();
            for (JToggleButton btn : toggleButtons) {
                if (btn.isSelected()) {
                    selected.add(btn.getText());
                }
            }
        }, true);

        dialog.addButton("Cancel", e -> {
            selected.clear();
            MMain.exit(null, 0);
        }, true);

        dialog.showAndWait();

        return selected.isEmpty() ? null : new HashSet<>(selected);
    }

    /**
     * @author Marco Scherzer
     * Copyright Marco Scherzer, All rights reserved
     * Test Main
     * unready
     */
    public static void main(String[] args) throws Exception {
        // 50 Dummy-Optionen erzeugen
        String[] testOptions = new String[50];
        testOptions[0] = "DeploymentManager"; // längster Text zum Testen
        for (int i = 1; i < 50; i++) {
            testOptions[i] = "Option " + (i + 1);
        }

        // Dialog starten
        MButtonOverviewDialog dlg = new MButtonOverviewDialog("Test 50 Buttons", testOptions);
        Set<String> chosen = dlg.showAndWait();

        // Ergebnis ausgeben
        if (chosen != null) {
            System.out.println("Ausgewählt: " + chosen);
        } else {
            System.out.println("Abgebrochen");
        }
    }
}




