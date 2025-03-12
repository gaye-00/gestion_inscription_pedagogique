package sn.uasz.m1.projet.gui.responsablePedagogique.services;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;

public class FormationService {
    private final ResponsableDAO responsableDAO;
    private final FormationDAO formationService;
    private final EtudiantDAO etudiantService;
    private final UeService ueService;

    public FormationService() {
        this.responsableDAO = new ResponsableDAO();
        this.formationService = new FormationDAO();
        this.etudiantService = new EtudiantDAO();
        this.ueService = new UeService();
    }

    public JPanel createNouvelleFormationPanel(FenetrePrincipal parent, PanelSwitcher panelSwitcher,
            String DASHBOARD_PANEL) {
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
        String[] labels = { "Code de formation:", "Intitulé:", "Niveau:", "Responsable pédagogique:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);
        String[] niveaux = { "Licence", "Master", "Doctorat" };

        // Pour le niveau, utiliser les valeurs de l'enum
        JComboBox<Niveau> niveauCombo = new JComboBox<>(Niveau.values());
        // JComboBox<String> niveauCombo = new JComboBox<>(niveaux);

        // Récupérer la liste des responsables pédagogiques depuis la base de données
        List<ResponsablePedagogique> responsables = responsableDAO.findAll();
        JComboBox<ResponsablePedagogique> respCombo = new JComboBox<>(
                responsables.toArray(new ResponsablePedagogique[0]));

        // Personnaliser l'affichage du ComboBox pour les responsables
        respCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ResponsablePedagogique) {
                    ResponsablePedagogique resp = (ResponsablePedagogique) value;
                    setText(resp.getNom() + " " + resp.getPrenom());
                }
                return this;
            }
        });

        JComponent[] fields = { codeField, intituleField, niveauCombo, respCombo };

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
            nouvelleFormation.setResponsable((ResponsablePedagogique) respCombo.getSelectedItem());

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

    /**
     * Récupère les statistiques depuis la base de données
     * 
     * @return Un tableau contenant les données pour chaque carte statistique
     */

    public JPanel createGererFormationsPanel(FenetrePrincipal parent, JPanel contentPanel, CardLayout cardLayout,
            String NOUVELLE_FORMATION_PANEL) {
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
        // String[] niveaux = { "Licence", "Master", "Doctorat" };
        JComboBox<Niveau> niveauCombo = new JComboBox<>(Niveau.values());
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
            List<Formation> formations = formationService.findAll();
            // for (Formation formation : DataStore.formationService.getAll()) {
            for (Formation formation : formations) {
                tableModel.addRow(new Object[] {
                        formation.getCode(),
                        formation.getNom(),
                        formation.getNiveau(),
                        formation.getNom() // ! formation.getDescription()
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
            } else {
                // Optionnel : gérer le cas où il n'y a pas d'éléments
                System.out.println("Le JComboBox Niveau est vide.");
            }

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

                    List<Formation> formations = formationService.findAll();
                    // Trouver la formation correspondante
                    // for (Formation formation : DataStore.formationService.getAll()) {
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
                        descriptionArea.setText(selectedFormation[0].getNom()); // ! .getDescription()

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
                    JOptionPane.showMessageDialog(parent,
                            "Veuillez remplir tous les champs obligatoires.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mettre à jour la formation

                selectedFormation[0].setCode(codeField.getText());
                selectedFormation[0].setNom(intituleField.getText());
                selectedFormation[0].setNiveau((Niveau) niveauCombo.getSelectedItem());
                // respCombo.getSelectedItem());

                // Remplacer l'ancienne formation
                formationService.update(selectedFormation[0]);
                // DataStore.updateFormation(selectedFormation[0], updatedFormation);

                JOptionPane.showMessageDialog(parent,
                        "Formation mise à jour avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Recharger les données
                loadData.run();
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                int confirm = JOptionPane.showConfirmDialog(parent,
                        "Êtes-vous sûr de vouloir supprimer cette formation?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Supprimer la formation
                    // DataStore.removeFormation(selectedFormation[0]);
                    formationService.delete(selectedFormation[0].getId());

                    JOptionPane.showMessageDialog(parent,
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
                ueService.showGestionUE(parent, selectedFormation[0]);
                // showUEsList(selectedFormation[0]);
            }
        });

        listeEtudiantsButton.addActionListener(e -> {
            if (selectedFormation[0] != null) {
                showEtudiantsList(parent, selectedFormation[0]);
            }
        });

        return panel;
    }

    // Méthode pour afficher la liste des étudiants inscrits à une formation
    private void showEtudiantsList(FenetrePrincipal parent, Formation formation) {
        // Créer une fenêtre de dialogue
        JDialog dialog = new JDialog(parent, "Étudiants inscrits à " + formation.getNom(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel(
                "Liste des étudiants inscrits à la formation: " + formation.getCode() + " - " + formation.getNom(),
                JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Tableau des étudiants
        String[] columnNames = { "Numéro", "Nom", "Prénom", "Email", "Groupe" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Récupérer les étudiants inscrits à cette formation
        // List<Etudiant> formationEtudiants =
        // DataStore.getEtudiantsByFormation(formation);
        List<Etudiant> formationEtudiants = etudiantService.getEtudiantsByFormation(formation);

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
    private void deleteUE(FenetrePrincipal parent, UE ue, Formation formation, DefaultTableModel model, int row) {
        int confirm = JOptionPane.showConfirmDialog(parent,
                "Êtes-vous sûr de vouloir supprimer l'UE " + ue.getNom() + " ?",
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Code de suppression à implémenter
            // DataStore.deleteUE(ue, formation);
            model.removeRow(row);
        }
    }

}