package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.win32.OS;

public final class StateIdle extends State {

    private static StateIdle instance;

    public static StateIdle instance() {
        if (instance == null) {
            instance = new StateIdle();
        }
        return instance;
    }

    boolean reloading = false;

    private StateIdle() {
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        State nextState = null;
        while (true) {
            logger.info("Idle");
            if (Thread.interrupted()) {
                throw new InterruptedException("StateIdle is interrupted.");
            }
            if (reloading) {
                logger.info("reloading...");
                Thread.sleep(2000);
                continue;
            }
            if (OS.instance().isClickableActive(Clickable.BUTTON_WAS_ATTACKED_HEADLINE)
                    || OS.instance().isClickableActive(Clickable.BUTTON_WAS_ATTACKED_OKAY)) {
                logger.info("Was attacked.");
                OS.instance().leftClick(Clickable.BUTTON_WAS_ATTACKED_OKAY, 250);
            } else if (OS.instance().isClickableActive(Clickable.BUTTON_ATTACK)) {
                nextState = StateMainMenu.instance();
                break;
            } else if (OS.instance().isClickableActive(Clickable.BUTTON_NEXT)) {
                nextState = StateAttack.instance();
                break;
            } else if (OS.instance().isClickableActive(Clickable.BUTTON_FIND_A_MATCH)) {
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
