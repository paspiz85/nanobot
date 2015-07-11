package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.EnemyInfo;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.parsing.TroopsInfo;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Settings;

import java.util.Arrays;

/**
 * Attack state is when bot find and attack an opponent.
 *
 * @author paspiz85
 *
 */
public final class StateAttack extends State<AttackScreenParser> implements Constants {

    private static StateAttack instance;

    public static StateAttack instance() {
        if (instance == null) {
            instance = new StateAttack();
        }
        return instance;
    }

    private EnemyInfo prevLoot;

    private StateAttack() {
        super(Parser.getInstance(AttackScreenParser.class));
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
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException("StateAttack is interrupted.");
            }
            final long id = System.currentTimeMillis();
            logger.info("Found opponent " + id);
            // to avoid fog
            os.sleepRandom(500);
            EnemyInfo enemyInfo;
            boolean doAttack = false;
            try {
                enemyInfo = getParser().parseEnemyInfo();
                logger.info(String.format("Detected %s.", enemyInfo.toString()));
                doAttack = doConditionsMatch(enemyInfo);
                if (doAttack && Settings.instance().isDetectEmptyCollectors()) {
                    final Boolean isCollectorFullBase = getParser().isCollectorFullBase();
                    doAttack = isCollectorFullBase == null ? false : isCollectorFullBase;
                    if (!doAttack) {
                        logger.info("Detected empty collectors.");
                    }
                }
            } catch (final BotBadBaseException e) {
                os.saveScreenshot("bad_base_" + id);
                throw e;
            }
            if (doAttack) {
                // // debug
                // if (true) {
                // attack or let user manually attack
                final Attack attackStrategy = Settings.instance().getAttackStrategy();
                if (attackStrategy != Attack.manualStrategy()) {
                    final TroopsInfo troopsInfo = context.getTroopsInfo();
                    if (troopsInfo != null) {
                        final int[] troopsCount = troopsInfo.getTroopsCount();
                        logger.info("Troops count: " + Arrays.toString(troopsCount));
                        attackStrategy.attack(enemyInfo, troopsCount);
                    }
                    os.leftClick(getParser().getButtonEndBattle(), true);
                    os.sleepRandom(1200);
                    os.leftClick(getParser().getButtonEndBattleQuestionOK(), true);
                    os.sleepRandom(1200);
                    os.leftClick(getParser().getButtonEndBattleReturnHome(), true);
                    os.sleepRandom(1200);
                } else {
                    if (enemyInfo.equals(prevLoot)) {
                        logger.info("User is manually attacking/deciding.");
                    }
                    prevLoot = enemyInfo;
                    Thread.sleep(5000);
                }
                break;
            } else {
                // next
                // make sure you dont immediately check for next button because
                // you may see the original one
                os.leftClick(getParser().getButtonNext(), true);
                os.sleepRandom(666);
                sleepUntilPointFound(() -> getParser().searchButtonNext());
                // to avoid server/client sync from nexting too fast
                os.sleepRandom(1000);
            }
        }
        context.setState(StateIdle.instance());
    }
}
