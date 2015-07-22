package it.paspiz85.nanobot.util;

/**
 * Represent a display game area.
 *
 * @author paspiz85
 *
 */
public final class Area {

    public static Area byEdges(final int x1, final int y1, final int x2, final int y2) {
        return new Area(new Point(x1, y1), new Point(x2, y2));
    }

    public static Area byEdges(final Point edge1, final Point edge2) {
        return new Area(edge1, edge2);
    }

    public static Area bySize(final Point edge1, final Size size) {
        return new Area(edge1, new Point(edge1.x() + size.x() - 1, edge1.y() + size.y() - 1));
    }

    private final Point edge1;

    private final Point edge2;

    private final Size size;

    private Area(final Point edge1, final Point edge2) {
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.size = new Size(edge2.x() - edge1.x() + 1, edge2.y() - edge1.y() + 1);
    }

    public Point getEdge1() {
        return edge1;
    }

    public Point getEdge2() {
        return edge2;
    }

    public Size getSize() {
        return size;
    }
}
