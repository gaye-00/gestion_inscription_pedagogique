package sn.uasz.m1.projet.fenetre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.w3c.dom.events.DocumentEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

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
    private List<UE> ues = new ArrayList<>();

    public Formation(String code, String intitule, String niveau, String description) {
        this.code = code;
        this.intitule = intitule;
        this.niveau = niveau;
        this.description = description;
        ues = new ArrayList<UE>();
    }

    // Getters

    public void setCode(String code) {
        this.code = code;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UE> getUes() {
        return ues;
    }

    public void setUes(List<UE> ues) {
        this.ues = ues;
    }

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
    private boolean obligatoire = false;

    public UE(String code, String intitule, int credits, String description, Formation formation,
            boolean estObligatoire) {
        this.code = code;
        this.intitule = intitule;
        this.credits = credits;
        this.description = description;
        this.formation = formation;
        this.obligatoire = estObligatoire;
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
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

        ues.add(new UE("INF1001", "Algorithmique", 6, "Introduction aux algorithmes", f, false));
        ues.add(new UE("INF1002", "Programmation C", 6, "Bases de la programmation en C", f, false));

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

    public static void deleteUE(UE ue) {
        ues.remove(ue);
    }

    public static void updateUE(UE ue) {
        ues.remove(ue);
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

    // Dans la classe DataStore, ajoutez ces méthodes

    /**
     * Récupère toutes les UEs liées à une formation spécifique
     */
    public static ArrayList<UE> getUEsByFormation(Formation formation) {
        ArrayList<UE> result = new ArrayList<>();

        for (UE ue : ues) {
            // Vérifier si l'UE appartient à la formation spécifiée
            if (ue.getFormation() != null && ue.getFormation().getCode().equals(formation.getCode())) {
                result.add(ue);
            }
        }

        return result;
    }

    /**
     * Récupère tous les étudiants inscrits à une formation spécifique
     */
    public static ArrayList<Etudiant> getEtudiantsByFormation(Formation formation) {
        ArrayList<Etudiant> result = new ArrayList<>();

        // Supposons que la classe Inscription lie un étudiant à une formation
        // Si vous avez une liste d'inscriptions dans DataStore
        // for (Inscription inscription : inscriptions) {
        // if (inscription.getFormation().getCode().equals(formation.getCode())) {
        // result.add(inscription.getEtudiant());
        // }
        // }

        // Si vous n'avez pas de classe Inscription, mais que l'étudiant a une référence
        // à sa formation
        // Alors utilisez plutôt ce code:
        /*
         * for (Etudiant etudiant : etudiants) {
         * if (etudiant.getFormation() != null &&
         * etudiant.getFormation().getCode().equals(formation.getCode())) {
         * result.add(etudiant);
         * }
         * }
         */

        return result;
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
                "Dashboard", "Gérer Formations", "Liste Inscriptions","Gérer Étudiants", "Gérer Groupes", 
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

        JPanel gererFormationsPanel = createGererFormationsPanel();
        contentPanel.add(gererFormationsPanel, "Gérer Formations");
        // Ajouter les autres panneaux
        for (String btnText : buttons) {
            JButton button = createSidebarButton(btnText);
            sidePanel.add(button);

            // Si ce n'est pas un panneau déjà créé, créer un nouveau panel pour cet élément
            if (!btnText.equals(DASHBOARD_PANEL) &&
                    !btnText.equals(NOUVELLE_FORMATION_PANEL) &&
                    !btnText.equals(NOUVELLE_UE_PANEL) &&
                    !btnText.equals("Gérer Formations") &&
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
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Description:", "Caractère de l’UE:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        for (Formation formation : DataStore.getFormations()) {
            formationModel.addElement(formation);
        }
        JComboBox<Formation> formationComboBox = new JComboBox<>(formationModel);
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

        // Boutons radio pour le caractère de l'UE
        JRadioButton obligatoireRadio = new JRadioButton("Obligatoire", true);
        JRadioButton optionnelRadio = new JRadioButton("Optionnel");
        ButtonGroup caractereGroup = new ButtonGroup();
        caractereGroup.add(obligatoireRadio);
        caractereGroup.add(optionnelRadio);
        JPanel caracterePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        caracterePanel.add(obligatoireRadio);
        caracterePanel.add(optionnelRadio);

        JComponent[] fields = { codeField, intituleField, formationComboBox, creditSpinner, scrollPane,
                caracterePanel };

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
            boolean estObligatoire = obligatoireRadio.isSelected();

            UE nouvelleUE = new UE(
                    codeField.getText(),
                    intituleField.getText(),
                    (int) creditSpinner.getValue(),
                    descriptionArea.getText(),
                    selectedFormation,
                    estObligatoire);

            DataStore.addUE(nouvelleUE);
            JOptionPane.showMessageDialog(this,
                    "Unité d'enseignement créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs
            codeField.setText("");
            intituleField.setText("");
            formationComboBox.setSelectedIndex(0);
            creditSpinner.setValue(3);
            descriptionArea.setText("");
            obligatoireRadio.setSelected(true);

            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

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

    private JPanel createGererFormationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gérer les Formations", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panneau central avec tableau et formulaire
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Modèle de table pour les formations
        String[] columnNames = { "Code", "Intitulé", "Niveau", "Description" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre les cellules non modifiables
            }
        };

        // Créer le tableau
        JTable formationsTable = new JTable(tableModel);
        formationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        formationsTable.getTableHeader().setReorderingAllowed(false);

        // Ajouter un TableRowSorter pour le filtrage
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        formationsTable.setRowSorter(sorter);

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Rechercher:");
        JTextField searchField = new JTextField(20);
        JComboBox<String> searchColumnCombo = new JComboBox<>(columnNames);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("dans"));
        searchPanel.add(searchColumnCombo);

        // Ajouter un ActionListener pour le champ de recherche
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            private void updateFilter() {
                String text = searchField.getText();
                int columnIndex = searchColumnCombo.getSelectedIndex();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
                }
            }
        });

        // Ajouter le panneau de recherche en haut du panneau central
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Panneau de défilement pour le tableau
        JScrollPane scrollPane = new JScrollPane(formationsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Le reste du code reste inchangé...
        // Panneau d'informations et de modifications
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0),
                BorderFactory.createTitledBorder("Détails de la formation")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs du formulaire de modification
        String[] labels = { "Code:", "Intitulé:", "Niveau:", "Description:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);
        String[] niveaux = { "Licence 1", "Licence 2", "Licence 3", "Master 1", "Master 2", "Doctorat" };
        JComboBox<String> niveauCombo = new JComboBox<>(niveaux);
        JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        JComponent[] fields = { codeField, intituleField, niveauCombo, descScrollPane };

        // Ajouter les champs au panneau d'informations
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            infoPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.9;
            infoPanel.add(fields[i], gbc);
        }

        // Boutons pour afficher les UE et étudiants inscrits
        JPanel relatedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        relatedPanel.setBackground(Color.WHITE);

        JButton listeUEsButton = new JButton("Gerer UEs");
        JButton listeEtudiantsButton = new JButton("Liste des Étudiants inscrits");

        listeUEsButton.setBackground(new Color(52, 152, 219));
        listeUEsButton.setForeground(Color.WHITE);
        listeEtudiantsButton.setBackground(new Color(52, 152, 219));
        listeEtudiantsButton.setForeground(Color.WHITE);

        relatedPanel.add(listeUEsButton);
        relatedPanel.add(listeEtudiantsButton);

        // État initial des boutons
        listeUEsButton.setEnabled(false);
        listeEtudiantsButton.setEnabled(false);

        // Ajouter le panneau de boutons sous le formulaire
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        infoPanel.add(relatedPanel, gbc);

        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Le reste du code reste le même
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));

        JButton refreshButton = new JButton("Rafraîchir");
        JButton saveButton = new JButton("Enregistrer");
        JButton deleteButton = new JButton("Supprimer");
        JButton addButton = new JButton("Nouvelle Formation");

        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(52, 152, 219));
        addButton.setForeground(Color.WHITE);

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // État initial des boutons
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Variables pour garder la trace de la formation sélectionnée
        final int[] selectedRow = { -1 };
        final Formation[] selectedFormation = { null };

        // Fonction pour charger les données
        Runnable loadData = () -> {
            tableModel.setRowCount(0); // Effacer les données existantes

            // Charger les formations depuis DataStore
            for (Formation formation : DataStore.getFormations()) {
                tableModel.addRow(new Object[] {
                        formation.getCode(),
                        formation.getIntitule(),
                        formation.getNiveau(),
                        formation.getDescription()
                });
            }

            // Réinitialiser la sélection
            selectedRow[0] = -1;
            selectedFormation[0] = null;
            saveButton.setEnabled(false);
            deleteButton.setEnabled(false);
            listeUEsButton.setEnabled(false);
            listeEtudiantsButton.setEnabled(false);

            // Effacer les champs
            codeField.setText("");
            intituleField.setText("");
            niveauCombo.setSelectedIndex(0);
            descriptionArea.setText("");

            // Réinitialiser le champ de recherche
            searchField.setText("");
        };

        // Le reste du code reste le même
        // Charger les données initiales
        loadData.run();

        // Écouteur de sélection pour le tableau
        formationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow[0] = formationsTable.getSelectedRow();

                if (selectedRow[0] >= 0) {
                    // Convertir l'index de la ligne du modèle de vue au modèle de données
                    int modelRow = formationsTable.convertRowIndexToModel(selectedRow[0]);
                    String code = tableModel.getValueAt(modelRow, 0).toString();

                    // Trouver la formation correspondante
                    for (Formation formation : DataStore.getFormations()) {
                        if (formation.getCode().equals(code)) {
                            selectedFormation[0] = formation;
                            break;
                        }
                    }

                    // Remplir les champs
                    if (selectedFormation[0] != null) {
                        codeField.setText(selectedFormation[0].getCode());
                        intituleField.setText(selectedFormation[0].getIntitule());
                        niveauCombo.setSelectedItem(selectedFormation[0].getNiveau());
                        descriptionArea.setText(selectedFormation[0].getDescription());

                        // Activer les boutons
                        saveButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        listeUEsButton.setEnabled(true);
                        listeEtudiantsButton.setEnabled(true);
                    }
                }
            }
        });

        // Actions des boutons (comme avant)
        refreshButton.addActionListener(e -> loadData.run());

        addButton.addActionListener(e -> cardLayout.show(contentPanel, NOUVELLE_FORMATION_PANEL));

        saveButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                // Vérifier que les champs requis sont remplis
                if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Veuillez remplir tous les champs obligatoires.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mettre à jour la formation
                Formation updatedFormation = new Formation(
                        codeField.getText(),
                        intituleField.getText(),
                        niveauCombo.getSelectedItem().toString(),
                        descriptionArea.getText());

                // Remplacer l'ancienne formation
                // DataStore.updateFormation(selectedFormation[0], updatedFormation);

                JOptionPane.showMessageDialog(this,
                        "Formation mise à jour avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Recharger les données
                loadData.run();
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer cette formation?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Supprimer la formation
                    // DataStore.removeFormation(selectedFormation[0]);

                    JOptionPane.showMessageDialog(this,
                            "Formation supprimée avec succès!",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);

                    // Recharger les données
                    loadData.run();
                }
            }
        });

        // Actions pour les nouveaux boutons
        listeUEsButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                showGestionUE(selectedFormation[0]);
                // showUEsList(selectedFormation[0]);
            }
        });

        listeEtudiantsButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                showEtudiantsList(selectedFormation[0]);
            }
        });

        return panel;
    }
    // private JPanel createGererFormationsPanel() {
    // JPanel panel = new JPanel(new BorderLayout());
    // panel.setBackground(Color.WHITE);

    // // Titre
    // JLabel titleLabel = new JLabel("Gérer les Formations", JLabel.CENTER);
    // titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    // titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    // panel.add(titleLabel, BorderLayout.NORTH);

    // // Panneau central avec tableau et formulaire
    // JPanel centerPanel = new JPanel(new BorderLayout());
    // centerPanel.setBackground(Color.WHITE);
    // centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    // // Modèle de table pour les formations
    // String[] columnNames = { "Code", "Intitulé", "Niveau", "Description" };
    // DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
    // @Override
    // public boolean isCellEditable(int row, int column) {
    // return false; // Rendre les cellules non modifiables
    // }
    // };

    // // Créer le tableau
    // JTable formationsTable = new JTable(tableModel);
    // formationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // formationsTable.getTableHeader().setReorderingAllowed(false);

    // // Panneau de défilement pour le tableau
    // JScrollPane scrollPane = new JScrollPane(formationsTable);
    // centerPanel.add(scrollPane, BorderLayout.CENTER);

    // // Panneau d'informations et de modifications
    // JPanel infoPanel = new JPanel(new GridBagLayout());
    // infoPanel.setBackground(Color.WHITE);
    // infoPanel.setBorder(BorderFactory.createCompoundBorder(
    // BorderFactory.createEmptyBorder(10, 0, 0, 0),
    // BorderFactory.createTitledBorder("Détails de la formation")));

    // GridBagConstraints gbc = new GridBagConstraints();
    // gbc.fill = GridBagConstraints.HORIZONTAL;
    // gbc.insets = new Insets(5, 5, 5, 5);

    // // Champs du formulaire de modification
    // String[] labels = { "Code:", "Intitulé:", "Niveau:", "Description:" };
    // JTextField codeField = new JTextField(20);
    // JTextField intituleField = new JTextField(20);
    // String[] niveaux = { "Licence 1", "Licence 2", "Licence 3", "Master 1",
    // "Master 2", "Doctorat" };
    // JComboBox<String> niveauCombo = new JComboBox<>(niveaux);
    // JTextArea descriptionArea = new JTextArea(3, 20);
    // JScrollPane descScrollPane = new JScrollPane(descriptionArea);

    // JComponent[] fields = { codeField, intituleField, niveauCombo, descScrollPane
    // };

    // // Ajouter les champs au panneau d'informations
    // for (int i = 0; i < labels.length; i++) {
    // JLabel label = new JLabel(labels[i]);
    // gbc.gridx = 0;
    // gbc.gridy = i;
    // gbc.gridwidth = 1;
    // gbc.weightx = 0.1;
    // infoPanel.add(label, gbc);

    // gbc.gridx = 1;
    // gbc.weightx = 0.9;
    // infoPanel.add(fields[i], gbc);
    // }

    // // Boutons pour afficher les UE et étudiants inscrits
    // JPanel relatedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    // relatedPanel.setBackground(Color.WHITE);

    // JButton listeUEsButton = new JButton("Liste des UEs");
    // JButton listeEtudiantsButton = new JButton("Liste des Étudiants inscrits");

    // listeUEsButton.setBackground(new Color(52, 152, 219));
    // listeUEsButton.setForeground(Color.WHITE);
    // listeEtudiantsButton.setBackground(new Color(52, 152, 219));
    // listeEtudiantsButton.setForeground(Color.WHITE);

    // relatedPanel.add(listeUEsButton);
    // relatedPanel.add(listeEtudiantsButton);

    // // État initial des boutons
    // listeUEsButton.setEnabled(false);
    // listeEtudiantsButton.setEnabled(false);

    // // Ajouter le panneau de boutons sous le formulaire
    // gbc.gridx = 0;
    // gbc.gridy = labels.length;
    // gbc.gridwidth = 2;
    // gbc.weightx = 1.0;
    // infoPanel.add(relatedPanel, gbc);

    // centerPanel.add(infoPanel, BorderLayout.SOUTH);
    // panel.add(centerPanel, BorderLayout.CENTER);

    // // Boutons d'action principaux
    // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    // buttonPanel.setBackground(Color.WHITE);
    // buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));

    // JButton refreshButton = new JButton("Rafraîchir");
    // JButton saveButton = new JButton("Enregistrer");
    // JButton deleteButton = new JButton("Supprimer");
    // JButton addButton = new JButton("Nouvelle Formation");

    // saveButton.setBackground(new Color(46, 204, 113));
    // saveButton.setForeground(Color.WHITE);
    // deleteButton.setBackground(new Color(231, 76, 60));
    // deleteButton.setForeground(Color.WHITE);
    // addButton.setBackground(new Color(52, 152, 219));
    // addButton.setForeground(Color.WHITE);

    // buttonPanel.add(refreshButton);
    // buttonPanel.add(addButton);
    // buttonPanel.add(saveButton);
    // buttonPanel.add(deleteButton);

    // panel.add(buttonPanel, BorderLayout.SOUTH);

    // // État initial des boutons
    // saveButton.setEnabled(false);
    // deleteButton.setEnabled(false);

    // // Variables pour garder la trace de la formation sélectionnée
    // final int[] selectedRow = { -1 };
    // final Formation[] selectedFormation = { null };

    // // Fonction pour charger les données
    // Runnable loadData = () -> {
    // tableModel.setRowCount(0); // Effacer les données existantes

    // // Charger les formations depuis DataStore
    // for (Formation formation : DataStore.getFormations()) {
    // tableModel.addRow(new Object[] {
    // formation.getCode(),
    // formation.getIntitule(),
    // formation.getNiveau(),
    // formation.getDescription()
    // });
    // }

    // // Réinitialiser la sélection
    // selectedRow[0] = -1;
    // selectedFormation[0] = null;
    // saveButton.setEnabled(false);
    // deleteButton.setEnabled(false);
    // listeUEsButton.setEnabled(false);
    // listeEtudiantsButton.setEnabled(false);

    // // Effacer les champs
    // codeField.setText("");
    // intituleField.setText("");
    // niveauCombo.setSelectedIndex(0);
    // descriptionArea.setText("");
    // };

    // // Charger les données initiales
    // loadData.run();

    // // Écouteur de sélection pour le tableau
    // formationsTable.getSelectionModel().addListSelectionListener(e -> {
    // if (!e.getValueIsAdjusting()) {
    // selectedRow[0] = formationsTable.getSelectedRow();

    // if (selectedRow[0] >= 0) {
    // String code = tableModel.getValueAt(selectedRow[0], 0).toString();

    // // Trouver la formation correspondante
    // for (Formation formation : DataStore.getFormations()) {
    // if (formation.getCode().equals(code)) {
    // selectedFormation[0] = formation;
    // break;
    // }
    // }

    // // Remplir les champs
    // if (selectedFormation[0] != null) {
    // codeField.setText(selectedFormation[0].getCode());
    // intituleField.setText(selectedFormation[0].getIntitule());
    // niveauCombo.setSelectedItem(selectedFormation[0].getNiveau());
    // descriptionArea.setText(selectedFormation[0].getDescription());

    // // Activer les boutons
    // saveButton.setEnabled(true);
    // deleteButton.setEnabled(true);
    // listeUEsButton.setEnabled(true);
    // listeEtudiantsButton.setEnabled(true);
    // }
    // }
    // }
    // });

    // // Actions des boutons
    // refreshButton.addActionListener(e -> loadData.run());

    // addButton.addActionListener(e -> cardLayout.show(contentPanel,
    // NOUVELLE_FORMATION_PANEL));

    // saveButton.addActionListener(e -> {
    // if (selectedFormation[0] != null) {
    // // Vérifier que les champs requis sont remplis
    // if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()) {
    // JOptionPane.showMessageDialog(this,
    // "Veuillez remplir tous les champs obligatoires.",
    // "Erreur", JOptionPane.ERROR_MESSAGE);
    // return;
    // }

    // // Mettre à jour la formation
    // Formation updatedFormation = new Formation(
    // codeField.getText(),
    // intituleField.getText(),
    // niveauCombo.getSelectedItem().toString(),
    // descriptionArea.getText());

    // // Remplacer l'ancienne formation
    // // DataStore.updateFormation(selectedFormation[0], updatedFormation);

    // JOptionPane.showMessageDialog(this,
    // "Formation mise à jour avec succès!",
    // "Succès", JOptionPane.INFORMATION_MESSAGE);

    // // Recharger les données
    // loadData.run();
    // }
    // });

    // deleteButton.addActionListener(e -> {
    // if (selectedFormation[0] != null) {
    // int confirm = JOptionPane.showConfirmDialog(this,
    // "Êtes-vous sûr de vouloir supprimer cette formation?",
    // "Confirmation de suppression",
    // JOptionPane.YES_NO_OPTION);

    // if (confirm == JOptionPane.YES_OPTION) {
    // // Supprimer la formation
    // // DataStore.removeFormation(selectedFormation[0]);

    // JOptionPane.showMessageDialog(this,
    // "Formation supprimée avec succès!",
    // "Succès", JOptionPane.INFORMATION_MESSAGE);

    // // Recharger les données
    // loadData.run();
    // }
    // }
    // });

    // // Actions pour les nouveaux boutons
    // listeUEsButton.addActionListener(e -> {
    // if (selectedFormation[0] != null) {
    // showUEsList(selectedFormation[0]);
    // }
    // });

    // listeEtudiantsButton.addActionListener(e -> {
    // if (selectedFormation[0] != null) {
    // showEtudiantsList(selectedFormation[0]);
    // }
    // });

    // return panel;
    // }
    /**
     * Affiche l'interface de gestion des UE pour une formation donnée
     * 
     * @param formation La formation dont on souhaite gérer les UE
     */
    /**
     * Affiche l'interface de gestion des UE pour une formation donnée
     * 
     * @param formation La formation dont on souhaite gérer les UE
     */
    private void showGestionUE(Formation formation) {
        // Création du panneau principal avec une marge intérieure
        JPanel gestionUEPanel = new JPanel(new BorderLayout(10, 10));
        gestionUEPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        gestionUEPanel.setBackground(new Color(240, 240, 240));

        // Panneau d'en-tête avec dégradé
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180),
                        getWidth(), 0, new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, 60));

        // Titre avec une police moderne et une couleur claire
        JLabel titleLabel = new JLabel("Gestion des UE - " + formation.getIntitule(), JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        gestionUEPanel.add(headerPanel, BorderLayout.NORTH);

        // Configuration de la table avec un modèle personnalisé
        String[] columnNames = { "Code", "Intitulé", "Crédits", "Description", "Caractéristique" };
        DefaultTableModel ueTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Empêche l'édition directe dans la table
            }
        };

        JTable ueTable = new JTable(ueTableModel);
        ueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ueTable.setRowHeight(30);
        ueTable.setIntercellSpacing(new Dimension(10, 5));
        ueTable.setShowGrid(false);
        ueTable.setFillsViewportHeight(true);

        // Style d'en-tête de colonne
        JTableHeader header = ueTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));

        // Définir les largeurs de colonnes
        ueTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Code
        ueTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Intitulé
        ueTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Crédits
        ueTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Description
        ueTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Caractéristique

        // Récupérer les UEs liées à cette formation
        ArrayList<UE> formationUEs = DataStore.getUEsByFormation(formation);
        for (UE ue : formationUEs) {
            ueTableModel.addRow(new Object[] {
                    ue.getCode(),
                    ue.getIntitule(),
                    ue.getCredits(),
                    ue.getDescription(),
                    ue.isObligatoire() ? "obligatoire" : "optionnel"
            });
        }

        // Ajout d'un rendu alterné pour les lignes
        ueTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (isSelected) {
                    comp.setBackground(new Color(173, 216, 230));
                    comp.setForeground(Color.BLACK);
                } else {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                    comp.setForeground(Color.BLACK);
                }

                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return comp;
            }
        });

        // Ajouter la table dans un JScrollPane avec style
        JScrollPane scrollPane = new JScrollPane(ueTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        gestionUEPanel.add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons avec dégradé léger
        JPanel buttonsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 240),
                        0, getHeight(), new Color(220, 220, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Style des boutons
        JButton addUEButton = createStyledButton("Ajouter une UE", new Color(46, 139, 87));
        JButton modifyUEButton = createStyledButton("Modifier UE", new Color(70, 130, 180));
        JButton deleteUEButton = createStyledButton("Supprimer UE", new Color(178, 34, 34));

        // Référence au dialogue pour pouvoir le fermer après les opérations CRUD
        JDialog[] dialogRef = new JDialog[1];

        // Ajouter des actions aux boutons
        addUEButton.addActionListener(e -> {
            JDialog addDialog = new JDialog((JDialog) dialogRef[0], "Nouvelle UE", true);
            addDialog.setContentPane(createNouvelleUEPanel(formation, addDialog, ueTableModel));
            addDialog.pack();
            addDialog.setSize(650, 500);
            addDialog.setLocationRelativeTo(dialogRef[0]);
            addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addDialog.setVisible(true);
        });

        modifyUEButton.addActionListener(e -> {
            int selectedRow = ueTable.getSelectedRow();
            if (selectedRow != -1) {
                String ueCode = (String) ueTableModel.getValueAt(selectedRow, 0);
                UE selectedUE = findUEByCode(formationUEs, ueCode);
                if (selectedUE != null) {
                    JDialog modifyDialog = new JDialog((JDialog) dialogRef[0], "Modifier UE", true);
                    modifyDialog.setContentPane(
                            createModifierUEPanel(selectedUE, formation, modifyDialog, ueTableModel, selectedRow));
                    modifyDialog.pack();
                    modifyDialog.setSize(650, 500);
                    modifyDialog.setLocationRelativeTo(dialogRef[0]);
                    modifyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    modifyDialog.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(dialogRef[0],
                        "Veuillez sélectionner une UE à modifier",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteUEButton.addActionListener(e -> {
            int selectedRow = ueTable.getSelectedRow();
            if (selectedRow != -1) {
                String ueCode = (String) ueTableModel.getValueAt(selectedRow, 0);
                UE selectedUE = findUEByCode(formationUEs, ueCode);
                if (selectedUE != null) {
                    int confirm = JOptionPane.showConfirmDialog(dialogRef[0],
                            "Êtes-vous sûr de vouloir supprimer l'UE " + selectedUE.getIntitule() + " ?",
                            "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        DataStore.deleteUE(selectedUE);
                        ueTableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(dialogRef[0],
                                "L'UE a été supprimée avec succès.",
                                "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialogRef[0],
                        "Veuillez sélectionner une UE à supprimer",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonsPanel.add(addUEButton);
        buttonsPanel.add(modifyUEButton);
        buttonsPanel.add(deleteUEButton);
        gestionUEPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Création d'une boite de dialogue personnalisée
        JDialog dialog = new JDialog(this, "Gestion des UE", true);
        dialogRef[0] = dialog;
        dialog.setContentPane(gestionUEPanel);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * Crée un bouton stylisé avec effet de survol
     */

    /**
     * Trouver une UE par son code
     */
    private UE findUEByCode(ArrayList<UE> ues, String code) {
        for (UE ue : ues) {
            if (ue.getCode().equals(code)) {
                return ue;
            }
        }
        return null;
    }

    /**
     * Création d'un panneau pour ajouter une nouvelle UE
     */
    private JPanel createNouvelleUEPanel(Formation preselectedFormation, JDialog parentDialog,
            DefaultTableModel tableModel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre avec dégradé
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180),
                        getWidth(), 0, new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(titlePanel.getPreferredSize().width, 60));

        JLabel titleLabel = new JLabel("Nouvelle Unité d'Enseignement", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Description:", "Caractéristique:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        // Remplir le modèle avec les formations disponibles
        for (Formation formation : DataStore.getFormations()) {
            formationModel.addElement(formation);
        }

        // Présélectionner la formation si elle est fournie
        if (preselectedFormation != null) {
            formationModel.setSelectedItem(preselectedFormation);
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
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        // Boutons radio pour le caractère de l'UE
        JRadioButton obligatoireRadio = new JRadioButton("Obligatoire", true);
        JRadioButton optionnelRadio = new JRadioButton("Optionnel");
        ButtonGroup caractereGroup = new ButtonGroup();
        caractereGroup.add(obligatoireRadio);
        caractereGroup.add(optionnelRadio);
        JPanel caracterePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        caracterePanel.add(obligatoireRadio);
        caracterePanel.add(optionnelRadio);
        JComponent[] fields = { codeField, intituleField, formationComboBox, creditSpinner, descScrollPane,
                caracterePanel };

        // Ajouter les champs au formulaire
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton annulerButton = createStyledButton("Annuler", new Color(120, 120, 120));
        JButton enregistrerButton = createStyledButton("Enregistrer", new Color(46, 204, 113));

        buttonPanel.add(annulerButton);
        buttonPanel.add(enregistrerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action des boutons
        enregistrerButton.addActionListener(e -> {
            if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()
                    || formationComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(parentDialog,
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
                    selectedFormation, false);

            // Ajouter la caractéristique si disponible

            DataStore.addUE(nouvelleUE);

            // Ajouter la nouvelle UE à la table
            tableModel.addRow(new Object[] {
                    nouvelleUE.getCode(),
                    nouvelleUE.getIntitule(),
                    nouvelleUE.getCredits(),
                    nouvelleUE.getDescription(),
                    nouvelleUE.isObligatoire() ? "obligatoire" : "optionnel"
            });

            JOptionPane.showMessageDialog(parentDialog,
                    "Unité d'enseignement créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            parentDialog.dispose();
        });

        annulerButton.addActionListener(e -> {
            parentDialog.dispose();
        });

        return panel;
    }

    /**
     * Création d'un panneau pour modifier une UE existante
     */
    private JPanel createModifierUEPanel(UE ue, Formation formation, JDialog parentDialog, DefaultTableModel tableModel,
            int rowIndex) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre avec dégradé
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180),
                        getWidth(), 0, new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(titlePanel.getPreferredSize().width, 60));

        JLabel titleLabel = new JLabel("Modifier Unité d'Enseignement", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Description:", "Caractéristique:" };
        JTextField codeField = new JTextField(ue.getCode(), 20);
        JTextField intituleField = new JTextField(ue.getIntitule(), 20);

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        // Remplir le modèle avec les formations disponibles
        for (Formation f : DataStore.getFormations()) {
            formationModel.addElement(f);
            if (ue.getFormation() != null && f.getCode().equals(ue.getFormation().getCode())) {
                formationModel.setSelectedItem(f);
            }
        }

        JComboBox<Formation> formationComboBox = new JComboBox<>(formationModel);

        // Personnaliser l'affichage des formations dans la liste déroulante
        formationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Formation) {
                    Formation f = (Formation) value;
                    setText(f.getCode() + " - " + f.getIntitule());
                }
                return this;
            }
        });

        SpinnerModel creditModel = new SpinnerNumberModel(ue.getCredits(), 1, 10, 1);
        JSpinner creditSpinner = new JSpinner(creditModel);

        JTextArea descriptionArea = new JTextArea(ue.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);

        // Boutons radio pour le caractère de l'UE
        JRadioButton obligatoireRadio = new JRadioButton("Obligatoire", true);
        JRadioButton optionnelRadio = new JRadioButton("Optionnel");
        ButtonGroup caractereGroup = new ButtonGroup();
        caractereGroup.add(obligatoireRadio);
        caractereGroup.add(optionnelRadio);
        JPanel caracterePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        caracterePanel.add(obligatoireRadio);
        caracterePanel.add(optionnelRadio);

        // Sélectionner le bon bouton radio en fonction de l'UE actuelle
        if (ue.isObligatoire()) {
            obligatoireRadio.setSelected(true);
        } else {
            optionnelRadio.setSelected(true);
        }

        JComponent[] fields = { codeField, intituleField, formationComboBox, creditSpinner, descScrollPane,
                caracterePanel };

        // Ajouter les champs au formulaire
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton annulerButton = createStyledButton("Annuler", new Color(120, 120, 120));
        JButton enregistrerButton = createStyledButton("Enregistrer", new Color(46, 204, 113));

        buttonPanel.add(annulerButton);
        buttonPanel.add(enregistrerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action des boutons
        enregistrerButton.addActionListener(e -> {
            if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()
                    || formationComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(parentDialog,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Formation selectedFormation = (Formation) formationComboBox.getSelectedItem();

            // Mettre à jour l'UE
            ue.setCode(codeField.getText());
            ue.setIntitule(intituleField.getText());
            ue.setCredits((int) creditSpinner.getValue());
            ue.setDescription(descriptionArea.getText());
            ue.setFormation(selectedFormation);

            // Mettre à jour dans la base de données
            DataStore.updateUE(ue);

            // Mettre à jour la table
            tableModel.setValueAt(ue.getCode(), rowIndex, 0);
            tableModel.setValueAt(ue.getIntitule(), rowIndex, 1);
            tableModel.setValueAt(ue.getCredits(), rowIndex, 2);
            tableModel.setValueAt(ue.getDescription(), rowIndex, 3);
            // tableModel.setValueAt(ue.getCaracteristique() != null ?
            // ue.getCaracteristique() : "", rowIndex, 4);

            JOptionPane.showMessageDialog(parentDialog,
                    "Unité d'enseignement modifiée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            parentDialog.dispose();
        });

        annulerButton.addActionListener(e -> {
            parentDialog.dispose();
        });

        return panel;
    }

    /**
     * Crée un bouton stylisé avec effet de survol
     */
    private JButton createStyledButton(String text, Color mainColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(mainColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(mainColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(mainColor);
            }
        });

        return button;
    }

    /**
     * Trouver une UE par son code
     */
    // private UE findUEByCode(ArrayList<UE> ues, String code) {
    // for (UE ue : ues) {
    // if (ue.getCode().equals(code)) {
    // return ue;
    // }
    // }
    // return null;
    // }

    /**
     * Ajouter une nouvelle UE à la formation
     */
    private void addUE(Formation formation) {
        // À implémenter : code pour ajouter une UE
        // Exemple : ouvrir un formulaire d'ajout
    }

    /**
     * Modifier une UE existante
     */
    private void modifyUE(UE ue, Formation formation) {
        // À implémenter : code pour modifier une UE
        // Exemple : ouvrir un formulaire pré-rempli
    }

    /**
     * Supprimer une UE de la formation
     */
    private void deleteUE(UE ue, Formation formation, DefaultTableModel model, int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer l'UE " + ue.getIntitule() + " ?",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Code de suppression à implémenter
            // DataStore.deleteUE(ue, formation);
            model.removeRow(row);
        }
    }

    // Méthode pour afficher la liste des UEs d'une formation
    private void showUEsList(Formation formation) {
        // Créer une fenêtre de dialogue
        JDialog dialog = new JDialog(this, "UEs de la formation " + formation.getIntitule(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel(
                "Liste des UEs de la formation: " + formation.getCode() + " - " + formation.getIntitule(),
                JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Tableau des UEs
        String[] columnNames = { "Code UE", "Intitulé", "Crédits", "Description" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Récupérer les UEs liées à cette formation
        ArrayList<UE> formationUEs = DataStore.getUEsByFormation(formation);

        for (UE ue : formationUEs) {
            tableModel.addRow(new Object[] {
                    ue.getCode(),
                    ue.getIntitule(),
                    ue.getCredits(),
                    ue.getDescription()
            });
        }

        JTable uesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(uesTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Bouton de fermeture
        JButton closeButton = new JButton("Fermer");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // Méthode pour afficher la liste des étudiants inscrits à une formation
    private void showEtudiantsList(Formation formation) {
        // Créer une fenêtre de dialogue
        JDialog dialog = new JDialog(this, "Étudiants inscrits à " + formation.getIntitule(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel(
                "Liste des étudiants inscrits à la formation: " + formation.getCode() + " - " + formation.getIntitule(),
                JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Tableau des étudiants
        String[] columnNames = { "Numéro", "Nom", "Prénom", "Email", "Groupe" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Récupérer les étudiants inscrits à cette formation
        List<Etudiant> formationEtudiants = DataStore.getEtudiantsByFormation(formation);

        for (Etudiant etudiant : formationEtudiants) {
            tableModel.addRow(new Object[] {
                    etudiant.getIne(),
                    etudiant.getNom(),
                    etudiant.getPrenom(),
                    etudiant.getEmail(),

            });
        }

        JTable etudiantsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(etudiantsTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Bouton de fermeture
        JButton closeButton = new JButton("Fermer");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
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