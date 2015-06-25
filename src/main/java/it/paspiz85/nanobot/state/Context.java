package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;

import java.util.logging.Logger;

/**
 * Execution context of bot.
 *
 * @author v-ppizzuti
 *
 */
public final class Context {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private State current;

    private boolean disconnected;

    private boolean waitDone;

    private int trainCount;

    public int getTrainCount() {
        return trainCount;
    }

    public void handle() throws BotException, InterruptedException {
        current.handle(this);
    }

    public void incrementTrainCount() {
        trainCount++;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public boolean isWaitDone() {
        return waitDone;
    }

    public void setDisconnected(final boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setState(final State state) {
        logger.fine("Setting next state to: " + state.getClass().getSimpleName());
        this.current = state;
        if (state instanceof StateIdle) {
            trainCount = 0;
        }
        if (state instanceof StateTrainTroops) {
            trainCount++;
            logger.fine("Train count is " + trainCount);
        }
    }

    public void setWaitDone(final boolean waitDone) {
        this.waitDone = waitDone;
    }
}
