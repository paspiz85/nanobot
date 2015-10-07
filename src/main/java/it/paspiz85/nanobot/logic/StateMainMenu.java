package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.game.MainScreen;
import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.scripting.ScriptManager;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.Collections;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * This state is when bot is in main menu.
 *
 * @author paspiz85
 *
 */
public final class StateMainMenu extends State<MainScreen> {

    public static StateMainMenu instance() {
        return Utils.singleton(StateMainMenu.class, () -> new StateMainMenu());
    }

    private StateMainMenu() {
        super(Screen.getInstance(MainScreen.class));
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
            collect("gold", () -> getScreen().searchFullGoldMine());
        }
        if (context.getTrainCount() % 20 == 2) {
            collect("elisir", () -> getScreen().searchFullElixirCollector());
        }
        if (context.getTrainCount() % 20 == 3) {
            collect("dark elisir", () -> getScreen().searchFullDarkElixirDrill());
        }
    }

    private void extra(final Context context) throws InterruptedException {
        try {
            ScriptManager.instance().run("_loop.js", Collections.singletonMap("context", context));
        } catch (final InterruptedException ex) {
            throw ex;
        } catch (final IllegalArgumentException ex) {
            logger.log(Level.FINE, ex.getMessage());
        } catch (final Exception ex) {
            logger.log(Level.WARNING, "Error in extra script execution", ex);
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
        extra(context);
        final Point buttonTrainClose = Screen.getInstance(ManageTroopsScreen.class).searchButtonTrainClose();
        if (buttonTrainClose != null) {
            logger.log(Level.FINE, "Close previous train");
            platform.leftClick(buttonTrainClose, true);
            platform.sleepRandom(500);
        }
        logger.log(Level.FINE, "Open troops");
        platform.leftClick(getScreen().getButtonTroops(), true);
        platform.sleepRandom(500);
        context.setState(StateManageTroops.instance());
    }

    public void handleAttack(final Context context) throws InterruptedException {
        platform.zoomUp();
        logger.log(Level.FINE, "Press Attack");
        platform.leftClick(getScreen().getButtonAttack(), true);
        platform.sleepRandom(1000);
        context.setState(StateBattleBegin.instance());
    }
}
