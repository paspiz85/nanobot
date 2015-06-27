package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;

/**
 * {@link Attack4Side} variant that attack in parallel from top and from bottom.
 *
 * @author paspiz85
 *
 */
public final class Attack4SideParallel extends Attack {

    Attack4SideParallel(final OS os) {
        super(os);
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 4 sides in parallel.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            os.leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
            os.sleepRandom(100);
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 4 + unitCount % 4);
            final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 4);
            final Point[] rightToBottomPoints = pointsBetweenFromToInclusive(RIGHT, BOTTOM_RIGHT, unitCount / 4);
            final Point[] leftToBottomPoints = pointsBetweenFromToInclusive(LEFT, BOTTOM_LEFT, unitCount / 4);
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
