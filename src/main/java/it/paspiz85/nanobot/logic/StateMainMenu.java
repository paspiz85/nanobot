package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
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
        // TODO change message
        logger.fine("StateMainMenu");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        if (!context.isLanguageChecked()) {
            logger.info("Checking language...");
            if (getParser().findTrainButton() == null) {
                throw new IllegalStateException("Make sure in-game language is English");
            }
            context.setLanguageChecked(true);
        }
        os.zoomUp();
        os.sleepRandom(350);
        final Point firstRax = Settings.instance().getFirstBarrackPosition();
        logger.fine("Try open barracks");
        os.leftClick(firstRax, false);
        os.sleepRandom(500);
        Point trainButton = getParser().findTrainButton();
        if (trainButton == null) {
            // maybe rax was already open and we closed it back
            logger.fine("Try open barracks again");
            os.leftClick(firstRax, false);
            os.sleepRandom(500);
            trainButton = getParser().findTrainButton();
        }
        if (trainButton == null) {
            throw new BotConfigurationException("Barracks location is not correct.");
        }
        logger.fine("Press Train");
        os.leftClick(trainButton, false);
        os.sleepRandom(500);
        if (os.isClickableActive(Clickable.BUTTON_RAX_FULL)) {
            logger.info("Camp is full");
            logger.fine("Close barracks");
            os.leftClick(Clickable.BUTTON_RAX_CLOSE, true);
            os.sleepRandom(200);
            logger.fine("Press Attack");
            os.leftClick(Clickable.BUTTON_ATTACK, true);
            os.sleepRandom(1000);
            context.setState(StateFindAMatch.instance());
        } else {
            context.setState(StateTrainTroops.instance());
        }
        os.sleepRandom(500);
    }
}
