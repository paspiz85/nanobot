package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotException;

public class StateNoAction extends State {

	private static StateNoAction instance = new StateNoAction();

	public static StateNoAction instance() {
		return instance;
	}

	private StateNoAction() {
	}

	@Override
	public void handle(Context context) throws BotException,
			InterruptedException {
		logger.info("StateNoAction");
	}

}
