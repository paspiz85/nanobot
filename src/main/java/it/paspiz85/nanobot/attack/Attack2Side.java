package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.win32.OS;

public final class Attack2Side extends Attack {

    Attack2Side(final OS os) {
        super(os);
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
        logger.info("Dropping units from 2 sides.");
        for (int unitIdx = 0; unitIdx < attackGroup.length; unitIdx++) {
            final int unitCount = attackGroup[unitIdx];
            // select unit
            os.leftClick(Clickable.getButtonAttackUnit(unitIdx + 1).getPoint(), true);
            os.sleepRandom(100);
            final Point[] topToRightPoints = pointsBetweenFromToInclusive(TOP, RIGHT, unitCount / 2 + unitCount % 2);
            final Point[] topToLeftPoints = pointsBetweenFromToInclusive(TOP, LEFT, unitCount / 2);
            // drop units
            for (final Point[] points : new Point[][] { topToRightPoints, topToLeftPoints }) {
                for (final Point point : points) {
                    os.leftClick(point, false);
                    os.sleepRandom(PAUSE_BETWEEN_UNIT_DROP);
                }
            }
        }
    }
}
