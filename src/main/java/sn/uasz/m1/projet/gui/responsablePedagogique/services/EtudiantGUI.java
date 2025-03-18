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
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
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
import sn.uasz.m1.projet.dao.EnseignantDAO;
import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;
import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;

import sn.uasz.m1.projet.model.person.Utilisateur;

public class EtudiantGUI {
    private final ResponsableDAO responsableDAO;
    private final FormationDAO formationDAO;
    private final EtudiantDAO etudiantService;
    private final EnseignantDAO enseignantDAO;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final UeService ueService;
    private final GroupeGUI groupeService;
    static Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
    static Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    static Color DANGER_COLOR = new Color(231, 76, 60); // Red
    static Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
    static Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
    static Color BORDER_COLOR = new Color(189, 195, 199); // Border color

    public EtudiantGUI() {
        this.responsableDAO = new ResponsableDAO();
        this.enseignantDAO = new EnseignantDAO();
        this.formationDAO = new FormationDAO();
        this.etudiantService = new EtudiantDAO();
        this.ueService = new UeService();
        this.groupeService = new GroupeGUI();

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
                String status = etudiant.isInscriptionValidee() ? "✅Validée" : "En Attente❌";

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

        dialog.setSize(screenSize);
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
                String status = etudiant.isInscriptionValidee() ? "✅Validée" : "En Attente❌";
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