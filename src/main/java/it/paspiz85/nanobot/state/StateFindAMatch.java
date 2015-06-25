package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

/**
 * This state is when bot switch from training mode to attack mode.
 * 
 * @author v-ppizzuti
 *
 */
public final class StateFindAMatch extends State {

    private static StateFindAMatch instance;

    public static StateFindAMatch instance() {
        if (instance == null) {
            instance = new StateFindAMatch();
        }
        return instance;
    }

    private StateFindAMatch() {
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.info("StateFindAMatch");
        if (Thread.interrupted()) {
            throw new InterruptedException("StateFindAMatch is interrupted.");
        }
        if (OS.instance().isClickableActive(Clickable.BUTTON_FIND_A_MATCH)) {
            OS.instance().leftClick(Clickable.BUTTON_FIND_A_MATCH.getPoint(), true);
            OS.instance().sleepRandom(300);
            OS.instance().leftClick(Clickable.BUTTON_SHIELD_DISABLE.getPoint(), true);
            OS.instance().sleepRandom(100);
            OS.instance().sleepTillClickableIsActive(Clickable.BUTTON_NEXT);
            context.setState(StateAttack.instance());
        } else {
            context.setState(StateIdle.instance());
        }
    }
}
