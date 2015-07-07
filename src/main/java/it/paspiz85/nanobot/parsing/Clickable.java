package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Point;

import java.awt.Color;

/**
 * All coordinates (are client coordinates) of clickable points.
 *
 * @author paspiz85
 */
public enum Clickable {
    BUTTON_ATTACK_UNIT_1(72, 600, null), BUTTON_ATTACK_UNIT_2(145, 600, null), BUTTON_ATTACK_UNIT_3(217, 600, null), BUTTON_ATTACK_UNIT_4(
            288, 600, null), BUTTON_ATTACK_UNIT_5(361, 600, null), BUTTON_ATTACK_UNIT_6(435, 600, null), BUTTON_ATTACK_UNIT_7(
                    505, 600, null), BUTTON_RAX_ARCHER(331, 333, null, "Archer"), BUTTON_RAX_BALLOON(212, 451, null, "Balloon"), BUTTON_RAX_BARB(
                            212, 324, null, "Barb"), BUTTON_RAX_DRAGON(542, 451, null, "Dragon"), BUTTON_RAX_GIANT(432, 333, null,
                                    "Giant"), BUTTON_RAX_GOBLIN(542, 333, null, "Goblin"), BUTTON_RAX_HEALER(432, 451, null, "Healer"), BUTTON_RAX_HOGRIDER(
                                            331, 333, null, "Hog Rider"), BUTTON_RAX_MINION(212, 324, null, "Minion"), BUTTON_RAX_NO_UNIT(null, null,
                                                    null, "No Unit"), BUTTON_RAX_PEKKA(642, 451, null, "Pekka"), BUTTON_RAX_WB(642, 333, null, "Wall Breaker"), BUTTON_RAX_WIZARD(
                                                            331, 451, null, "Wizard");

    public static Clickable fromDescription(final String description) {
        if (description == null) {
            throw new NullPointerException();
        }
        for (final Clickable c : Clickable.values()) {
            if (description.equals(c.getDescription())) {
                return c;
            }
        }
        throw new IllegalArgumentException(description);
    }

    public static Clickable getButtonAttackUnit(final int x) {
        Clickable result;
        switch (x) {
        case 1:
            result = BUTTON_ATTACK_UNIT_1;
            break;
        case 2:
            result = BUTTON_ATTACK_UNIT_2;
            break;
        case 3:
            result = BUTTON_ATTACK_UNIT_3;
            break;
        case 4:
            result = BUTTON_ATTACK_UNIT_4;
            break;
        case 5:
            result = BUTTON_ATTACK_UNIT_5;
            break;
        case 6:
            result = BUTTON_ATTACK_UNIT_6;
            break;
        case 7:
            result = BUTTON_ATTACK_UNIT_7;
            break;
        default:
            throw new IllegalArgumentException(x + "");
        }
        return result;
    }

    private Color color;

    private String description;

    private Point point;

    private Clickable(final Integer x, final Integer y, final Color color) {
        if (x != null && y != null) {
            this.point = new Point(x, y);
        }
        this.color = color;
    }

    private Clickable(final Integer x, final Integer y, final Color color, final String description) {
        this(x, y, color);
        this.description = description;
    }

    public Color getColor() {
        return color;
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
