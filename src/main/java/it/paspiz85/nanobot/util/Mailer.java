package it.paspiz85.nanobot.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility to send mails.
 *
 * @author paspiz85
 *
 */
public final class Mailer {

    /**
     * Interface to compose mail message.
     *
     * @author paspiz85
     *
     */
    @FunctionalInterface
    public interface MessageComposer {

        void compose(MimeMessage message) throws MessagingException, UnsupportedEncodingException;
    }

    private static Mailer instance;

    public static Mailer instance() {
        if (instance == null) {
            instance = new Mailer();
        }
        return instance;
    }

    private Properties mailServerProperties;

    private Mailer() {
    }

    private Properties getMailServerProperties() {
        if (mailServerProperties == null) {
            mailServerProperties = new Properties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
        }
        return mailServerProperties;
    }

    public void send(final MessageComposer messageComposer) throws UnsupportedEncodingException, MessagingException {
        final Session mailSession = Session.getDefaultInstance(getMailServerProperties(), null);
        final MimeMessage mailMessage = new MimeMessage(mailSession);
        messageComposer.compose(mailMessage);
        mailMessage.setFrom(new InternetAddress("clash.nanobot@gmail.com", "NanoBot"));
        final Transport transport = mailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com", "clash.nanobot", "Java8Mail");
        transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
        transport.close();
    }
}
