package sn.uasz.m1.projet.email;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body) throws EmailException;
}