
package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.awt.*;

/*
 * Copyright Marco Scherzer, All rights reserved.
 */
public final class MFullscreenOverlay {
    private final JWindow window = new JWindow();
    private final SpinnerPanel spinner = new SpinnerPanel();
    private final Timer anim;

    public MFullscreenOverlay() {
        // Bildschirmgröße bestimmen
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        // 5% der Breite, 1/3 der Höhe, rechts mittig
        int overlayWidth = (int)(screen.width * 0.05);
        int overlayHeight = (int)(screen.height * 0.33);
        int x = screen.x + screen.width - overlayWidth;
        int y = screen.y + (screen.height - overlayHeight) / 2;

        window.setBounds(x, y, overlayWidth, overlayHeight);
        window.setAlwaysOnTop(true);
        window.setBackground(new Color(0,0,0,0));
        window.setFocusableWindowState(false);
        window.setFocusable(false);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 80)); // halbtransparent
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);

        JLabel statusLabel = new JLabel("Warten auf Login…", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        root.add(statusLabel, BorderLayout.NORTH);
        root.add(spinner, BorderLayout.CENTER);
        window.setContentPane(root);

        // Animation: alle 50ms Winkel erhöhen
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
    }

    // Panel für den Spinner
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

            // Spinner mit 12 Speichen
            for (int i = 0; i < 12; i++) {
                float alpha = (float) i / 12f;
                g2.setColor(new Color(0, 255, 0, (int)(alpha * 255))); // grün, abgestuft
                double rad = Math.toRadians(angle + i * 30);
                int x = (int)(cx + Math.cos(rad) * size/2);
                int y = (int)(cy + Math.sin(rad) * size/2);
                g2.fillOval(x-4, y-4, 8, 8);
            }

            g2.dispose();
        }
    }
}






