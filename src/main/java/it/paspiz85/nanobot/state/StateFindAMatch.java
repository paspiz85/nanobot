package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

public class StateFindAMatch extends State {

    private static final StateFindAMatch instance = new StateFindAMatch();

    public static StateFindAMatch instance() {
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
            OS.instance().leftClick(Clickable.BUTTON_FIND_A_MATCH, 300);
            OS.instance().leftClick(Clickable.BUTTON_SHIELD_DISABLE, 100);
            OS.instance().sleepTillClickableIsActive(Clickable.BUTTON_NEXT);
            context.setState(StateAttack.instance());
        } else {
            context.setState(StateIdle.instance());
        }
    }
}
