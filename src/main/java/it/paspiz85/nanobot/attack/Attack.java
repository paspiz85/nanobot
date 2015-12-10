package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.game.AttackScreen;
import it.paspiz85.nanobot.game.EnemyInfo;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.game.TroopsInfo;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Base attack strategy.
 *
 * @author paspiz85
 *
 */
public abstract class Attack {

    private static AttackScreen attackScreenParser = Screen.getInstance(AttackScreen.class);

    private static List<Attack> attacks;

    protected static final Point BOTTOM_LEFT = new Point(300, 536);

    protected static final Point BOTTOM_RIGHT = new Point(537, 538);

    protected static final Point LEFT = new Point(19, 307);

    private static ManualAttack manualAttack;

    protected static final int PAUSE_BETWEEN_UNIT_DROP = 61;

    protected static final Point RIGHT = new Point(836, 307);

    protected static final Point TOP = new Point(429, 18);

    private static int diffLoot(final Integer prevLoot, final Integer currLoot) {
        return zeroIfNull(prevLoot) > zeroIfNull(currLoot) ? zeroIfNull(prevLoot) - zeroIfNull(currLoot) : 0;
    }

    private static List<Attack> getAttacks() {
        if (attacks == null) {
            final List<Attack> list = new ArrayList<>();
            list.add(manualAttack());
            list.add(new Attack2Side());
            list.add(new Attack4Side());
            list.add(new Attack4SideParallel());
            list.add(new Attack4SideParallelHalf2Wave());
            list.add(new Attack4SideParallelFull2Wave());
            attacks = list;
        }
        return attacks;
    }

    public static List<String> getAvailableStrategies() {
        return getAttacks().stream().map(Attack::getName).collect(Collectors.toList());
    }

    protected static Point getButtonAttackUnit(final int x) {
        return attackScreenParser.getButtonAttackUnit(x);
    }

    public static final Attack getByName(final String name) {
        for (final Attack attack : Attack.getAttacks()) {
            if (attack.getName().equals(name)) {
                return attack;
            }
        }
        throw new IllegalArgumentException(name);
    }

    private static Attack manualAttack() {
        if (manualAttack == null) {
            manualAttack = new ManualAttack();
        }
        return manualAttack;
    }

    public static String manualStrategy() {
        return manualAttack().getName();
    }

    public static String noStrategy() {
        return "NoAttack";
    }

    private static int zeroIfNull(final Integer n) {
        return n == null ? 0 : n;
    }

    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected final Platform platform = Platform.instance();

    Attack() {
    }

    public final void attack(final EnemyInfo loot, final TroopsInfo troopsInfo) throws InterruptedException {
        platform.zoomUp();
        logger.log(Level.CONFIG, getDescription());
        doDropUnits(troopsInfo);
        sleepUntilLootDoesNotChange(loot);
        logger.log(Level.INFO, "No more loot");
    }

    protected abstract void doDropUnits(TroopsInfo troopsInfo) throws InterruptedException;

    protected abstract String getDescription();

    public final String getName() {
        return getClass().getSimpleName();
    }

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

    private void sleepUntilLootDoesNotChange(final EnemyInfo loot) throws InterruptedException {
        Thread.sleep(10000);
        EnemyInfo prevLoot = loot;
        int diff = Integer.MAX_VALUE;
        final int delta = 5;
        while (diff > delta) {
            Thread.sleep(15000);
            EnemyInfo currLoot;
            try {
                currLoot = Screen.getInstance(AttackScreen.class).parseEnemyInfo();
            } catch (final BotBadBaseException e) {
                Thread.sleep(2000);
                // in case of 100% win/no troops left, attack screen will end
                // prematurely.
                return;
            }
            diff = 0;
            diff += diffLoot(prevLoot.getGold(), currLoot.getGold());
            diff += diffLoot(prevLoot.getElixir(), currLoot.getElixir());
            diff += diffLoot(prevLoot.getDarkElixir(), currLoot.getDarkElixir());
            prevLoot = currLoot;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
