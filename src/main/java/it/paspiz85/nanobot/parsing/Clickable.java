package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Point;

import java.awt.Color;

/**
 * All coordinates (are client coordinates) of clickable points.
 *
 * @author v-ppizzuti
 */
public enum Clickable {
    BUTTON_ATTACK(63, 599, new Color(0xF0E8D8)), // make sure to pick a solid
    // spot, some parts are
    // transparent
    BUTTON_ATTACK_UNIT_1(72, 600, null), BUTTON_ATTACK_UNIT_2(145, 600, null), BUTTON_ATTACK_UNIT_3(217, 600, null), BUTTON_ATTACK_UNIT_4(
            288, 600, null), BUTTON_ATTACK_UNIT_5(361, 600, null), BUTTON_ATTACK_UNIT_6(435, 600, null), BUTTON_ATTACK_UNIT_7(
            505, 600, null), BUTTON_END_BATTLE(59, 517, null), BUTTON_END_BATTLE_QUESTION_OKAY(509, 394, null), BUTTON_END_BATTLE_RETURN_HOME(
            437, 532, new Color(0xCEE870)), BUTTON_FIND_A_MATCH(148, 529, new Color(0xD84B00)), BUTTON_NEXT(816, 484,
            new Color(0xECAC28)), BUTTON_RAX_ARCHER(331, 333, null, "Archer"), BUTTON_RAX_BALLOON(212, 451, null,
            "Balloon"), BUTTON_RAX_BARB(212, 324, null, "Barb"), BUTTON_RAX_CLOSE(729, 145, new Color(0xF8FCFF)), BUTTON_RAX_DRAGON(
            542, 451, null, "Dragon"), BUTTON_RAX_FULL(156, 507, new Color(0xD04048)), BUTTON_RAX_GIANT(432, 333, null,
            "Giant"), BUTTON_RAX_GOBLIN(542, 333, null, "Goblin"), BUTTON_RAX_HEALER(432, 451, null, "Healer"), BUTTON_RAX_NEXT(
            767, 361, new Color(0xF8B046)),
    // trainables
    BUTTON_RAX_NO_UNIT(null, null, null, "No Unit"), BUTTON_RAX_PEKKA(642, 451, null, "Pekka"), BUTTON_RAX_WB(642, 333,
            null, "Wall Breaker"), BUTTON_RAX_WIZARD(331, 451, null, "Wizard"), BUTTON_SHIELD_DISABLE(512, 397, null), BUTTON_WAS_ATTACKED_HEADLINE(
            437, 158, new Color(0x585450)), BUTTON_WAS_ATTACKED_OKAY(432, 507, new Color(0x5CAC10)), // 160,250
    // to
    // 700,420
    UNIT_BLUESTACKS_DC(699, 343, new Color(0x282828)), UNIT_RECONNECT(435, 400, null);

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
        this.point = new Point(x, y);
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
}
