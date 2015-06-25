package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.win32.OS;

import java.util.logging.Logger;

/**
 * Base attack strategy.
 *
 * @author v-ppizzuti
 *
 */
public abstract class Attack {

    protected static int BOTTOM_LEFT_X = 300;

    protected static int BOTTOM_LEFT_Y = 536;

    protected static int BOTTOM_RIGHT_X = 537;

    protected static int BOTTOM_RIGHT_Y = 538;

    protected static int LEFT_X = 19;

    protected static int LEFT_Y = 307;

    protected static final int PAUSE_BETWEEN_UNIT_DROP = 61;

    protected static int RIGHT_X = 836;

    protected static int RIGHT_Y = 307;

    protected static int TOP_X = 429;

    protected static int TOP_Y = 18;

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
    protected final int[][] pointsBetweenFromToInclusive(final int fromX, final int fromY, final int toX,
            final int toY, final int count) {
        int[][] result;
        if (count <= 0) {
            result = new int[0][0];
        } else if (count == 1) {
            result =  new int[][] { { (toX + fromX) / 2, (toY + fromY) / 2 } };
        } else {
            result = new int[count][2];
            final double deltaX = (toX - fromX) / (count - 1);
            final double deltaY = (toY - fromY) / (count - 1);
            for (int i = 0; i < count; i++) {
                result[i][0] = (int) (fromX + deltaX * i);
                result[i][1] = (int) (fromY + deltaY * i);
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
