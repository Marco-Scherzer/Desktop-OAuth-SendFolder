package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright Marco Scherzer, All rights reserved
 */
public final class MFullscreenOverlay {
    private final JWindow window = new JWindow();
    private final JProgressBar progress = new JProgressBar(SwingConstants.VERTICAL);

    public MFullscreenOverlay() {
        // Bildschirmgröße bestimmen
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        // 5% der Breite, 1/3 der Höhe
        int overlayWidth = (int)(screen.width * 0.05);
        int overlayHeight = (int)(screen.height * 0.33);

        // Rechts positionieren, vertikal mittig (Start bei 1/3 von oben)
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
                g2.setColor(new Color(0, 0, 0, 80)); // halbtransparentes Schwarz
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);

        // Fortschrittsanzeige konfigurieren
        progress.setIndeterminate(true); // animiert hin und her
        progress.setForeground(Color.GREEN); // klassisch grün
        progress.setBackground(new Color(245,245,245));
        progress.setBorderPainted(false);

        root.add(progress, BorderLayout.CENTER);
        window.setContentPane(root);
    }

    public void showOverlay() {
        window.setVisible(true);
        window.toFront();
    }

    public void hideOverlay() {
        window.setVisible(false);
    }
}




