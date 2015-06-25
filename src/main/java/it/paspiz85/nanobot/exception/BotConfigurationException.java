package it.paspiz85.nanobot.exception;

/**
 * Wrong bot configuration.
 *
 * @author paspiz85
 *
 */
public class BotConfigurationException extends BotException {

    private static final long serialVersionUID = 1L;

    public BotConfigurationException(final String msg) {
        super(msg);
    }

    public BotConfigurationException(final String msg, final Throwable t) {
        super(msg, t);
    }
}
