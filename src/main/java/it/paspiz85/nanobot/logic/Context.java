package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.TroopsInfo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Execution context of bot.
 *
 * @author paspiz85
 *
 */
public final class Context {

    private State<?> current;

    private boolean disconnected;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private int trainCount;

    private TroopsInfo troopsInfo;

    private boolean waitDone;

    public int getTrainCount() {
        return trainCount;
    }

    public TroopsInfo getTroopsInfo() {
        return troopsInfo;
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

    public void setState(final State<?> state) {
        logger.log(Level.FINE, "Next state to " + state.getClass().getSimpleName());
        this.current = state;
        if (state instanceof StateIdle) {
            trainCount = 0;
        }
        if (state instanceof StateManageTroops) {
            trainCount++;
            logger.log(Level.FINE, "Train count is " + trainCount);
        }
    }

    public void setTroopsInfo(final TroopsInfo troopsInfo) {
        this.troopsInfo = troopsInfo;
    }

    public void setWaitDone(final boolean waitDone) {
        this.waitDone = waitDone;
    }
}
