package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.game.TroopsInfo;
import it.paspiz85.nanobot.util.Point;

/**
 * {@link Attack4Side} variant that attack in parallel from top and from bottom.
 *
 * @author paspiz85
 *
 */
public final class Attack4SideParallel extends Attack {

    Attack4SideParallel() {
    }

    @Override
    protected void doDropUnits(final TroopsInfo troopsInfo) throws InterruptedException {
        final int[] attackGroup = troopsInfo.getTroopsCount();
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            platform.leftClick(getButtonAttackUnit(unitIdx + 1), true);
            platform.sleepRandom(100);
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
            final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 4);
            final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
            final Point[] leftToBottomPoints = pointsBetweenFromToInclusive(LEFT, BOTTOM_LEFT, unitCount / 4);
            // drop units
            // top to mid from both sides in parallel
            for (int i = 0; i < topToRightPoints.length; i++) {
                final Point topRightPoint = topToRightPoints[i];
                platform.leftClick(topRightPoint, false);
                platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                if (i < topToLeftPoints.length) {
                    final Point topLeftPoint = topToLeftPoints[i];
                    platform.leftClick(topLeftPoint, false);
                    platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
            // mid to bottom from both sides in parallel
            for (int i = 0; i < rightToBottomPoints.length; i++) {
                final Point rightToBottomPoint = rightToBottomPoints[i];
                platform.leftClick(rightToBottomPoint, false);
                platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                final Point leftToBottomPoint = leftToBottomPoints[i];
                platform.leftClick(leftToBottomPoint, false);
                platform.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
            }
        }
    }

    @Override
    protected String getDescription() {
        return "Dropping units from 4 sides in parallel";
    }
}
