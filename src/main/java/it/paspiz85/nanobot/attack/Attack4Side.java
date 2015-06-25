package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
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
            OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1), 100);
            // if count is less than 4, only first side will be used.
            final int[][] topToRightPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, RIGHT_X, RIGHT_Y, unitCount / 4
                    + unitCount % 4);
            final int[][] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT_X, RIGHT_Y, BOTTOM_RIGHT_X,
                    BOTTOM_RIGHT_Y, unitCount / 4);
            final int[][] bottomToLeftPoints = pointsBetweenFromToInclusive(BOTTOM_LEFT_X, BOTTOM_LEFT_Y, LEFT_X,
                    LEFT_Y, unitCount / 4);
            final int[][] leftToTopPoints = pointsBetweenFromToInclusive(LEFT_X, LEFT_Y, TOP_X, TOP_Y, unitCount / 4);
            // drop units
            for (final int[][] points : new int[][][] { topToRightPoints, rightToBottomPoints, bottomToLeftPoints,
                    leftToTopPoints }) {
                for (final int[] point : points) {
                    OS.instance().leftClick(point[0], point[1], PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
