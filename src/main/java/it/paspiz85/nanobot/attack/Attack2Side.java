package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

public final class Attack2Side extends Attack {

    private static Attack2Side instance;

    public static Attack2Side instance() {
        if (instance == null) {
            instance = new Attack2Side();
        }
        return instance;
    }

    private Attack2Side() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 2 sides.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1), 100);
            final int[][] topToRightPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, RIGHT_X, RIGHT_Y, unitCount / 2
                    + unitCount % 2);
            final int[][] topToLeftPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, LEFT_X, LEFT_Y, unitCount / 2);
            // drop units
            for (final int[][] points : new int[][][] { topToRightPoints, topToLeftPoints }) {
                for (final int[] point : points) {
                    OS.instance().leftClick(point[0], point[1], PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
