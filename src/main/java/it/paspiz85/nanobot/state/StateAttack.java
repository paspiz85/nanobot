package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.attack.ManualAttack;
import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class StateAttack extends State {

    private static final StateAttack instance = new StateAttack();

    public static StateAttack instance() {
        return instance;
    }

    private int[] prevLoot;

    private StateAttack() {
    }

    public boolean doConditionsMatch(int gold, int elixir, int de) {
        // if threshold is 0 or not set, do not match based on them
        int goldThreshold = Settings.instance().getGoldThreshold();
        int elixirThreshold = Settings.instance().getElixirThreshold();
        int darkElixirThreshold = Settings.instance().getDarkElixirThreshold();
        if (Settings.instance().isMatchAllConditions()) {
            gold = goldThreshold == 0 ? Integer.MAX_VALUE : gold;
            elixir = elixirThreshold == 0 ? Integer.MAX_VALUE : elixir;
            de = darkElixirThreshold == 0 ? Integer.MAX_VALUE : de;
            return gold >= goldThreshold && elixir >= elixirThreshold && de >= darkElixirThreshold;
        } else {
            gold = goldThreshold == 0 ? Integer.MIN_VALUE : gold;
            elixir = elixirThreshold == 0 ? Integer.MIN_VALUE : elixir;
            de = darkElixirThreshold == 0 ? Integer.MIN_VALUE : de;
            return gold >= goldThreshold || elixir >= elixirThreshold || de >= darkElixirThreshold;
        }
    }

    @Override
    public void handle(Context context) throws InterruptedException, BotException {
        while (true) {
            logger.info("StateAttack");
            if (Thread.interrupted()) {
                throw new InterruptedException("StateAttack is interrupted.");
            }
            int[] loot;
            try {
                loot = Parsers.getAttackScreen().parseLoot();
            } catch (BotBadBaseException e) {
                try {
                    OS.instance().saveScreenShot(Area.ENEMY_LOOT, "bug", "bad_base_" + System.currentTimeMillis());
                } catch (IOException e1) {
                    logger.log(Level.SEVERE, e1.getMessage(), e1);
                }
                throw e;
            }
            int[] attackGroup = Parsers.getAttackScreen().parseTroopCount();
            int gold = loot[0];
            int elixir = loot[1];
            int de = loot[2];
            boolean doAttack = doConditionsMatch(gold, elixir, de)
                    && (!Settings.instance().isDetectEmptyCollectors() || Parsers.getAttackScreen()
                            .isCollectorFullBase());
            if (doAttack) {
                // // debug
                // if (true) {
                // attack or let user manually attack
                if (Settings.instance().getAttackStrategy() != ManualAttack.instance()) {
                    playAttackReady();
                    Settings.instance().getAttackStrategy().attack(loot, attackGroup);
                    OS.instance().leftClick(Clickable.BUTTON_END_BATTLE, 1200);
                    OS.instance().leftClick(Clickable.BUTTON_END_BATTLE_QUESTION_OKAY, 1200);
                    OS.instance().leftClick(Clickable.BUTTON_END_BATTLE_RETURN_HOME, 1200);
                } else {
                    if (Arrays.equals(prevLoot, loot)) {
                        logger.info("User is manually attacking/deciding.");
                    } else {
                        playAttackReady();
                    }
                    prevLoot = loot;
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
                context.setState(StateIdle.instance());
                break;
            } else {
                // next
                // make sure you dont immediately check for next button because
                // you may see the original one
                OS.instance().leftClick(Clickable.BUTTON_NEXT, 666);
                OS.instance().sleepTillClickableIsActive(Clickable.BUTTON_NEXT);
                // to avoid server/client sync from nexting too fast
                OS.instance().sleepRandom(1000);
            }
        }
    }

    void playAttackReady() {
        if (!Settings.instance().isPlaySound()) {
            return;
        }
        String[] clips = new String[] { "../audio/fight.wav", "../audio/finishim.wav", "../audio/getoverhere.wav" };
        URL resource = this.getClass().getResource(clips[OS.random().nextInt(clips.length)]);
        try (Clip clip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resource)) {
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(2000);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Unable to play audio.", ex);
        }
    }
}
