package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Constants;

import java.util.logging.Logger;

/**
 * State in which bot can be.
 *
 * @author paspiz85
 *
 */
public abstract class State implements Constants {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public abstract void handle(Context context) throws BotException, InterruptedException;
}
