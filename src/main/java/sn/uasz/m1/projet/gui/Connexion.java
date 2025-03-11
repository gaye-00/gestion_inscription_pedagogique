// package sn.uasz.m1.projet.gui;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Font;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
// import java.awt.Insets;

// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JPasswordField;
// import javax.swing.JTextField;

// import org.kordamp.ikonli.materialdesign.MaterialDesign;
// import org.kordamp.ikonli.swing.FontIcon;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.EntityManagerFactory;
// import jakarta.persistence.Persistence;
// import jakarta.persistence.TypedQuery;
// import sn.uasz.m1.projet.gui.etudiant.MainFrameEtudiant;
// import sn.uasz.m1.projet.gui.responsablePedagogique.MainFrame;
// import sn.uasz.m1.projet.model.person.User;
// import sn.uasz.m1.projet.model.person.Utilisateur;

// public class Connexion extends JFrame {
//     private final JTextField emailField;
//     private final JPasswordField passwordField;
//     private final JButton connectButton;
//     private final EntityManagerFactory emf;
//     private final EntityManager em;

//     public Connexion() {
//         setTitle("Connexion - Club Sportif UASZ");
//         setSize(450, 300);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         // Création du panel principal avec une couleur de fond
//         JPanel mainPanel = new JPanel(new BorderLayout());
//         mainPanel.setBackground(new Color(240, 240, 240));

//         // Panel du titre
//         JPanel titlePanel = new JPanel();
//         titlePanel.setBackground(new Color(51, 153, 255));
//         JLabel titleLabel = new JLabel("Bienvenue au Club Sportif UASZ");
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
//         titleLabel.setForeground(Color.WHITE);
//         titlePanel.add(titleLabel);

//         // Panel du formulaire
//         JPanel formPanel = new JPanel(new GridBagLayout());
//         formPanel.setBackground(new Color(240, 240, 240));
//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         // Icône et champ email
//         FontIcon userIcon = FontIcon.of(MaterialDesign.MDI_ACCOUNT);
//         userIcon.setIconSize(20);
//         gbc.gridx = 0; gbc.gridy = 0;
//         formPanel.add(new JLabel(userIcon), gbc);

//         gbc.gridx = 1;
//         JLabel emailLabel = new JLabel("Nom utilisateur");
//         emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//         formPanel.add(emailLabel, gbc);

//         gbc.gridx = 1; gbc.gridy = 1;
//         emailField = new JTextField(20);
//         emailField.setPreferredSize(new Dimension(200, 30));
//         formPanel.add(emailField, gbc);

//         // Icône et champ mot de passe
//         FontIcon lockIcon = FontIcon.of(MaterialDesign.MDI_LOCK);
//         lockIcon.setIconSize(20);
//         gbc.gridx = 0; gbc.gridy = 2;
//         formPanel.add(new JLabel(lockIcon), gbc);

//         gbc.gridx = 1;
//         JLabel passwordLabel = new JLabel("Mot de passe");
//         passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//         formPanel.add(passwordLabel, gbc);

//         gbc.gridx = 1; gbc.gridy = 3;
//         passwordField = new JPasswordField(20);
//         passwordField.setPreferredSize(new Dimension(200, 30));
//         formPanel.add(passwordField, gbc);

//         // Bouton de connexion
//         gbc.gridx = 1; gbc.gridy = 4;
//         gbc.insets = new Insets(20, 10, 10, 10);
//         connectButton = new JButton("Se connecter");
//         connectButton.setBackground(new Color(51, 153, 255));
//         connectButton.setForeground(Color.WHITE);
//         connectButton.setFont(new Font("Arial", Font.BOLD, 14));
//         connectButton.setFocusPainted(false);
//         connectButton.setBorderPainted(false);
//         connectButton.setPreferredSize(new Dimension(200, 35));
        
//         // Ajout de l'icône de connexion
//         FontIcon loginIcon = FontIcon.of(MaterialDesign.MDI_LOGIN);
//         loginIcon.setIconSize(18);
//         connectButton.setIcon(loginIcon);
        
//         connectButton.addActionListener(e -> seConnecter());
//         formPanel.add(connectButton, gbc);

//         // Assemblage des panels
//         mainPanel.add(titlePanel, BorderLayout.NORTH);
//         mainPanel.add(formPanel, BorderLayout.CENTER);
//         add(mainPanel);

//         // Initialiser l'EntityManager
//         emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
//         em = emf.createEntityManager();
//     }

//     private int verifierCredentials(String email, String password) {
//         try {
//             TypedQuery<Utilisateur> query = em.createQuery(
//                 "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password",
//                 Utilisateur.class
//             );
//             query.setParameter("email", email);
//             query.setParameter("password", password);

//             Utilisateur user = query.getSingleResult();
//             String className = user.getClass().getSimpleName();

//             if (className.equals("Etudiant")) {
//                 return 2; 
//             } else if (className.equals("ResponsablePedagogique")) {
//                 return 1;
//             } else {
//                 return 0;
//             }
            
//         } catch (Exception e) {
//             System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
//             return 0;
//         }
//     }

//     private void seConnecter() {
//         String email = emailField.getText();
//         String password = new String(passwordField.getPassword());

//         if (email.isEmpty() || password.isEmpty()) {
//             showError("Veuillez remplir tous les champs", "Erreur");
//             return;
//         }

//         if (verifierCredentials(email, password) == 2) {
//             dispose();
//             new MainFrameEtudiant().setVisible(true);
//             // JOptionPane.showMessageDialog(
//             //     this,
//             //     "Test Connexion réussie Etudiant",
//             //     "Succès",
//             //     JOptionPane.INFORMATION_MESSAGE,
//             //     FontIcon.of(MaterialDesign.MDI_CHECK, 20, Color.GREEN)
//             // );
//             // this.dispose();
//         } else if (verifierCredentials(email, password) == 1) {
//             User user = new User("username", "ResponsablePedagogique");
//                 dispose();
//                 new MainFrame(user).setVisible(true);
//             // JOptionPane.showMessageDialog(
//             //     this,
//             //     "Test Connexion réussie Responsable pedagogique",
//             //     "Succès",
//             //     JOptionPane.INFORMATION_MESSAGE,
//             //     FontIcon.of(MaterialDesign.MDI_CHECK, 20, Color.GREEN)
//             // );
//             // this.dispose();
//         } else {
//             showError("Email ou mot de passe incorrect", "Erreur d'authentification");
//         }
//     }

//     private void showError(String message, String title) {
//         JOptionPane.showMessageDialog(
//             this,
//             message,
//             title,
//             JOptionPane.ERROR_MESSAGE,
//             FontIcon.of(MaterialDesign.MDI_ALERT, 20, Color.RED)
//         );
//     }

//     @Override
//     public void dispose() {
//         if (em != null && em.isOpen()) {
//             em.close();
//         }
//         if (emf != null && emf.isOpen()) {
//             emf.close();
//         }
//         super.dispose();
//     }
// }

package sn.uasz.m1.projet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import sn.uasz.m1.projet.gui.etudiant.MainFrameEtudiant;
import sn.uasz.m1.projet.gui.responsablePedagogique.MainFrame;
import sn.uasz.m1.projet.model.person.User;
import sn.uasz.m1.projet.model.person.Utilisateur;

public class Connexion extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton connectButton;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public Connexion() {
        setTitle("Connexion - Club Sportif UASZ");
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

    private void seConnecter() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs", "Erreur");
            return;
        }

        int result = verifierCredentials(email, password);
        if (result == 2) {
            dispose();
            new MainFrameEtudiant().setVisible(true);
        } else if (result == 1) {
            User user = new User(email, "ResponsablePedagogique");
            dispose();
            new MainFrame(user).setVisible(true);
        } else {
            showError("Nom utilisateur ou mot de passe incorrect", "Erreur d'authentification");
        }
    }

    private int verifierCredentials(String email, String password) {
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password",
                Utilisateur.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            Utilisateur user = query.getSingleResult();
            return "Etudiant".equals(user.getClass().getSimpleName()) ? 2 : 1;
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des credentials : " + e.getMessage());
            return 0;
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
