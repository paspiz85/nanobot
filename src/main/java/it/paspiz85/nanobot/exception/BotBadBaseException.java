package it.paspiz85.nanobot.exception;

/**
 * Exception raised when bad base found.
 *
 * @author paspiz85
 *
 */
public class BotBadBaseException extends BotException {

    private static final long serialVersionUID = 1L;

    public BotBadBaseException(final String msg) {
        super(msg);
    }

    public BotBadBaseException(final String msg, final Throwable t) {
        super(msg, t);
    }
}
