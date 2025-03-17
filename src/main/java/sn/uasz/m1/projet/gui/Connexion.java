package sn.uasz.m1.projet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.gui.etudiant.FormulaireEtudiant;
import sn.uasz.m1.projet.gui.etudiant.MainFrameEtudiant;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.model.person.User;
import sn.uasz.m1.projet.model.person.Utilisateur;

public class Connexion extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton connectButton;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public Connexion() {
        setTitle("Connexion - Gestion des inscriptions pedagogiques");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Connexion Utilisateur", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setIcon(FontIcon.of(MaterialDesign.MDI_ACCOUNT_CIRCLE, 40, new Color(41, 128, 185)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Nom utilisateur :");
        emailLabel.setIcon(FontIcon.of(MaterialDesign.MDI_ACCOUNT, 16, new Color(41, 128, 185)));
        emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setIcon(FontIcon.of(MaterialDesign.MDI_LOCK, 16, new Color(41, 128, 185)));
        passwordField = new JPasswordField(20);

        connectButton = new JButton("Se connecter");
        connectButton.setIcon(FontIcon.of(MaterialDesign.MDI_LOGIN, 20, Color.WHITE));
        connectButton.setBackground(new Color(41, 128, 185));
        connectButton.setForeground(Color.WHITE);
        connectButton.setFocusPainted(false);
        connectButton.setPreferredSize(new Dimension(200, 35));
        connectButton.addActionListener(e -> seConnecter());

        // Texte cliquable pour l'inscription des étudiants
        JLabel inscriptionLabel = new JLabel("<html><u>Pas encore inscrit ? Cliquez ici.</u></html>");
        inscriptionLabel.setForeground(new Color(41, 128, 185));
        inscriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inscriptionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inscriptionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new FormulaireEtudiant().setVisible(true);
            }
        });

        // Ajouter le texte sous le bouton "Se connecter"
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        gbc.gridwidth = 2;
        formPanel.add(inscriptionLabel, gbc);


        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(connectButton, gbc);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();
    }

    // private void seConnecter() {
    //     String email = emailField.getText();
    //     String password = new String(passwordField.getPassword());

    //     if (email.isEmpty() || password.isEmpty()) {
    //         showError("Veuillez remplir tous les champs", "Erreur");
    //         return;
    //     }

    //     int result = verifierCredentials(email, password);
    //     if (result == 2) {
    //         dispose();
    //         new MainFrameEtudiant().setVisible(true);
    //     } else if (result == 1) {
    //         User user = new User(email, "ResponsablePedagogique");
    //         dispose();
    //         new MainFrame(user).setVisible(true);
    //     } else {
    //         showError("Nom utilisateur ou mot de passe incorrect", "Erreur d'authentification");
    //     }
    // }

    // private void seConnecter() {
    //     String email = emailField.getText();
    //     String password = new String(passwordField.getPassword());
    
    //     if (email.isEmpty() || password.isEmpty()) {
    //         showError("Veuillez remplir tous les champs", "Erreur");
    //         return;
    //     }
    
    //     Long userId = verifierCredentials(email, password);
    //     if (userId != null) {
    //         dispose();
    //         new MainFrameEtudiant(userId).setVisible(true); // Passe l'ID ici
    //     } else {
    //         showError("Nom utilisateur ou mot de passe incorrect", "Erreur d'authentification");
    //     }
    // }    

    // private int verifierCredentials(String email, String password) {
    //     try {
    //         TypedQuery<Utilisateur> query = em.createQuery(
    //             "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password",
    //             Utilisateur.class
    //         );
    //         query.setParameter("email", email);
    //         query.setParameter("password", password);
    //         Utilisateur user = query.getSingleResult();
    //         return "Etudiant".equals(user.getClass().getSimpleName()) ? 2 : 1;
    //     } catch (Exception e) {
    //         System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
    //         return 0;
    //     }
    // }

    // private Long verifierCredentials(String email, String password) {
    //     try {
    //         TypedQuery<Utilisateur> query = em.createQuery(
    //             "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password",
    //             Utilisateur.class
    //         );
    //         query.setParameter("email", email);
    //         query.setParameter("password", password);
    //         Utilisateur user = query.getSingleResult();
    //         return user.getId(); // Retourne l'ID de l'utilisateur
    //     } catch (Exception e) {
    //         System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
    //         return null; // Retourne null en cas d'échec
    //     }
    // } 
    
    private void seConnecter() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs", "Erreur");
            return;
        }

        Utilisateur utilisateur = verifierCredentials(email, password);
        // System.out.println("#### Role : " + utilisateur.getRole());
        if (utilisateur != null) {
            // Si l'utilisateur est un étudiant
            if ("Etudiant".equals(utilisateur.getRole())) {
                dispose();
                new MainFrameEtudiant(utilisateur.getId()).setVisible(true);
            }
            // Si l'utilisateur est un responsable pédagogique
            else if ("ResponsablePedagogique".equals(utilisateur.getRole())) {
                User user = new User(email, "ResponsablePedagogique");
                dispose();
                new FenetrePrincipal(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Nom utilisateur ou mot de passe incorrect",
                "Erreur d'authentification",
                JOptionPane.ERROR_MESSAGE,
                FontIcon.of(MaterialDesign.MDI_ALERT, 20, Color.RED)
            );
            // showError("Nom utilisateur ou mot de passe incorrect", "Erreur d'authentification");
        }
    }

    private Utilisateur verifierCredentials(String email, String password) {
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password",
                Utilisateur.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            // System.out.println("#### Query : " + query.toString());
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
            return null; // Retourne null si l'utilisateur n'est pas trouvé
        }
    }


    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            JOptionPane.ERROR_MESSAGE,
            FontIcon.of(MaterialDesign.MDI_ALERT, 20, Color.RED)
        );
    }

    @Override
    public void dispose() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        super.dispose();
    }
}
