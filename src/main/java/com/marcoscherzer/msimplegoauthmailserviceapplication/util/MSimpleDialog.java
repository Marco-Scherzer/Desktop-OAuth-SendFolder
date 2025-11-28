package com.marcoscherzer.msimplegoauthmailserviceapplication.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleDialog {
    private final JComponent content;

    private String title = "";
    private int messageType = JOptionPane.INFORMATION_MESSAGE;
    private Icon icon = null;
    private final List<Object> buttonLabels = new ArrayList<>();
    private final List<ActionListener> buttonHandlers = new ArrayList<>();
    private final List<Boolean> buttonCloseFlags = new ArrayList<>();
    private JOptionPane pane;
    private JDialog dialog;
    private final int width;
    private final int height;

    /**
     * Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleDialog(JComponent content, int width, int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    public MSimpleDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MSimpleDialog setMessageType(int messageType) {
        this.messageType = messageType;
        return this;
    }

    public MSimpleDialog setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public MSimpleDialog addButton(String label, ActionListener handler, boolean close) {
        buttonLabels.add(label);
        buttonHandlers.add(handler);
        buttonCloseFlags.add(close);
        return this;
    }

    public void pressButton(int index) {
        if (pane != null && dialog != null && index >= 0 && index < buttonLabels.size()) {
            pane.setValue(buttonLabels.get(index));
            if (buttonCloseFlags.get(index)) {
                dialog.dispose();
            }
            ActionListener handler = buttonHandlers.get(index);
            if (handler != null) {
                handler.actionPerformed(null);
            }
        }
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     */
    public void showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            pane = new JOptionPane(
                    content,
                    messageType,
                    JOptionPane.DEFAULT_OPTION,
                    icon,
                    buttonLabels.toArray(),
                    buttonLabels.isEmpty() ? null : buttonLabels.get(0)
            );

            dialog = new JDialog((Frame) null, title, true); // modal
            dialog.setUndecorated(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            // Icon + Titel oben mittig
            if (icon != null || !title.isEmpty()) {
                JPanel iconTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                Icon effectiveIcon = (icon != null) ? icon : UIManager.getIcon("OptionPane.informationIcon");
                if (effectiveIcon != null) {
                    iconTitlePanel.add(new JLabel(effectiveIcon));
                }
                if (!title.isEmpty()) {
                    JLabel titleLabel = new JLabel(title);
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
                    iconTitlePanel.add(titleLabel);
                }
                panel.add(iconTitlePanel, BorderLayout.NORTH);
            }

            // Content mittig mit zusätzlichem Abstand links/rechts
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            contentPanel.add(content, BorderLayout.CENTER);
            panel.add(contentPanel, BorderLayout.CENTER);

            // Buttons unten mittig
            if (!buttonLabels.isEmpty()) {
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                for (int i = 0; i < buttonLabels.size(); i++) {
                    final int idx = i;
                    JButton btn = new JButton(buttonLabels.get(i).toString());
                    btn.addActionListener(e -> {
                        pane.setValue(buttonLabels.get(idx));
                        if (buttonCloseFlags.get(idx)) {
                            dialog.dispose();
                        }
                        ActionListener handler = buttonHandlers.get(idx);
                        if (handler != null) {
                            handler.actionPerformed(e);
                        }
                    });
                    buttonPanel.add(btn);

                    if (i == 0) {
                        dialog.getRootPane().setDefaultButton(btn);
                    }
                }
                panel.add(buttonPanel, BorderLayout.SOUTH);
            }

            dialog.setContentPane(panel);
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeAndWait(task);
        }
    }

    public JDialog getUIComponent() {
        return dialog;
    }
}





















