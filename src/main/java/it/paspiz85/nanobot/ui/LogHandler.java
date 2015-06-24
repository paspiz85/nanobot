package it.paspiz85.nanobot.ui;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class LogHandler extends Handler {

    /**
     * Doing basically what <code>SimpleFormatter</code> does. Java does not let
     * you have two different formats for <code>SimpleFormatter</code>. <br>
     * Not only that, there is also no way to utilize
     * <code>SimpleFormatter.format()</code> method
     *
     * @author norecha
     */
    private static class LogFormatter extends Formatter {

        private static final String FORMAT = "[%1$tm.%1$td.%1$ty %1$tl:%1$tM:%1$tS %1$Tp] %4$s: %5$s %n";

        private final Date date = new Date();

        @Override
        public String format(LogRecord record) {
            date.setTime(record.getMillis());
            String message = formatMessage(record);
            return String.format(FORMAT, date, null, record.getLoggerName(), record.getLevel().getLocalizedName(),
                    message, null);
        }
    }

    static void initialize(TextArea textArea) {
        for (Handler h : Logger.getLogger("").getHandlers()) {
            if (h instanceof LogHandler) {
                ((LogHandler) h).setTextArea(textArea);
            }
        }
    }

    private final Formatter formatter = new LogFormatter();

    private TextArea textArea;

    @Override
    public void close() throws SecurityException {
        this.textArea = null;
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() < Level.CONFIG.intValue()) {
            return;
        }
        if (textArea != null) {
            Platform.runLater(() -> textArea.appendText(formatter.format(record)));
        }
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }
}
