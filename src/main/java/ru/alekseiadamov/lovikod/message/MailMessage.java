package ru.alekseiadamov.lovikod.message;

import ru.alekseiadamov.lovikod.config.MailConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailMessage {
    private final Message message;

    private MailMessage(final String messageText) {
        final MailConfig mailConfig = MailConfig.get();
        Session session = Session.getInstance(mailConfig.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getUser(), mailConfig.getPassword());
            }
        });
        try {
            this.message = new MimeMessage(session);
            this.message.setFrom(new InternetAddress(mailConfig.getFromAddress()));
            this.message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailConfig.getToAddress()));
            this.message.setSubject(mailConfig.getTitle());

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            this.message.setContent(multipart);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to create mail message");
        }
    }

    public static MailMessage create(final String messageText) {
        return new MailMessage(messageText);
    }

    public void send() {
        try {
            Transport.send(this.message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send mail message");
        }
    }
}
