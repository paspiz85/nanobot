package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;

/**
 * Attack from 4 side parallel (before top and then bottom) in 2 wave of units.
 *
 * @author paspiz85
 *
 */
public final class Attack4SideParallelHalf2Wave extends Attack {

    Attack4SideParallelHalf2Wave() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 4 sides in parallel in 2 half waves.");
        for (int wave = 0; wave < 2; wave++) {
            for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
                int unitCount = attackGroup[unitIdx];
                unitCount = unitCount / 2 + wave * (unitCount % 2);
                // select unit
                os.leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
                os.sleepRandom(100);
                final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
                final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 4);
                // drop units
                // top to mid from both sides in parallel
                for (int i = 0; i < topToRightPoints.length; i++) {
                    final Point topRightPoint = topToRightPoints[i];
                    os.leftClick(topRightPoint, false);
                    os.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                    if (i < topToLeftPoints.length) {
                        final Point topLeftPoint = topToLeftPoints[i];
                        os.leftClick(topLeftPoint, false);
                        os.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                    }
                }
            }
        }
        for (int wave = 0; wave < 2; wave++) {
            for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
                int unitCount = attackGroup[unitIdx];
                unitCount = unitCount / 2 + wave * (unitCount % 2);
                // select unit
                os.leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
                os.sleepRandom(100);
                final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
                final Point[] leftToBottomPoints = pointsBetweenFromToInclusive(LEFT, BOTTOM_LEFT, unitCount / 4);
                // mid to bottom from both sides in parallel
                for (int i = 0; i < rightToBottomPoints.length; i++) {
                    final Point rightToBottomPoint = rightToBottomPoints[i];
                    os.leftClick(rightToBottomPoint, false);
                    os.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                    final Point leftToBottomPoint = leftToBottomPoints[i];
                    os.leftClick(leftToBottomPoint, false);
                    os.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
