package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

public class Attack2Side extends Attack {

    private static final Attack2Side instance = new Attack2Side();

    public static Attack2Side instance() {
        return instance;
    }

    private Attack2Side() {
    }

    @Override
    protected void doDropUnits(int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 2 sides.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            int unitCount = attackGroup[unitIdx];
            // select unit
            OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1), 100);
            int[][] topToRightPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, RIGHT_X, RIGHT_Y, unitCount / 2
                    + unitCount % 2);
            int[][] topToLeftPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, LEFT_X, LEFT_Y, unitCount / 2);
            // drop units
            for (int[][] points : new int[][][] { topToRightPoints, topToLeftPoints }) {
                for (int[] point : points) {
                    OS.instance().leftClick(point[0], point[1], PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
