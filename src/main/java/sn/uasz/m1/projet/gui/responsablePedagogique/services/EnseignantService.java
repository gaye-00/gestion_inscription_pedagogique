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
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import sn.uasz.m1.projet.dao.EnseignantDAO;
import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;

public class EnseignantService {
    private final ResponsableDAO responsableDAO;
    private final FormationDAO formationService;
    private final EtudiantDAO etudiantService;
    private final EnseignantDAO enseignantService;
    private final UeService ueService;
    static Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
    static Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    static Color DANGER_COLOR = new Color(231, 76, 60); // Red
    static Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
    static Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
    static Color BORDER_COLOR = new Color(189, 195, 199); // Border color
    private DefaultTableModel tableModel;
    private final String[] columnNames = { "Matricule", "Prenom", "Nom", "email", "telephone" };

    public EnseignantService() {
        this.responsableDAO = new ResponsableDAO();
        this.formationService = new FormationDAO();
        this.enseignantService = new EnseignantDAO();
        this.ueService = new UeService();
        this.etudiantService = new EtudiantDAO();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre les cellules non modifiables
            }
        };
    }

    public JPanel createNouvelEnseignantPanel(FenetrePrincipal parent, PanelSwitcher panelSwitcher,
            String DASHBOARD_PANEL) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setSize(650, 400);

        // Titre
        JLabel titleLabel = new JLabel("Nouvel Enseignant", JLabel.CENTER);
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
        String[] labels = { "Matricule:", "Nom:", "Prénom:", "Email:", "Téléphone:" };
        JTextField matriculeField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField telephoneField = new JTextField(20);

        JComponent[] fields = { matriculeField, nomField, prenomField, emailField, telephoneField };

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
            if (matriculeField.getText().isEmpty() ||
                    nomField.getText().isEmpty() ||
                    prenomField.getText().isEmpty() ||
                    emailField.getText().isEmpty() ||
                    telephoneField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(parent,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Enseignant nouvelEnseignant = new Enseignant();
            nouvelEnseignant.setMatricule(matriculeField.getText());
            nouvelEnseignant.setNom(nomField.getText());
            nouvelEnseignant.setPrenom(prenomField.getText());
            nouvelEnseignant.setEmail(emailField.getText());
            nouvelEnseignant.setTelephone(telephoneField.getText());

            if (enseignantService.create(nouvelEnseignant)) {
                JOptionPane.showMessageDialog(parent,
                        "Enseignant ajouté avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshTable(tableModel);
                // Réinitialiser les champs
                matriculeField.setText("");
                nomField.setText("");
                prenomField.setText("");
                emailField.setText("");
                telephoneField.setText("");
                // Revenir au tableau de bord via la méthode fournie
                panelSwitcher.showPanel(DASHBOARD_PANEL);

            } else {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de la création de la formation. Veuillez réessayer.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        });

        annulerButton.addActionListener(e -> {

            // Revenir au tableau de bord via la méthode fournie
            panelSwitcher.showPanel(DASHBOARD_PANEL);
        });

        return panel;
    }

    public JPanel createGererEnseignantsPanel(FenetrePrincipal parent, PanelSwitcher panelSwitcher,
            String NOUVEL_ENSEIGNANT_PANEL) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        System.out.println("#### Création du panneau de gestion des enseignants");
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // Create a more attractive title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titleLabel = new JLabel("Gérer les Enseignants", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        // Better table styling
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        // Ajouter un TableRowSorter pour le filtrage
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

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

        JComboBox<String> searchColumnCombo = new JComboBox<>(columnNames);
        searchColumnCombo.setMaximumSize(new Dimension(150, 30));
        searchColumnCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(inLabel);
        searchPanel.add(searchColumnCombo);
        searchPanel.add(Box.createHorizontalGlue());

        // Ajouter un DocumentListener pour le champ de recherche
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

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Panneau de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Actualiser");

        // Style buttons
        styleButton(refreshButton, new Color(149, 165, 166)); // Gray
        styleButton(addButton, SUCCESS_COLOR);
        styleButton(deleteButton, DANGER_COLOR);
        styleButton(editButton, PRIMARY_COLOR);
        // deleteButton.setEnabled(false);
        // editButton.setEnabled(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Charger les données
        refreshTable(tableModel);
       

        // Actions des boutons
        addButton.addActionListener(e -> panelSwitcher.showPanel(NOUVEL_ENSEIGNANT_PANEL));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                editButton.setEnabled(true);
                String matricule = (String) tableModel.getValueAt(selectedRow, 0);
                EnseignantDAO enseignantDAO = new EnseignantDAO();
                Enseignant enseignant = enseignantDAO.findByMatricule(matricule);
                if (enseignant != null) {
                    JDialog modifyDialog = new JDialog(parent, "Modifier Enseignant", true);
                    modifyDialog.setContentPane(
                            createModifierEnseignantPanel(enseignant, modifyDialog, tableModel, selectedRow));
                    modifyDialog.pack();
                    modifyDialog.setSize(650, 400);
                    modifyDialog.setLocationRelativeTo(parent);
                    modifyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    modifyDialog.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un enseignant");
                // editButton.setEnabled(false);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                deleteButton.setEnabled(true);
                String matricule = (String) tableModel.getValueAt(selectedRow, 0);
                // Suppression
                EnseignantDAO enseignantDAO = new EnseignantDAO();

                if (enseignantDAO.deleteByMatricule(matricule)) {
                    JOptionPane.showMessageDialog(parent, "Enseignant supprimé avec succès");
                    refreshTable(tableModel);
                    panelSwitcher.showPanel("Gérer Enseignants");

                } else {
                    JOptionPane.showMessageDialog(parent, "Erreur lors de la suppression de l'enseignant");
                }

            } else {
                JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un enseignant");
                deleteButton.setEnabled(false);
            }
        });

        refreshButton.addActionListener(e -> refreshTable(tableModel));

        return panel;
    }

    private JPanel createModifierEnseignantPanel(Enseignant enseignant, JDialog parentDialog,
            DefaultTableModel tableModel, int rowIndex) {
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

        JLabel titleLabel = new JLabel("Modifier Enseignant", JLabel.CENTER);
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
        String[] labels = { "Matricule:", "Nom:", "Prénom:", "Email:", "Téléphone:" };
        JTextField matriculeField = new JTextField(enseignant.getMatricule(), 20);
        JTextField nomField = new JTextField(enseignant.getNom(), 20);
        JTextField prenomField = new JTextField(enseignant.getPrenom(), 20);
        JTextField emailField = new JTextField(enseignant.getEmail(), 20);
        JTextField telephoneField = new JTextField(enseignant.getTelephone(), 20);

        // Rendre le champ matricule non éditable car c'est un identifiant
        matriculeField.setEnabled(false);
        matriculeField.setBackground(new Color(240, 240, 240));

        JComponent[] fields = { matriculeField, nomField, prenomField, emailField, telephoneField };

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
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(parentDialog,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mettre à jour l'enseignant
            enseignant.setNom(nomField.getText());
            enseignant.setPrenom(prenomField.getText());
            enseignant.setEmail(emailField.getText());
            enseignant.setTelephone(telephoneField.getText());

            // Mettre à jour dans la base de données
            enseignantService.update(enseignant);

            // Mettre à jour la table
            tableModel.setValueAt(enseignant.getMatricule(), rowIndex, 0);
            tableModel.setValueAt(enseignant.getNom(), rowIndex, 1);
            tableModel.setValueAt(enseignant.getPrenom(), rowIndex, 2);
            tableModel.setValueAt(enseignant.getEmail(), rowIndex, 3);
            tableModel.setValueAt(enseignant.getTelephone(), rowIndex, 4);

            JOptionPane.showMessageDialog(parentDialog,
                    "Enseignant modifié avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            parentDialog.dispose();

        });

        annulerButton.addActionListener(e -> parentDialog.dispose());

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

    // Méthode pour styliser les champs de texte de manière cohérente
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
    }

    /**
     * Fonction pour filtrer la table en fonction du texte de recherche
     */

    private void filterTable(DefaultTableModel tableModel, JTable etudiantsTable, String searchText,
            List<Enseignant> enseignants) {
        tableModel.setRowCount(0); // Réinitialise la table
        for (Enseignant e : enseignants) {
            if (e.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getMatricule().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getTelephone().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getPrenom().toLowerCase().contains(searchText.toLowerCase())) {

                tableModel.addRow(new Object[] {
                        e.getMatricule(),
                        e.getNom(),
                        e.getPrenom(),
                        e.getEmail(),
                        e.getTelephone()
                });
            }

        }

    }

    private void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Enseignant> enseignants = enseignantService.findAll();
        for (Enseignant e : enseignants) {
            model.addRow(new Object[] {
                    e.getMatricule(),
                    e.getNom(),
                    e.getPrenom(),
                    e.getEmail(),
                    e.getTelephone()
            });
        }

    }

    // Méthode pour styliser les boutons de manière cohérente
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(130, 30));
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

    // Créer un renderer personnalisé pour les labels de statut
    class StatusRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                JLabel label = new JLabel((String) value);

                // Vérifier si la valeur contient "Validée✅"
                if (((String) value).contains("Validée")) {
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