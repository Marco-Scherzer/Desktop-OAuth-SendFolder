package com.marcoscherzer.msimplegoauthmailerapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Copyright Marco Scherzer, All rights reserved
 */
public final class MFullscreenOverlay {
    private final JWindow window = new JWindow();
    private final JScrollBar vbar = new JScrollBar(Adjustable.VERTICAL);
    private final Timer anim;
    private int dir = +1;

    public MFullscreenOverlay() {
        // Bildschirmgröße bestimmen
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        // Nur 5% der Breite nutzen
        int overlayWidth = (int)(screen.width * 0.05);
        window.setBounds(screen.x, screen.y, overlayWidth, screen.height);

        window.setAlwaysOnTop(true);
        window.setBackground(new Color(0,0,0,0)); // Transparenz
        window.setFocusableWindowState(false);
        window.setFocusable(false);

        JPanel root = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 80)); // halbtransparentes Schwarz
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);

        // Scrollbar konfigurieren
        vbar.setMinimum(0);
        vbar.setMaximum(100);
        vbar.setValue(0);
        vbar.setPreferredSize(new Dimension(18, 100));
        vbar.setEnabled(false);
        vbar.setOpaque(false);
        vbar.setBorder(null);
        vbar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(70, 130, 180, 200); // steel blue
                trackColor = new Color(255, 255, 255, 60);
            }
            @Override protected JButton createDecreaseButton(int orientation) { return zero(); }
            @Override protected JButton createIncreaseButton(int orientation) { return zero(); }
            private JButton zero() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0,0));
                b.setOpaque(false);
                b.setBorder(BorderFactory.createEmptyBorder());
                return b;
            }
        });

        root.add(vbar);
        window.setContentPane(root);

        window.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                vbar.setBounds(0, 0, vbar.getPreferredSize().width, window.getHeight());
            }
            @Override public void componentShown(ComponentEvent e) {
                vbar.setBounds(0, 0, vbar.getPreferredSize().width, window.getHeight());
            }
        });

        // Animation für Scrollbar
        anim = new Timer(20, e -> {
            int v = vbar.getValue() + dir;
            if (v <= vbar.getMinimum()) { v = vbar.getMinimum(); dir = +1; }
            if (v >= vbar.getMaximum() - vbar.getVisibleAmount()) { v = vbar.getMaximum() - vbar.getVisibleAmount(); dir = -1; }
            vbar.setValue(v);
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
}



