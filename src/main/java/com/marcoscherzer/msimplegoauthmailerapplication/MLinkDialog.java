package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MLinkDialog {

    private String result;
    private int dialogWidth = 600;
    private int dialogHeight = 200;

    private String[] markerSigns = new String[]{"/", "?", "=", "&"};
    private int maxLineLength = 60;

    private HyperlinkListener hyperlinkListener;

    private final StringBuilder bodyBuilder = new StringBuilder();

    public MLinkDialog() { }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog setDialogSize(int width, int height) {
        this.dialogWidth = width;
        this.dialogHeight = height;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog setWrap(String[] markerSigns, int maxLineLength) {
        this.markerSigns = markerSigns;
        this.maxLineLength = maxLineLength;
        return this;
    }

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
    public static HyperlinkListener createDefaultHyperlinkListener() {
        return e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && e.getURL() != null) {
                try {
                    Desktop.getDesktop().browse(URI.create(e.getURL().toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog addText(String text, String color, int fontSizePx, String decoration) {
        bodyBuilder.append("<p style='color:")
                .append(color)
                .append("; font-size:")
                .append(fontSizePx)
                .append("px; text-decoration:")
                .append(decoration)
                .append(";'>")
                .append(applyWrap(text))
                .append("</p>");
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MLinkDialog addHyperlink(String text, String url, String color, int fontSizePx, String decoration) {
        bodyBuilder.append("<p><a href='")
                .append(url)
                .append("' style='color:")
                .append(color)
                .append("; font-size:")
                .append(fontSizePx)
                .append("px; text-decoration:")
                .append(decoration)
                .append(";'>")
                .append(applyWrap(text))
                .append("</a></p>");
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private String applyWrap(String input) {
        String wrapped = input;
        for (String marker : markerSigns) {
            wrapped = wrapped.replace(marker, marker + "<wbr>");
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char c : wrapped.toCharArray()) {
            sb.append(c);
            count++;
            if (count >= maxLineLength) {
                sb.append("<br>");
                count = 0;
            }
        }
        return sb.toString();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final String showAndWait() throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            JEditorPane editorPane = new JEditorPane(
                    "text/html",
                    "<html><body style='width:" + dialogWidth + "px; font-family:sans-serif;'>" +
                            bodyBuilder.toString() +
                            "</body></html>"
            );
            editorPane.setEditable(false);
            editorPane.setBackground(UIManager.getColor("Panel.background"));
            editorPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));

            if (hyperlinkListener != null) {
                editorPane.addHyperlinkListener(hyperlinkListener);
            }

            int option = JOptionPane.showConfirmDialog(
                    null,
                    editorPane,
                    "Custom HTML Dialog",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                result = "OK";
            } else {
                result = null;
            }
        };

        SwingUtilities.invokeAndWait(task);
        return result;
    }
}













