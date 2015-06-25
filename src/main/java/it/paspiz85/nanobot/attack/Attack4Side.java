package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.win32.OS;

public final class Attack4Side extends Attack {

    private static Attack4Side instance;

    public static Attack4Side instance() {
        if (instance == null) {
            instance = new Attack4Side();
        }
        return instance;
    }

    private Attack4Side() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 4 sides.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
            OS.instance().sleepRandom(100);
            // if count is less than 4, only first side will be used.
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
            final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
            final Point[] bottomToLeftPoints = pointsBetweenFromToInclusive(BOTTOM_LEFT, LEFT, unitCount / 4);
            final Point[] leftToTopPoints = pointsBetweenFromToInclusive(LEFT, TOP, unitCount / 4);
            // drop units
            for (final Point[] points : new Point[][] { topToRightPoints, rightToBottomPoints, bottomToLeftPoints,
                    leftToTopPoints }) {
                for (final Point point : points) {
                    OS.instance().leftClick(point, false);
                    OS.instance().sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
