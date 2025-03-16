package sn.uasz.m1.projet.gui.responsablePedagogique;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.dao.UEDAO;
import sn.uasz.m1.projet.dto.Etat;
import sn.uasz.m1.projet.gui.Connexion;
import sn.uasz.m1.projet.gui.responsablePedagogique.services.FormationService;
import sn.uasz.m1.projet.gui.responsablePedagogique.services.UeService;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;
import sn.uasz.m1.projet.model.person.User;

public class FenetrePrincipal extends JFrame implements PanelSwitcher {
    private JPanel contentPanel;
    private JPanel dashboardPanel;
    private CardLayout cardLayout;
    private final User currentUser;
    private FormationDAO formationService = new FormationDAO();
    private UEDAO ueDao = new UEDAO();
    private ResponsableDAO responsableService = new ResponsableDAO();

    // Constante pour le nom du panel dashboard
    private static final String DASHBOARD_PANEL = "Dashboard";
    private static final String NOUVELLE_FORMATION_PANEL = "Nouvelle Formation";
    private static final String NOUVELLE_UE_PANEL = "Nouvelle UE";
    private static final String NOUVEL_ETUDIANT_PANEL = "Nouvel Étudiant";
    private static final String NOUVELLE_INSCRIPTION_PANEL = "Nouvelle Inscription";
    private FormationService formationGUI = new FormationService();
    private UeService UeGUI = new UeService();
    private EntityManagerFactory emf = null;
    private EntityManager em = null;

    public FenetrePrincipal(User user) {
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

        String[] buttons = { "Dashboard", "Gérer Formations", "Liste Inscriptions", "Gérer Étudiants",
                "Gérer Groupes", };

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Créer le dashboard
        dashboardPanel = createDashboardPanel();
        contentPanel.add(dashboardPanel, DASHBOARD_PANEL);

        // Créer les panneaux de formulaires
        JPanel nouvelleFormationPanel = formationGUI.createNouvelleFormationPanel(this, this,
                DASHBOARD_PANEL);
        contentPanel.add(nouvelleFormationPanel, NOUVELLE_FORMATION_PANEL);

        JPanel nouvelleUEPanel = createNouvelleUEPanel();
        contentPanel.add(nouvelleUEPanel, NOUVELLE_UE_PANEL);

        JPanel nouvelEtudiantPanel = createNouvelEtudiantPanel();
        contentPanel.add(nouvelEtudiantPanel, NOUVEL_ETUDIANT_PANEL);

        JPanel gererFormationsPanel = formationGUI.createGererFormationsPanel(this, contentPanel, cardLayout,
                NOUVELLE_FORMATION_PANEL);
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

    @Override
    public void showPanel(String panelName) {
        // dashboardPanel
        // refreshDashboard();
        cardLayout.show(contentPanel, panelName);

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
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Volume horaire:",
                "Coefficient:", "Responsable UE:", "Description:", "Caractère de l'UE:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Récupérer la liste des formations depuis la base de données
        List<Formation> formations = formationService.findAll();
        formationService.findAll();
        JComboBox<Formation> formationComboBox = new JComboBox<>(formations.toArray(new Formation[0]));

        // Personnaliser l'affichage des formations dans le ComboBox
        formationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Formation) {
                    Formation formation = (Formation) value;
                    setText(formation.getCode() + " - " + formation.getNom());
                }
                return this;
            }
        });

        SpinnerModel creditModel = new SpinnerNumberModel(3, 1, 30, 1);
        JSpinner creditSpinner = new JSpinner(creditModel);

        SpinnerModel volumeHoraireModel = new SpinnerNumberModel(30, 5, 100, 5);
        JSpinner volumeHoraireSpinner = new JSpinner(volumeHoraireModel);

        SpinnerModel coefficientModel = new SpinnerNumberModel(1.0, 0.5, 5.0, 0.5);
        JSpinner coefficientSpinner = new JSpinner(coefficientModel);

        JTextField responsableField = new JTextField(20);

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

        JComponent[] fields = {
                codeField, intituleField, formationComboBox, creditSpinner,
                volumeHoraireSpinner, coefficientSpinner, responsableField, scrollPane, caracterePanel
        };

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
                    || formationComboBox.getSelectedItem() == null || responsableField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Formation selectedFormation = (Formation) formationComboBox.getSelectedItem();
            boolean estObligatoire = obligatoireRadio.isSelected();

            // Créer un nouvel objet UE
            UE nouvelleUE = new UE();
            nouvelleUE.setCode(codeField.getText());
            nouvelleUE.setNom(intituleField.getText());
            nouvelleUE.setCredits((Integer) creditSpinner.getValue());
            nouvelleUE.setVolumeHoraire((Integer) volumeHoraireSpinner.getValue());
            nouvelleUE.setCoefficient((Double) coefficientSpinner.getValue());
            nouvelleUE.setNomResponsable(responsableField.getText());
            nouvelleUE.setFormation(selectedFormation);
            nouvelleUE.setObligatoire(estObligatoire);

            // Sauvegarder dans la base de données
            ueDao.create(nouvelleUE);

            JOptionPane.showMessageDialog(this,
                    "Unité d'enseignement créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Réinitialiser les champs
            codeField.setText("");
            intituleField.setText("");
            formationComboBox.setSelectedIndex(0);
            creditSpinner.setValue(3);
            volumeHoraireSpinner.setValue(30);
            coefficientSpinner.setValue(1.0);
            responsableField.setText("");
            descriptionArea.setText("");
            obligatoireRadio.setSelected(true);

            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

    // Méthode pour récupérer les formations depuis la BD

    // Méthode pour sauvegarder une UE dans la BD

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

            // Valider le format de la date
            String dateText = dateField.getText();
            LocalDate dateNaissance = null;

            try {
                // Convertir la chaîne de texte en LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateNaissance = LocalDate.parse(dateText, formatter);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Format de date invalide. Veuillez utiliser le format JJ/MM/AAAA.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer un EntityManager à partir de l'EntityManagerFactory
            emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
            em = emf.createEntityManager();
            EntityTransaction transaction = null;

            try {
                // Démarrer une transaction
                transaction = em.getTransaction();
                transaction.begin();

                // Créer un nouvel étudiant avec les données du formulaire
                Etudiant nouvelEtudiant = new Etudiant();
                nouvelEtudiant.setIne(ineField.getText());
                nouvelEtudiant.setNom(nomField.getText());
                nouvelEtudiant.setPrenom(prenomField.getText());
                nouvelEtudiant.setDateNaissance(dateNaissance); // Utiliser l'objet LocalDate
                nouvelEtudiant.setEmail(emailField.getText());

                // Initialiser la collection d'UEs
                nouvelEtudiant.setUes(new HashSet<>());

                // Persister l'étudiant dans la base de données
                em.persist(nouvelEtudiant);

                // Valider la transaction
                transaction.commit();

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

                // Rafraîchir la liste des étudiants si nécessaire
                refreshStudentList();

            } catch (Exception ex) {
                // En cas d'erreur, annuler la transaction
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }

                // Afficher un message d'erreur
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'ajout de l'étudiant: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                // Fermer l'EntityManager
                em.close();
            }
        });

        annulerButton.addActionListener(e -> {
            cardLayout.show(contentPanel, DASHBOARD_PANEL);
        });

        return panel;
    }

    // Méthode pour rafraîchir le tableau de bord
    // Méthode pour rafraîchir le tableau de bord
    private void refreshDashboard() {
        // Récupérer les nouvelles statistiques depuis la base de données
        String[][] newStatData = getStatisticsFromDatabase();

        // Créer un nouveau conteneur de statistiques
        JPanel statsContainer = new JPanel(new GridLayout(2, 2, 20, 20));
        statsContainer.setBackground(Color.WHITE);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Couleurs pour les différentes cartes
        Color[] cardColors = {
                new Color(41, 128, 185), // Bleu
                new Color(39, 174, 96), // Vert
                new Color(142, 68, 173), // Violet
                new Color(230, 126, 34) // Orange
        };

        // Créer les nouvelles cartes de statistiques
        for (int i = 0; i < newStatData.length; i++) {
            JPanel statCard = createStatCard(newStatData[i][0], newStatData[i][1], newStatData[i][2], cardColors[i]);
            statsContainer.add(statCard);
        }

        // Vider le conteneur précédent de statistiques avant d'ajouter les nouvelles
        JPanel oldStatsContainer = (JPanel) dashboardPanel.getComponent(1); // Obtenir l'ancien conteneur de stats
        oldStatsContainer.removeAll(); // Retirer toutes les cartes

        // Ajouter les nouvelles cartes
        dashboardPanel.remove(1); // Retirer l'ancien conteneur des statistiques
        dashboardPanel.add(statsContainer, BorderLayout.CENTER); // Ajouter le nouveau conteneur des statistiques

        // Repeindre et revalider le panneau pour appliquer les changements
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    // Méthode pour rafraîchir la liste des étudiants
    private void refreshStudentList() {
        // Création d'un nouvel EntityManager
        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e ORDER BY e.nom, e.prenom", Etudiant.class);
            List<Etudiant> etudiants = query.getResultList();

            // Ici, vous devriez mettre à jour le modèle de votre table ou liste
            // Par exemple :
            // etudiantTableModel.setEtudiants(etudiants);
            // etudiantTableModel.fireTableDataChanged();

            // Si vous avez un JTable, vous pouvez le mettre à jour comme ceci:
            // DefaultTableModel model = (DefaultTableModel) etudiantTable.getModel();
            // model.setRowCount(0);
            // for (Etudiant etudiant : etudiants) {
            // model.addRow(new Object[]{
            // etudiant.getIne(),
            // etudiant.getNom(),
            // etudiant.getPrenom(),
            // etudiant.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            // etudiant.getEmail()
            // });
            // }
        } finally {
            em.close();
        }
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

        // Récupérer les statistiques depuis la base de données
        String[][] statData = getStatisticsFromDatabase();

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
                if (action.equals("Nouvel Étudiant")) {
                    cardLayout.show(contentPanel, NOUVEL_ETUDIANT_PANEL);
                } else if (action.equals("Nouvelle Formation")) {
                    cardLayout.show(contentPanel, NOUVELLE_FORMATION_PANEL);
                } else if (action.equals("Nouvelle UE")) {
                    cardLayout.show(contentPanel, NOUVELLE_UE_PANEL);
                } else if (action.equals("Nouvelle Inscription")) {
                    cardLayout.show(contentPanel, NOUVELLE_INSCRIPTION_PANEL);
                }
            });
            quickActions.add(actionBtn);
        }

        dashboardPanel.add(quickActions, BorderLayout.SOUTH);

        return dashboardPanel;
    }

    /**
     * Récupère les statistiques depuis la base de données
     * 
     * @return Un tableau contenant les données pour chaque carte statistique
     */
    private String[][] getStatisticsFromDatabase() {
        String[][] statData = new String[4][3];

        // Initialiser les titres et descriptions
        statData[0][0] = "Formations";
        statData[0][2] = "Nombre total de formations disponibles";

        statData[1][0] = "Étudiants";
        statData[1][2] = "Nombre total d'étudiants inscrits";

        statData[2][0] = "Unités d'Enseignement";
        statData[2][2] = "Nombre total d'UEs définies";

        statData[3][0] = "Inscriptions";
        statData[3][2] = "Nombre d'inscriptions validées";

        // Créer un EntityManager pour interroger la base de données
        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();

        try {
            Etat etat = responsableService.etatDashboard();

            // Compter les Forations
            statData[0][1] = etat.getFormmations().toString();

            // Compter les étudiants

            statData[1][1] = etat.getEtudiants().toString();

            // Compter les UEs

            statData[2][1] = etat.getUes().toString();

            // Compter les inscriptions (basé sur le nombre d'étudiants qui ont au moins une
            // UE assignée)

            statData[3][1] = etat.getInscriptions().toString();
        } catch (Exception e) {
            // En cas d'erreur, initialiser à 0
            statData[0][1] = "0";
            statData[1][1] = "0";
            statData[2][1] = "0";
            statData[3][1] = "0";

            e.printStackTrace();
        } finally {
            // Fermer l'EntityManager
            em.close();
        }

        return statData;
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

    // // Ajouter un TableRowSorter pour le filtrage
    // TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    // formationsTable.setRowSorter(sorter);

    // // Panneau de recherche
    // JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    // searchPanel.setBackground(Color.WHITE);
    // searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    // JLabel searchLabel = new JLabel("Rechercher:");
    // JTextField searchField = new JTextField(20);
    // JComboBox<String> searchColumnCombo = new JComboBox<>(columnNames);

    // searchPanel.add(searchLabel);
    // searchPanel.add(searchField);
    // searchPanel.add(new JLabel("dans"));
    // searchPanel.add(searchColumnCombo);

    // // Ajouter un ActionListener pour le champ de recherche
    // searchField.getDocument().addDocumentListener(new DocumentListener() {
    // @Override
    // public void insertUpdate(javax.swing.event.DocumentEvent e) {
    // updateFilter();
    // }

    // @Override
    // public void removeUpdate(javax.swing.event.DocumentEvent e) {
    // updateFilter();
    // }

    // @Override
    // public void changedUpdate(javax.swing.event.DocumentEvent e) {
    // updateFilter();
    // }

    // private void updateFilter() {
    // String text = searchField.getText();
    // int columnIndex = searchColumnCombo.getSelectedIndex();

    // if (text.trim().length() == 0) {
    // sorter.setRowFilter(null);
    // } else {
    // sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
    // }
    // }
    // });

    // // Ajouter le panneau de recherche en haut du panneau central
    // centerPanel.add(searchPanel, BorderLayout.NORTH);

    // // Panneau de défilement pour le tableau
    // JScrollPane scrollPane = new JScrollPane(formationsTable);
    // centerPanel.add(scrollPane, BorderLayout.CENTER);

    // // Le reste du code reste inchangé...
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

    // JButton listeUEsButton = new JButton("Gerer UEs");
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

    // // Le reste du code reste le même
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
    // List<Formation> formations = formationService.findAll();
    // // for (Formation formation : DataStore.formationService.findAll()) {
    // for (Formation formation : formations) {
    // tableModel.addRow(new Object[] {
    // formation.getCode(),
    // formation.getNom(),
    // formation.getNiveau(),
    // formation.getNom() // ! formation.getDescription()
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

    // // Réinitialiser le champ de recherche
    // searchField.setText("");
    // };

    // // Le reste du code reste le même
    // // Charger les données initiales
    // loadData.run();

    // // Écouteur de sélection pour le tableau
    // formationsTable.getSelectionModel().addListSelectionListener(e -> {
    // if (!e.getValueIsAdjusting()) {
    // selectedRow[0] = formationsTable.getSelectedRow();

    // if (selectedRow[0] >= 0) {
    // // Convertir l'index de la ligne du modèle de vue au modèle de données
    // int modelRow = formationsTable.convertRowIndexToModel(selectedRow[0]);
    // String code = tableModel.getValueAt(modelRow, 0).toString();

    // List<Formation> formations = formationService.findAll();
    // // Trouver la formation correspondante
    // // for (Formation formation : DataStore.formationService.findAll()) {
    // for (Formation formation : formations) {
    // if (formation.getCode().equals(code)) {
    // selectedFormation[0] = formation;
    // break;
    // }
    // }

    // // Remplir les champs
    // if (selectedFormation[0] != null) {
    // codeField.setText(selectedFormation[0].getCode());
    // intituleField.setText(selectedFormation[0].getNom());
    // niveauCombo.setSelectedItem(selectedFormation[0].getNiveau());
    // descriptionArea.setText(selectedFormation[0].getNom()); // !
    // .getDescription()

    // // Activer les boutons
    // saveButton.setEnabled(true);
    // deleteButton.setEnabled(true);
    // listeUEsButton.setEnabled(true);
    // listeEtudiantsButton.setEnabled(true);
    // }
    // }
    // }
    // });

    // // Actions des boutons (comme avant)
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
    // Formation updatedFormation = new Formation();
    // updatedFormation.setCode(codeField.getText());
    // updatedFormation.setNom(intituleField.getText());
    // updatedFormation.setNiveau((Niveau) niveauCombo.getSelectedItem());
    // // updatedFormation.setResponsable((ResponsablePedagogique)
    // // respCombo.getSelectedItem());

    // // Remplacer l'ancienne formation
    // saveFormation(updatedFormation);
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
    // showGestionUE(selectedFormation[0]);
    // // showUEsList(selectedFormation[0]);
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
        JLabel titleLabel = new JLabel("Gestion des UE - " + formation.getNom(), JLabel.CENTER);
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
        // ArrayList<UE> formationUEs = DataStore.getUEsByFormation(formation);
        List<UE> formationUEs = getUEsByFormation(formation);
        for (UE ue : formationUEs) {
            ueTableModel.addRow(new Object[] {
                    ue.getCode(),
                    ue.getNom(),
                    ue.getCredits(),
                    ue.getNom(), // ! getDescription
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
                System.out.println("###########" + selectedUE + " Le code est de" + ueCode);
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
                    if (selectedUE.isObligatoire()) {
                        JOptionPane.showMessageDialog(null, "Impossible de supprier cet UE , car elle est obligatoire");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(dialogRef[0],
                            "Êtes-vous sûr de vouloir supprimer l'UE " + selectedUE.getNom() + " ?",
                            "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // DataStore.deleteUE(selectedUE);
                        if (deleteUE(selectedUE)) {
                            ueTableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(dialogRef[0],
                                    "L'UE a été supprimée avec succès.",
                                    "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(dialogRef[0],
                                    "Une erreur s'est produite lors de la suppression de l'UE.",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
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
     * Supprime une UE de la base de données.
     * 
     * @param ue L'UE à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteUE(UE ue) {
        if (ue == null) {
            return false;
        }

        // EntityManagerFactory emf = null;
        // EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // Récupérer l'UE depuis la base de données pour s'assurer qu'elle est gérée
            UE ueToDelete = em.find(UE.class, ue.getId());

            if (ueToDelete != null) {
                // Si l'UE est associée à une formation, on doit d'abord supprimer cette
                // association
                if (ueToDelete.getFormation() != null) {
                    Formation formation = ueToDelete.getFormation();
                    formation.removeUE(ueToDelete);
                    em.merge(formation);
                }

                // Supprimer les associations avec les étudiants
                for (Etudiant etudiant : new HashSet<>(ueToDelete.getEtudiants())) {
                    etudiant.getUes().remove(ueToDelete);
                    em.merge(etudiant);
                }

                // Supprimer l'UE
                em.remove(ueToDelete);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    /**
     * Récupère les UEs liées à une formation spécifique.
     * 
     * @param formation La formation dont on veut récupérer les UEs
     * @return La liste des UEs liées à cette formation, ou une liste vide si aucune
     *         UE n'est trouvée
     */
    public List<UE> getUEsByFormation(Formation formation) {
        if (formation == null) {
            return new ArrayList<>();
        }

        // Utilisation de JPA pour récupérer les UEs liées à la formation
        emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
        em = emf.createEntityManager();
        try {
            TypedQuery<UE> query = em.createQuery(
                    "SELECT u FROM UE u WHERE u.formation = :formation", UE.class);
            query.setParameter("formation", formation);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Crée un bouton stylisé avec effet de survol
     */

    /**
     * Trouver une UE par son code
     */
    private UE findUEByCode(List<UE> ues, String code) {
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
        // for (Formation formation : DataStore.formationService.findAll()) {
        for (Formation formation : formationService.findAll()) {
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
                    setText(formation.getCode() + " - " + formation.getNom());
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

            UE nouvelleUE = new UE();
            nouvelleUE.setCode(codeField.getText());
            nouvelleUE.setNom(intituleField.getText());
            nouvelleUE.setCredits((int) creditSpinner.getValue());
            // nouvelleUE.setDescription(descriptionArea.getText());
            nouvelleUE.setFormation(selectedFormation);
            nouvelleUE.setObligatoire(obligatoireRadio.isSelected());

            // Ajouter la caractéristique si disponible
            // DataStore.addUE(nouvelleUE);
            if (addUE(nouvelleUE)) {
                formationComboBox.addItem(selectedFormation);
            } else {
                JOptionPane.showMessageDialog(parentDialog,
                        "Une erreur s'est produite lors de l'ajout de l'UE.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ajouter la nouvelle UE à la table
            tableModel.addRow(new Object[] {
                    nouvelleUE.getCode(),
                    nouvelleUE.getNom(),
                    nouvelleUE.getCredits(),
                    nouvelleUE.getNom(), // ! nouvelleUE.getDescription(),
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
     * Ajoute une UE dans la base de données.
     * 
     * @param ue L'UE à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addUE(UE ue) {
        if (ue == null) {
            return false;
        }

        // EntityManagerFactory emf = null;
        // EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // Vérifier si l'UE existe déjà avec le même code (qui est unique)
            if (ue.getCode() != null && !ue.getCode().isEmpty()) {
                TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u) FROM UE u WHERE u.code = :code", Long.class);
                query.setParameter("code", ue.getCode());
                Long count = query.getSingleResult();

                if (count > 0) {
                    // UE avec ce code existe déjà
                    em.getTransaction().rollback();
                    return false;
                }
            }

            // Si l'UE a une formation associée, s'assurer que la formation est bien gérée
            if (ue.getFormation() != null) {
                Formation formation = em.find(Formation.class, ue.getFormation().getId());
                if (formation != null) {
                    ue.setFormation(formation);
                    formation.getUes().add(ue);
                }
            }

            // Persister l'UE
            em.persist(ue);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
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
        JTextField intituleField = new JTextField(ue.getNom(), 20); // ! getIntitule

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        // Remplir le modèle avec les formations disponibles
        // for (Formation f : DataStore.formationService.findAll()) {
        for (Formation f : formationService.findAll()) {
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
                    setText(f.getCode() + " - " + f.getNom());
                }
                return this;
            }
        });

        // SpinnerModel creditModel = new SpinnerNumberModel(ue.getCredits(), 1, 10, 1);
        SpinnerModel creditModel = new SpinnerNumberModel((int) ue.getCredits(), 1, 10, 1);
        JSpinner creditSpinner = new JSpinner(creditModel);

        JTextArea descriptionArea = new JTextArea(ue.getNom(), 5, 20);
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
            ue.setNom(intituleField.getText());
            ue.setCredits((int) creditSpinner.getValue());
            // ue.setDescription(descriptionArea.getText());
            ue.setObligatoire(obligatoireRadio.isSelected() ? true : false);
            ue.setFormation(selectedFormation);

            // Mettre à jour dans la base de données
            // DataStore.updateUE(ue);

            if (updateUE(ue)) {
                formationComboBox.addItem(selectedFormation);
            } else {
                JOptionPane.showMessageDialog(parentDialog,
                        "Une erreur s'est produite lors de la modification de l'UE.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mettre à jour la table
            tableModel.setValueAt(ue.getCode(), rowIndex, 0);
            tableModel.setValueAt(ue.getNom(), rowIndex, 1);
            tableModel.setValueAt(ue.getCredits(), rowIndex, 2);
            tableModel.setValueAt(ue.getNom(), rowIndex, 3); // ! ue.getDescription()
            // tableModel.setValueAt(ue.getCaracteristique() != null ?
            // ue.getCaracteristique() : "", rowIndex, 4);
            tableModel.setValueAt(ue.isObligatoire() ? "obligatoire" : "optionnel", rowIndex, 4);
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
     * Met à jour une UE existante dans la base de données.
     * 
     * @param ue L'UE avec les nouvelles valeurs à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateUE(UE ue) {
        if (ue == null || ue.getId() == null) {
            return false;
        }

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // Vérifier si l'UE existe dans la base de données
            UE existingUE = em.find(UE.class, ue.getId());
            if (existingUE == null) {
                em.getTransaction().rollback();
                return false;
            }

            // Vérifier si le nouveau code est déjà utilisé par une autre UE
            if (ue.getCode() != null && !ue.getCode().equals(existingUE.getCode())) {
                TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u) FROM UE u WHERE u.code = :code AND u.id != :id", Long.class);
                query.setParameter("code", ue.getCode());
                query.setParameter("id", ue.getId());
                Long count = query.getSingleResult();

                if (count > 0) {
                    // Code déjà utilisé par une autre UE
                    em.getTransaction().rollback();
                    return false;
                }
            }

            // Mettre à jour les attributs de l'UE existante
            existingUE.setCode(ue.getCode());
            existingUE.setNom(ue.getNom());
            existingUE.setVolumeHoraire(ue.getVolumeHoraire());
            existingUE.setCoefficient(ue.getCoefficient());
            existingUE.setCredits(ue.getCredits());
            existingUE.setNomResponsable(ue.getNomResponsable());
            existingUE.setObligatoire(ue.isObligatoire());

            // Gérer le changement de formation si nécessaire
            if (ue.getFormation() != null &&
                    (existingUE.getFormation() == null ||
                            !existingUE.getFormation().getId().equals(ue.getFormation().getId()))) {

                // Enlever l'UE de l'ancienne formation si elle existe
                if (existingUE.getFormation() != null) {
                    Formation oldFormation = em.find(Formation.class, existingUE.getFormation().getId());
                    if (oldFormation != null) {
                        oldFormation.removeUE(existingUE);
                        em.merge(oldFormation);
                    }
                }

                // Ajouter l'UE à la nouvelle formation
                Formation newFormation = em.find(Formation.class, ue.getFormation().getId());
                if (newFormation != null) {
                    newFormation.addUE(existingUE);
                    em.merge(newFormation);
                }
            } else if (ue.getFormation() == null && existingUE.getFormation() != null) {
                // Retirer l'UE de sa formation actuelle si la nouvelle formation est null
                Formation oldFormation = em.find(Formation.class, existingUE.getFormation().getId());
                if (oldFormation != null) {
                    oldFormation.removeUE(existingUE);
                    em.merge(oldFormation);
                }
                existingUE.setFormation(null);
            }

            // Persister les changements
            em.merge(existingUE);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
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
     * Supprimer une UE de la formation
     */
    private void deleteUE(UE ue, Formation formation, DefaultTableModel model, int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer l'UE " + ue.getNom() + " ?",
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
        JDialog dialog = new JDialog(this, "UEs de la formation " + formation.getNom(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel(
                "Liste des UEs de la formation: " + formation.getCode() + " - " + formation.getNom(),
                JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Tableau des UEs
        String[] columnNames = { "Code UE", "Intitulé", "Crédits", "Description" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Récupérer les UEs liées à cette formation
        // ArrayList<UE> formationUEs = DataStore.getUEsByFormation(formation);
        List<UE> formationUEs = getUEsByFormation(formation);

        for (UE ue : formationUEs) {
            tableModel.addRow(new Object[] {
                    ue.getCode(),
                    ue.getNom(),
                    ue.getCredits(),
                    ue.getNom() // ! ue.getDescription()
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

    /**
     * Récupère les étudiants inscrits à une formation spécifique.
     * Un étudiant est considéré comme inscrit à une formation s'il est inscrit à au
     * moins une UE
     * appartenant à cette formation.
     * 
     * @param formation La formation dont on veut récupérer les étudiants inscrits
     * @return La liste des étudiants inscrits à cette formation, ou une liste vide
     *         si aucun étudiant n'est trouvé
     */
    public List<Etudiant> getEtudiantsByFormation(Formation formation) {
        if (formation == null) {
            return new ArrayList<>();
        }

        // EntityManagerFactory emf = null;
        // EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU");
            em = emf.createEntityManager();

            // Requête JPQL pour récupérer les étudiants inscrits à au moins une UE de cette
            // formation
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT DISTINCT e FROM Etudiant e JOIN e.ues ue WHERE ue.formation = :formation",
                    Etudiant.class);
            query.setParameter("formation", formation);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
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
            // new LoginFrame();
            new Connexion().setVisible(true);
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