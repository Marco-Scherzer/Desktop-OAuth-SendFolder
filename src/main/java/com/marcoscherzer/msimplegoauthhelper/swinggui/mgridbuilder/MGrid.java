package com.marcoscherzer.msimplegoauthhelper.swinggui.mgridbuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Copyright Marco Scherzer, All rights reserved
 * Fast implemented incomplete (core)
 * Swing-Version of MGridBuilder and Android MGridBuilder.
 * Copyright Marco Scherzer. All rights reserved.
 */
public final class MGrid extends JPanel {

    private final JPanel[][] cellMatrix;
    private final List<Float> columnWidths;
    private final List<MGridBuilder.MGridLine> lines;

    private final int width;
    private final int height;
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public MGrid(int linesCount, int columns,
                 List<Float> columnWidths,
                 List<MGridBuilder.MGridLine> lineDefs,
                 int width, int height) {
        super(new GridBagLayout());
        this.columnWidths = columnWidths;
        this.lines = lineDefs;
        this.width = width;
        this.height = height;
        cellMatrix = new JPanel[linesCount][columns];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Create matrix wrappers with proportional weights
        for (int row = 0; row < linesCount; row++) {
            for (int col = 0; col < columns; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                gbc.weightx = (col < columnWidths.size()) ? columnWidths.get(col) : 1.0f;
                float weightY = 1.0f;
                if (row < lineDefs.size()) {
                    weightY = lineDefs.get(row).heightWeight;
                }
                gbc.weighty = weightY;

                JPanel wrapper = new JPanel(new BorderLayout());
                cellMatrix[row][col] = wrapper;
                this.add(wrapper, gbc);
            }
        }

        // Populate contents with per-cell styling
        for (int row = 0; row < linesCount; row++) {
            MGridBuilder.MGridLine line = lineDefs.get(row);
            int col = 0;
            for (MGridBuilder.Cell cell : line.cells) {
                if (col >= columns) break;
                applyCell(row, col, cell);
                col++;
            }
        }
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    private void applyCell(int row, int col, MGridBuilder.Cell cell) {
        JPanel wrapper = cellMatrix[row][col];

        // Prozentwerte in Pixel umrechnen
        int tpPx = Math.round(height * cell.tp);
        int lpPx = Math.round(width  * cell.lp);
        int bpPx = Math.round(height * cell.bp);
        int rpPx = Math.round(width  * cell.rp);

        int tmPx = Math.round(height * cell.tm);
        int lmPx = Math.round(width  * cell.lm);
        int bmPx = Math.round(height * cell.bm);
        int rmPx = Math.round(width  * cell.rm);

        // Apply background/padding style to wrapper
        if (cell.style != null) {
            cell.style.styleComponent(wrapper, width, height, tpPx, lpPx, bpPx, rpPx);
        } else {
            wrapper.setBorder(new EmptyBorder(tpPx, lpPx, bpPx, rpPx));
        }

        // Replace content
        wrapper.removeAll();
        wrapper.add(cell.comp, BorderLayout.CENTER);

        // Update constraints for margins and alignment
        GridBagLayout layout = (GridBagLayout) this.getLayout();
        GridBagConstraints gbc = layout.getConstraints(wrapper);
        gbc.insets = new Insets(tmPx, lmPx, bmPx, rmPx);
        gbc.anchor = cell.align;
        layout.setConstraints(wrapper, gbc);

        wrapper.revalidate();
        wrapper.repaint();
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public <T extends Component> T getContent(int line, int column) {
        JPanel wrapper = cellMatrix[line][column];
        if (wrapper.getComponentCount() > 0) {
            return (T) wrapper.getComponent(0);
        }
        return null;
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public void setContent(int line, int column, Component content) {
        JPanel wrapper = cellMatrix[line][column];
        wrapper.removeAll();
        if (content != null) {
            wrapper.add(content, BorderLayout.CENTER);
        }
        wrapper.revalidate();
        wrapper.repaint();
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public int getLineCnt() {
        return cellMatrix.length;
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public int getColumnCnt() {
        return cellMatrix[0].length;
    }
}
