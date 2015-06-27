package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

/**
 * This state is when bot is in main menu.
 *
 * @author paspiz85
 *
 */
public final class StateMainMenu extends State<MainScreenParser> {

    private static StateMainMenu instance;

    public static StateMainMenu instance() {
        if (instance == null) {
            instance = new StateMainMenu();
        }
        return instance;
    }

    private StateMainMenu() {
        super(Parser.getInstance(MainScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws BotConfigurationException, InterruptedException {
        // TODO fix log level and message
        logger.fine("StateMainMenu");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        OS.instance().zoomUp();
        OS.instance().sleepRandom(350);
        final Point firstRax = Settings.instance().getFirstBarrackPosition();
        logger.fine("Try open barracks");
        OS.instance().leftClick(firstRax, false);
        OS.instance().sleepRandom(500);
        Point trainButton = getParser().findTrainButton();
        if (trainButton == null) {
            // maybe rax was already open and we closed it back
            logger.fine("Try open barracks again");
            OS.instance().leftClick(firstRax, false);
            OS.instance().sleepRandom(500);
            trainButton = getParser().findTrainButton();
        }
        if (trainButton == null) {
            throw new BotConfigurationException("Barracks location is not correct.");
        }
        logger.fine("Press Train");
        OS.instance().leftClick(trainButton, false);
        OS.instance().sleepRandom(500);
        if (OS.instance().isClickableActive(Clickable.BUTTON_RAX_FULL)) {
            logger.info("Camp is full");
            logger.fine("Close barracks");
            OS.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, true);
            OS.instance().sleepRandom(200);
            logger.fine("Press Attack");
            OS.instance().leftClick(Clickable.BUTTON_ATTACK, true);
            OS.instance().sleepRandom(1000);
            context.setState(StateFindAMatch.instance());
        } else {
            context.setState(StateTrainTroops.instance());
        }
        OS.instance().sleepRandom(500);
    }
}
