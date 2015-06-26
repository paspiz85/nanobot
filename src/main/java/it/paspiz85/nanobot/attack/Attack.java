package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.parsing.Loot;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.win32.OS;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Base attack strategy.
 *
 * @author paspiz85
 *
 */
public abstract class Attack {

    private static ManualAttack manualStrategy;

    private static Attack[] availableStrategies;

    protected static final Point BOTTOM_LEFT = new Point(300, 536);

    protected static final Point BOTTOM_RIGHT = new Point(537, 538);

    protected static final Point LEFT = new Point(19, 307);

    protected static final int PAUSE_BETWEEN_UNIT_DROP = 61;

    protected static final Point RIGHT = new Point(836, 307);

    protected static final Point TOP = new Point(429, 18);

    public static Attack[] getAvailableStrategies() {
        if (availableStrategies == null) {
            final OS os = OS.instance();
            final List<Attack> list = new ArrayList<>();
            list.add(manualStrategy());
            list.add(new Attack2Side(os));
            list.add(new Attack4Side(os));
            list.add(new Attack4SideParallel(os));
            list.add(new Attack4SideParallelHalf2Wave(os));
            list.add(new Attack4SideParallelFull2Wave(os));
            availableStrategies = list.toArray(new Attack[0]);
        }
        return availableStrategies;
    }

    public static Attack manualStrategy() {
        if (manualStrategy == null) {
            manualStrategy = new ManualAttack(OS.instance());
        }
        return manualStrategy;
    }

    protected final OS os;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected Attack(final OS os) {
        this.os = os;
    }

    public final void attack(final Loot loot, final int[] attackGroup) throws InterruptedException {
        logger.info("Attacking...");
        os.zoomUp();
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

    private void sleepUntilLootDoesNotChange(final Loot loot) throws InterruptedException {
        Thread.sleep(10000);
        Loot prevLoot = loot;
        int diff = Integer.MAX_VALUE;
        final int delta = 500;
        while (diff > delta) {
            Thread.sleep(15000);
            Loot currLoot;
            try {
                currLoot = Parsers.getAttackScreen().parseLoot();
            } catch (final BotBadBaseException e) {
                Thread.sleep(2000);
                // in case of 100% win/no troops left, attack screen will end
                // prematurely.
                return;
            }
            diff = 0;
            diff += prevLoot.getGold() > currLoot.getGold() ? prevLoot.getGold() - currLoot.getGold() : 0;
            diff += prevLoot.getElixir() > currLoot.getElixir() ? prevLoot.getElixir() - currLoot.getElixir() : 0;
            diff += prevLoot.getDarkElixir() > currLoot.getDarkElixir() ? prevLoot.getDarkElixir()
                    - currLoot.getDarkElixir() : 0;
                    prevLoot = currLoot;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
