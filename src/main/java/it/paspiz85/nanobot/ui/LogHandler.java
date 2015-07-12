package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.util.Settings;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Handler for logging in a textarea on GUI.
 *
 * @author paspiz85
 *
 */
public class LogHandler extends Handler {

    /**
     * Doing basically what <code>SimpleFormatter</code> does. Java does not let
     * you have two different formats for <code>SimpleFormatter</code>. <br>
     * Not only that, there is also no way to utilize
     * <code>SimpleFormatter.format()</code> method
     *
     * @author paspiz85
     */
    private static class LogFormatter extends Formatter {

        private static final String FORMAT = "[%1$tm.%1$td.%1$ty %1$tl:%1$tM:%1$tS %1$Tp] %4$s: %5$s %n";

        private final Date date = new Date();

        @Override
        public String format(final LogRecord record) {
            date.setTime(record.getMillis());
            final String message = formatMessage(record);
            return String.format(FORMAT, date, null, record.getLoggerName(), record.getLevel().getLocalizedName(),
                    message, null);
        }
    }

    private static final long CLEAN_INTERVAL = 2 * 60 * 60 * 1000;

    static void initialize(final TextArea textArea) {
        for (final Handler h : Logger.getLogger("").getHandlers()) {
            if (h instanceof LogHandler) {
                ((LogHandler) h).setTextArea(textArea);
            }
        }
    }

    private final Formatter formatter = new LogFormatter();

    private TextArea textArea;

    private Long lastCleanTime = null;

    private int lastCleanPosition = 0;

    private synchronized void append(final LogRecord record) {
        textArea.appendText(formatter.format(record));
        final long t = System.currentTimeMillis();
        if (lastCleanTime == null || lastCleanTime + CLEAN_INTERVAL < t) {
            textArea.deleteText(0, lastCleanPosition);
            lastCleanTime = t;
            lastCleanPosition = textArea.getLength();
        }
    }

    @Override
    public void close() throws SecurityException {
        this.textArea = null;
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(final LogRecord record) {
        if (textArea != null && record.getLevel().intValue() >= Settings.instance().getLogLevel().intValue()) {
            Platform.runLater(() -> append(record));
        }
    }

    public void setTextArea(final TextArea textArea) {
        this.textArea = textArea;
    }
}
