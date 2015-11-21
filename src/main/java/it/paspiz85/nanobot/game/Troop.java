package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Point;

/**
 * All coordinates of troops.
 *
 * @author paspiz85
 */
public enum Troop {
    NO_UNIT("No Unit", 0, null, null), // ..............
    BARB("Barb", 1, 212, 324), // ..............
    ARCHER("Archer", 1, 331, 333), // ..............
    GIANT("Giant", 5, 432, 333), // ..............
    GOBLIN("Goblin", 1, 542, 333), // ..............
    WB("Wall Breaker", 2, 642, 333), // ..............
    BALLOON("Balloon", 5, 212, 451), // ..............
    WIZARD("Wizard", 4, 331, 451), // ..............
    HEALER("Healer", 14, 432, 451), // ..............
    DRAGON("Dragon", 20, 542, 451), // ..............
    PEKKA("Pekka", 25, 642, 451), // ..............
    MINION("Minion", 2, 212, 324), // ..............
    HOGRIDER("Hog Rider", 5, 331, 333), // ..............
    VALKYRIE("Valkyrie", 8, 432, 333), // ..............
    GOLEM("Goblin", 30, 542, 333), // ..............
    WITCH("Witch", 12, 642, 333), // ..............
    LAVAHOUND("Lava Hound", 30, 212, 451), // ..............
    BARBARIAN_KING("Barbarian King", 0, null, null), // ..............
    ARCHER_QUEEN("Archer Queen", 0, null, null); // ..............

    public static Troop fromDescription(final String description) {
        if (description == null) {
            throw new NullPointerException();
        }
        for (final Troop c : Troop.values()) {
            if (description.equals(c.getDescription())) {
                return c;
            }
        }
        throw new IllegalArgumentException(description);
    }

    private final String description;

    private final Point point;

    private final int position;

    private Troop(final String description, final int position, final Integer x, final Integer y) {
        this.description = description;
        this.position = position;
        if (x != null && y != null) {
            this.point = new Point(x, y);
        } else {
            this.point = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public int getPosition() {
        return position;
    }

    public Point getTrainButton() {
        return point;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
