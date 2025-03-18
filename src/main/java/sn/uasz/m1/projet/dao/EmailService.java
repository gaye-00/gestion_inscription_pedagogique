package sn.uasz.m1.projet.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private static final String SMTP_HOST;
    private static final int SMTP_PORT;
    private static final String USERNAME;
    private static final String PASSWORD;
    static {
        Properties properties = new Properties();
        try {
            // Charger le fichier properties
            properties.load(new FileInputStream("email.properties"));
            // Initialiser les variables statiques avec les valeurs du fichier properties
            SMTP_HOST = properties.getProperty("smtp.host");
            SMTP_PORT = Integer.parseInt(properties.getProperty("smtp.port"));
            USERNAME = properties.getProperty("smtp.username");
            PASSWORD = properties.getProperty("smtp.password");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement des propriétés email");
        }
    }

    public static void envoyerEmail(String destinataire, String sujet, String messageTexte, boolean isHtml) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        // Création d'une session authentifiée
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(messageTexte);

            if (isHtml) {
                message.setContent(messageTexte, "text/html; charset=UTF-8");
            } else {
                message.setText(messageTexte);
            }

            // Envoi du message
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + destinataire);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
