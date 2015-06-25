package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.Parsers;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

import java.awt.Point;

public final class StateMainMenu extends State {

    private static StateMainMenu instance;

    public static StateMainMenu instance() {
        if (instance == null) {
            instance = new StateMainMenu();
        }
        return instance;
    }

    private StateMainMenu() {
    }

    @Override
    public void handle(final Context context) throws BotConfigurationException, InterruptedException {
        // logger.info("StateMainMenu");
        if (Thread.interrupted()) {
            throw new InterruptedException("StateMainMenu is interrupted.");
        }
        OS.instance().zoomUp();
        OS.instance().sleepRandom(350);
        final Point firstRax = Settings.instance().getFirstBarrackPosition();
        OS.instance().leftClick(firstRax.x, firstRax.y, 500);
        Point trainButton = Parsers.getMainscreen().findTrainButton();
        if (trainButton == null) {
            // maybe rax was already open and we closed it back. try one more
            // time
            OS.instance().leftClick(firstRax.x, firstRax.y, 500);
            trainButton = Parsers.getMainscreen().findTrainButton();
        }
        if (trainButton == null) {
            throw new BotConfigurationException("Barracks location is not correct.");
        }
        OS.instance().leftClick(trainButton.x, trainButton.y, 500);
        // camp is full
        if (OS.instance().isClickableActive(Clickable.BUTTON_RAX_FULL)) {
            logger.info("Camp is full");
            OS.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, 200);
            OS.instance().leftClick(Clickable.BUTTON_ATTACK, 1000);
            context.setState(StateFindAMatch.instance());
        } else {
            context.setState(StateTrainTroops.instance());
        }
        Thread.sleep(500 + OS.random().nextInt(500));
    }
}
