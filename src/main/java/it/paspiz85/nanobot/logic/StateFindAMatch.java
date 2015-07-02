package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Settings;

/**
 * This state is when bot switch from training mode to attack mode.
 *
 * @author paspiz85
 *
 */
public final class StateFindAMatch extends State<AttackScreenParser> {

    private static StateFindAMatch instance;

    public static StateFindAMatch instance() {
        if (instance == null) {
            instance = new StateFindAMatch();
        }
        return instance;
    }

    private StateFindAMatch() {
        super(Parser.getInstance(AttackScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.info("Start battle.");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        if (Settings.instance().getAttackStrategy() != Attack.noStrategy()
                && os.isClickableActive(Clickable.BUTTON_FIND_A_MATCH)) {
            os.leftClick(Clickable.BUTTON_FIND_A_MATCH, true);
            os.sleepRandom(300);
            os.leftClick(Clickable.BUTTON_SHIELD_DISABLE, true);
            os.sleepRandom(100);
            sleepUntilPointFound(() -> getParser().searchNextButton());
            context.setState(StateAttack.instance());
        } else {
            context.setState(StateIdle.instance());
        }
    }
}
