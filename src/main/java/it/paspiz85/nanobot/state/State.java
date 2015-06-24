package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;

import java.util.logging.Logger;

public abstract class State {

	protected final Logger logger = Logger.getLogger(getClass().getName());

	public abstract void handle(Context context) throws BotException, InterruptedException;
}
