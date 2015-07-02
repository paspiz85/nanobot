package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;

/**
 * Represent a display game area.
 *
 * @author paspiz85
 *
 */
public enum Area implements Constants {
    ATTACK_GROUP(24, 554, 836, 653), BARRACKS_BUTTONS(188, 581, 679, 679), ENEMY_BASE(31, 0, 831, 510), ENEMY_LOOT(17,
            68, 138, 240), FULLSCREEN(0, 0, BS_RES_X - 1, BS_RES_Y - 1), ATTACK_BUTTON(25, 634, 100, 652), CAMPS_FULL(
                    405, 150, 499, 167), NEXT_BUTTON(692, 488, 739, 547);

    private final Point p1;

    private final Point p2;

    private Area(final int x1, final int y1, final int x2, final int y2) {
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x2, y2);
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }
}
