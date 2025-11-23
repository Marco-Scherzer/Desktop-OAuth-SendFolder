package com.marcoscherzer.msimplegoauthhelper.swinggui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MFullscreenOverlay {

    // --- Globale Konstanten ---
    private static final double WFAC = 0.33;   // Breitenfaktor (33% der Screenbreite)
    private static final double HFAC = 0.15;   // Höhenfaktor (15% der Screenhöhe)

    // Abstand zwischen Text und Spinner (prozentual von Overlay-Höhe)
    private static final double GAPFAC = 0.05;   // 5% der Overlay-Höhe
    // Abstand oben (prozentual von Overlay-Höhe)
    private static final double TOPFAC = 0.08;   // 8% der Overlay-Höhe

    private static final Color OVERLAY_BG_COLOR = new Color(7, 7, 7, 200);      // halbtransparentes Overlay
    private static final Color WINDOW_BG_COLOR = new Color(0, 0, 0, 0);        // komplett transparent
    private static final Color STATUS_TEXT_COLOR = Color.WHITE;                // Statuslabel-Farbe
    private static final Color SPINNER_COLOR_BASE = new Color(0, 128, 230);    // Spinner-Grundfarbe

    private final JWindow window = new JWindow();
    private final SpinnerPanel spinner = new SpinnerPanel();
    private final Timer anim;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MFullscreenOverlay() {
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        int overlayWidth = (int)(screen.width * WFAC);
        int overlayHeight = (int)(screen.height * HFAC);

        int gap = (int)(overlayHeight * GAPFAC);   // Abstand Text–Spinner
        int topGap = (int)(overlayHeight * TOPFAC); // Abstand oben

        int x = screen.x + (screen.width - overlayWidth) / 2; // mittig horizontal
        int y = screen.y;                                     // oben am Screen

        window.setBounds(x, y, overlayWidth, overlayHeight);
        window.setAlwaysOnTop(true);
        window.setBackground(WINDOW_BG_COLOR);
        window.setFocusableWindowState(false);
        window.setFocusable(false);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(OVERLAY_BG_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);

        // --- Schriftgröße kleiner und zentriert ---
        int fontSize = (int)(overlayHeight * 0.18); // Schrift = 18% der Overlay-Höhe
        JLabel statusLabel = new JLabel("Waiting for Login");
        statusLabel.setForeground(STATUS_TEXT_COLOR);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Panel mit BoxLayout (Y_AXIS) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Abstand oben prozentual
        contentPanel.add(Box.createRigidArea(new Dimension(0, topGap)));

        // Text
        contentPanel.add(statusLabel);

        // Abstand zwischen Text und Spinner
        contentPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        // Spinnergröße abhängig von Overlay-Höhe
        int spinnerSize = (int)(overlayHeight * 0.55); // Spinner = 55% der Overlay-Höhe
        spinner.setPreferredSize(new Dimension(spinnerSize, spinnerSize));
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(spinner);

        root.add(contentPanel, BorderLayout.CENTER);
        window.setContentPane(root);

        anim = new Timer(50, e -> {
            spinner.rotate();
            spinner.repaint();
        });
    }

    public void showOverlay() {
        anim.start();
        window.setVisible(true);
        window.toFront();
    }

    public void hideOverlay() {
        anim.stop();
        window.setVisible(false);
        window.dispose();
    }

    private static class SpinnerPanel extends JPanel {
        private int angle = 0;

        SpinnerPanel() {
            setOpaque(false);
        }

        void rotate() {
            angle = (angle + 10) % 360;
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Radius abhängig von Panel-Höhe
            int radius = (int)(getHeight() * 0.4); // 40% der Höhe als Radius
            int dotSize = (int)(getHeight() * 0.1); // Punkte = 10% der Höhe
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            for (int i = 0; i < 12; i++) {
                float alpha = (float) i / 12f;
                Color c = new Color(
                        SPINNER_COLOR_BASE.getRed(),
                        SPINNER_COLOR_BASE.getGreen(),
                        SPINNER_COLOR_BASE.getBlue(),
                        (int)(alpha * 255));
                g2.setColor(c);
                double rad = Math.toRadians(angle + i * 30);
                int x = (int)(cx + Math.cos(rad) * radius);
                int y = (int)(cy + Math.sin(rad) * radius);
                g2.fillOval(x - dotSize/2, y - dotSize/2, dotSize, dotSize);
            }

            g2.dispose();
        }
    }
}


























