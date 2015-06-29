package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Point;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Bot model.
 *
 * @author paspiz85
 *
 */
public final class Model {

    private static Model instance;

    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    // TODO remove singleton def and usage
    private final Looper looper = Looper.instance();

    private Model() {
    }

    public boolean isRunning() {
        return looper.isRunning();
    }

    public void start(final BooleanSupplier setupResolution, final Supplier<Point> setupBarracks)
            throws InterruptedException, BotException {
        looper.start(setupResolution, setupBarracks);
    }
}
