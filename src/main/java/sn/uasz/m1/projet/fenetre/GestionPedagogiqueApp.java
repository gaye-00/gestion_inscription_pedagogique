package sn.uasz.m1.projet.fenetre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GestionPedagogiqueApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Button.focus", Color.WHITE); // Suppression du contour bleu sur les boutons
            new LoginFrame();
        });
    }
}

// Classes modèles pour stocker les données
class Formation {
    private String code;
    private String intitule;
    private String niveau;
    private String description;

    public Formation(String code, String intitule, String niveau, String description) {
        this.code = code;
        this.intitule = intitule;
        this.niveau = niveau;
        this.description = description;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getIntitule() {
        return intitule;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getDescription() {
        return description;
    }
}

class UE {
    private String code;
    private String intitule;
    private int credits;
    private String description;
    private Formation formation; // Added formation reference

    public UE(String code, String intitule, int credits, String description, Formation formation) {
        this.code = code;
        this.intitule = intitule;
        this.credits = credits;
        this.description = description;
        this.formation = formation;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getIntitule() {
        return intitule;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    public Formation getFormation() {
        return formation;
    }
}

class Etudiant {
    private String ine;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;

    public Etudiant(String ine, String nom, String prenom, String dateNaissance, String email) {
        this.ine = ine;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
    }

    // Getters
    public String getIne() {
        return ine;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEmail() {
        return email;
    }
}

// Stockage temporaire des données (à remplacer par une base de données)
class DataStore {
    private static ArrayList<Formation> formations = new ArrayList<>();
    private static ArrayList<UE> ues = new ArrayList<>();
    private static ArrayList<Etudiant> etudiants = new ArrayList<>();

    // Initialiser avec quelques données
    static {
        Formation f = new Formation("INFO-L1", "Licence 1 Informatique", "Licence",
                "Première année de licence en informatique");
        formations.add(f);
        formations.add(new Formation("MATH-L1", "Licence 1 Mathématiques", "Licence",
                "Première année de licence en mathématiques"));

        ues.add(new UE("INF1001", "Algorithmique", 6, "Introduction aux algorithmes", f));
        ues.add(new UE("INF1002", "Programmation C", 6, "Bases de la programmation en C", f));

        etudiants.add(new Etudiant("E20230001", "Diop", "Amadou", "15/05/2002", "amadou.diop@uasz.edu.sn"));
        etudiants.add(new Etudiant("E20230002", "Fall", "Fatou", "22/09/2001", "fatou.fall@uasz.edu.sn"));
    }

    // Méthodes pour ajouter des éléments
    public static void addFormation(Formation formation) {
        formations.add(formation);
    }

    public static void addUE(UE ue) {
        ues.add(ue);
    }

    public static void addEtudiant(Etudiant etudiant) {
        etudiants.add(etudiant);
    }

    // Méthodes pour récupérer des données
    public static ArrayList<Formation> getFormations() {
        return formations;
    }

    public static ArrayList<UE> getUEs() {
        return ues;
    }

    public static ArrayList<Etudiant> getEtudiants() {
        return etudiants;
    }

    // Méthodes pour compter les éléments
    public static int getFormationCount() {
        return formations.size();
    }

    public static int getUECount() {
        return ues.size();
    }

    public static int getEtudiantCount() {
        return etudiants.size();
    }
}

class User {
    private String username;
    private String role;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Connexion - Gestion Pédagogique");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("Système de Gestion Pédagogique", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginButton = new JButton("Connexion");
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setFocusPainted(false);

        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Actions
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(username, password)) {
                User user = new User(username, "Administrateur"); // Role would normally come from database
                dispose();
                new MainFrame(user);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Identifiants incorrects. Veuillez réessayer.",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
        });

        add(mainPanel);
        setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        // Simplified authentication - in real app, this would check against database
        return username.equals("admin") && password.equals("admin");
    }
}

class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private User currentUser;

    // Constante pour le nom du panel dashboard
    private static final String DASHBOARD_PANEL = "Dashboard";
    private static final String NOUVELLE_FORMATION_PANEL = "Nouvelle Formation";
    private static final String NOUVELLE_UE_PANEL = "Nouvelle UE";
    private static final String NOUVEL_ETUDIANT_PANEL = "Nouvel Étudiant";

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("Gestion Pédagogique");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panneau supérieur pour les informations utilisateur
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panneau latéral
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(9, 1, 5, 5));
        sidePanel.setBackground(new Color(45, 52, 54)); // Couleur foncée

        String[] buttons = {
                "Dashboard", "Nouvelle Formation", "Nouvelle UE", "Nouvel Étudiant",
                "Gérer Formations", "Gérer UEs", "Gérer Étudiants", "Gérer Groupes", "Liste Inscriptions"
        };

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Créer le dashboard
        JPanel dashboardPanel = createDashboardPanel();
        contentPanel.add(dashboardPanel, DASHBOARD_PANEL);

        // Créer les panneaux de formulaires
        JPanel nouvelleFormationPanel = createNouvelleFormationPanel();
        contentPanel.add(nouvelleFormationPanel, NOUVELLE_FORMATION_PANEL);

        JPanel nouvelleUEPanel = createNouvelleUEPanel();
        contentPanel.add(nouvelleUEPanel, NOUVELLE_UE_PANEL);

        JPanel nouvelEtudiantPanel = createNouvelEtudiantPanel();
        contentPanel.add(nouvelEtudiantPanel, NOUVEL_ETUDIANT_PANEL);

        // Ajouter les autres panneaux
        for (String btnText : buttons) {
            JButton button = createSidebarButton(btnText);
            sidePanel.add(button);

            // Si ce n'est pas un panneau déjà créé, créer un nouveau panel pour cet élément
            if (!btnText.equals(DASHBOARD_PANEL) &&
                    !btnText.equals(NOUVELLE_FORMATION_PANEL) &&
                    !btnText.equals(NOUVELLE_UE_PANEL) &&
                    !btnText.equals(NOUVEL_ETUDIANT_PANEL)) {
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                panel.add(new JLabel("Page: " + btnText));
                contentPanel.add(panel, btnText);
            }

            button.addActionListener(e -> cardLayout.show(contentPanel, btnText));
        }

        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Afficher le dashboard au démarrage
        cardLayout.show(contentPanel, DASHBOARD_PANEL);

        setVisible(true);
    }

    private JPanel createNouvelleFormationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Nouvelle Formation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "Code de formation:", "Intitulé:", "Niveau:", "Description:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);
        String[] niveaux = { "Licence 1", "Licence 2", "Licence 3", "Master 1", "Master 2", "Doctorat" };
        JComboBox<String> niveauCombo = new JComboBox<>(niveaux);
        JTextArea descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        JComponent[] fields = { codeField, intituleField, niveauCombo, scrollPane };

        // Ajouter les champs au formulaire
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.9;
            formPanel.add(fields[i], gbc);
        }

        panel.add(formPanel, BorderLayout.CENTER);

        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 60));

        JButton annulerButton = new JButton("Annuler");
        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.setBackground(new Color(46, 204, 113));
        enregistrerButton.setForeground(Color.WHITE);

        buttonPanel.add(annulerButton);
        buttonPanel.add(enregistrerButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action des boutons
        enregistrerButton.addActionListener(e -> {
            if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Formation nouvelleFormation = new Formation(
                    codeField.getText(),
                    intituleField.getText(),
                    niveauCombo.getSelectedItem().toString(),
                    descriptionArea.getText());

            DataStore.addFormation(nouvelleFormation);

            JOptionPane.showMessageDialog(this,
                    "Formation créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs
            codeField.setText("");
            intituleField.setText("");
            niveauCombo.setSelectedIndex(0);
            descriptionArea.setText("");

            // Revenir au dashboard
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

    private JPanel createNouvelleUEPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Nouvelle Unité d'Enseignement", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Description:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        // Remplir le modèle avec les formations disponibles
        for (Formation formation : DataStore.getFormations()) {
            formationModel.addElement(formation);
        }
        JComboBox<Formation> formationComboBox = new JComboBox<>(formationModel);
        // Personnaliser l'affichage des formations dans la liste déroulante
        formationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Formation) {
                    Formation formation = (Formation) value;
                    setText(formation.getCode() + " - " + formation.getIntitule());
                }
                return this;
            }
        });

        SpinnerModel creditModel = new SpinnerNumberModel(3, 1, 10, 1);
        JSpinner creditSpinner = new JSpinner(creditModel);
        JTextArea descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        JComponent[] fields = { codeField, intituleField, formationComboBox, creditSpinner, scrollPane };

        // Ajouter les champs au formulaire
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            formPanel.add(label, gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.9;
            formPanel.add(fields[i], gbc);
        }
        panel.add(formPanel, BorderLayout.CENTER);

        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 60));
        JButton annulerButton = new JButton("Annuler");
        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.setBackground(new Color(46, 204, 113));
        enregistrerButton.setForeground(Color.WHITE);
        buttonPanel.add(annulerButton);
        buttonPanel.add(enregistrerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action des boutons
        enregistrerButton.addActionListener(e -> {
            if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()
                    || formationComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Formation selectedFormation = (Formation) formationComboBox.getSelectedItem();
            UE nouvelleUE = new UE(
                    codeField.getText(),
                    intituleField.getText(),
                    (int) creditSpinner.getValue(),
                    descriptionArea.getText(),
                    selectedFormation);

            DataStore.addUE(nouvelleUE);
            JOptionPane.showMessageDialog(this,
                    "Unité d'enseignement créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs
            codeField.setText("");
            intituleField.setText("");
            formationComboBox.setSelectedIndex(0); // Sélectionner la première formation
            creditSpinner.setValue(3);
            descriptionArea.setText("");

            // Revenir au dashboard
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

    // private JPanel createNouvelleUEPanel() {
    // JPanel panel = new JPanel(new BorderLayout());
    // panel.setBackground(Color.WHITE);

    // // Titre
    // JLabel titleLabel = new JLabel("Nouvelle Unité d'Enseignement",
    // JLabel.CENTER);
    // titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    // titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    // panel.add(titleLabel, BorderLayout.NORTH);

    // // Formulaire
    // JPanel formPanel = new JPanel(new GridBagLayout());
    // formPanel.setBackground(Color.WHITE);
    // formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

    // GridBagConstraints gbc = new GridBagConstraints();
    // gbc.fill = GridBagConstraints.HORIZONTAL;
    // gbc.insets = new Insets(10, 10, 10, 10);

    // // Champs du formulaire
    // String[] labels = {"Code UE:", "Intitulé:", "Crédits:", "Description:"};
    // JTextField codeField = new JTextField(20);
    // JTextField intituleField = new JTextField(20);
    // SpinnerModel creditModel = new SpinnerNumberModel(3, 1, 10, 1);
    // JSpinner creditSpinner = new JSpinner(creditModel);
    // JTextArea descriptionArea = new JTextArea(5, 20);
    // JScrollPane scrollPane = new JScrollPane(descriptionArea);

    // JComponent[] fields = {codeField, intituleField, creditSpinner, scrollPane};

    // // Ajouter les champs au formulaire
    // for (int i = 0; i < labels.length; i++) {
    // JLabel label = new JLabel(labels[i]);
    // gbc.gridx = 0;
    // gbc.gridy = i;
    // gbc.gridwidth = 1;
    // gbc.weightx = 0.1;
    // formPanel.add(label, gbc);

    // gbc.gridx = 1;
    // gbc.weightx = 0.9;
    // formPanel.add(fields[i], gbc);
    // }

    // panel.add(formPanel, BorderLayout.CENTER);

    // // Boutons d'action
    // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    // buttonPanel.setBackground(Color.WHITE);
    // buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 60));

    // JButton annulerButton = new JButton("Annuler");
    // JButton enregistrerButton = new JButton("Enregistrer");
    // enregistrerButton.setBackground(new Color(46, 204, 113));
    // enregistrerButton.setForeground(Color.WHITE);

    // buttonPanel.add(annulerButton);
    // buttonPanel.add(enregistrerButton);

    // panel.add(buttonPanel, BorderLayout.SOUTH);

    // // Action des boutons
    // enregistrerButton.addActionListener(e -> {
    // if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()) {
    // JOptionPane.showMessageDialog(this,
    // "Veuillez remplir tous les champs obligatoires.",
    // "Erreur", JOptionPane.ERROR_MESSAGE);
    // return;
    // }

    // UE nouvelleUE = new UE(
    // codeField.getText(),
    // intituleField.getText(),
    // (int) creditSpinner.getValue(),
    // descriptionArea.getText()
    // );

    // DataStore.addUE(nouvelleUE);

    // JOptionPane.showMessageDialog(this,
    // "Unité d'enseignement créée avec succès!",
    // "Succès", JOptionPane.INFORMATION_MESSAGE);

    // // Réinitialiser les champs
    // codeField.setText("");
    // intituleField.setText("");
    // creditSpinner.setValue(3);
    // descriptionArea.setText("");

    // // Revenir au dashboard
    // cardLayout.show(contentPanel, DASHBOARD_PANEL);
    // });

    // annulerButton.addActionListener(e -> {
    // cardLayout.show(contentPanel, DASHBOARD_PANEL);
    // });

    // return panel;
    // }

    private JPanel createNouvelEtudiantPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Nouvel Étudiant", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "INE:", "Nom:", "Prénom:", "Date de naissance:", "Email:" };
        JTextField ineField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField dateField = new JTextField("JJ/MM/AAAA", 20);
        JTextField emailField = new JTextField(20);

        JComponent[] fields = { ineField, nomField, prenomField, dateField, emailField };

        // Ajouter les champs au formulaire
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.9;
            formPanel.add(fields[i], gbc);
        }

        panel.add(formPanel, BorderLayout.CENTER);

        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 60));

        JButton annulerButton = new JButton("Annuler");
        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.setBackground(new Color(46, 204, 113));
        enregistrerButton.setForeground(Color.WHITE);

        buttonPanel.add(annulerButton);
        buttonPanel.add(enregistrerButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton de réinitialisation de date
        dateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dateField.getText().equals("JJ/MM/AAAA")) {
                    dateField.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dateField.getText().isEmpty()) {
                    dateField.setText("JJ/MM/AAAA");
                }
            }
        });

        // Action des boutons
        enregistrerButton.addActionListener(e -> {
            if (ineField.getText().isEmpty() || nomField.getText().isEmpty() ||
                    prenomField.getText().isEmpty() || dateField.getText().equals("JJ/MM/AAAA") ||
                    emailField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Etudiant nouvelEtudiant = new Etudiant(
                    ineField.getText(),
                    nomField.getText(),
                    prenomField.getText(),
                    dateField.getText(),
                    emailField.getText());

            DataStore.addEtudiant(nouvelEtudiant);

            JOptionPane.showMessageDialog(this,
                    "Étudiant ajouté avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs
            ineField.setText("");
            nomField.setText("");
            prenomField.setText("");
            dateField.setText("JJ/MM/AAAA");
            emailField.setText("");

            // Revenir au dashboard
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);

        // Titre du dashboard
        JLabel dashboardTitle = new JLabel("Tableau de Bord", JLabel.CENTER);
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardTitle.setForeground(new Color(52, 73, 94));
        dashboardTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        dashboardPanel.add(dashboardTitle, BorderLayout.NORTH);

        // Conteneur principal pour les cartes statistiques
        JPanel statsContainer = new JPanel(new GridLayout(2, 2, 20, 20));
        statsContainer.setBackground(Color.WHITE);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Données pour le dashboard
        String[][] statData = {
                { "Formations", String.valueOf(DataStore.getFormationCount()),
                        "Nombre total de formations disponibles" },
                { "Étudiants", String.valueOf(DataStore.getEtudiantCount()), "Nombre total d'étudiants inscrits" },
                { "Unités d'Enseignement", String.valueOf(DataStore.getUECount()), "Nombre total d'UEs définies" },
                { "Inscriptions", "0", "Nombre d'inscriptions validées" }
        };

        // Couleurs pour les différentes cartes
        Color[] cardColors = {
                new Color(41, 128, 185), // Bleu
                new Color(39, 174, 96), // Vert
                new Color(142, 68, 173), // Violet
                new Color(230, 126, 34) // Orange
        };

        // Créer les cartes de statistiques
        for (int i = 0; i < statData.length; i++) {
            JPanel statCard = createStatCard(statData[i][0], statData[i][1], statData[i][2], cardColors[i]);
            statsContainer.add(statCard);
        }

        dashboardPanel.add(statsContainer, BorderLayout.CENTER);

        // Section du bas avec des boutons d'action rapide
        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        quickActions.setBackground(Color.WHITE);
        quickActions.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] actionBtns = { "Nouvelle Formation", "Nouvelle UE", "Nouvel Étudiant", "Nouvelle Inscription" };

        for (String action : actionBtns) {
            JButton actionBtn = new JButton(action);
            actionBtn.setBackground(new Color(52, 152, 219));
            actionBtn.setForeground(Color.WHITE);
            actionBtn.setFocusPainted(false);

            actionBtn.addActionListener(e -> {
                cardLayout.show(contentPanel, action);
            });

            quickActions.add(actionBtn);
        }

        dashboardPanel.add(quickActions, BorderLayout.SOUTH);

        return dashboardPanel;
    }

    private JPanel createStatCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Titre de la carte
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);

        // Valeur (grand nombre)
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(new Color(52, 73, 94));

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));

        // Panneau supérieur pour titre et valeur
        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setBackground(Color.WHITE);
        topInfo.add(titleLabel, BorderLayout.NORTH);
        topInfo.add(valueLabel, BorderLayout.CENTER);

        card.add(topInfo, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 152, 219));
        panel.setPreferredSize(new Dimension(getWidth(), 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Logo ou titre de l'application
        JLabel titleLabel = new JLabel("Système de Gestion Pédagogique");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.WEST);

        // Informations utilisateur
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel userIcon = new JLabel("\uD83D\uDC64"); // Emoji utilisateur
        userIcon.setForeground(Color.WHITE);
        userIcon.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel userInfoLabel = new JLabel(currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(41, 128, 185));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        userPanel.add(userIcon);
        userPanel.add(userInfoLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutButton);

        panel.add(userPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(99, 110, 114));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(178, 190, 195));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(99, 110, 114));
            }
        });
        return button;
    }
}