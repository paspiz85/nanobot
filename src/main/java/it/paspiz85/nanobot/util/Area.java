package it.paspiz85.nanobot.util;

/**
 * Represent a display game area.
 *
 * @author paspiz85
 *
 */
public class Area {

    private final Point p1;

    private final Point p2;

    public Area(final int x1, final int y1, final int x2, final int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    private Area(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }
}
