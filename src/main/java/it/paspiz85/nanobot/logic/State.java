package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * State in which bot can be.
 *
 * @author paspiz85
 *
 * @param <P>
 *            parser to grab screen info.
 */
public abstract class State<P extends Parser> implements Constants {

    private static final OS DEFAULT_OS = OS.instance();

    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected final OS os = DEFAULT_OS;

    private final P parser;

    State(final P parser) {
        this.parser = parser;
    }

    public final P getParser() {
        return parser;
    }

    public abstract void handle(Context context) throws BotException, InterruptedException;

    // TODO remove
    protected final void sleepTillClickableIsActive(final Clickable clickable) throws InterruptedException {
        while (true) {
            if (os.isClickableActive(clickable)) {
                return;
            }
            final int time = OS.RANDOM.nextInt(250) + 750;
            logger.fine("Clickable not active, sleeping for " + time + " ms.");
            Thread.sleep(time);
        }
    }

    protected final Point sleepUntilPointFound(final Supplier<Point> supplier) throws InterruptedException {
        while (true) {
            final Point point = supplier.get();
            if (point != null) {
                return point;
            }
            logger.fine("Point not found.");
            os.sleepRandom(500);
        }
    }
}
