package com.marcoscherzer.msimplegoauthhelper.swinggui.mgridbuilder;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Copyright Marco Scherzer, All rights reserved
 * Fast implemented incomplete (core)
 * Swing-Version of MGridBuilder and Android MGridBuilder.
 * Copyright Marco Scherzer. All rights reserved.
 */
public class MBackgroundBuilder {

    private Color fillColor = null;
    private Color borderColor = null;
    private int borderThickness = 0;
    private int cornerRadius = 0;
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
    public MBackgroundBuilder setBorder(Color color, int thickness) {
        this.borderColor = color;
        this.borderThickness = thickness;
        return this;
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public MBackgroundBuilder setCornerRadius(int radius) {
        this.cornerRadius = radius;
        return this;
    }
    /**
     * Copyright Marco Scherzer, All rights reserved
     * Fast implemented incomplete (core)
     * Swing-Version of my javafx MGridBuilder and my Android MGridBuilder.
     * Copyright Marco Scherzer. All rights reserved.
     */
    public void styleComponent(JComponent comp, int tp, int lp, int bp, int rp) {
        Border pad = new EmptyBorder(tp, lp, bp, rp);
        Border border = null;

        if (borderColor != null && borderThickness > 0) {
            if (cornerRadius > 0) {
                border = new LineBorder(borderColor, borderThickness, true);
            } else {
                border = new LineBorder(borderColor, borderThickness);
            }
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
