package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.AttackScreen;
import it.paspiz85.nanobot.game.EnemyInfo;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.game.TroopsInfo;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * Attack state is when bot find and attack an opponent.
 *
 * @author paspiz85
 *
 */
public final class StateAttack extends State<AttackScreen> {

    public static StateAttack instance() {
        return Utils.singleton(StateAttack.class, () -> new StateAttack());
    }

    private EnemyInfo prevLoot;

    private StateAttack() {
        super(Screen.getInstance(AttackScreen.class));
    }

    public boolean doConditionsMatch(final EnemyInfo loot) {
        int gold = loot.getGold() == null ? 0 : loot.getGold();
        int elixir = loot.getElixir() == null ? 0 : loot.getElixir();
        int de = loot.getDarkElixir() == null ? 0 : loot.getDarkElixir();
        // if threshold is 0 or not set, do not match based on them
        final int goldThreshold = Settings.instance().getGoldThreshold();
        final int elixirThreshold = Settings.instance().getElixirThreshold();
        final int darkElixirThreshold = Settings.instance().getDarkElixirThreshold();
        boolean result = false;
        if (Settings.instance().isMatchAllConditions()) {
            gold = goldThreshold == 0 ? Integer.MAX_VALUE : gold;
            elixir = elixirThreshold == 0 ? Integer.MAX_VALUE : elixir;
            de = darkElixirThreshold == 0 ? Integer.MAX_VALUE : de;
            result = gold >= goldThreshold && elixir >= elixirThreshold && de >= darkElixirThreshold;
        } else {
            gold = goldThreshold == 0 ? Integer.MIN_VALUE : gold;
            elixir = elixirThreshold == 0 ? Integer.MIN_VALUE : elixir;
            de = darkElixirThreshold == 0 ? Integer.MIN_VALUE : de;
            result = gold >= goldThreshold || elixir >= elixirThreshold || de >= darkElixirThreshold;
        }
        return result;
    }

    @Override
    public void handle(final Context context) throws InterruptedException, BotException {
        State<?> nextState = StateIdle.instance();
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException(getClass().getSimpleName() + " is interrupted");
            }
            final long id = System.currentTimeMillis();
            logger.log(Level.INFO, "Found opponent " + id);
            // to avoid fog
            platform.sleepRandom(500);
            EnemyInfo enemyInfo;
            boolean doAttack = false;
            try {
                enemyInfo = getScreen().parseEnemyInfo();
                logger.log(Level.INFO, String.format("Detected %s", enemyInfo.toString()));
                doAttack = doConditionsMatch(enemyInfo);
                if (doAttack && Settings.instance().isDetectEmptyCollectors()) {
                    final Boolean isCollectorFullBase = getScreen().isCollectorFullBase();
                    doAttack = isCollectorFullBase == null ? false : isCollectorFullBase;
                    if (!doAttack) {
                        logger.log(Level.INFO, "Detected empty collectors");
                    }
                }
            } catch (final BotBadBaseException e) {
                platform.saveScreenshot("bad_base_" + id);
                throw e;
            }
            if (!doAttack) {
                platform.leftClick(getScreen().getButtonNext(), true);
                platform.sleepRandom(666);
                try {
                    sleepUntilPointFound(() -> getScreen().searchButtonNext());
                } catch (final TimeoutException e) {
                    logger.log(Level.WARNING, "Next button not found");
                    break;
                }
                // to avoid server/client sync from nexting too fast
                platform.sleepRandom(1000);
            } else {
                final Attack attackStrategy = Settings.instance().getAttackStrategy();
                if (attackStrategy == Attack.manualStrategy()) {
                    if (enemyInfo.equals(prevLoot)) {
                        logger.log(Level.INFO, "User is manually attacking/deciding");
                    }
                    prevLoot = enemyInfo;
                    Thread.sleep(5000);
                } else {
                    final TroopsInfo troopsInfo = context.getTroopsInfo();
                    if (troopsInfo != null) {
                        final int[] troopsCount = troopsInfo.getTroopsCount();
                        logger.log(Level.INFO, "Attacking with " + Arrays.toString(troopsCount));
                        attackStrategy.attack(enemyInfo, troopsCount);
                    }
                    platform.leftClick(getScreen().getButtonEndBattle(), true);
                    platform.sleepRandom(1200);
                    platform.leftClick(getScreen().getButtonEndBattleQuestionOK(), true);
                    platform.sleepRandom(1200);
                    nextState = StateIdle.instance();
                }
                break;
            }
        }
        context.setState(nextState);
    }
}
