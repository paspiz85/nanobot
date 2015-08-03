package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * This state is when bot sleeps.
 *
 * @author paspiz85
 *
 */
public final class StateIdle extends State<Parser> {

    public static StateIdle instance() {
        return Utils.singleton(StateIdle.class, () -> new StateIdle());
    }

    private final AttackScreenParser attackScreenParser;

    private final MainScreenParser mainScreenParser;

    private boolean reloading;

    private StateIdle() {
        super(Parser.getInstance(null));
        mainScreenParser = Parser.getInstance(MainScreenParser.class);
        attackScreenParser = Parser.getInstance(AttackScreenParser.class);
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        State<?> nextState = null;
        logger.log(Level.INFO, "Idle");
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException(getClass().getSimpleName() + " is interrupted");
            }
            if (reloading) {
                logger.log(Level.INFO, "reloading...");
                platform.zoomUp();
                Thread.sleep(2000);
                continue;
            } else {
                platform.zoomUp();
                platform.sleepRandom(350);
            }
            if (platform.matchColoredPoint(mainScreenParser.getPointWasAttackedHeadline())
                    || platform.matchColoredPoint(mainScreenParser.getButtonWasAttackedOK())) {
                logger.log(Level.INFO, "Was attacked");
                platform.leftClick(mainScreenParser.getButtonWasAttackedOK(), true);
                platform.sleepRandom(250);
            } else if (attackScreenParser.searchButtonEndBattleReturnHome() != null) {
                logger.log(Level.INFO, "Battle end");
                platform.leftClick(attackScreenParser.getButtonEndBattleReturnHome(), true);
                platform.sleepRandom(250);
            } else if (mainScreenParser.searchButtonTrainClose() != null) {
                platform.leftClick(mainScreenParser.getButtonTrainClose(), true);
                platform.sleepRandom(250);
                nextState = StateMainMenu.instance();
                break;
            } else if (mainScreenParser.searchButtonAttack() != null) {
                nextState = StateMainMenu.instance();
                break;
            } else if (attackScreenParser.searchButtonNext() != null) {
                nextState = StateAttack.instance();
                break;
            } else if (platform.matchColoredPoint(attackScreenParser.getButtonFindMatch())) {
                nextState = StateFindAMatch.instance();
                break;
            }
            Thread.sleep(1000);
        }
        context.setState(nextState);
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(final boolean reloading) {
        this.reloading = reloading;
    }
}
