// package sn.uasz.m1.projet.gui.etudiant;


// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Component;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
// import java.awt.Insets;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Random;

// import javax.swing.ButtonGroup;
// import javax.swing.JButton;
// import javax.swing.JComboBox;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JRadioButton;
// import javax.swing.JTextField;

// import org.kordamp.ikonli.materialdesign.MaterialDesign;
// import org.kordamp.ikonli.swing.FontIcon;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.EntityManagerFactory;
// import jakarta.persistence.Persistence;
// import jakarta.persistence.TypedQuery;
// import sn.uasz.m1.projet.model.formation.Formation;
// import sn.uasz.m1.projet.model.person.Etudiant;
// import sn.uasz.m1.projet.model.person.Sexe;

// public class FormulaireEtudiant extends JFrame {
//     private static final Color MAIN_COLOR = new Color(51, 153, 255);

//     private final JTextField prenomField = new JTextField(20);
//     private final JTextField nomField = new JTextField(20);
//     private final JRadioButton hommeButton = new JRadioButton("Homme");
//     private final JRadioButton femmeButton = new JRadioButton("Femme");
//     private final ButtonGroup sexeGroup = new ButtonGroup();
//     private final JComboBox<Formation> formationBox = new JComboBox<>();
    
//     private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
//     private final EntityManager em = emf.createEntityManager();

//     public FormulaireEtudiant() {
//         setTitle("Inscription Étudiant - Gestion des inscriptions pédagogiques");
//         setSize(600, 500);
//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         setLocationRelativeTo(null);
//         setLayout(new BorderLayout(10, 10));
        
//         JPanel mainPanel = new JPanel(new GridBagLayout());
//         mainPanel.setBackground(Color.WHITE);
//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);
//         gbc.fill = GridBagConstraints.HORIZONTAL;
        
//         // Prénom
//         addField(mainPanel, gbc, "Prénom :", prenomField, 0);
        
//         // Nom
//         addField(mainPanel, gbc, "Nom :", nomField, 1);

//         // Sexe
//         JPanel sexePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         sexeGroup.add(hommeButton);
//         sexeGroup.add(femmeButton);
//         sexePanel.add(hommeButton);
//         sexePanel.add(femmeButton);
//         addField(mainPanel, gbc, "Sexe :", sexePanel, 2);

//         // Formations disponibles
//         addField(mainPanel, gbc, "Formation :", formationBox, 3);
//         chargerFormations();

//         // Bouton Valider
//         JButton validerButton = new JButton("Valider");
//         validerButton.setBackground(MAIN_COLOR);
//         validerButton.setForeground(Color.WHITE);
//         validerButton.setFont(new Font("Arial", Font.BOLD, 14));
//         validerButton.setIcon(FontIcon.of(MaterialDesign.MDI_CHECK, 20, Color.WHITE));
//         validerButton.addActionListener(e -> validerInscription());

//         gbc.gridx = 0;
//         gbc.gridy = 4;
//         gbc.gridwidth = 2;
//         gbc.anchor = GridBagConstraints.CENTER;
//         mainPanel.add(validerButton, gbc);

//         add(mainPanel, BorderLayout.CENTER);
//     }
    
//     private void addField(JPanel panel, GridBagConstraints gbc, String labelText, Component field, int row) {
//         gbc.gridx = 0;
//         gbc.gridy = row;
//         gbc.weightx = 0.3;
//         gbc.anchor = GridBagConstraints.LINE_END;
//         panel.add(new JLabel(labelText), gbc);
        
//         gbc.gridx = 1;
//         gbc.weightx = 0.7;
//         gbc.anchor = GridBagConstraints.LINE_START;
//         panel.add(field, gbc);
//     }

//     private void chargerFormations() {
//         TypedQuery<Formation> query = em.createQuery("SELECT f FROM Formation f", Formation.class);
//         List<Formation> formations = query.getResultList();
//         formationBox.removeAllItems();
//         for (Formation formation : formations) {
//             formationBox.addItem(formation);
//         }
//     }

//     private void validerInscription() {
//         if (prenomField.getText().trim().isEmpty() || 
//             nomField.getText().trim().isEmpty() || 
//             (!hommeButton.isSelected() && !femmeButton.isSelected()) ||
//             formationBox.getSelectedItem() == null) {
//             JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         String prenom = prenomField.getText().trim();
//         String nom = nomField.getText().trim();
//         Sexe sexe = hommeButton.isSelected() ? Sexe.MASCULIN : Sexe.FEMININ;
//         Formation formation = (Formation) formationBox.getSelectedItem();

//         String email = genererEmail(prenom, nom);
//         String password = genererMotDePasse();

//         Etudiant etudiant = new Etudiant();
//         etudiant.setPrenom(prenom);
//         etudiant.setNom(nom);
//         etudiant.setSexe(sexe);
//         etudiant.setFormation(formation);
//         etudiant.setEmail(email);
//         etudiant.setPassword(password);
//         etudiant.setDateNaissance(LocalDate.now()); // TODO! Need to correct

//         try {
//             em.getTransaction().begin();
//             em.persist(etudiant);
//             em.getTransaction().commit();

//             JOptionPane.showMessageDialog(this,
//                 "Inscription réussie !\nEmail : " + email + "\nMot de passe : " + password,
//                 "Succès", JOptionPane.INFORMATION_MESSAGE);
//             dispose();

//         } catch (Exception e) {
//             e.printStackTrace();
//             if (em.getTransaction().isActive()) {
//                 em.getTransaction().rollback();
//             }
//             JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription", "Erreur", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private String genererEmail(String prenom, String nom) {
//         String emailBase = (prenom + "." + nom).toLowerCase().replaceAll("\\s+", "");
//         String email = emailBase + "@etu.uasz.sn";
        
//         int suffix = 1;
//         while (emailExiste(email)) {
//             email = emailBase + suffix + "@etu.uasz.sn";
//             suffix++;
//         }
//         return email;
//     }

//     private boolean emailExiste(String email) {
//         TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Etudiant e WHERE e.email = :email", Long.class);
//         query.setParameter("email", email);
//         return query.getSingleResult() > 0;
//     }

//     private String genererMotDePasse() {
//         Random random = new Random();
//         int password = 100000 + random.nextInt(900000); // Génère un entier à 6 chiffres
//         return String.valueOf(password);
//     }

//     @Override
//     public void dispose() {
//         if (em != null && em.isOpen()) em.close();
//         if (emf != null && emf.isOpen()) emf.close();
//         super.dispose();
//     }
// }



package sn.uasz.m1.projet.gui.etudiant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.Sexe;

public class FormulaireEtudiant extends JFrame {
    // Constantes pour le design
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color SECONDARY_COLOR = new Color(230, 240, 250);
    private static final Color ACCENT_COLOR = new Color(255, 102, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int BUTTON_RADIUS = 10;
    
    // Composants du formulaire
    private final JTextField prenomField = new JTextField(20);
    private final JTextField nomField = new JTextField(20);
    private final JRadioButton hommeButton = new JRadioButton("Homme");
    private final JRadioButton femmeButton = new JRadioButton("Femme");
    private final ButtonGroup sexeGroup = new ButtonGroup();
    private final JComboBox<FormationWrapper> formationBox = new JComboBox<>();
    
    // JPA
    private final EntityManagerFactory emf;
    private final EntityManager em;

    /**
     * Classe wrapper pour afficher seulement le nom de la formation dans la ComboBox
     */
    private static class FormationWrapper {
        private final Formation formation;
        
        public FormationWrapper(Formation formation) {
            this.formation = formation;
        }
        
        public Formation getFormation() {
            return formation;
        }
        
        @Override
        public String toString() {
            return formation.getNom();
        }
    }

    public FormulaireEtudiant() {
        // Initialisation JPA
        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();
        
        // Configuration de la fenêtre
        setupWindow();
        
        // Construction de l'interface
        buildUI();
    }
    
    /**
     * Configure les paramètres de base de la fenêtre
     */
    private void setupWindow() {
        setTitle("Inscription Étudiant - UASZ");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }
    
    /**
     * Construction de l'interface utilisateur complète
     */
    private void buildUI() {
        // En-tête
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Panneau principal avec le formulaire
        add(createFormPanel(), BorderLayout.CENTER);
        
        // Panneau des boutons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * Crée le panneau d'en-tête avec le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Icône pour l'en-tête
        FontIcon userIcon = FontIcon.of(MaterialDesign.MDI_ACCOUNT_PLUS, 36, Color.WHITE);
        JLabel iconLabel = new JLabel(userIcon);
        
        // Titre
        JLabel titleLabel = new JLabel("Inscription Nouvel Étudiant");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau principal du formulaire
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;
        
        // Styliser les composants
        styleComponents();
        
        // Prénom
        addFormField(formPanel, gbc, "Prénom :", prenomField, 0, MaterialDesign.MDI_ACCOUNT);
        
        // Nom
        addFormField(formPanel, gbc, "Nom :", nomField, 1, MaterialDesign.MDI_ACCOUNT_BOX);

        // Sexe
        JPanel sexePanel = createRadioButtonPanel();
        addFormField(formPanel, gbc, "Sexe :", sexePanel, 2, MaterialDesign.MDI_HUMAN_MALE_FEMALE);

        // Formation
        addFormField(formPanel, gbc, "Formation :", formationBox, 3, MaterialDesign.MDI_SCHOOL);
        chargerFormations();
        
        return formPanel;
    }
    
    /**
     * Crée le panneau des boutons d'action
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        // Bouton Annuler
        JButton annulerButton = createStyledButton("Annuler", MaterialDesign.MDI_CLOSE, Color.GRAY);
        annulerButton.addActionListener(e -> dispose());
        
        // Bouton Valider
        JButton validerButton = createStyledButton("Valider", MaterialDesign.MDI_CHECK, ACCENT_COLOR);
        validerButton.addActionListener(e -> validerInscription());
        
        buttonPanel.add(annulerButton);
        buttonPanel.add(validerButton);
        
        return buttonPanel;
    }
    
    /**
     * Crée un bouton stylisé avec une icône
     */
    private JButton createStyledButton(String text, MaterialDesign icon, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setIcon(FontIcon.of(icon, 20, Color.WHITE));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
    
    /**
     * Applique les styles aux composants du formulaire
     */
    private void styleComponents() {
        // Styliser les champs texte
        styleTextField(prenomField);
        styleTextField(nomField);
        
        // Styliser les boutons radio
        hommeButton.setFont(FIELD_FONT);
        femmeButton.setFont(FIELD_FONT);
        hommeButton.setBackground(Color.WHITE);
        femmeButton.setBackground(Color.WHITE);
        
        // Styliser la liste déroulante
        formationBox.setFont(FIELD_FONT);
        formationBox.setBackground(Color.WHITE);
    }
    
    /**
     * Applique un style à un champ texte
     */
    private void styleTextField(JTextField textField) {
        textField.setFont(FIELD_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
    
    /**
     * Crée un panneau pour les boutons radio de sexe
     */
    private JPanel createRadioButtonPanel() {
        JPanel sexePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        sexePanel.setBackground(Color.WHITE);
        
        sexeGroup.add(hommeButton);
        sexeGroup.add(femmeButton);
        
        // Ajouter des icônes aux boutons radio
        hommeButton.setIcon(FontIcon.of(MaterialDesign.MDI_HUMAN_MALE, 20, PRIMARY_COLOR));
        femmeButton.setIcon(FontIcon.of(MaterialDesign.MDI_HUMAN_FEMALE, 20, PRIMARY_COLOR));
        
        sexePanel.add(hommeButton);
        sexePanel.add(femmeButton);
        
        return sexePanel;
    }
    
    /**
     * Ajoute un champ au formulaire avec une icône
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, 
                             Component field, int row, MaterialDesign iconType) {
        // Créer un label avec icône
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setIcon(FontIcon.of(iconType, 20, PRIMARY_COLOR));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Ajouter le label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(label, gbc);
        
        // Ajouter le champ
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(field, gbc);
    }

    /**
     * Charge les formations depuis la base de données
     */
    private void chargerFormations() {
        TypedQuery<Formation> query = em.createQuery("SELECT f FROM Formation f", Formation.class);
        List<Formation> formations = query.getResultList();
        
        formationBox.removeAllItems();
        for (Formation formation : formations) {
            formationBox.addItem(new FormationWrapper(formation));
        }
    }

    /**
     * Valide l'inscription et enregistre l'étudiant
     */
    private void validerInscription() {
        // Validation des champs
        if (prenomField.getText().trim().isEmpty() || 
            nomField.getText().trim().isEmpty() || 
            (!hommeButton.isSelected() && !femmeButton.isSelected()) ||
            formationBox.getSelectedItem() == null) {
            
            showErrorDialog("Veuillez remplir tous les champs obligatoires");
            return;
        }

        // Récupération des données
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        Sexe sexe = hommeButton.isSelected() ? Sexe.MASCULIN : Sexe.FEMININ;
        Formation formation = ((FormationWrapper)formationBox.getSelectedItem()).getFormation();

        // Génération des identifiants
        String email = genererEmail(prenom, nom);
        String password = genererMotDePasse();

        // Création de l'étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setPrenom(prenom);
        etudiant.setNom(nom);
        etudiant.setSexe(sexe);
        etudiant.setFormation(formation);
        etudiant.setEmail(email);
        etudiant.setPassword(password);
        etudiant.setDateNaissance(LocalDate.now()); // TODO: Ajouter un DatePicker

        // Persistance
        try {
            em.getTransaction().begin();
            em.persist(etudiant);
            em.getTransaction().commit();

            showSuccessDialog("Inscription réussie !\n\nEmail : " + email + "\nMot de passe : " + password);
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showErrorDialog("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    /**
     * Génère un email étudiant unique
     */
    private String genererEmail(String prenom, String nom) {
        String baseEmail = (prenom + "." + nom).toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[àâä]", "a")
                .replaceAll("[ùûü]", "u")
                .replaceAll("[ôö]", "o")
                .replaceAll("[îï]", "i")
                .replaceAll("[ç]", "c");
                
        String email = baseEmail + "@etu.uasz.sn";
        
        int suffix = 1;
        while (emailExiste(email)) {
            email = baseEmail + suffix + "@etu.uasz.sn";
            suffix++;
        }
        return email;
    }

    /**
     * Vérifie si un email existe déjà dans la base de données
     */
    private boolean emailExiste(String email) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Etudiant e WHERE e.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    /**
     * Génère un mot de passe à 6 chiffres
     */
    private String genererMotDePasse() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this, 
                message,
                "Erreur", 
                JOptionPane.ERROR_MESSAGE, 
                FontIcon.of(MaterialDesign.MDI_ALERT_CIRCLE, 24, Color.RED)
        );
    }
    
    /**
     * Affiche une boîte de dialogue de succès
     */
    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(
                this, 
                message,
                "Inscription Réussie", 
                JOptionPane.INFORMATION_MESSAGE, 
                FontIcon.of(MaterialDesign.MDI_CHECK_CIRCLE, 24, new Color(0, 150, 0))
        );
    }

    /**
     * Libération des ressources à la fermeture
     */
    @Override
    public void dispose() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
        super.dispose();
    }
}