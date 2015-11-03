package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.game.AttackScreen;
import it.paspiz85.nanobot.game.BattleBeginScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * This state is when bot switch from training mode to attack mode.
 *
 * @author paspiz85
 *
 */
public final class StateBattleBegin extends State<BattleBeginScreen> {

    public static StateBattleBegin instance() {
        return Utils.singleton(StateBattleBegin.class, () -> new StateBattleBegin());
    }

    private StateBattleBegin() {
        super(Screen.getInstance(BattleBeginScreen.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.INFO, "Starting battle");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted");
        }
        if (Settings.instance().getAttackStrategy() != Attack.noStrategy().getName()
                && platform.matchColoredPoint(getScreen().getButtonFindMatch())) {
            platform.leftClick(getScreen().getButtonFindMatch(), true);
            platform.sleepRandom(300);
            platform.leftClick(getScreen().getButtonShieldDisable(), true);
            platform.sleepRandom(100);
            try {
                sleepUntilPointFound(() -> Screen.getInstance(AttackScreen.class).searchButtonNext());
                // platform.zoomUp();
                context.setState(StateAttack.instance());
                return;
            } catch (final TimeoutException e) {
                logger.log(Level.WARNING, "Next button not found");
            }
        }
        context.setState(StateIdle.instance());
    }
}
