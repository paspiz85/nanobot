package it.paspiz85.nanobot.exception;

public class BotException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BotException(final String msg) {
        super(msg);
    }

    public BotException(final String msg, final Throwable t) {
        super(msg, t);
    }
}
