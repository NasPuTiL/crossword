package com.crossword.mailAuthentication;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SimpleMailSend {
    private static String from = "XXXXXXXX";
    private static String pass = "XXXXXXXXXXXXXXXXXXXXXXXXX";
    private static String subject = "";
    private static String body = "Hello Crossword here, your activate link: \n ";
    private static String host = "smtp.gmail.com";

    private static Properties props = System.getProperties();

    static {
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
    }

    public void sendMessage(String mail, String token, String user) {
        body += "http://localhost:8080/crossword/authentication/me?token=";
        body += token;
        subject = "Dear " + user + ". Crossword - authorization";
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(mail);

            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setText(body);

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (
                MessagingException me) {
            me.printStackTrace();
        }
    }

}
