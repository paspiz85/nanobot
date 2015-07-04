package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;

/**
 * This state is when bot sleeps.
 *
 * @author paspiz85
 *
 */
public final class StateIdle extends State<Parser> {

    private static StateIdle instance;

    public static StateIdle instance() {
        if (instance == null) {
            instance = new StateIdle();
        }
        return instance;
    }

    private final AttackScreenParser attackScreenParser;

    private boolean reloading;

    private final MainScreenParser mainScreenParser;

    private StateIdle() {
        super(Parser.getInstance(null));
        mainScreenParser = Parser.getInstance(MainScreenParser.class);
        attackScreenParser = Parser.getInstance(AttackScreenParser.class);
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        State<?> nextState = null;
        while (true) {
            logger.info("Idle");
            if (Thread.interrupted()) {
                throw new InterruptedException("StateIdle is interrupted.");
            }
            if (reloading) {
                logger.info("reloading...");
                os.zoomUp();
                Thread.sleep(2000);
                continue;
            }
            if (os.isClickableActive(Clickable.BUTTON_WAS_ATTACKED_HEADLINE)
                    || os.isClickableActive(Clickable.BUTTON_WAS_ATTACKED_OKAY)) {
                logger.info("Was attacked.");
                os.leftClick(Clickable.BUTTON_WAS_ATTACKED_OKAY.getPoint(), true);
                os.sleepRandom(250);
            } else if (mainScreenParser.searchButtonTrainClose() != null) {
                os.leftClick(mainScreenParser.getButtonTrainClose(), true);
                os.sleepRandom(250);
                nextState = StateMainMenu.instance();
                break;
            } else if (mainScreenParser.searchButtonAttack() != null) {
                nextState = StateMainMenu.instance();
                break;
            } else if (attackScreenParser.searchButtonNext() != null) {
                nextState = StateAttack.instance();
                break;
            } else if (os.isClickableActive(Clickable.BUTTON_FIND_A_MATCH)) {
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
