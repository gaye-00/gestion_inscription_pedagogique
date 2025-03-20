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
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
import org.mindrot.jbcrypt.BCrypt;

import com.github.lgooddatepicker.components.DatePicker;

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
    private final JTextField adresseField = new JTextField(20);
    private final DatePicker dateNaissancePicker = new DatePicker();
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

        // Adresse
        addFormField(formPanel, gbc, "Adresse :", adresseField, 2, MaterialDesign.MDI_HOME);

        // Date de naissance
        addFormField(formPanel, gbc, "Date de Naissance :", dateNaissancePicker, 3, MaterialDesign.MDI_CALENDAR);

        // Sexe
        JPanel sexePanel = createRadioButtonPanel();
        addFormField(formPanel, gbc, "Sexe :", sexePanel, 4, MaterialDesign.MDI_HUMAN_MALE_FEMALE);

        // Formation
        addFormField(formPanel, gbc, "Formation :", formationBox, 5, MaterialDesign.MDI_SCHOOL);
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
        styleTextField(adresseField);
        
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
        
        // Configurer les boutons radio
        hommeButton.setSelected(true);  // Sélectionner homme par défaut
        
        // Ajouter les boutons au groupe
        sexeGroup.add(hommeButton);
        sexeGroup.add(femmeButton);
        
        // Ajouter des icônes aux boutons radio
        hommeButton.setIcon(FontIcon.of(MaterialDesign.MDI_HUMAN_MALE, 20, PRIMARY_COLOR));
        femmeButton.setIcon(FontIcon.of(MaterialDesign.MDI_HUMAN_FEMALE, 20, PRIMARY_COLOR));
        
        // Ajouter les boutons au panneau
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
            adresseField.getText().trim().isEmpty() ||
            dateNaissancePicker.getDate() == null ||
            (!hommeButton.isSelected() && !femmeButton.isSelected()) ||
            formationBox.getSelectedItem() == null) {
            
            showErrorDialog("Veuillez remplir tous les champs obligatoires");
            return;
        }

        // Récupération des données
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        String adresse = adresseField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getDate();
        Sexe sexe = hommeButton.isSelected() ? Sexe.MASCULIN : Sexe.FEMININ;
        Formation formation = ((FormationWrapper)formationBox.getSelectedItem()).getFormation();

        // Génération des identifiants
        String email = genererEmail(prenom, nom);
        // String password = genererMotDePasse();
        String plainPassword = genererMotDePasse(); // Stockez le mot de passe en clair pour l'affichage
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt()); // Cryptez le mot de passe


        // Création de l'étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setPrenom(prenom);
        etudiant.setNom(nom);
        etudiant.setSexe(sexe);
        etudiant.setFormation(formation);
        etudiant.setEmail(email);
        // etudiant.setPassword(password);
        etudiant.setPassword(hashedPassword); // Utilisez le mot de passe crypté
        etudiant.setAdresse(adresse);
        etudiant.setDateNaissance(dateNaissance);
        etudiant.setIne(genererIne());
        etudiant.setUes(formation.getUes() != null ? formation.getUes().stream().filter(fnctn -> fnctn.isObligatoire()).collect(Collectors.toSet()) : Collections.emptySet());

        // Persistance
        try {
            em.getTransaction().begin();
            em.persist(etudiant);
            em.getTransaction().commit();

            // showSuccessDialogWithCopy(email, password);
            // Utilisez le mot de passe en clair pour l'affichage
            showSuccessDialogWithCopy(email, plainPassword);
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
     * Affiche une boîte de dialogue de succès avec des boutons de copie
     */
    private void showSuccessDialogWithCopy(String email, String password) {
        // Création du panneau personnalisé
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Message de succès
        JLabel successLabel = new JLabel("Inscription réussie !");
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 3;
        gbc.gridy = 0;
        panel.add(successLabel, gbc);

        // Email
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Email :"), gbc);
        
        JTextField emailField = new JTextField(email);
        emailField.setEditable(false);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        JButton copyEmailButton = new JButton(FontIcon.of(MaterialDesign.MDI_CONTENT_COPY, 16));
        copyEmailButton.setToolTipText("Copier l'email");
        copyEmailButton.addActionListener(e -> {
            emailField.selectAll();
            emailField.copy();
        });
        gbc.gridx = 2;
        panel.add(copyEmailButton, gbc);

        // Mot de passe
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Mot de passe :"), gbc);
        
        JTextField passwordField = new JTextField(password);
        passwordField.setEditable(false);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        JButton copyPasswordButton = new JButton(FontIcon.of(MaterialDesign.MDI_CONTENT_COPY, 16));
        copyPasswordButton.setToolTipText("Copier le mot de passe");
        copyPasswordButton.addActionListener(e -> {
            passwordField.selectAll();
            passwordField.copy();
        });
        gbc.gridx = 2;
        panel.add(copyPasswordButton, gbc);

        // Affichage du dialogue
        JOptionPane.showMessageDialog(
            this,
            panel,
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

    public static String genererIne() {
        int annee = Year.now().getValue(); // Récupère l'année actuelle
        int randomNumbers = new Random().nextInt(100000); // Génère un nombre aléatoire entre 0 et 99999
        return String.format("%d%05d", annee, randomNumbers); // Assure que les 5 chiffres sont toujours présents
    }
}