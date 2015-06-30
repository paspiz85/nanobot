package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.game.GameConstants;
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
public final class StateMainMenu extends State<MainScreenParser> implements GameConstants {

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

    private void collecting(final Context context) throws InterruptedException {
        if (context.getTrainCount() % 20 == 0) {
            logger.info("Searching full collectors...");
            int count = 0;
            for (int i = 0; i < GOLD_MINE_MAX_NUMBER; i++) {
                final Point p = getParser().searchFullGoldMine();
                if (p != null) {
                    os.leftClick(p, false);
                    os.sleepRandom(200);
                    count++;
                }
            }
            for (int i = 0; i < ELIXIR_COLLECTOR_MAX_NUMBER; i++) {
                final Point p = getParser().searchFullElixirCollector();
                if (p != null) {
                    os.leftClick(p, false);
                    os.sleepRandom(200);
                    count++;
                }
            }
            for (int i = 0; i < DARK_ELIXIR_DRILL_MAX_NUMBER; i++) {
                final Point p = getParser().searchFullDarkElixirDrill();
                if (p != null) {
                    os.leftClick(p, false);
                    os.sleepRandom(200);
                    count++;
                }
            }
            logger.info(String.format("Found %d full collectors.", count));
        }
    }

    @Override
    public void handle(final Context context) throws BotConfigurationException, InterruptedException {
        logger.fine("Returned in main menu");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        os.zoomUp();
        os.sleepRandom(350);
        if (!context.isLanguageChecked()) {
            logger.info("Checking language...");
            if (getParser().searchAttackButton() == null) {
                throw new BotConfigurationException("Make sure in-game language is English!");
            }
            logger.info("Checking language OK.");
            context.setLanguageChecked(true);
        }
        collecting(context);
        final Point firstRax = Settings.instance().getFirstBarrackPosition();
        logger.fine("Try open barracks");
        os.leftClick(firstRax, false);
        os.sleepRandom(500);
        Point trainButton = getParser().searchTrainButton();
        if (trainButton == null) {
            // maybe rax was already open and we closed it back
            logger.fine("Try open barracks again");
            os.leftClick(firstRax, false);
            os.sleepRandom(500);
            trainButton = getParser().searchTrainButton();
        }
        if (trainButton == null) {
            throw new BotConfigurationException("Barracks location is not correct.");
        }
        logger.fine("Press Train");
        os.leftClick(trainButton, false);
        os.sleepRandom(500);
        if (os.isClickableActive(Clickable.BUTTON_RAX_FULL)) {
            logger.info("Camp is full.");
            logger.fine("Close barracks");
            os.leftClick(Clickable.BUTTON_RAX_CLOSE, true);
            os.sleepRandom(200);
            logger.fine("Press Attack.");
            os.leftClick(Clickable.BUTTON_ATTACK, true);
            os.sleepRandom(1000);
            context.setState(StateFindAMatch.instance());
        } else {
            context.setState(StateTrainTroops.instance());
        }
        os.sleepRandom(500);
    }
}
