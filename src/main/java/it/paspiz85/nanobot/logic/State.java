package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.util.Point;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * State in which bot can be.
 *
 * @author paspiz85
 *
 * @param <P>
 *            parser to grab screen info.
 */
public abstract class State<P extends Parser> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final P parser;

    protected final Platform platform = Platform.instance();

    State(final P parser) {
        this.parser = parser;
    }

    public final P getParser() {
        return parser;
    }

    public abstract void handle(Context context) throws BotException, InterruptedException;

    protected final Point sleepUntilPointFound(final Supplier<Point> supplier) throws InterruptedException {
        while (true) {
            final Point point = supplier.get();
            if (point != null) {
                return point;
            }
            logger.log(Level.FINE, "Point not found.");
            platform.sleepRandom(500);
        }
    }
}
