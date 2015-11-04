package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.TroopsInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Execution context of bot.
 *
 * @author paspiz85
 *
 */
public final class Context {

    private final Map<String, Object> attributes = new HashMap<>();

    private State<?> current;

    private boolean disconnected;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private boolean waitDone;

    public Object get(final ContextParam param) {
        return get(param.name());
    }

    public Object get(final String key) {
        return attributes.get(key);
    }

    public int getTrainCount() {
        return (Integer) get(ContextParam.TRAIN_COUNT);
    }

    public TroopsInfo getTroopsInfo() {
        return (TroopsInfo) this.get(ContextParam.TROOPS_INFO);
    }

    public void handle() throws BotException, InterruptedException {
        current.handle(this);
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public boolean isWaitDone() {
        return waitDone;
    }

    public Object put(final ContextParam param, final Object value) {
        param.check(value);
        return attributes.put(param.name(), value);
    }

    public Object put(final String key, final Object value) {
        Object result;
        try {
            final ContextParam param = Enum.valueOf(ContextParam.class, key);
            result = put(param, value);
        } catch (final IllegalArgumentException ex) {
            result = attributes.put(key, value);
        }
        return result;
    }

    public void setDisconnected(final boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setState(final State<?> state) {
        logger.log(Level.FINE, "Next state to " + state.getClass().getSimpleName());
        this.current = state;
        if (state instanceof StateIdle) {
            put(ContextParam.TRAIN_COUNT, 0);
        }
        if (state instanceof StateManageTroops) {
            int trainCount = getTrainCount();
            trainCount++;
            put(ContextParam.TRAIN_COUNT, trainCount);
            logger.log(Level.FINE, "Train count is " + trainCount);
        }
    }

    public void setTroopsInfo(final TroopsInfo troopsInfo) {
        this.put(ContextParam.TROOPS_INFO, troopsInfo);
    }

    public void setWaitDone(final boolean waitDone) {
        this.waitDone = waitDone;
    }
}
