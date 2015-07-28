package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

/**
 * This state is when bot is in main menu.
 *
 * @author paspiz85
 *
 */
public final class StateMainMenu extends State<MainScreenParser> {

    private static final int DARK_ELIXIR_DRILL_MAX_NUMBER = 4;

    private static final int ELIXIR_COLLECTOR_MAX_NUMBER = 8;

    private static final int GOLD_MINE_MAX_NUMBER = 8;

    public static StateMainMenu instance() {
        return Utils.singleton(StateMainMenu.class, () -> new StateMainMenu());
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
                    platform.leftClick(p, false);
                    platform.sleepRandom(200);
                    count++;
                }
            }
            for (int i = 0; i < ELIXIR_COLLECTOR_MAX_NUMBER; i++) {
                final Point p = getParser().searchFullElixirCollector();
                if (p != null) {
                    platform.leftClick(p, false);
                    platform.sleepRandom(200);
                    count++;
                }
            }
            for (int i = 0; i < DARK_ELIXIR_DRILL_MAX_NUMBER; i++) {
                final Point p = getParser().searchFullDarkElixirDrill();
                if (p != null) {
                    platform.leftClick(p, false);
                    platform.sleepRandom(200);
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
        platform.zoomUp();
        platform.sleepRandom(350);
        if (Settings.instance().isCollectResources()) {
            collecting(context);
        }
        final Point buttonTrainClose = getParser().searchButtonTrainClose();
        if (buttonTrainClose != null) {
            logger.fine("Close previous train");
            platform.leftClick(buttonTrainClose, true);
            platform.sleepRandom(500);
        }
        logger.fine("Open troops");
        platform.leftClick(getParser().getButtonTroops(), true);
        platform.sleepRandom(500);
        context.setState(StateManageTroops.instance());
    }
}
