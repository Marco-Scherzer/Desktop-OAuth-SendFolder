package com.marcoscherzer.msimplegoauthhelper.swinggui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MSpinnerOverlayFrame {

    // --- Globale Konstanten ---
    private static final double WFAC = 0.33;
    private static final double HFAC = 0.15;
    private static final double GAPFAC = 0.05;
    private static final double TOPFAC = 0.08;

    private static final Color OVERLAY_BG_COLOR = new Color(7, 7, 7, 200);
    private static final Color WINDOW_BG_COLOR = new Color(0, 0, 0, 0);
    private static final Color STATUS_TEXT_COLOR = Color.WHITE;
    private static final Color SPINNER_COLOR_BASE = new Color(0, 128, 230);

    private final JWindow window = new JWindow();
    private final SpinnerPanel spinner = new SpinnerPanel();
    private Timer anim;

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSpinnerOverlayFrame() {
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        int overlayWidth = (int)(screen.width * WFAC);
        int overlayHeight = (int)(screen.height * HFAC);

        int gap = (int)(overlayHeight * GAPFAC);
        int topGap = (int)(overlayHeight * TOPFAC);

        int x = screen.x + (screen.width - overlayWidth) / 2;
        int y = screen.y;

        if (SwingUtilities.isEventDispatchThread()) {
            initWindow(x, y, overlayWidth, overlayHeight, gap, topGap);
        } else {
            SwingUtilities.invokeLater(() -> initWindow(x, y, overlayWidth, overlayHeight, gap, topGap));
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void initWindow(int x, int y, int overlayWidth, int overlayHeight, int gap, int topGap) {
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

        int fontSize = (int)(overlayHeight * 0.18);
        JLabel statusLabel = new JLabel("Waiting for Login");
        statusLabel.setForeground(STATUS_TEXT_COLOR);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createRigidArea(new Dimension(0, topGap)));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        int spinnerSize = (int)(overlayHeight * 0.55);
        spinner.setPreferredSize(new Dimension(spinnerSize, spinnerSize));
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(spinner);

        root.add(contentPanel, BorderLayout.CENTER);
        window.setContentPane(root);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void showOverlay() {
        setVisible(true);
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setVisible(boolean visible) {
        Runnable task = () -> {
            if (visible) {
                if (!window.isVisible()) {
                    window.setVisible(true);
                    window.toFront();
                    anim = new Timer(50, e -> {
                        spinner.rotate();
                        spinner.repaint();
                    });
                    anim.start();
                }
            } else {
                if (window.isVisible()) {
                    if (anim != null) {
                        anim.stop();
                        anim = null;
                    }
                    window.setVisible(false);
                }
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void dispose() {
        Runnable task = () -> {
            if (anim != null) {
                anim.stop();
                anim = null;
            }
            window.dispose();
        };

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setMouseHandler(MouseListener listener) {
        Runnable task = () -> window.getContentPane().addMouseListener(listener);

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static final class SpinnerPanel extends JPanel {
        private int angle = 0;
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private SpinnerPanel() {
            setOpaque(false);
        }
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private final void rotate() {
            angle = (angle + 10) % 360;
        }
        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override protected final void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int radius = (int)(getHeight() * 0.4);
            int dotSize = (int)(getHeight() * 0.1);
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






























