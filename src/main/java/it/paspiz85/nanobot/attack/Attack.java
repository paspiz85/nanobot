package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.win32.OS;

import java.util.logging.Logger;

/**
 * Base attack strategy.
 *
 * @author v-ppizzuti
 *
 */
public abstract class Attack {

    protected static Point BOTTOM_LEFT = new Point(300, 536);

    protected static Point BOTTOM_RIGHT = new Point(537, 538);

    protected static Point LEFT = new Point(19, 307);

    protected static final int PAUSE_BETWEEN_UNIT_DROP = 61;

    protected static Point RIGHT = new Point(836, 307);

    protected static Point TOP = new Point(429, 18);

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public final void attack(final int[] loot, final int[] attackGroup) throws InterruptedException {
        logger.info("Attacking...");
        OS.instance().zoomUp();
        doDropUnits(attackGroup);
        sleepUntilLootDoesNotChange(loot);
        logger.info("No more loot.");
    }

    protected abstract void doDropUnits(int[] attackGroup) throws InterruptedException;

    // TODO
    protected final Point[] pointsBetweenFromToInclusive(final Point from, final Point to, final int count) {
        Point[] result;
        if (count <= 0) {
            result = new Point[0];
        } else if (count == 1) {
            result = new Point[] { new Point((to.x() + from.x()) / 2, (to.y() + from.y()) / 2) };
        } else {
            result = new Point[count];
            final double deltaX = (to.x() - from.x()) / (count - 1);
            final double deltaY = (to.y() - from.y()) / (count - 1);
            for (int i = 0; i < count; i++) {
                result[i] = new Point((int) (from.x() + deltaX * i), (int) (from.y() + deltaY * i));
            }
        }
        return result;
    }

    private void sleepUntilLootDoesNotChange(final int[] loot) throws InterruptedException {
        Thread.sleep(10000);
        int[] prevLoot = loot;
        int diff = Integer.MAX_VALUE;
        final int delta = 500;
        while (diff > delta) {
            Thread.sleep(15000);
            int[] currLoot;
            try {
                currLoot = Parsers.getAttackScreen().parseLoot();
            } catch (final BotBadBaseException e) {
                Thread.sleep(2000);
                // in case of 100% win/no troops left, attack screen will end
                // prematurely.
                return;
            }
            diff = 0;
            for (int i = 0; i < prevLoot.length; i++) {
                // in case of wrong parsing
                // [01.21.15 11:45:13 PM] INFO: [gold: 20174, elixir: 93476, de:
                // 218]
                // [01.21.15 11:45:28 PM] INFO: [gold: 201758, elixir: 31364,
                // de: 202]
                diff += prevLoot[i] > currLoot[i] ? prevLoot[i] - currLoot[i] : 0;
            }
            prevLoot = currLoot;
        }
    }
}
