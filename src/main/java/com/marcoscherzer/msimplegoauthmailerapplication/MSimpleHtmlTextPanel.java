package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URI;

/**
 * @author Marco Scherzer
 * @copyright Marco Scherzer, All rights reserved
 */
public final class MSimpleHtmlTextPanel extends JPanel {

    private final StringBuilder bodyBuilder = new StringBuilder();
    private final JEditorPane editorPane;

    private int dialogWidth = 600;
    private int dialogHeight = 200;
    private String[] markerSigns = new String[]{"/", "?", "=", "&"};
    private int maxLineLength = 60;

    public MSimpleHtmlTextPanel() {
        super(new BorderLayout());
        editorPane = new JEditorPane("text/html", "");
        editorPane.setEditable(false);
        editorPane.setBackground(UIManager.getColor("Panel.background"));
        this.add(editorPane, BorderLayout.CENTER);
        editorPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
        this.setSize(dialogWidth, dialogHeight);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleHtmlTextPanel setDialogSize(int width, int height) {
        this.dialogWidth = width;
        this.dialogHeight = height;
        editorPane.setPreferredSize(new Dimension(width, height));
        this.setSize(width, height);
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleHtmlTextPanel setWrap(String[] markerSigns, int maxLineLength) {
        this.markerSigns = markerSigns;
        this.maxLineLength = maxLineLength;
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MSimpleHtmlTextPanel addText(String text, String color, int fontSizePx, String decoration) {
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
    public final MSimpleHtmlTextPanel addHyperlink(String text, String url, String color, int fontSizePx, String decoration) {
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
    public final MSimpleHtmlTextPanel setHyperlinkListener(HyperlinkListener listener) {
        editorPane.addHyperlinkListener(listener);
        return this;
    }

    /**
     * Standardlistener: öffnet aktivierte Links im Systembrowser.
     *
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
    public final MSimpleHtmlTextPanel create() {
        editorPane.setText("<html><body style='width:" + dialogWidth + "px; font-family:sans-serif;'>" +
                bodyBuilder.toString() +
                "</body></html>");
        return this;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final JEditorPane getEditorPane() {
        return editorPane;
    }
}

