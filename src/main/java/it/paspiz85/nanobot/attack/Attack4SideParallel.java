package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.win32.OS;

public final class Attack4SideParallel extends Attack {

    private static Attack4SideParallel instance;

    public static Attack4SideParallel instance() {
        if (instance == null) {
            instance = new Attack4SideParallel();
        }
        return instance;
    }

    private Attack4SideParallel() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 4 sides in parallel.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
            OS.instance().sleepRandom(100);
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
            final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 4);
            final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
            final Point[] leftToBottomPoints = pointsBetweenFromToInclusive(LEFT, BOTTOM_LEFT, unitCount / 4);
            // drop units
            // top to mid from both sides in parallel
            for (int i = 0; i < topToRightPoints.length; i++) {
                final Point topRightPoint = topToRightPoints[i];
                OS.instance().leftClick(topRightPoint, false);
                OS.instance().sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                if (i < topToLeftPoints.length) {
                    final Point topLeftPoint = topToLeftPoints[i];
                    OS.instance().leftClick(topLeftPoint, false);
                    OS.instance().sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
            // mid to bottom from both sides in parallel
            for (int i = 0; i < rightToBottomPoints.length; i++) {
                final Point rightToBottomPoint = rightToBottomPoints[i];
                OS.instance().leftClick(rightToBottomPoint, false);
                OS.instance().sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                final Point leftToBottomPoint = leftToBottomPoints[i];
                OS.instance().leftClick(leftToBottomPoint, false);
                OS.instance().sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
            }
        }
    }
}
