package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MLinkDialog {

    private String result;
    private HyperlinkListener hyperlinkListener;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog() { }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog setHyperlinkListener(HyperlinkListener listener) {
        this.hyperlinkListener = listener;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String showAndWait(String text, String redirectUrl)
            throws InterruptedException, InvocationTargetException {

        Runnable task = () -> {
            // HTML-EditorPane für Linkanzeige
            JEditorPane editorPane = new JEditorPane(
                    "text/html",
                    "<html><body style='width:580px;'>" +
                            "<p>" + text + "</p>" +
                            "<a href='" + redirectUrl + "'>" + redirectUrl + "</a>" +
                            "</body></html>"
            );
            editorPane.setEditable(false);
            editorPane.setBackground(UIManager.getColor("Panel.background"));

            // Nur wenn ein Listener gesetzt ist, wird der Link klickbar
            if (hyperlinkListener != null) {
                editorPane.addHyperlinkListener(hyperlinkListener);
            }

            // ScrollPane mit Scrollbars "as needed"
            JScrollPane scrollPane = new JScrollPane(
                    editorPane,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            scrollPane.setPreferredSize(new Dimension(600, 200));

            int option = JOptionPane.showConfirmDialog(
                    null,
                    scrollPane,
                    "Google OAuth Redirect Link",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                result = redirectUrl;
            } else {
                result = null;
            }
        };

        SwingUtilities.invokeAndWait(task);
        return result;
    }
}


