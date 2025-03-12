package sn.uasz.m1.projet.gui.responsablePedagogique.services;

import java.awt.BorderLayout;
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
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.dao.UEDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.UE;

public class UeService {
    private final ResponsableDAO responsableDAO = new ResponsableDAO();
    private FormationDAO formationService = new FormationDAO();
    private UEDAO ueService = new UEDAO();

    public UeService() {

    }

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
    public void showGestionUE(FenetrePrincipal parent, Formation formation) {
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
        List<UE> formationUEs = ueService.getUEsByFormation(formation.getId());
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
                UE selectedUE = ueService.findUEByCode(formationUEs, ueCode);
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
                UE selectedUE = ueService.findUEByCode(formationUEs, ueCode);
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
                        if (ueService.delete(selectedUE.getId())) {
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
        JDialog dialog = new JDialog(parent, "Gestion des UE", true);
        dialogRef[0] = dialog;
        dialog.setContentPane(gestionUEPanel);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
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
        // for (Formation formation : DataStore.formationService.getAll()) {
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
            if (ueService.create(nouvelleUE)) {
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
        // for (Formation f : DataStore.formationService.getAll()) {
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

            if (ueService.update(ue)) {
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

    // Méthode pour afficher la liste des UEs d'une formation
    public void showUEsList(FenetrePrincipal parent, Formation formation) {
        // Créer une fenêtre de dialogue
        JDialog dialog = new JDialog(parent, "UEs de la formation " + formation.getNom(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);
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
        List<UE> formationUEs = ueService.getUEsByFormation(formation.getId());

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

}