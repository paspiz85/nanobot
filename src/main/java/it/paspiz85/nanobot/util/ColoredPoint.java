package it.paspiz85.nanobot.util;

import java.awt.Color;

/**
 * Model a point in a 2D display with a color.
 *
 * @author paspiz85
 *
 */
public class ColoredPoint extends Point {

    private final Color color;

    public ColoredPoint(final int x, final int y, final Color color) {
        super(x, y);
        if (color == null) {
            throw new NullPointerException("null color");
        }
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
