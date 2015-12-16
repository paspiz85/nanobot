package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.platform.Platform;

import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * State in which bot can be.
 *
 * @author paspiz85
 *
 * @param <S>
 *            screen to grab info.
 */
public abstract class State<S extends Screen> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final S screen;

    protected final Platform platform = Platform.instance();

    State(final S screen) {
        this.screen = screen;
    }

    public final S getScreen() {
        return screen;
    }

    public abstract void handle(Context context) throws BotException, InterruptedException;

    protected final void sleepUntil(final Supplier<Boolean> supplier) throws InterruptedException, TimeoutException {
        logger.log(Level.FINER, "Waiting for point");
        for (int i = 0; i < 100; i++) {
            if (supplier.get()) {
                return;
            }
            logger.log(Level.FINER, "Point not found");
            platform.sleepRandom(500);
        }
        throw new TimeoutException("Point not found");
    }
}
