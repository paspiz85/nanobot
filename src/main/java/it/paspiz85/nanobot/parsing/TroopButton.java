package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Point;

/**
 * All coordinates of troops.
 *
 * @author paspiz85
 */
public enum TroopButton {
    ARCHER(331, 333, "Archer"), BALLOON(212, 451, "Balloon"), BARB(212, 324, "Barb"), DRAGON(542, 451, "Dragon"), GIANT(
            432, 333, "Giant"), GOBLIN(542, 333, "Goblin"), HEALER(432, 451, "Healer"), HOGRIDER(331, 333, "Hog Rider"), MINION(
                    212, 324, "Minion"), NO_UNIT(null, null, "No Unit"), PEKKA(642, 451, "Pekka"), WB(642, 333, "Wall Breaker"), WIZARD(
            331, 451, "Wizard");

    public static TroopButton fromDescription(final String description) {
        if (description == null) {
            throw new NullPointerException();
        }
        for (final TroopButton c : TroopButton.values()) {
            if (description.equals(c.getDescription())) {
                return c;
            }
        }
        throw new IllegalArgumentException(description);
    }

    private final String description;

    private final Point point;

    private TroopButton(final Integer x, final Integer y, final String description) {
        if (x != null && y != null) {
            this.point = new Point(x, y);
        } else {
            this.point = null;
        }
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
