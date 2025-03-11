package sn.uasz.m1.projet.gui.etudiant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

public class EtudiantLoginPanel extends JPanel {
    private final MainFrameEtudiant mainFrame;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    // private final EtudiantDAO etudiantDAO;

    public EtudiantLoginPanel(MainFrameEtudiant mainFrame) {
        this.mainFrame = mainFrame;
        // this.etudiantDAO = new EtudiantDAO();
        
        // Configuration du panneau
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Création du titre
        JLabel titleLabel = new JLabel("Connexion Étudiant", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setIcon(FontIcon.of(MaterialDesign.MDI_ACCOUNT_CIRCLE, 36, new Color(41, 128, 185)));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Création du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Ajout des champs de texte
        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setIcon(FontIcon.of(MaterialDesign.MDI_EMAIL, 16, new Color(41, 128, 185)));
        emailField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setIcon(FontIcon.of(MaterialDesign.MDI_LOCK, 16, new Color(41, 128, 185)));
        passwordField = new JPasswordField(20);
        
        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        loginButton.setIcon(FontIcon.of(MaterialDesign.MDI_LOGIN, 16, Color.WHITE));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        // Ajout des écouteurs
        loginButton.addActionListener((ActionEvent e) -> {
            login();
        });

        // loginButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         login();
        //     }
        // });
        
        // Ajout des composants à la grille
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(loginButton, gbc);
        
        // Création du panneau central pour centrer le formulaire
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(formPanel);
        
        // Ajout des composants au panneau principal
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Image de fond décorative
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2025 - Université Assane Seck de Ziguinchor");
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Veuillez remplir tous les champs",
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try {
            // Vérification des identifiants
            // Long etudiantId = etudiantDAO.authenticate(email, password);
            Long etudiantId = 1L; // todo pour tester
            
            if (etudiantId != null) {
                mainFrame.showDashboard(etudiantId);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Email ou mot de passe incorrect",
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(
                this,
                "Erreur lors de la connexion: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }
}
