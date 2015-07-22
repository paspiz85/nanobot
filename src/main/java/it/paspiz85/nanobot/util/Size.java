package it.paspiz85.nanobot.util;

/**
 * Model a size in a 2D display.
 *
 * @author paspiz85
 *
 */
public final class Size implements Comparable<Size> {

    private final int x;

    private final int y;

    public Size(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(final Size o) {
        return this.x != o.x ? this.x - o.x : this.y - o.y;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Size other = (Size) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
