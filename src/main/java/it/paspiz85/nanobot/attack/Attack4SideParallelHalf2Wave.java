package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

public final class Attack4SideParallelHalf2Wave extends Attack {

    private static Attack4SideParallelHalf2Wave instance;

    public static Attack4SideParallelHalf2Wave instance() {
        if (instance == null) {
            instance = new Attack4SideParallelHalf2Wave();
        }
        return instance;
    }

    private Attack4SideParallelHalf2Wave() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 4 sides in parallel in 2 half waves.");
        for (int wave = 0; wave < 2; wave++) {
            for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
                int unitCount = attackGroup[unitIdx];
                unitCount = unitCount / 2 + wave * (unitCount % 2);
                // select unit
                OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1), 100);
                final int[][] topToRightPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, RIGHT_X, RIGHT_Y, unitCount
                        / 4 + unitCount % 4);
                final int[][] topToLeftPoints = pointsBetweenFromToInclusive(TOP_X, TOP_Y, LEFT_X, LEFT_Y,
                        unitCount / 4);
                // drop units
                // top to mid from both sides in parallel
                for (int i = 0; i < topToRightPoints.length; i++) {
                    final int[] topRightPoint = topToRightPoints[i];
                    OS.instance().leftClick(topRightPoint[0], topRightPoint[1], PAUSE_BETWEEN_UNIT_DROP);
                    if (i < topToLeftPoints.length) {
                        final int[] topLeftPoint = topToLeftPoints[i];
                        OS.instance().leftClick(topLeftPoint[0], topLeftPoint[1], PAUSE_BETWEEN_UNIT_DROP);
                    }
                }
            }
        }
        for (int wave = 0; wave < 2; wave++) {
            for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
                int unitCount = attackGroup[unitIdx];
                unitCount = unitCount / 2 + wave * (unitCount % 2);
                // select unit
                OS.instance().leftClick(Clickable.getButtonAttackUnit(unitIdx + 1), 100);
                final int[][] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT_X, RIGHT_Y, BOTTOM_RIGHT_X,
                        BOTTOM_RIGHT_Y, unitCount / 4);
                final int[][] leftToBottomPoints = pointsBetweenFromToInclusive(LEFT_X, LEFT_Y, BOTTOM_LEFT_X,
                        BOTTOM_LEFT_Y, unitCount / 4);
                // mid to bottom from both sides in parallel
                for (int i = 0; i < rightToBottomPoints.length; i++) {
                    final int[] rightToBottomPoint = rightToBottomPoints[i];
                    OS.instance().leftClick(rightToBottomPoint[0], rightToBottomPoint[1], PAUSE_BETWEEN_UNIT_DROP);
                    final int[] leftToBottomPoint = leftToBottomPoints[i];
                    OS.instance().leftClick(leftToBottomPoint[0], leftToBottomPoint[1], PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
