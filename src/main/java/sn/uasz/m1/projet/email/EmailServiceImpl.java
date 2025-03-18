// package sn.uasz.m1.projet.email;

// import java.util.Properties;

// import jakarta.mail.Authenticator;
// import jakarta.mail.Message;
// import jakarta.mail.MessagingException;
// import jakarta.mail.PasswordAuthentication;
// import jakarta.mail.Session;
// import jakarta.mail.Transport;
// import jakarta.mail.internet.InternetAddress;
// import jakarta.mail.internet.MimeMessage;

// public class EmailServiceImpl implements EmailService {
    
//     private final String smtpHost;
//     private final int smtpPort;
//     private final String username;
//     private final String password;
    
//     public EmailServiceImpl(String smtpHost, int smtpPort, String username, String password) {
//         this.smtpHost = smtpHost;
//         this.smtpPort = smtpPort;
//         this.username = username;
//         this.password = password;
//     }
    
//     @Override
//     public void sendEmail(String toEmail, String subject, String body) throws EmailException {
//         try {
//             // Configuration SMTP
//             Properties props = new Properties();
//             props.put("mail.smtp.auth", "true");
//             props.put("mail.smtp.starttls.enable", "true");
//             props.put("mail.smtp.host", smtpHost);
//             props.put("mail.smtp.port", smtpPort);
            
//             // Création de la session
//             Session session = Session.getInstance(props, new Authenticator() {
//                 @Override
//                 protected PasswordAuthentication getPasswordAuthentication() {
//                     return new PasswordAuthentication(username, password);
//                 }
//             });
            
//             // Création du message
//             Message message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(username));
//             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//             message.setSubject(subject);
            
//             // Configuration du contenu HTML
//             message.setContent(body, "text/html; charset=utf-8");
            
//             // Envoi du message
//             Transport.send(message);
            
//         } catch (MessagingException e) {
//             throw new EmailException("Erreur lors de l'envoi de l'email", e);
//         }
//     }
// }

package sn.uasz.m1.projet.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailServiceImpl implements EmailService {
    
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;

    public EmailServiceImpl(String test) throws IOException {
        Properties props = new Properties();
        // Chargement depuis le classpath (resources)
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (is == null) {
                throw new IOException("Le fichier email.properties n'a pas été trouvé dans le classpath");
            }
            props.load(is);
        }
        
        this.smtpHost = props.getProperty("smtp.host");
        this.smtpPort = Integer.parseInt(props.getProperty("smtp.port"));
        this.username = props.getProperty("smtp.username");
        this.password = props.getProperty("smtp.password");
    }
    
    @Override
    public void sendEmail(String toEmail, String subject, String body) throws EmailException {
        try {
            // Configuration SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            
            // Création de la session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            
            // Configuration du contenu HTML
            message.setContent(body, "text/html; charset=utf-8");
            
            // Envoi du message
            Transport.send(message);
            
        } catch (MessagingException e) {
            throw new EmailException("Erreur lors de l'envoi de l'email", e);
        }
    }
}