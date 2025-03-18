package sn.uasz.m1.projet.email;

import java.io.IOException;

public class EmailExample {
    public static void main(String[] args) throws IOException {
        try {
            // String test = "test";
            EmailService emailService = new EmailServiceImpl("");
            
            String toEmail = "gayeabdoulaye163@gmail.com";
            String subject = "Test Email 2";
            String body = "<h1>Bonjour!</h1><p>Ceci est un email de test avec du <b>HTML</b>.</p>";
            
            emailService.sendEmail(toEmail, subject, body);
            System.out.println("Email envoyé avec succès!");
            
        } catch (EmailException e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }
}
