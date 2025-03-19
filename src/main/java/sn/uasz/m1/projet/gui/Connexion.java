package sn.uasz.m1.projet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.gui.etudiant.FormulaireEtudiant;
import sn.uasz.m1.projet.gui.etudiant.MainFrameEtudiant;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.model.person.Utilisateur;

public class Connexion extends JFrame {
    // Ajout de constantes pour les couleurs
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton connectButton;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public Connexion() {
        // Configuration de base de la fenêtre
        setTitle("Connexion - Gestion des inscriptions pédagogiques");
        setSize(800, 500); // Fenêtre plus grande
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panneau principal avec deux sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Section gauche pour le formulaire
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 20));

        // En-tête avec logo et titre
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Bienvenue", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("Connectez-vous à votre compte", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Style des champs
        emailField = createStyledTextField("Nom d'utilisateur", MaterialDesign.MDI_ACCOUNT);
        passwordField = createStyledPasswordField("Mot de passe", MaterialDesign.MDI_LOCK);
        
        // Bouton de connexion
        connectButton = new JButton("Se connecter");
        connectButton.setIcon(FontIcon.of(MaterialDesign.MDI_LOGIN, 20, Color.WHITE));
        connectButton.setBackground(PRIMARY_COLOR);
        connectButton.setForeground(Color.WHITE);
        connectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connectButton.setFocusPainted(false);
        connectButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        connectButton.addActionListener(e -> seConnecter());

        // Lien d'inscription
        JLabel inscriptionLabel = new JLabel("<html><u>Pas encore inscrit ? Créer un compte</u></html>", JLabel.CENTER);
        inscriptionLabel.setForeground(SECONDARY_COLOR);
        inscriptionLabel.setFont(LABEL_FONT);
        inscriptionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inscriptionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new FormulaireEtudiant().setVisible(true);
            }
        });

        // Ajout des composants au formulaire
        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(emailField, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);
        formPanel.add(passwordField, gbc);
        formPanel.add(Box.createVerticalStrut(25), gbc);
        formPanel.add(connectButton, gbc);
        formPanel.add(Box.createVerticalStrut(15), gbc);
        formPanel.add(inscriptionLabel, gbc);

        leftPanel.add(headerPanel, BorderLayout.NORTH);
        leftPanel.add(formPanel, BorderLayout.CENTER);

        // Section droite pour l'illustration
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PRIMARY_COLOR);
        
        // Grande icône décorative
        JLabel iconLabel = new JLabel(FontIcon.of(MaterialDesign.MDI_SCHOOL, 200, Color.WHITE));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Texte décoratif
        JLabel welcomeText = new JLabel("<html><center>Système de Gestion<br>des Inscriptions Pédagogiques</center></html>", JLabel.CENTER);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setBorder(new EmptyBorder(30, 20, 30, 20));

        rightPanel.add(iconLabel, BorderLayout.CENTER);
        rightPanel.add(welcomeText, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        // Initialisation JPA
        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();
    }

    private JTextField createStyledTextField(String placeholder, MaterialDesign icon) {
        JTextField field = new JTextField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 45, 10, 10)
        ));
        field.setFont(LABEL_FONT);
        
        JLabel iconLabel = new JLabel(FontIcon.of(icon, 20, PRIMARY_COLOR));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.add(iconLabel, BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);
        
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder, MaterialDesign icon) {
        JPasswordField field = new JPasswordField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 45, 10, 10)
        ));
        field.setFont(LABEL_FONT);
        
        JLabel iconLabel = new JLabel(FontIcon.of(icon, 20, PRIMARY_COLOR));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.add(iconLabel, BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);
        
        return field;
    }

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
               
                dispose();
                new FenetrePrincipal(utilisateur).setVisible(true);
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
            // D'abord, récupérer l'utilisateur par email uniquement
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email",
                Utilisateur.class
            );
            query.setParameter("email", email);
            
            Utilisateur utilisateur = query.getSingleResult();
            
            // Vérifier si le mot de passe correspond
            if (utilisateur != null && BCrypt.checkpw(password, utilisateur.getPassword())) {
                return utilisateur;
            }
            
            return null;
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
            return null;
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
