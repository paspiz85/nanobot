package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.game.TroopsInfo;
import it.paspiz85.nanobot.util.Point;

/**
 * Attack from all 4 side using a quarter of units for each side.
 *
 * @author paspiz85
 *
 */
public final class Attack4Side extends Attack {

    Attack4Side() {
    }

    @Override
    protected void doDropUnits(final TroopsInfo troopsInfo) throws InterruptedException {
        int[] attackGroup = troopsInfo.getTroopsCount();
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            platform.leftClick(getButtonAttackUnit(unitIdx + 1), true);
            platform.sleepRandom(100);
            // if count is less than 4, only first side will be used.
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
            final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
            final Point[] bottomToLeftPoints = pointsBetweenFromToInclusive(BOTTOM_LEFT, LEFT, unitCount / 4);
            final Point[] leftToTopPoints = pointsBetweenFromToInclusive(LEFT, TOP, unitCount / 4);
            // drop units
            for (final Point[] points : new Point[][] { topToRightPoints, rightToBottomPoints, bottomToLeftPoints,
                    leftToTopPoints }) {
                for (final Point point : points) {
                    platform.leftClick(point, false);
                    platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }

    @Override
    protected String getDescription() {
        return "Dropping units from 4 sides";
    }
}
