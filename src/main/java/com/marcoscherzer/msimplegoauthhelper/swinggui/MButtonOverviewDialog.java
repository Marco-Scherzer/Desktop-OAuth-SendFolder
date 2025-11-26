package com.marcoscherzer.msimplegoauthhelper.swinggui;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.marcoscherzer.msimplegoauthhelper.swinggui.mgridbuilder.MGridBuilder;
import com.marcoscherzer.msimplegoauthhelper.swinggui.mgridbuilder.MGridBuilder.MGridLine;
import com.marcoscherzer.msimplegoauthmailserviceapplication.MMain;
import com.marcoscherzer.msimplegoauthmailserviceapplication.util.MSimpleDialog;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class MButtonOverviewDialog {

    private final String title;
    private final String[] options;
    private final Set<String> selected = new HashSet<>();

    private int currentPage = 0;

    // Basisgröße
    private final int dialogWidth = 900;
    private final int dialogHeight = 600;

    // Prozentuale Layout-Konstanten
    private final float leftColumnPercent = 0.9f;
    private final float rightColumnPercent = 0.1f;
    private final float topRowPercent = 0.1f;
    private final float middleRowPercent = 0.8f;
    private final float bottomRowPercent = 0.1f;

    // Buttons pro Seite
    private final int btnLineCnt = 5;
    private final int btnHeightCnt = 5;

    public MButtonOverviewDialog(String title, String... options) {
        this.title = title;
        this.options = options;
    }

    public Set<String> showAndWait() throws InterruptedException, InvocationTargetException {
        java.util.List<JToggleButton> toggleButtons = new ArrayList<>();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> selectionList = new JList<>(listModel);

        // Rechte Seite als 1x1 Grid
        MGridBuilder rightBuilder = new MGridBuilder();
        rightBuilder.setColumnWidths(1f);
        MGridLine rightLine = rightBuilder.addLine(1f);
        rightLine.add(new JScrollPane(selectionList), 1);
        JPanel rightPanel = rightBuilder.create((int)(dialogWidth * rightColumnPercent), dialogHeight);

        // CardPanel mit Seiten
        JPanel cardPanel = new JPanel(new CardLayout());

        int totalPages = (int) Math.ceil((double) options.length / (btnLineCnt * btnHeightCnt));
        for (int p = 0; p < totalPages; p++) {
            MGridBuilder pageBuilder = new MGridBuilder();
            float[] colWidths = new float[btnLineCnt];
            Arrays.fill(colWidths, 1f);
            pageBuilder.setColumnWidths(colWidths);

            for (int r = 0; r < btnHeightCnt; r++) {
                MGridLine line = pageBuilder.addLine(1f);
                for (int c = 0; c < btnLineCnt; c++) {
                    int idx = p * btnLineCnt * btnHeightCnt + r * btnLineCnt + c;
                    if (idx >= options.length) break;
                    JToggleButton toggle = new JToggleButton(options[idx]);
                    toggle.addActionListener(e -> {
                        selected.clear();
                        listModel.clear();
                        for (JToggleButton btn : toggleButtons) {
                            if (btn.isSelected()) {
                                selected.add(btn.getText());
                                listModel.addElement(btn.getText());
                            }
                        }
                    });
                    toggleButtons.add(toggle);
                    line.add(toggle, btnLineCnt);
                }
            }
            JPanel pagePanel = pageBuilder.create((int)(dialogWidth * leftColumnPercent),
                    (int)(dialogHeight * middleRowPercent));
            cardPanel.add(pagePanel, "page" + p);
        }

        JButton upButton = new JButton("▲");
        JButton downButton = new JButton("▼");
        upButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "page" + currentPage);
            }
        });
        downButton.addActionListener(e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "page" + currentPage);
            }
        });

        // Linke Seite als 1x3 Grid
        MGridBuilder leftBuilder = new MGridBuilder();
        leftBuilder.setColumnWidths(1f);
        MGridLine lineTop = leftBuilder.addLine(topRowPercent);
        lineTop.add(upButton, 1);
        MGridLine lineMid = leftBuilder.addLine(middleRowPercent);
        lineMid.add(new JScrollPane(cardPanel), 1);
        MGridLine lineBot = leftBuilder.addLine(bottomRowPercent);
        lineBot.add(downButton, 1);
        JPanel leftPanel = leftBuilder.create((int)(dialogWidth * leftColumnPercent), dialogHeight);

        // Hauptlayout als 1x2 Grid
        MGridBuilder mainBuilder = new MGridBuilder();
        mainBuilder.setColumnWidths(leftColumnPercent, rightColumnPercent);
        MGridLine mainLine = mainBuilder.addLine(1f);
        mainLine.add(leftPanel, 2);
        mainLine.add(rightPanel, 2);
        JPanel mainPanel = mainBuilder.create(dialogWidth, dialogHeight);

        MSimpleDialog dialog = new MSimpleDialog(mainPanel, dialogWidth, dialogHeight)
                .setTitle(title)
                .setIcon(UIManager.getIcon("OptionPane.informationIcon"));

        dialog.addButton("OK", e -> {
            selected.clear();
            listModel.clear();
            for (JToggleButton btn : toggleButtons) {
                if (btn.isSelected()) {
                    selected.add(btn.getText());
                    listModel.addElement(btn.getText());
                }
            }
        }, true);

        dialog.addButton("Cancel", e -> {
            selected.clear();
            listModel.clear();
            MMain.exit(null, 0);
        }, true);

        dialog.showAndWait();
        return selected.isEmpty() ? null : new HashSet<>(selected);
    }

    public static void main(String[] args) throws Exception {
        FlatCarbonIJTheme.setup();
        UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 16));
        String[] testOptions = new String[50];
        for (int i = 0; i < testOptions.length; i++) {
            testOptions[i] = "Option " + (i + 1);
        }
        MButtonOverviewDialog dlg = new MButtonOverviewDialog("PageSlide Test", testOptions);
        Set<String> chosen = dlg.showAndWait();
        System.out.println(chosen != null ? "Ausgewählt: " + chosen : "Abgebrochen");
    }
}

