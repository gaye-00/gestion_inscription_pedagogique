package sn.uasz.m1.projet.gui.responsablePedagogique;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.dto.Etat;
import sn.uasz.m1.projet.gui.Connexion;
import sn.uasz.m1.projet.gui.responsablePedagogique.services.EnseignantService;
import sn.uasz.m1.projet.gui.responsablePedagogique.services.FormationService;
import sn.uasz.m1.projet.gui.responsablePedagogique.services.UeService;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.person.User;
import sn.uasz.m1.projet.model.person.Utilisateur;

public class FenetrePrincipal extends JFrame implements PanelSwitcher {
    private JPanel contentPanel;
    private JPanel dashboardPanel;
    private CardLayout cardLayout;
    private final Utilisateur currentUser;
    // private FormationDAO formationService = new FormationDAO();
    // private UEDAO ueDao = new UEDAO();
    private ResponsableDAO responsableService = new ResponsableDAO();

    // Constante pour le nom du panel dashboard
    private static final String DASHBOARD_PANEL = "Dashboard";
    private static final String GERER_FORMATION_PANEL = "Gérer Formations";
    private static final String GERER_ENSEIGNANT_PANEL = "Gérer Enseignants";
    private static final String NOUVELLE_FORMATION_PANEL = "Nouvelle Formation";
    private static final String NOUVELLE_UE_PANEL = "Nouvelle UE";
    private static final String NOUVEL_ENSEIGNANT_PANEL = "Nouveau Enseigant";
    private static final String NOUVELLE_INSCRIPTION_PANEL = "Nouvelle Inscription";
    private FormationService formationGUI = new FormationService();
    private EnseignantService enseignantGUI = new EnseignantService();
    private UeService UeGUI = new UeService();

    private EntityManagerFactory emf = null;
    private EntityManager em = null;

    public FenetrePrincipal(Utilisateur user) {
        this.currentUser = user;

        setTitle("Gestion Pédagogique");
        // Obtenir la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Définir la taille de la fenêtre pour correspondre à la taille de l'écran
        setSize(screenSize);
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
                "Dashboard",
                "Gérer Formations",
                "Gérer Enseignants"

        };

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Créer le dashboard
        dashboardPanel = createDashboardPanel();
        // reloadDashboard();
        contentPanel.add(dashboardPanel, DASHBOARD_PANEL);

        // Créer les panneaux de formulaires
        JPanel nouvelleFormationPanel = formationGUI.createNouvelleFormationPanel(user, this, this,
                GERER_FORMATION_PANEL);
        contentPanel.add(nouvelleFormationPanel, NOUVELLE_FORMATION_PANEL);

        JPanel nouvelleUEPanel = UeGUI.createNouvelleUEPanelExterne(this, DASHBOARD_PANEL);
        contentPanel.add(nouvelleUEPanel, NOUVELLE_UE_PANEL);

        JPanel nouvelEnseignantPanel = enseignantGUI.createNouvelEnseignantPanel(this, this, GERER_ENSEIGNANT_PANEL);
        contentPanel.add(nouvelEnseignantPanel, NOUVEL_ENSEIGNANT_PANEL);

        JPanel gererFormationsPanel = formationGUI.createGererFormationsPanel(this, this,
                NOUVELLE_FORMATION_PANEL);
        contentPanel.add(gererFormationsPanel, GERER_FORMATION_PANEL);

        // Dans le constructeur de FenetrePrincipal
        contentPanel.add(enseignantGUI.createGererEnseignantsPanel(this, this,
                NOUVEL_ENSEIGNANT_PANEL), GERER_ENSEIGNANT_PANEL);
        // Ajouter les autres panneaux
        for (String btnText : buttons) {
            JButton button = createSidebarButton(btnText);
            sidePanel.add(button);

            // Si ce n'est pas un panneau déjà créé, créer un nouveau panel pour cet élément
            if (!btnText.equals(DASHBOARD_PANEL) &&
                    !btnText.equals(NOUVELLE_FORMATION_PANEL) &&
                    !btnText.equals(NOUVELLE_UE_PANEL) &&
                    !btnText.equals(GERER_FORMATION_PANEL) &&
                    !btnText.equals(GERER_ENSEIGNANT_PANEL) && // Ajoutez cette condition
                    !btnText.equals(NOUVEL_ENSEIGNANT_PANEL)) {
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
        reloadDashboard();

        cardLayout.show(contentPanel, panelName);
    }

    private void reloadDashboard() {
        // Supprimer l'ancien panneau
        contentPanel.remove(dashboardPanel);

        // Recréer un nouveau panneau
        dashboardPanel = createDashboardPanel();

        // Ajouter le nouveau panneau au contentPanel
        contentPanel.add(dashboardPanel, DASHBOARD_PANEL);

        // Rafraîchir l'affichage
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createDashboardPanel() {
        if (dashboardPanel != null)
            contentPanel.remove(dashboardPanel);
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

        String[] actionBtns = { NOUVELLE_FORMATION_PANEL, NOUVELLE_UE_PANEL, NOUVEL_ENSEIGNANT_PANEL,
                NOUVELLE_INSCRIPTION_PANEL };

        for (String action : actionBtns) {
            JButton actionBtn = new JButton(action);
            actionBtn.setBackground(new Color(52, 152, 219));
            actionBtn.setForeground(Color.WHITE);
            actionBtn.setFocusPainted(false);
            actionBtn.addActionListener(e -> {
                if (action.equals(NOUVEL_ENSEIGNANT_PANEL)) {
                    cardLayout.show(contentPanel, NOUVEL_ENSEIGNANT_PANEL);
                } else if (action.equals(NOUVELLE_FORMATION_PANEL)) {
                    cardLayout.show(contentPanel, NOUVELLE_FORMATION_PANEL);
                } else if (action.equals(NOUVELLE_UE_PANEL)) {
                    cardLayout.show(contentPanel, NOUVELLE_UE_PANEL);
                } else if (action.equals(NOUVELLE_INSCRIPTION_PANEL)) {
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

        statData[2][0] = "Enseignants";
        statData[2][2] = "Nombre total d'Enseignants";

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

            statData[2][1] = etat.getEnseignants().toString();

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

        JLabel userInfoLabel = new JLabel(
                currentUser.getPrenom() + " " + currentUser.getNom() + " (" + currentUser.getEmail() + ")");
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