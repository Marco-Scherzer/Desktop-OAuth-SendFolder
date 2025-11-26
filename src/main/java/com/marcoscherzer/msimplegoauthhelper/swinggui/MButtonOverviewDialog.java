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

    private int currentPage = 0;
    private final int rowsPerPage = 5; // Anzahl Zeilen pro Seite

    // Basisgröße des Dialogs
    private final int dialogWidth = 900;
    private final int dialogHeight = 600;

    public MButtonOverviewDialog(String title, String... options) {
        this.title = title;
        this.options = options;
    }

    public Set<String> showAndWait() throws InterruptedException, InvocationTargetException {
        java.util.List<JToggleButton> toggleButtons = new ArrayList<>();

        // Prozentuale Berechnung der Buttongröße
        int buttonWidth = (int) (dialogWidth * 0.12 *0.9);
        int buttonHeight = (int) (dialogHeight * 0.07 *0.9);
        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

        int fontSize = (int) (buttonHeight * 0.4 * 0.9);
        Font buttonFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);

        // Liste für ausgewählte Elemente (rechts)
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> selectionList = new JList<>(listModel);
        selectionList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        JScrollPane selectionScroll = new JScrollPane(selectionList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //selectionScroll.setMinimumSize(new Dimension(100,1000));

        // Alle Zeilen vorbereiten
        java.util.List<JPanel> rowPanels = new ArrayList<>();
        int columns = 7;
        for (int i = 0; i < options.length; i += columns) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            for (int j = i; j < i + columns && j < options.length; j++) {
                JToggleButton toggle = new JToggleButton(options[j]);
                toggle.setPreferredSize(buttonSize);
                toggle.setMinimumSize(buttonSize);
                toggle.setFont(buttonFont);

                // Listener: aktualisiert Liste sofort
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
                rowPanel.add(toggle);
            }
            rowPanels.add(rowPanel);
        }

        // Hauptcontainer mit CardLayout für Seiten
        JPanel cardPanel = new JPanel(new CardLayout());

        int totalPages = (int) Math.ceil((double) rowPanels.size() / rowsPerPage);
        for (int p = 0; p < totalPages; p++) {
            JPanel page = new JPanel(new GridLayout(rowsPerPage, 1, 5, 5));
            int start = p * rowsPerPage;
            int end = Math.min(start + rowsPerPage, rowPanels.size());
            for (int k = start; k < end; k++) {
                page.add(rowPanels.get(k));
            }
            cardPanel.add(page, "page" + p);
        }

        // Pfeile für Navigation
        JButton upButton = new JButton("▲");
        JButton downButton = new JButton("▼");
        upButton.setFont(buttonFont);
        downButton.setFont(buttonFont);

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

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(upButton, BorderLayout.NORTH);
        navPanel.add(cardPanel, BorderLayout.CENTER);
        navPanel.add(downButton, BorderLayout.SOUTH);

        // SplitPane: links 90%, rechts 10%
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, selectionScroll);
        splitPane.setResizeWeight(0.9); // 90% links, 10% rechts
        splitPane.setDividerSize(5);

        JScrollPane mainScroll = new JScrollPane(splitPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        MSimpleDialog dialog = new MSimpleDialog(mainScroll, dialogWidth, dialogHeight)
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
        String[] testOptions = new String[50];
        for (int i = 0; i < testOptions.length; i++) {
            testOptions[i] = "Option " + (i + 1);
        }
        MButtonOverviewDialog dlg = new MButtonOverviewDialog("PageSlide Test", testOptions);
        Set<String> chosen = dlg.showAndWait();
        System.out.println(chosen != null ? "Ausgewählt: " + chosen : "Abgebrochen");
    }
}
