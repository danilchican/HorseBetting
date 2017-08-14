package com.epam.horsebetting.util;

import com.epam.horsebetting.config.EnvironmentConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private final class Property {
        private static final String PREFIX = "mail.smtp.";
        private static final String HOST_PROP = PREFIX + "host";
        private static final String PORT_PROP = PREFIX + "port";
        private static final String PASSWORD_PROP = PREFIX + "password";
        private static final String AUTH_PROP = PREFIX + "auth";
        private static final String STARTTLS_ENABLE_PROP = PREFIX + "starttls.enable";
        private static final String SENDER_PROP = "mail.from";
    }

    /**
     * Props to send email.
     */
    private Properties props;

    /**
     * Session to authenticate
     */
    private Session session;

    /**
     * Default constructor.
     */
    public MailSender() {
        this.props = new Properties();
        this.init();
    }

    /**
     * Init configuration.
     */
    private void init() {
        EnvironmentConfig env = new EnvironmentConfig();

        this.props.put(Property.HOST_PROP, env.obtainMailHost());
        this.props.put(Property.PORT_PROP, env.obtainMailPort());
        this.props.put(Property.SENDER_PROP, env.obtainMailSender());
        this.props.put(Property.PASSWORD_PROP, env.obtainMailPassword());

        this.props.put(Property.AUTH_PROP, env.obtainMailAuth());
        this.props.put(Property.STARTTLS_ENABLE_PROP, env.obtainMailEnableStartTLS());

        session = Session.getInstance(this.props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.obtainMailSender(), env.obtainMailPassword());
                    }
                });
    }

    /**
     * Send message to recipient.
     *
     * @param recipient
     * @param subject
     * @param messageText
     * @throws MessagingException
     */
    public void send(String recipient, String subject, String messageText) throws MessagingException {
        EnvironmentConfig env = new EnvironmentConfig();
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(env.obtainMailSender()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        message.setSubject(subject);
        message.setText(messageText);

        Transport.send(message);
    }
}
