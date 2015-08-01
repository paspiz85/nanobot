package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * This state is when bot is in main menu.
 *
 * @author paspiz85
 *
 */
public final class StateMainMenu extends State<MainScreenParser> {

    public static StateMainMenu instance() {
        return Utils.singleton(StateMainMenu.class, () -> new StateMainMenu());
    }

    private StateMainMenu() {
        super(Parser.getInstance(MainScreenParser.class));
    }

    private void collect(final String resource, final Supplier<Point> pointSearch) throws InterruptedException {
        platform.zoomUp();
        platform.sleepRandom(350);
        logger.log(Level.INFO, String.format("Searching full collectors of %s...", resource));
        int count = 0;
        while (true) {
            final Point p = pointSearch.get();
            if (p == null) {
                logger.log(Level.INFO, String.format("Found %d full collectors of %s", count, resource));
                return;
            }
            platform.leftClick(p, false);
            platform.sleepRandom(200);
            count++;
        }
    }

    private void collecting(final Context context) throws InterruptedException {
        if (context.getTrainCount() % 20 == 1) {
            collect("gold", () -> getParser().searchFullGoldMine());
        }
        if (context.getTrainCount() % 20 == 2) {
            collect("elisir", () -> getParser().searchFullElixirCollector());
        }
        if (context.getTrainCount() % 20 == 3) {
            collect("dark elisir", () -> getParser().searchFullDarkElixirDrill());
        }
    }

    @Override
    public void handle(final Context context) throws BotConfigurationException, InterruptedException {
        logger.log(Level.FINE, "Returned in main menu");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted");
        }
        if (Settings.instance().isCollectResources()) {
            collecting(context);
        }
        final Point buttonTrainClose = getParser().searchButtonTrainClose();
        if (buttonTrainClose != null) {
            logger.log(Level.FINE, "Close previous train");
            platform.leftClick(buttonTrainClose, true);
            platform.sleepRandom(500);
        }
        logger.log(Level.FINE, "Open troops");
        platform.leftClick(getParser().getButtonTroops(), true);
        platform.sleepRandom(500);
        context.setState(StateManageTroops.instance());
    }
}
