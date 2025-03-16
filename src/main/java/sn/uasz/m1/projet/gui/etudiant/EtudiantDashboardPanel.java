package sn.uasz.m1.projet.gui.etudiant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; 

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.UEDAO;
import sn.uasz.m1.projet.gui.Connexion;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;

public class EtudiantDashboardPanel extends JPanel {
    private final MainFrameEtudiant mainFrame;
    private final EtudiantDAO etudiantDAO;
    private final FormationDAO formationDAO;
    private final UEDAO ueDAO;
    
    private JLabel nameLabel;
    private JLabel ineLabel;
    private JLabel emailLabel; 
    private JLabel sexeLabel;
    private JLabel formationLabel;
    private JLabel groupeTdLabel;
    private JLabel groupeTpLabel;
    private JPanel ueObligatoiresPanel;
    private JPanel ueOptionellesPanel;
    private JButton inscriptionButton;
    private JButton logoutButton;
    
    private Etudiant etudiant;
    private Formation formation;
    private List<UE> uesObligatoires;
    private List<UE> uesOptionelles;
    private Set<UE> uesInscrites;
    private Map<Long, JCheckBox> optionCheckboxes;
    private boolean inscriptionValidee = false;
    
    public EtudiantDashboardPanel(MainFrameEtudiant mainFrame) {
        this.mainFrame = mainFrame;
        this.etudiantDAO = new EtudiantDAO();
        this.formationDAO = new FormationDAO();
        this.ueDAO = new UEDAO();
        this.optionCheckboxes = new HashMap<>();
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Création de l'en-tête
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Création du panneau central avec un JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Onglet Informations personnelles
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("Informations", FontIcon.of(MaterialDesign.MDI_ACCOUNT, 16, new Color(41, 128, 185)), infoPanel);
        
        // Onglet UEs obligatoires
        JPanel ueObligatoiresTab = createUEObligatoiresPanel();
        tabbedPane.addTab("UEs Obligatoires", FontIcon.of(MaterialDesign.MDI_BOOK_OPEN, 16, new Color(41, 128, 185)), ueObligatoiresTab);
        
        // Onglet UEs optionnelles
        JPanel ueOptionellesTab = createUEOptionellesPanel();
        tabbedPane.addTab("UEs Optionnelles", FontIcon.of(MaterialDesign.MDI_BOOK_PLUS, 16, new Color(41, 128, 185)), ueOptionellesTab);
        
        // Onglet Groupes
        JPanel groupesTab = createGroupesPanel();
        // tabbedPane.addTab("Groupes", FontIcon.of(MaterialDesign.MDI_ACCOUNT_GROUP, 16, new Color(41, 128, 185)), groupesTab);
        tabbedPane.addTab("Groupes", FontIcon.of(MaterialDesign.MDI_GROUP, 16, new Color(41, 128, 185)), groupesTab);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Création du pied de page
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Tableau de bord étudiant", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setIcon(FontIcon.of(MaterialDesign.MDI_VIEW_DASHBOARD, 24, new Color(41, 128, 185)));
        
        nameLabel = new JLabel("", JLabel.RIGHT);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        logoutButton = new JButton();
        logoutButton.setIcon(FontIcon.of(MaterialDesign.MDI_LOGOUT, 16, new Color(231, 76, 60)));
        logoutButton.setToolTipText("Déconnexion");
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(nameLabel);
        rightPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder(null, "Informations personnelles", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, new Color(41, 128, 185)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Création des étiquettes
        // ineLabel = createInfoLabel("INE :", MaterialDesign.MDI_CARD_ACCOUNT_DETAILS);
        ineLabel = createInfoLabel("INE :", MaterialDesign.MDI_DETAILS);
        emailLabel = createInfoLabel("Email :", MaterialDesign.MDI_EMAIL);
        sexeLabel = createInfoLabel("Sexe :", MaterialDesign.MDI_GENDER_MALE_FEMALE);
        formationLabel = createInfoLabel("Formation :", MaterialDesign.MDI_SCHOOL);
        
        // Ajout des composants à la grille
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("INE :"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(ineLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Email :"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(emailLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(new JLabel("Sexe :"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(sexeLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(new JLabel("Formation :"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(formationLabel, gbc);
        
        // Ajout d'un panel de remplissage pour pousser le contenu vers le haut
        JPanel fillerPanel = new JPanel();
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(fillerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUEObligatoiresPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        ueObligatoiresPanel = new JPanel();
        ueObligatoiresPanel.setLayout(new BoxLayout(ueObligatoiresPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(ueObligatoiresPanel);
        scrollPane.setBorder(new TitledBorder(null, "UEs Obligatoires", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, new Color(41, 128, 185)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUEOptionellesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        ueOptionellesPanel = new JPanel();
        ueOptionellesPanel.setLayout(new BoxLayout(ueOptionellesPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(ueOptionellesPanel);
        scrollPane.setBorder(new TitledBorder(null, "UEs Optionnelles", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, new Color(41, 128, 185)));
        
        inscriptionButton = new JButton("S'inscrire aux UEs sélectionnées");
        inscriptionButton.setIcon(FontIcon.of(MaterialDesign.MDI_CHECKBOX_MARKED_CIRCLE, 16, Color.WHITE));
        inscriptionButton.setBackground(new Color(39, 174, 96));
        inscriptionButton.setForeground(Color.WHITE);
        inscriptionButton.setFocusPainted(false);
        inscriptionButton.addActionListener(e -> inscrireUEsOptionelles());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(inscriptionButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGroupesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel groupesPanel = new JPanel(new GridBagLayout());
        groupesPanel.setBorder(new TitledBorder(null, "Mes Groupes", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, new Color(41, 128, 185)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Création des étiquettes
        // groupeTdLabel = createInfoLabel("Groupe TD :", MaterialDesign.MDI_ACCOUNT_GROUP);
        groupeTdLabel = createInfoLabel("Groupe TD :", MaterialDesign.MDI_GROUP);
        groupeTpLabel = createInfoLabel("Groupe TP :", MaterialDesign.MDI_LAPTOP);
        
        // Ajout des composants à la grille
        gbc.gridx = 0;
        gbc.gridy = 0;
        groupesPanel.add(new JLabel("Groupe TD :"), gbc);
        
        gbc.gridx = 1;
        groupesPanel.add(groupeTdLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        groupesPanel.add(new JLabel("Groupe TP :"), gbc);
        
        gbc.gridx = 1;
        groupesPanel.add(groupeTpLabel, gbc);
        
        // Ajout d'un panel de remplissage pour pousser le contenu vers le haut
        JPanel fillerPanel = new JPanel();
        panel.add(groupesPanel, BorderLayout.NORTH);
        panel.add(fillerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2025 - Université Assane Seck de Ziguinchor");
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private JLabel createInfoLabel(String text, MaterialDesign icon) {
        JLabel label = new JLabel(text);
        label.setIcon(FontIcon.of(icon, 16, new Color(41, 128, 185)));
        return label;
    }
    
    public void loadEtudiantData(Long etudiantId) {
        try {
            System.out.println("###### Chargement des données de l'étudiant avec l'ID: " + etudiantId);
            // Chargement des données de l'étudiant
            etudiant = etudiantDAO.findById(etudiantId);
            if (etudiant == null) {
                JOptionPane.showMessageDialog(this, "Étudiant non trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
                new Connexion().setVisible(true);
                return;
            }
            
            // Mise à jour des labels d'information
            nameLabel.setText(etudiant.getPrenom() + " " + etudiant.getNom() + " ");
            ineLabel.setText(etudiant.getIne());
            emailLabel.setText(etudiant.getEmail());
            sexeLabel.setText(etudiant.getSexe().toString());
            
            // Chargement de la formation de l'étudiant
            formation = formationDAO.findFormationByEtudiant(etudiantId);
            System.out.println("### formation : " +formation);
            if (formation != null) {
                formationLabel.setText(formation.getNom() + " - " + formation.getNiveau());
                
                // Chargement des UEs obligatoires et optionnelles
                uesObligatoires = ueDAO.findUEsObligatoiresByFormation(formation.getId());
                uesOptionelles = ueDAO.findUEsOptionnellesByFormation(formation.getId());
                
                // Chargement des UEs auxquelles l'étudiant est inscrit
                uesInscrites = etudiant.getUes();
                
                // Vérification si l'inscription est validée
                inscriptionValidee = etudiantDAO.isInscriptionValidee(etudiantId);
                
                // Mise à jour des interfaces
                updateUEsObligatoiresPanel();
                updateUEsOptionellesPanel();


                // etudiant = etudiantDAO.findById(etudiantId);
                // System.out.println("#### Étudiant chargé: " + (etudiant != null ? etudiant.getNom() : "NULL"));

                // formation = formationDAO.findFormationByEtudiant(etudiantId);
                // System.out.println("#### Formation chargée: " + (formation != null ? formation.getNom() : "NULL"));

                // uesInscrites = etudiant.getUes();
                // System.out.println("#### UEs inscrites: " + (uesInscrites != null ? uesInscrites.size() : "NULL"));
            } else {
                formationLabel.setText("Non assigné");
            }
            
            // Affichage des groupes
            Groupe groupe = etudiant.getGroupe();
            if (groupe != null) {
                // groupeTdLabel.setText("Groupe " + groupe.getNumeroTD());
                // groupeTpLabel.setText("Groupe " + groupe.getNumeroTP());
                groupeTdLabel.setText("Groupe " + groupe.getNumero());
                groupeTpLabel.setText("Groupe " + groupe.getNumero());
            } else {
                groupeTdLabel.setText("Non assigné");
                groupeTpLabel.setText("Non assigné");
            }
            
            // Mise à jour du bouton d'inscription
            inscriptionButton.setEnabled(!inscriptionValidee && formation != null);
            if (inscriptionValidee) {
                inscriptionButton.setText("Inscription validée");
                inscriptionButton.setIcon(FontIcon.of(MaterialDesign.MDI_CHECK_CIRCLE, 16, Color.WHITE));
                inscriptionButton.setBackground(new Color(149, 165, 166));
            } else {
                inscriptionButton.setText("S'inscrire aux UEs sélectionnées");
                inscriptionButton.setIcon(FontIcon.of(MaterialDesign.MDI_CHECKBOX_MARKED_CIRCLE, 16, Color.WHITE));
                inscriptionButton.setBackground(new Color(39, 174, 96));
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erreur lors du chargement des données: " + e.getMessage());
        }
    }
    
    private void updateUEsObligatoiresPanel() {
        ueObligatoiresPanel.removeAll();
        
        if (uesObligatoires != null && !uesObligatoires.isEmpty()) {
            for (UE ue : uesObligatoires) {
                JPanel ueItemPanel = createUEItemPanel(ue, true, false);
                ueObligatoiresPanel.add(ueItemPanel);
                ueObligatoiresPanel.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel noUELabel = new JLabel("Aucune UE obligatoire disponible", JLabel.CENTER);
            noUELabel.setForeground(Color.GRAY);
            ueObligatoiresPanel.add(noUELabel);
        }
        
        ueObligatoiresPanel.revalidate();
        ueObligatoiresPanel.repaint();
    }
    
    private void updateUEsOptionellesPanel() {
        ueOptionellesPanel.removeAll();
        optionCheckboxes.clear();
        
        if (uesOptionelles != null && !uesOptionelles.isEmpty()) {
            for (UE ue : uesOptionelles) {
                boolean isInscrit = uesInscrites != null && uesInscrites.contains(ue);
                JPanel ueItemPanel = createUEItemPanel(ue, isInscrit, true);
                ueOptionellesPanel.add(ueItemPanel);
                ueOptionellesPanel.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel noUELabel = new JLabel("Aucune UE optionnelle disponible", JLabel.CENTER);
            noUELabel.setForeground(Color.GRAY);
            ueOptionellesPanel.add(noUELabel);
        }
        
        ueOptionellesPanel.revalidate();
        ueOptionellesPanel.repaint();
    }
    
    private JPanel createUEItemPanel(UE ue, boolean isInscrit, boolean isOptionnel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        if (isInscrit) {
            panel.setBackground(new Color(240, 255, 240));
        }
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JLabel codeLabel = new JLabel(ue.getCode());
        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        codeLabel.setForeground(new Color(41, 128, 185));
        
        JLabel nomLabel = new JLabel(ue.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        leftPanel.add(codeLabel, BorderLayout.NORTH);
        leftPanel.add(nomLabel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 2, 10, 0));
        rightPanel.setOpaque(false);
        
        JLabel volumeLabel = new JLabel("Volume: " + ue.getVolumeHoraire() + "h");
        JLabel coeffLabel = new JLabel("Coefficient: " + ue.getCoefficient());
        // JLabel creditsLabel = new JLabel("Crédits: " + ue.getNombreCredits());
        // JLabel responsableLabel = new JLabel("Resp.: " + ue.getResponsable());
        JLabel creditsLabel = new JLabel("Crédits: " + ue.getCredits());
        JLabel responsableLabel = new JLabel("Resp.: " + ue.getNomResponsable());
        
        rightPanel.add(volumeLabel);
        rightPanel.add(coeffLabel);
        rightPanel.add(creditsLabel);
        rightPanel.add(responsableLabel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(rightPanel, BorderLayout.CENTER);
        
        if (isOptionnel) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setOpaque(false);
            
            if (isInscrit) {
                checkBox.setSelected(true);
                checkBox.setEnabled(!inscriptionValidee);
            } else {
                checkBox.setSelected(false);
                checkBox.setEnabled(!inscriptionValidee);
            }
            
            optionCheckboxes.put(ue.getId(), checkBox);
            centerPanel.add(checkBox, BorderLayout.EAST);
        }
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void inscrireUEsOptionelles() {
        if (inscriptionValidee) {
            JOptionPane.showMessageDialog(this, 
                    "Votre inscription pédagogique a déjà été validée.", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            // Récupération des UEs sélectionnées
            List<Long> selectedUEIds = new ArrayList<>();
            
            for (Map.Entry<Long, JCheckBox> entry : optionCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedUEIds.add(entry.getKey());
                }
            }
            
            // Vérification du nombre d'options requis
            int requiredOptions = formation.getNombreOptionsRequis();
            
            if (selectedUEIds.size() < requiredOptions) {
                JOptionPane.showMessageDialog(this, 
                        "Vous devez sélectionner au moins " + requiredOptions + " UE(s) optionnelle(s).", 
                        "Erreur d'inscription", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirmation
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Êtes-vous sûr de vouloir vous inscrire aux UEs sélectionnées ?\n" +
                    "Une fois validée par le responsable pédagogique, cette inscription ne pourra plus être modifiée.", 
                    "Confirmation d'inscription", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Inscription aux UEs sélectionnées
                boolean success = etudiantDAO.inscrireUEsOptionelles(etudiant.getId(), selectedUEIds);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                            "Votre demande d'inscription a été soumise avec succès.\n" +
                            "Vous recevrez une notification par email une fois que votre inscription sera validée.", 
                            "Inscription réussie", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    // Rechargement des données
                    loadEtudiantData(etudiant.getId());
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Une erreur est survenue lors de l'inscription.", 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'inscription: " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir vous déconnecter ?",
            "Confirmation de déconnexion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        mainFrame.dispose();
        
        if (confirm == JOptionPane.YES_OPTION) {
            // mainFrame.showLogin();
            new Connexion().setVisible(true);
        }
    }
}