package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.EnemyInfo;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.parsing.TroopsInfo;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Settings;

import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
        int gold = loot.getGold();
        int elixir = loot.getElixir();
        int de = loot.getDarkElixir();
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
            logger.info("Searching opponent...");
            if (Thread.interrupted()) {
                throw new InterruptedException("StateAttack is interrupted.");
            }
            final long id = System.currentTimeMillis();
            if (Settings.instance().isLogEnemyBase()) {
                os.saveScreenshot("base_" + id);
            }
            EnemyInfo enemyInfo;
            boolean doAttack = false;
            try {
                enemyInfo = getParser().parseEnemyInfo();
                logger.info(String.format("Opponent %d has %s.", id, enemyInfo.toString()));
                doAttack = doConditionsMatch(enemyInfo);
                if (doAttack && Settings.instance().isDetectEmptyCollectors()) {
                    doAttack = getParser().isCollectorFullBase();
                    if (!doAttack) {
                        logger.info(String.format("Opponent %d has empty collectors.", id));
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
                    playAttackReady();
                    final TroopsInfo troopsInfo = context.getTroopsInfo();
                    if (troopsInfo != null) {
                        int[] troopsCount = troopsInfo.getTroopsCount();
                        logger.info("Troops count: " +Arrays.toString(troopsCount));
                        attackStrategy.attack(enemyInfo, troopsCount);
                    }
                    os.leftClick(Clickable.BUTTON_END_BATTLE.getPoint(), true);
                    os.sleepRandom(1200);
                    os.leftClick(Clickable.BUTTON_END_BATTLE_QUESTION_OKAY.getPoint(), true);
                    os.sleepRandom(1200);
                    os.leftClick(Clickable.BUTTON_END_BATTLE_RETURN_HOME.getPoint(), true);
                    os.sleepRandom(1200);
                } else {
                    if (enemyInfo.equals(prevLoot)) {
                        logger.info("User is manually attacking/deciding.");
                    } else {
                        playAttackReady();
                    }
                    prevLoot = enemyInfo;
                    /**
                     * NOTE: minor race condition 1. Matching base found. 2.
                     * sound is played. 3. prevLoot is set to full available
                     * loot 4. Thread.sleep(XXX) 5. StateIdle -> next is
                     * available to state is set back to attack. 6. user drops
                     * units, loot number changes AFTER state is set BEFORE this
                     * state parsed the image. 7. loot is different now. 8.
                     * sound is played again which is wrong. 9. won't happen
                     * more than once since next button won't be available after
                     * attack has started.
                     */
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

    void playAttackReady() {
        if (!Settings.instance().isPlaySound()) {
            return;
        }
        final String[] clips = new String[] { "../audio/fight.wav", "../audio/finishim.wav", "../audio/getoverhere.wav" };
        final URL resource = this.getClass().getResource(clips[OS.RANDOM.nextInt(clips.length)]);
        try (Clip clip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resource)) {
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(2000);
        } catch (final Exception ex) {
            logger.log(Level.WARNING, "Unable to play audio.", ex);
        }
    }
}
