package com.marcoscherzer.msimplegoauthmailerclientapplication;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MAppLoggingArea {

    private JTextArea logArea;
    private JFrame logFrame;
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MAppLoggingArea(boolean wait) throws InterruptedException, InvocationTargetException {
        Runnable task = () -> {
            logArea = new JTextArea(20, 80);
            logArea.setEditable(false);

            logFrame = new JFrame("Log Output");
            logFrame.add(new JScrollPane(logArea));
            logFrame.pack();
            logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            logFrame.setVisible(true);

            OutputStream outStream = new OutputStream() {
                @Override
                public void write(byte[] b, int off, int len) {
                    final String text = new String(b, off, len);
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(text);
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                    });
                }

                @Override
                public void write(int b) {
                    write(new byte[]{(byte) b}, 0, 1);
                }
            };

            PrintStream ps = new PrintStream(outStream, true);
            System.setOut(ps);
            System.setErr(ps);
        };

        if (wait) {
            SwingUtilities.invokeAndWait(task);
        } else {
            SwingUtilities.invokeLater(task);
        }
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final JTextArea getLogArea() {
        return logArea;
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final JFrame getLogFrame() {
        return logFrame;
    }
}

