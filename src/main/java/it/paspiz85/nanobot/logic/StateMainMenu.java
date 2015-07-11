package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.game.GameConstants;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.parsing.TroopsInfo;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

import java.util.Arrays;

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
        if (Settings.instance().isCollectResources()) {
            collecting(context);
        }
        if (Settings.instance().isTrainTroops()) {
            training(context);
        }
        context.setTroopsInfo(null);
        if (getParser().areCampsFull()) {
            logger.info("Camp is full.");
            logger.fine("Close barracks");
            os.leftClick(getParser().getButtonTrainClose(), true);
            os.sleepRandom(200);
            reviewTroops(context);
            logger.fine("Press Attack.");
            os.leftClick(getParser().getButtonAttack(), true);
            os.sleepRandom(1000);
            context.setState(StateFindAMatch.instance());
        } else {
            context.setState(StateTrainTroops.instance());
        }
        os.sleepRandom(500);
    }

    private void reviewTroops(final Context context) throws InterruptedException {
        os.leftClick(getParser().getButtonTroops(), true);
        os.sleepRandom(300);
        final TroopsInfo troopsInfo = getParser().parseTroopsInfo();
        final int[] troopsCount = troopsInfo.getTroopsCount();
        logger.info("Troops count: " + Arrays.toString(troopsCount));
        context.setTroopsInfo(troopsInfo);
        os.leftClick(getParser().getButtonTrainClose(), true);
        os.sleepRandom(200);
    }

    private void training(final Context context) throws InterruptedException, BotConfigurationException {
        final Point buttonTrainClose = getParser().searchButtonTrainClose();
        if (buttonTrainClose != null) {
            logger.fine("Close previous train");
            os.leftClick(buttonTrainClose, true);
            os.sleepRandom(500);
        }
        os.leftClick(getParser().getButtonTroops(), true);
        os.sleepRandom(500);
        os.leftClick(getParser().getButtonTrainNext(), true);
        os.sleepRandom(500);
    }
}
