package it.paspiz85.nanobot.util;

/**
 * Model a point in a 2D display.
 * 
 * @author v-ppizzuti
 *
 */
public class Point {

    private final int x;

    private final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
