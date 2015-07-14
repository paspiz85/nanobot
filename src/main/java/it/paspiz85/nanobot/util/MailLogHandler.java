package it.paspiz85.nanobot.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

/**
 * Handler for logging via mail.
 *
 * @author paspiz85
 *
 */
public class MailLogHandler extends Handler {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(final LogRecord record) {
        if (record.getLevel().intValue() < this.getLevel().intValue()
                || record.getLoggerName().equals(getClass().getName())) {
            return;
        }
        try {
            Mailer.instance().send(
                    (message) -> {
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress("clash.nanobot+log."
                                + record.getLevel().getName().toLowerCase() + "@gmail.com"));
                        message.setSubject(record.getLevel() + ": " + record.getMessage());
                        String emailBody = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">";
                        emailBody += "<tr><td>Version</td><td>" + BuildInfo.instance().getVersion() + "</td></tr>";
                        emailBody += "<tr><td>BuildTime</td><td>" + BuildInfo.instance().getTimestamp() + "</td></tr>";
                        emailBody += "<tr><td>Time</td><td>" + record.getMillis() + "</td></tr>";
                        emailBody += "<tr><td>Thread</td><td>" + record.getThreadID() + "</td></tr>";
                        emailBody += "<tr><td>Logger</td><td>" + record.getLoggerName() + "</td></tr>";
                        emailBody += "<tr><td>Class</td><td>" + record.getSourceClassName() + "</td></tr>";
                        emailBody += "<tr><td>Method</td><td>" + record.getSourceMethodName() + "</td></tr>";
                        emailBody += "<tr><td>Level</td><td>" + record.getLevel() + "</td></tr>";
                        emailBody += "<tr><td>Message</td><td>" + record.getMessage() + "</td></tr>";
                        final Throwable e = record.getThrown();
                        if (e != null) {
                            final StringWriter writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            emailBody += "<tr><td>Exception</td><td>" + writer.toString().replace("\n", "<br/>") + "</td></tr>";
                        }
                        emailBody += "</table>";
                        message.setContent(emailBody, "text/html");
                    });
        } catch (final Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
