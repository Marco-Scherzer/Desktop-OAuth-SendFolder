
package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.awt.*;
/*
 * Copyright Marco Scherzer, All rights reserved.
 */
public final class MFullscreenOverlay {

    // --- Globale Konstanten ---
    private static final double WFAC = 0.06;   // Breitenfaktor (6% der Screenbreite)
    private static final double HFAC = 0.30;   // Höhenfaktor (30% der Screenhöhe)

    private static final Color OVERLAY_BG_COLOR = new Color(7, 7, 7, 200);      // halbtransparentes Overlay
    private static final Color WINDOW_BG_COLOR = new Color(0, 0, 0, 0);        // komplett transparent
    private static final Color STATUS_TEXT_COLOR = Color.WHITE;                // Statuslabel-Farbe
    private static final Color SPINNER_COLOR_BASE = new Color(0, 128, 230);    // Spinner-Grundfarbe

    private final JWindow window = new JWindow();
    private final JLayeredPane layeredPane = new JLayeredPane();
    private final SpinnerPanel spinner = new SpinnerPanel();
    private final JLabel iconLabel;
    private final Timer anim;
    /*
     * Copyright Marco Scherzer, All rights reserved.
     */
    public MFullscreenOverlay() {
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        int overlayWidth = (int)(screen.width * WFAC);
        int overlayHeight = (int)(screen.height * HFAC);
        int x = screen.x + screen.width - overlayWidth;
        int y = screen.y + (screen.height - overlayHeight) / 2;

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

        layeredPane.setPreferredSize(new Dimension(overlayWidth, overlayHeight));
        layeredPane.setOpaque(false);

        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/12.png"));
        int scaledIconWidth = (int)(screen.width * WFAC * 0.7);
        double scaleFactor = (double) scaledIconWidth / rawIcon.getIconWidth();
        int scaledIconHeight = (int)(rawIcon.getIconHeight() * scaleFactor);

        Image scaled = rawIcon.getImage().getScaledInstance(
                scaledIconWidth, scaledIconHeight, Image.SCALE_SMOOTH);
        iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setOpaque(false);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        spinner.setOpaque(false);

        layeredPane.add(iconLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(spinner, JLayeredPane.PALETTE_LAYER);

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(STATUS_TEXT_COLOR);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        root.add(statusLabel, BorderLayout.NORTH);
        root.add(layeredPane, BorderLayout.CENTER);
        window.setContentPane(root);

        SwingUtilities.invokeLater(() -> {
            int textHeight = statusLabel.getPreferredSize().height;
            int lpHeight = layeredPane.getHeight();
            int lpWidth = layeredPane.getWidth();

            spinner.setBounds(0, -textHeight/2, lpWidth, lpHeight);
            iconLabel.setBounds((lpWidth - scaledIconHeight) / 2,
                    (lpHeight - scaledIconHeight) / 2 - textHeight/2,
                    scaledIconHeight, scaledIconHeight);
        });

        anim = new Timer(50, e -> {
            spinner.rotate();
            spinner.repaint();
        });
    }
    /*
     * Copyright Marco Scherzer, All rights reserved.
     */
    public void showOverlay() {
        anim.start();
        window.setVisible(true);
        window.toFront();
    }
    /*
     * Copyright Marco Scherzer, All rights reserved.
     */
    public void hideOverlay() {
        anim.stop();
        window.setVisible(false);
    }
    /*
     * Copyright Marco Scherzer, All rights reserved.
     */
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

            int size = Math.min(getWidth(), getHeight()) - 20;
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
                int x = (int)(cx + Math.cos(rad) * size/2);
                int y = (int)(cy + Math.sin(rad) * size/2);
                g2.fillOval(x-4, y-4, 8, 8);
            }

            g2.dispose();
        }
    }

}















