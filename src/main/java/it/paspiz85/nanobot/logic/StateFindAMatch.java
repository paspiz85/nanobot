package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * This state is when bot switch from training mode to attack mode.
 *
 * @author paspiz85
 *
 */
public final class StateFindAMatch extends State<AttackScreenParser> {

    public static StateFindAMatch instance() {
        return Utils.singleton(StateFindAMatch.class, () -> new StateFindAMatch());
    }

    private StateFindAMatch() {
        super(Parser.getInstance(AttackScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.INFO, "Start battle.");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        if (Settings.instance().getAttackStrategy() != Attack.noStrategy()
                && platform.matchColoredPoint(getParser().getButtonFindMatch())) {
            platform.leftClick(getParser().getButtonFindMatch(), true);
            platform.sleepRandom(300);
            platform.leftClick(getParser().getButtonShieldDisable(), true);
            platform.sleepRandom(100);
            sleepUntilPointFound(() -> getParser().searchButtonNext());
            context.setState(StateAttack.instance());
        } else {
            context.setState(StateIdle.instance());
        }
    }
}
