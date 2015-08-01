package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.util.Point;

/**
 * Attack from 2 side (top-right and top-left) using half units for each side.
 *
 * @author paspiz85
 *
 */
public final class Attack2Side extends Attack {

    Attack2Side() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            platform.leftClick(getButtonAttackUnit(unitIdx + 1), true);
            platform.sleepRandom(100);
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 2 + unitCount % 2);
            final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 2);
            // drop units
            for (final Point[] points : new Point[][] { topToRightPoints, topToLeftPoints }) {
                for (final Point point : points) {
                    platform.leftClick(point, false);
                    platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }

    @Override
    protected String getDescription() {
        return "Dropping units from 2 sides";
    }
}
