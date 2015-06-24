package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Robot;

import java.awt.Point;

public class StateMainMenu extends State {
	private static final StateMainMenu instance = new StateMainMenu();

	public static StateMainMenu instance() {
		return instance;
	}

	private StateMainMenu() {
	}

	@Override
	public void handle(Context context) throws BotConfigurationException,
			InterruptedException {
		logger.info("StateMainMenu");
		if (Thread.interrupted()) {
			throw new InterruptedException("StateMainMenu is interrupted.");
		}
		Robot.instance().zoomUp();

		Robot.instance().sleepRandom(350);
		Point firstRax = Settings.instance().getFirstBarrackPosition();
		Robot.instance().leftClick(firstRax.x, firstRax.y, 500);

		Point trainButton = Parsers.getMainscreen().findTrainButton();
		if (trainButton == null) {
			// maybe rax was already open and we closed it back. try one more
			// time
			Robot.instance().leftClick(firstRax.x, firstRax.y, 500);
			trainButton = Parsers.getMainscreen().findTrainButton();
		}

		if (trainButton == null) {
			throw new BotConfigurationException(
					"Barracks location is not correct.");
		}

		Robot.instance().leftClick(trainButton.x, trainButton.y, 500);

		// camp is full
		if (Robot.instance().isClickableActive(Clickable.BUTTON_RAX_FULL)) {
			logger.info("Camp is full");
			Robot.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, 200);

			Robot.instance().leftClick(Clickable.BUTTON_ATTACK, 1000);

			context.setState(StateFindAMatch.instance());
		} else {
			context.setState(StateTrainTroops.instance());
		}
		Thread.sleep(500 + Robot.random().nextInt(500));
	}

}
