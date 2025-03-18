package sn.uasz.m1.projet.gui.responsablePedagogique.services;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import org.w3c.dom.events.MouseEvent;

import sn.uasz.m1.projet.dao.EnseignantDAO;
import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Utilisateur;

public class FormationService {
    private final ResponsableDAO responsableDAO;
    private final FormationDAO formationService;
    private final EtudiantDAO etudiantService;
    private final EnseignantDAO enseignantDAO;
    private final UeService ueService;
    static Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
    static Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    static Color DANGER_COLOR = new Color(231, 76, 60); // Red
    static Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
    static Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
    static Color BORDER_COLOR = new Color(189, 195, 199); // Border color
    private final String[] columnNamesFormation = { "Code", "Intitulé", "Niveau", "Responsable", "EmailResp",
            "Nb_ue_otionnel",
            "Max_Effectif_Groupe" };
    private DefaultTableModel tableModel;

    public FormationService() {
        this.responsableDAO = new ResponsableDAO();
        this.enseignantDAO = new EnseignantDAO();
        this.formationService = new FormationDAO();
        this.etudiantService = new EtudiantDAO();
        this.ueService = new UeService();
        tableModel = new DefaultTableModel(columnNamesFormation, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre les cellules non modifiables
            }
        };
    }

    public JPanel createNouvelleFormationPanel(Utilisateur user, FenetrePrincipal parent, PanelSwitcher panelSwitcher,
            String DASHBOARD_PANEL) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Nouvelle Formation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        SpinnerModel nombreUeOptModel = new SpinnerNumberModel(2, 1, 10, 2);
        JSpinner nombreUeOptSpinner = new JSpinner(nombreUeOptModel);

        SpinnerModel maxEffectifGroupeModel = new SpinnerNumberModel(10, 5, 50, 5);

        JSpinner maxEffectifGroupeSpinner = new JSpinner(maxEffectifGroupeModel);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs du formulaire
        String[] labels = { "Code de formation:", "Intitulé:", "Niveau:", "nombre d'ue Optionnel",
                "max Effectif groupe", "Responsable:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Pour le niveau, utiliser les valeurs de l'enum
        JComboBox<Niveau> niveauCombo = new JComboBox<>(Niveau.values());
        // JComboBox<String> niveauCombo = new JComboBox<>(niveaux);

        // Récupérer la liste des responsables pédagogiques depuis la base de données
        List<Enseignant> responsables = enseignantDAO.findAll();
        JComboBox<Enseignant> respCombo = new JComboBox<>(
                responsables.toArray(new Enseignant[0]));

        // Personnaliser l'affichage du ComboBox pour les responsables
        respCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Enseignant) {
                    Enseignant resp = (Enseignant) value;
                    setText(resp.getNom() + " " + resp.getPrenom());
                }
                return this;
            }
        });

        JComponent[] fields = { codeField, intituleField, niveauCombo, nombreUeOptSpinner, maxEffectifGroupeSpinner,
                respCombo };

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
                JOptionPane.showMessageDialog(parent,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer une nouvelle formation
            Formation nouvelleFormation = new Formation();
            nouvelleFormation.setCode(codeField.getText());
            nouvelleFormation.setNom(intituleField.getText());
            nouvelleFormation.setNiveau((Niveau) niveauCombo.getSelectedItem());
            nouvelleFormation.setResponsableFormation((Enseignant) respCombo.getSelectedItem());
            nouvelleFormation.setNombreOptionsRequis((int) nombreUeOptSpinner.getValue());
            nouvelleFormation.setMaxEffectifGroupe((int) maxEffectifGroupeSpinner.getValue());
            nouvelleFormation.setResponsable((ResponsablePedagogique) user);
            if (respCombo.getItemCount() > 0 && respCombo.getSelectedItem() != null)
                nouvelleFormation.setResponsableFormation((Enseignant) respCombo.getSelectedItem());
            // Sauvegarder dans la base de données
            boolean success = formationService.create(nouvelleFormation);
            if (success) {
                JOptionPane.showMessageDialog(null,
                        "Formation créée avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser les champs
                codeField.setText("");
                intituleField.setText("");
                if (niveauCombo.getItemCount() > 0) {
                    niveauCombo.setSelectedIndex(0);
                } else {
                    // Optionnel : gérer le cas où il n'y a pas d'éléments
                    System.out.println("Le JComboBox Niveau est vide.");
                }

                if (respCombo.getItemCount() > 0) {
                    respCombo.setSelectedIndex(0);
                } else {
                    // Optionnel : gérer le cas où il n'y a pas d'éléments
                    System.out.println("Le JComboBox Responsable est vide.");
                }

                refreshTable(tableModel);
                // Revenir au tableau de bord via la méthode fournie
                panelSwitcher.showPanel(DASHBOARD_PANEL);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de la création de la formation. Veuillez réessayer.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        });

        annulerButton.addActionListener(e -> {
            // cardLayout.show(contentPanel, DASHBOARD_PANEL);
            panelSwitcher.showPanel(DASHBOARD_PANEL);
        });

        return panel;
    }

    public JPanel createGererFormationsPanel(FenetrePrincipal parent, JPanel contentPanel, CardLayout cardLayout,
            String NOUVELLE_FORMATION_PANEL) {
        // Define a consistent color palette

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a more attractive title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titleLabel = new JLabel("Gérer les Formations", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Panneau central avec tableau et formulaire
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));



        // Créer le tableau
        JTable formationsTable = new JTable(tableModel);

        // Better table styling
        formationsTable.setRowHeight(30);
        formationsTable.setIntercellSpacing(new Dimension(5, 5));
        formationsTable.setShowGrid(true);
        formationsTable.setGridColor(new Color(230, 230, 230));
        formationsTable.setSelectionBackground(PRIMARY_COLOR);
        formationsTable.setSelectionForeground(Color.WHITE);
        formationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        formationsTable.getTableHeader().setBackground(new Color(220, 220, 220));
        formationsTable.getTableHeader().setForeground(TEXT_COLOR);
        formationsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        formationsTable.getTableHeader().setReorderingAllowed(false);
        formationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        formationsTable.setFillsViewportHeight(true);

        // Ajouter un TableRowSorter pour le filtrage
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        formationsTable.setRowSorter(sorter);

        // Enhanced search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)));

        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JTextField searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(250, 30));
        // Style text field
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));

        JLabel inLabel = new JLabel(" dans ");
        inLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        inLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JComboBox<String> searchColumnCombo = new JComboBox<>(columnNamesFormation);
        searchColumnCombo.setMaximumSize(new Dimension(150, 30));
        searchColumnCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(inLabel);
        searchPanel.add(searchColumnCombo);
        searchPanel.add(Box.createHorizontalGlue());

        // Ajouter un DocumentListener pour le champ de recherche
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

        // Panneau de défilement pour le tableau avec bordure
        JScrollPane scrollPane = new JScrollPane(formationsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panneau d'informations et de modifications avec style amélioré
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR),
                        "Détails de la formation"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Champs du formulaire de modification
        String[] labels = { "Code:", "Intitulé:", "Niveau:", "nombre d'ue Optionnel",
                "max Effectif groupe", "Responsable:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);
        JComboBox<Niveau> niveauCombo = new JComboBox<>(Niveau.values());
        SpinnerModel nombreUeOptModel = new SpinnerNumberModel(2, 1, 10, 2);
        JSpinner nombreUeOptSpinner = new JSpinner(nombreUeOptModel);

        SpinnerModel maxEffectifGroupeModel = new SpinnerNumberModel(10, 5, 50, 5);

        JSpinner maxEffectifGroupeSpinner = new JSpinner(maxEffectifGroupeModel);

        // Style text fields
        styleTextField(codeField);
        styleTextField(intituleField);

        // Style combobox
        niveauCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        // Récupérer la liste des responsables pédagogiques depuis la base de données
        List<Enseignant> responsables = enseignantDAO.findAll();
        JComboBox<Enseignant> respCombo = new JComboBox<>(
                responsables.toArray(new Enseignant[0]));

        // Personnaliser l'affichage du ComboBox pour les responsables
        respCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Enseignant) {
                    Enseignant resp = (Enseignant) value;
                    setText(resp.getNom() + " " + resp.getPrenom());
                }
                return this;
            }
        });

        JComponent[] fields = { codeField, intituleField, niveauCombo, nombreUeOptSpinner, maxEffectifGroupeSpinner,
                respCombo };

        // Ajouter les champs au panneau d'informations avec style amélioré
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(TEXT_COLOR);

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

        JButton listeUEsButton = new JButton("Gérer UEs");
        JButton listeEtudiantsButton = new JButton("Lister Étudiants inscrits");

        // Style buttons
        styleButton(listeUEsButton, PRIMARY_COLOR);
        styleButton(listeEtudiantsButton, PRIMARY_COLOR);

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

        // Button panel with improved layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton refreshButton = new JButton("Rafraîchir");
        JButton saveButton = new JButton("Enregistrer");
        JButton deleteButton = new JButton("Supprimer");
        JButton addButton = new JButton("Nouvelle Formation");

        // Style buttons
        styleButton(refreshButton, new Color(149, 165, 166)); // Gray
        styleButton(addButton, PRIMARY_COLOR);
        styleButton(saveButton, SUCCESS_COLOR);
        styleButton(deleteButton, DANGER_COLOR);

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        // Add status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        statusPanel.setBackground(new Color(236, 240, 241));

        JLabel statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Combine button panel and status bar
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(BACKGROUND_COLOR);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        panel.add(southPanel, BorderLayout.SOUTH);

        // État initial des boutons
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Variables pour garder la trace de la formation sélectionnée
        final int[] selectedRow = { -1 };
        final Formation[] selectedFormation = { null };

        // Fonction pour charger les données
        Runnable loadData = () -> {
            tableModel.setRowCount(0); // Effacer les données existantes
            statusLabel.setText("Chargement des données...");

            // Charger les formations 
            List<Formation> formations = formationService.findAll();
            for (Formation formation : formations) {
                tableModel.addRow(new Object[] {
                        formation.getCode(),
                        formation.getNom(),
                        formation.getNiveau(),
                        (formation.getResponsableFormation() != null)
                                ? formation.getResponsableFormation()
                                : null,
                        (formation.getResponsableFormation() != null) ? formation.getResponsableFormation().getEmail()
                                : null,
                        formation.getNombreOptionsRequis(),
                        formation.getMaxEffectifGroupe()
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
            if (niveauCombo.getItemCount() > 0) {
                niveauCombo.setSelectedIndex(0);
            }
            if (respCombo.getItemCount() > 0) {
                respCombo.setSelectedIndex(0);
            }
            nombreUeOptSpinner.setValue(2);
            maxEffectifGroupeSpinner.setValue(10);

            // Réinitialiser le champ de recherche
            searchField.setText("");

            statusLabel.setText("Données chargées. " + formations.size() + " formations trouvées.");
        };

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
                    statusLabel.setText("Formation sélectionnée: " + code);

                    List<Formation> formations = formationService.findAll();
                    // Trouver la formation correspondante
                    for (Formation formation : formations) {
                        if (formation.getCode().equals(code)) {
                            selectedFormation[0] = formation;
                            break;
                        }
                    }

                    // Remplir les champs
                    if (selectedFormation[0] != null) {
                        codeField.setText(selectedFormation[0].getCode());
                        intituleField.setText(selectedFormation[0].getNom());
                        niveauCombo.setSelectedItem(selectedFormation[0].getNiveau());

                        nombreUeOptSpinner.setValue(selectedFormation[0].getNombreOptionsRequis());
                        maxEffectifGroupeSpinner.setValue(selectedFormation[0].getMaxEffectifGroupe());
                        if (selectedFormation[0].getResponsableFormation() != null) {
                            respCombo.setSelectedItem(selectedFormation[0].getResponsableFormation());
                        } else {
                            if (respCombo.getItemCount() > 0)
                                respCombo.setSelectedIndex(0);
                        }
                        // Activer les boutons
                        saveButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        listeUEsButton.setEnabled(true);
                        listeEtudiantsButton.setEnabled(true);
                    }
                }
            }
        });

        // Actions des boutons avec animations et retour visuel
        refreshButton.addActionListener(e -> {
            // Animation simple pour le bouton rafraîchir
            refreshButton.setEnabled(false);
            Timer timer = new Timer(1000, event -> {
                refreshButton.setEnabled(true);
                ((Timer) event.getSource()).stop();
            });
            timer.start();

            // Mise à jour du statut et rechargement des données
            statusLabel.setText("Actualiser");
            loadData.run();
        });

        addButton.addActionListener(e -> {
            // Effet de transition lors du changement de panel
            statusLabel.setText("Nouvelle formation");

            // Animation de transition
            centerPanel.setVisible(false);
            Timer timer = new Timer(100, event -> {
                centerPanel.setVisible(true);
                cardLayout.show(contentPanel, NOUVELLE_FORMATION_PANEL);
                ((Timer) event.getSource()).stop();
            });
            timer.start();
        });

        saveButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                // Vérifier que les champs requis sont remplis
                if (codeField.getText().isEmpty() || intituleField.getText().isEmpty()) {
                    // Style pour la boîte de dialogue d'erreur
                    UIManager.put("OptionPane.background", Color.WHITE);
                    UIManager.put("Panel.background", Color.WHITE);
                    UIManager.put("OptionPane.messageForeground", DANGER_COLOR);
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
                    UIManager.put("Button.background", PRIMARY_COLOR);
                    UIManager.put("Button.foreground", Color.WHITE);

                    JOptionPane.showMessageDialog(parent,
                            "Veuillez remplir tous les champs obligatoires.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Animation pour le bouton d'enregistrement
                saveButton.setBackground(new Color(39, 174, 96)); // Vert plus foncé pour l'effet
                Timer timer = new Timer(300, event -> {
                    saveButton.setBackground(SUCCESS_COLOR);
                    ((Timer) event.getSource()).stop();
                });
                timer.start();

                // Mise à jour du statut
                statusLabel.setText("Enregistrement en cours...");

                // Mettre à jour la formation
                selectedFormation[0].setCode(codeField.getText());
                selectedFormation[0].setNom(intituleField.getText());
                selectedFormation[0].setNiveau((Niveau) niveauCombo.getSelectedItem());
                if (respCombo.getItemCount() > 0 && respCombo.getSelectedItem() != null) {
                    selectedFormation[0].setResponsableFormation((Enseignant) respCombo.getSelectedItem());
                }

                selectedFormation[0].setNombreOptionsRequis((int) nombreUeOptSpinner.getValue());
                selectedFormation[0].setMaxEffectifGroupe((int) maxEffectifGroupeSpinner.getValue());

                // Enregistrer les modifications
                formationService.update(selectedFormation[0]);

                // Style pour la boîte de dialogue de succès
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", SUCCESS_COLOR);
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
                UIManager.put("Button.background", PRIMARY_COLOR);
                UIManager.put("Button.foreground", Color.WHITE);

                JOptionPane.showMessageDialog(parent,
                        "Formation mise à jour avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Mise à jour du statut et rechargement
                statusLabel.setText("Formation mise à jour: " + selectedFormation[0].getCode());
                loadData.run();
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                // Style pour la boîte de dialogue de confirmation
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
                UIManager.put("Button.background", PRIMARY_COLOR);
                UIManager.put("Button.foreground", Color.WHITE);

                int confirm = JOptionPane.showConfirmDialog(parent,
                        "Êtes-vous sûr de vouloir supprimer cette formation?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Animation pour le bouton de suppression
                    deleteButton.setBackground(new Color(192, 57, 43)); // Rouge plus foncé pour l'effet
                    Timer timer = new Timer(300, event -> {
                        deleteButton.setBackground(DANGER_COLOR);
                        ((Timer) event.getSource()).stop();
                    });
                    timer.start();

                    // Mise à jour du statut
                    statusLabel.setText("Suppression en cours...");

                    // Supprimer la formation
                    formationService.delete(selectedFormation[0].getId());

                    // Style pour la boîte de dialogue de succès
                    UIManager.put("OptionPane.messageForeground", SUCCESS_COLOR);

                    JOptionPane.showMessageDialog(parent,
                            "Formation supprimée avec succès!",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);

                    // Mise à jour du statut et rechargement
                    statusLabel.setText("Formation supprimée");
                    loadData.run();
                }
            }
        });

        // Actions pour les boutons de gestion UE et étudiants avec effets visuels
        listeUEsButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                // Animation pour le bouton
                listeUEsButton.setBackground(new Color(41, 128, 185)); // Bleu plus foncé pour l'effet
                Timer timer = new Timer(300, event -> {
                    listeUEsButton.setBackground(PRIMARY_COLOR);
                    ((Timer) event.getSource()).stop();
                });
                timer.start();

                // Mise à jour du statut
                statusLabel.setText("Ouverture de la gestion des UEs pour " + selectedFormation[0].getNom() + "...");

                // Afficher la gestion des UEs
                ueService.showGestionUE(parent, selectedFormation[0]);
            }
        });

        listeEtudiantsButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                // Animation pour le bouton
                listeEtudiantsButton.setBackground(new Color(41, 128, 185)); // Bleu plus foncé pour l'effet
                Timer timer = new Timer(300, event -> {
                    listeEtudiantsButton.setBackground(PRIMARY_COLOR);
                    ((Timer) event.getSource()).stop();
                });
                timer.start();

                // Mise à jour du statut
                statusLabel.setText("Affichage des étudiants inscrits à " + selectedFormation[0].getNom() + "...");

                // Afficher la liste des étudiants
                showEtudiantsList(parent, selectedFormation[0]);
            }
        });

        return panel;
    }

    // Méthode pour styliser les champs de texte de manière cohérente
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
    }

    // Méthode pour styliser les boutons de manière cohérente
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(200, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ajouter des effets de survol avec un MouseListener
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Assombrir légèrement la couleur au survol
                button.setBackground(darkenColor(bgColor, 0.1f));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Restaurer la couleur d'origine
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Assombrir davantage la java.awt.event.MouseEvent lors du clic
                button.setBackground(darkenColor(bgColor, 0.2f));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // Restaurer la couleur de survol
                button.setBackground(darkenColor(bgColor, 0.1f));
            }
        });
    }

    // Méthode pour assombrir une couleur (pour les effets de survol)
    private Color darkenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], Math.max(0, hsb[2] - factor));
    }

    public void showEtudiantsList(FenetrePrincipal parent, Formation formation) {
        Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
        Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
        Color DANGER_COLOR = new Color(231, 76, 60); // Red
        Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
        Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
        Color BORDER_COLOR = new Color(189, 195, 199); // Border color

        // Création du panneau principal avec une marge intérieure
        JPanel etudiantsPanel = new JPanel(new BorderLayout(10, 10));
        etudiantsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        etudiantsPanel.setBackground(new Color(240, 240, 240));

        // Panneau d'en-tête avec dégradé
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10)) {
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
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Titre avec une police moderne et une couleur claire
        JLabel titleLabel = new JLabel("Gestion des Étudiants de la formation  " + formation.getNom(), JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Champ de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrer le champ de recherche
        JLabel searchLabel = new JLabel("Rechercher:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JTextField searchField = new JTextField(20);
        // searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // searchField.setForeground(Color.DARK_GRAY);
        searchField.setMaximumSize(new Dimension(250, 30));
        // Style text field
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));

        searchField.setPreferredSize(new Dimension(200, 30));

        // Ajouter le titre et le champ de recherche au panneau d'en-tête
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(searchPanel);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        etudiantsPanel.add(headerPanel, BorderLayout.NORTH);

        // Configuration de la table avec un modèle personnalisé
        String[] columnNames = { "INE", "Prénom", "Nom", "Adresse", "DateNaissance", "Email", "Groupe", "Inscription" };

        DefaultTableModel etudiantsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Empêche l'édition directe dans la table
            }
        };

        JTable etudiantsTable = new JTable(etudiantsTableModel);
        etudiantsTable.setRowHeight(30);
        etudiantsTable.setIntercellSpacing(new Dimension(5, 5));
        etudiantsTable.setShowGrid(true);
        etudiantsTable.setGridColor(new Color(230, 230, 230));
        etudiantsTable.setSelectionBackground(PRIMARY_COLOR);
        etudiantsTable.setSelectionForeground(Color.WHITE);
        etudiantsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        etudiantsTable.getTableHeader().setBackground(new Color(220, 220, 220));
        etudiantsTable.getTableHeader().setForeground(TEXT_COLOR);
        etudiantsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        etudiantsTable.getTableHeader().setReorderingAllowed(false);
        etudiantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        etudiantsTable.setFillsViewportHeight(true);

        JTableHeader header = etudiantsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));

        // Définir les largeurs de colonnes
        etudiantsTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Code
        etudiantsTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Intitulé
        etudiantsTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Crédits
        etudiantsTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Description
        etudiantsTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Caractéristique

        // Variables pour garder la trace de la formation sélectionnée
        final int[] selectedRow = { -1 };
        final Etudiant[] selectedEtudiant = { null };

        Runnable loadData = () -> {
            // Effacer les données existantes
            etudiantsTableModel.setRowCount(0);
            // Récupérer les étudiants liés à cette formation
            List<Etudiant> formationEtudiants = etudiantService.getEtudiantsByFormation(formation);
            // Dans votre code principal:
            for (Etudiant etudiant : formationEtudiants) {
                // Déterminer le statut d'inscription
                String status = etudiant.isInscriptionValidee() ? "✅Validée" : "❌En Attente";

                // Ajouter la ligne avec le statut en texte (le renderer s'occupera de
                // l'affichage)
                etudiantsTableModel.addRow(new Object[] {
                        etudiant.getIne(),
                        etudiant.getPrenom(),
                        etudiant.getNom(),
                        etudiant.getAdresse(),
                        etudiant.getDateNaissance(),
                        etudiant.getEmail(),
                        (etudiant.getGroupe() != null) ? etudiant.getGroupe().getNumero() : "Non Affecté",
                        status
                });
            }

            // Configurer le renderer pour la colonne de statut (supposons que c'est la
            // colonne 4)
            etudiantsTable.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());

            // Filtrage en temps réel
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterTable(etudiantsTableModel, etudiantsTable, searchField.getText(), formationEtudiants);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterTable(etudiantsTableModel, etudiantsTable, searchField.getText(), formationEtudiants);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterTable(etudiantsTableModel, etudiantsTable, searchField.getText(), formationEtudiants);
                }
            });
        };
        loadData.run();
        // Ajouter la table dans un JScrollPane avec style
        JScrollPane scrollPane = new JScrollPane(etudiantsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        etudiantsPanel.add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons en bas
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        // buttonsPanel.setLayout(new FlowLayout();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        etudiantsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        JButton saveButton = new JButton("VALIDER ");
        JButton annulerButton = new JButton("ANNULER ");
        // Style buttons
        styleButton(saveButton, SUCCESS_COLOR);
        styleButton(annulerButton, DANGER_COLOR);
        saveButton.setEnabled(false);
        annulerButton.setEnabled(false);
        // Écouteur de sélection pour le tableau
        etudiantsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow[0] = etudiantsTable.getSelectedRow();
                if (selectedRow[0] >= 0) {
                    // Convertir l'index de la ligne du modèle de vue au modèle de données
                    int modelRow = etudiantsTable.convertRowIndexToModel(selectedRow[0]);
                    String code = etudiantsTableModel.getValueAt(modelRow, 0).toString();
                    // statusLabel.setText("Etudiant sélectionné: " + code);
                    List<Etudiant> formationEtudiants = etudiantService.getEtudiantsByFormation(formation);
                    for (Etudiant etudiant : formationEtudiants) {
                        if (etudiant.getIne().equals(code)) {
                            selectedEtudiant[0] = etudiant;
                            break;
                        }
                    }
                    // Remplir les champs
                    if (selectedEtudiant[0] != null && !selectedEtudiant[0].isInscriptionValidee()) {

                        // Activer les boutons
                        saveButton.setEnabled(true);
                        annulerButton.setEnabled(false);

                    } else {
                        saveButton.setEnabled(false);
                        annulerButton.setEnabled(true);
                    }
                }
            }
        });

        saveButton.addActionListener(e -> {
            if (selectedEtudiant[0] != null) {
                // Animation pour le bouton d'enregistrement
                saveButton.setBackground(new Color(39, 174, 96)); // Vert plus foncé pour l'effet
                Timer timer = new Timer(300, event -> {
                    saveButton.setBackground(SUCCESS_COLOR);
                    ((Timer) event.getSource()).stop();
                });
                timer.start();
                // Enregistrer les modifications
                etudiantService.validerInscription(selectedEtudiant[0].getId());
                // Style pour la boîte de dialogue de succès
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", SUCCESS_COLOR);
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
                UIManager.put("Button.background", PRIMARY_COLOR);
                UIManager.put("Button.foreground", Color.WHITE);

                JOptionPane.showMessageDialog(parent,
                        "Inscription validée avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadData.run();
            }
        });

        annulerButton.addActionListener(e -> {
            if (selectedEtudiant[0] != null) {
                // Couleur initiale et animation pour le bouton
                Color initialColor = annulerButton.getBackground();
                annulerButton.setBackground(new Color(231, 76, 60)); // Rouge clair pour effet visuel

                Timer timer = new Timer(300, event -> {
                    annulerButton.setBackground(initialColor);
                    ((Timer) event.getSource()).stop();
                });
                timer.start();

                // Annulation de l'inscription
                etudiantService.invaliderInscription(selectedEtudiant[0].getId());

                // Personnalisation des couleurs de la boîte de dialogue
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", new Color(39, 174, 96)); // Vert de confirmation
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
                UIManager.put("Button.background", new Color(41, 128, 185)); // Bleu primaire
                UIManager.put("Button.foreground", Color.WHITE);

                JOptionPane.showMessageDialog(parent,
                        "Inscription annulée avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadData.run();
            }
        });

        buttonsPanel.add(annulerButton, BorderLayout.WEST);
        buttonsPanel.add(saveButton, BorderLayout.EAST);

        // Création d'une boite de dialogue personnalisée
        JDialog dialog = new JDialog(parent, "Gestion des Étudiants", true);
        dialog.setContentPane(etudiantsPanel);
        dialog.setSize(1000, 800);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * Fonction pour filtrer la table en fonction du texte de recherche
     */

    private void filterTable(DefaultTableModel tableModel, JTable etudiantsTable, String searchText,
            List<Etudiant> etudiants) {
        tableModel.setRowCount(0); // Réinitialise la table
        for (Etudiant etudiant : etudiants) {
            if (etudiant.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                    etudiant.getIne().toLowerCase().contains(searchText.toLowerCase()) ||
                    etudiant.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                    etudiant.getPrenom().toLowerCase().contains(searchText.toLowerCase())) {
                String status = etudiant.isInscriptionValidee() ? "✅Validée" : "❌En Attente";
                tableModel.addRow(new Object[] {
                        etudiant.getIne(),
                        etudiant.getPrenom(),
                        etudiant.getNom(),
                        etudiant.getAdresse(),
                        etudiant.getDateNaissance(),
                        etudiant.getEmail(),
                        (etudiant.getGroupe() != null) ? etudiant.getGroupe().getNumero() : "Non Affecté",
                        status
                });
            }
            // Configurer le renderer pour la colonne de statut (supposons que c'est la
            // colonne 4)
            etudiantsTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

        }
    }

    private void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        // Charger les formations depuis DataStore
        List<Formation> formations = formationService.findAll();
        for (Formation formation : formations) {
            tableModel.addRow(new Object[] {
                    formation.getCode(),
                    formation.getNom(),
                    formation.getNiveau(),
                    (formation.getResponsableFormation() != null)
                            ? formation.getResponsableFormation()
                            : null,
                    (formation.getResponsableFormation() != null) ? formation.getResponsableFormation().getEmail()
                            : null,
                    formation.getNombreOptionsRequis(),
                    formation.getMaxEffectifGroupe()
            });
        }

    }

    // Créer un renderer personnalisé pour les labels de statut
    class StatusRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                JLabel label = new JLabel((String) value);
                if (((String) value).contains("Valid")) {
                    label.setIcon(FontIcon.of(MaterialDesign.MDI_CHECK_CIRCLE));
                    label.setForeground(SUCCESS_COLOR);
                } else {
                    label.setIcon(FontIcon.of(MaterialDesign.MDI_CLOSE_CIRCLE));
                    label.setForeground(DANGER_COLOR);
                }
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }
}