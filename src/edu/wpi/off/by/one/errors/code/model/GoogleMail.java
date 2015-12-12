package edu.wpi.off.by.one.errors.code.model;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * This class allows to send an email from a set GMail account
 * @author Lu
 * @author Jules Voltaire
 */
public class GoogleMail {
    //region Attributes
    private final String from;
    private final String pass;
    private final String host;
    private final Properties props;
    //endregion

    /**
     * Constructor
     */
    public GoogleMail() {
        from = "goat.there@gmail.com";
        pass = "wpiteam7mapper";
        host = "smtp.gmail.com";
        props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

    }

    /**
     * Sends an email to the to the address
     * @param to The email address to send the email to
     * @param subject The subject of the email
     * @param body The body of the email
     */
    public void send(String to, String subject, String body) {
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
