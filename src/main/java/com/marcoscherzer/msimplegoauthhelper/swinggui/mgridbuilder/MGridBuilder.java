package com.marcoscherzer.msimplegoauthhelper.swinggui.mgridbuilder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright Marco Scherzer, All rights reserved
 * Fast implemented incomplete (core)
 * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
 * Copyright Marco Scherzer. All rights reserved.
 */
public final class MGridBuilder {

    private final List<Float> columnWidths = new ArrayList<>();
    private final List<MGridLine> lines = new ArrayList<>();
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public static final class MBackgroundBuilder {
        private Color fillColor = null;
        private Color borderColor = null;
        private float borderThicknessPercent = 0f; // Prozent der Höhe
        private float cornerRadiusPercent = 0f;    // Prozent der Höhe
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MBackgroundBuilder setFillColor(Color color) {
            this.fillColor = color;
            return this;
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MBackgroundBuilder setBorder(Color color, float thicknessPercent) {
            this.borderColor = color;
            this.borderThicknessPercent = thicknessPercent;
            return this;
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MBackgroundBuilder setCornerRadius(float radiusPercent) {
            this.cornerRadiusPercent = radiusPercent;
            return this;
        }

        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public void styleComponent(JComponent comp, int width, int height,
                                   int tpPx, int lpPx, int bpPx, int rpPx) {
            Border pad = new EmptyBorder(tpPx, lpPx, bpPx, rpPx);
            Border border = null;

            int borderPx = Math.round(height * borderThicknessPercent);
            int radiusPx = Math.round(height * cornerRadiusPercent);

            if (borderColor != null && borderPx > 0) {
                border = new LineBorder(borderColor, borderPx, radiusPx > 0);
            }

            if (border != null) {
                comp.setBorder(new CompoundBorder(border, pad));
            } else {
                comp.setBorder(pad);
            }

            if (fillColor != null) {
                comp.setBackground(fillColor);
                comp.setOpaque(true);
            }
        }
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    static final class Cell { // später private
        final JComponent comp;
        final float tp, lp, bp, rp;   // paddings (Prozent)
        final float tm, lm, bm, rm;   // margins (Prozent)
        final MBackgroundBuilder style;
        final int align;
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        Cell(JComponent comp,
             float tp, float lp, float bp, float rp,
             float tm, float lm, float bm, float rm,
             MBackgroundBuilder style,
             int align) {
            this.comp = comp;
            this.tp = tp; this.lp = lp; this.bp = bp; this.rp = rp;
            this.tm = tm; this.lm = lm; this.bm = bm; this.rm = rm;
            this.style = style;
            this.align = align;
        }
    }

    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public static final class MGridLine {
        final float heightWeight;
        final List<Cell> cells = new ArrayList<>();
        private int currentCol = 0;
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine(float heightWeight) {
            this.heightWeight = heightWeight;
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        private MGridLine attach(
                final JComponent comp,
                final float tp, final float lp, final float bp, final float rp,
                final float tm, final float lm, final float bm, final float rm,
                final MBackgroundBuilder style,
                final int align,
                final int totalColumns
        ) {
            if (currentCol >= totalColumns) {
                throw new IllegalStateException("Zu wenige Spalten: currentCol=" + currentCol + " >= totalColumns=" + totalColumns);
            }
            cells.add(new Cell(comp, tp, lp, bp, rp, tm, lm, bm, rm, style, align));
            currentCol++;
            return this;
        }

        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine add(JComponent comp, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, 0f,0f,0f,0f, null, GridBagConstraints.CENTER, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine add(JComponent comp, MBackgroundBuilder style, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, 0f,0f,0f,0f, style, GridBagConstraints.CENTER, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMargins(JComponent comp, float allMarginPercent, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f,
                    allMarginPercent,allMarginPercent,allMarginPercent,allMarginPercent,
                    null, GridBagConstraints.CENTER, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMargins(JComponent comp, float tm, float lm, float bm, float rm, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, tm,lm,bm,rm, null, GridBagConstraints.CENTER, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithPadding(JComponent comp, float tp, float lp, float bp, float rp, MBackgroundBuilder style, int totalColumns) {
            return attach(comp, tp,lp,bp,rp, 0f,0f,0f,0f, style, GridBagConstraints.CENTER, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMarginsAndPaddings(JComponent comp, float allPaddingPercent, float allMarginPercent, MBackgroundBuilder style, int totalColumns) {
            return attach(comp,
                    allPaddingPercent,allPaddingPercent,allPaddingPercent,allPaddingPercent,
                    allMarginPercent,allMarginPercent,allMarginPercent,allMarginPercent,
                    style, GridBagConstraints.CENTER, totalColumns);
        }

        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine add(JComponent comp, int align, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, 0f,0f,0f,0f, null, align, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine add(JComponent comp, MBackgroundBuilder style, int align, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, 0f,0f,0f,0f, style, align, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMargins(JComponent comp, float allMarginPercent, int align, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f,
                    allMarginPercent,allMarginPercent,allMarginPercent,allMarginPercent,
                    null, align, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMargins(JComponent comp, float tm, float lm, float bm, float rm, int align, int totalColumns) {
            return attach(comp, 0f,0f,0f,0f, tm,lm,bm,rm, null, align, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithPadding(JComponent comp, float tp, float lp, float bp, float rp, MBackgroundBuilder style, int align, int totalColumns) {
            return attach(comp, tp,lp,bp,rp, 0f,0f,0f,0f, style, align, totalColumns);
        }
        /**
         * Copyright Marco Scherzer, All rights reserved
         * Fast implemented incomplete (core)
         * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
         * Copyright Marco Scherzer. All rights reserved.
         */
        public MGridLine addWithMarginsAndPaddings(JComponent comp, float allPaddingPercent, float allMarginPercent, MBackgroundBuilder style, int align, int totalColumns) {
            return attach(comp,
                    allPaddingPercent,allPaddingPercent,allPaddingPercent,allPaddingPercent,
                    allMarginPercent,allMarginPercent,allMarginPercent,allMarginPercent,
                    style, align, totalColumns);
        }
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public MGridBuilder() {}
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public void setColumnWidths(float... widths) {
        columnWidths.clear();
        for (float w : widths) {
            columnWidths.add(w);
        }
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public MGridLine addLine(float heightWeight) {
        MGridLine line = new MGridLine(heightWeight);
        lines.add(line);
        return line;
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public MGrid create(int width, int height) {
        int lineCnt = lines.size();
        int colCnt = columnWidths.size();
        return new MGrid(lineCnt, colCnt, columnWidths, lines, width, height);
    }
}




