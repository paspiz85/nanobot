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
    ATTACK_GROUP(new Point(24, 554), new Point(836, 653)), BARRACKS_BUTTONS(new Point(188, 581), new Point(679, 679)), ENEMY_BASE(
            new Point(31, 0), new Point(831, 510)), ENEMY_LOOT(new Point(17, 68), new Point(138, 240)), FULLSCREEN(
            new Point(0, 0), new Point(BS_RES_X - 1, BS_RES_Y - 1));

    private final Point p1;

    private final Point p2;

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
