package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;

/**
 * Mock state that do nothing.
 *
 * @author v-ppizzuti
 *
 */
public final class StateNoAction extends State {

    private static StateNoAction instance;

    public static StateNoAction instance() {
        if (instance == null) {
            instance = new StateNoAction();
        }
        return instance;
    }

    private StateNoAction() {
    }

    @Override
    public void handle(final Context context) throws BotException, InterruptedException {
        logger.info("StateNoAction");
    }
}
