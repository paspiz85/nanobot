package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;

import java.util.logging.Logger;

public class Context {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private State current;

    private boolean disconnected = false;

    private boolean waitDone = false;

    public void handle() throws BotException, InterruptedException {
        current.handle(this);
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public boolean isWaitDone() {
        return waitDone;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setState(State state) {
        logger.finest("Setting next state to: " + state.getClass().getSimpleName());
        this.current = state;
    }

    public void setWaitDone(boolean waitDone) {
        this.waitDone = waitDone;
    }
}
