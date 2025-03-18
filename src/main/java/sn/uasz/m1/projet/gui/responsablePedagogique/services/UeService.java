package sn.uasz.m1.projet.gui.responsablePedagogique.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.RowFilter;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
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
import sn.uasz.m1.projet.dao.UEDAO;
import sn.uasz.m1.projet.gui.responsablePedagogique.FenetrePrincipal;

import sn.uasz.m1.projet.interfacesEcouteur.PanelSwitcher;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;

public class UeService {
    private final ResponsableDAO responsableDAO = new ResponsableDAO();
    private FormationDAO formationService = new FormationDAO();
    private UEDAO ueService = new UEDAO();
    private EtudiantDAO etudiantService = new EtudiantDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    static Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
    static Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    static Color DANGER_COLOR = new Color(231, 76, 60); // Red
    static Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
    static Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
    static Color BORDER_COLOR = new Color(189, 195, 199); // Border color
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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
        Color PRIMARY_COLOR = new Color(52, 152, 219); // Blue
        Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
        Color DANGER_COLOR = new Color(231, 76, 60); // Red
        Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray background
        Color TEXT_COLOR = new Color(44, 62, 80); // Dark text
        Color BORDER_COLOR = new Color(189, 195, 199); // Border color
        // Création du panneau principal avec une marge intérieure
        JPanel gestionUEPanel = new JPanel(new BorderLayout(10, 10));
        gestionUEPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        gestionUEPanel.setBackground(new Color(240, 240, 240));

        // Panneau d'en-tête avec dégradé
        // JPanel headerPanel = new JPanel();
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
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Utiliser BoxLayout en Y_AXIS
        // headerPanel.setBackground(new Color(240, 240, 240));

        // Titre avec une police moderne et une couleur claire
        JLabel titleLabel = new JLabel("Gestion des UE de la formation : " + formation.getNom(), JLabel.CENTER);
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
        searchField.setMaximumSize(new Dimension(250, 30));
        // Style text field
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));

        searchField.setPreferredSize(new Dimension(200, 30));
        // Action de recherche (filtrer la table par exemple)

        // Ajouter le titre et le champ de recherche au panneau d'en-tête
        headerPanel.add(titleLabel); // Titre en premier
        headerPanel.add(Box.createVerticalStrut(10)); // Espacement vertical entre le titre et le champ de recherche
        headerPanel.add(searchPanel); // Champ de recherche en dessous du titre
        searchPanel.add(searchField);

        gestionUEPanel.add(headerPanel, BorderLayout.NORTH);

        // Configuration de la table avec un modèle personnalisé
        String[] columnNames = { "Code", "Intitulé", "Crédits", "Coefficient", "Volume Horaire", "Enseignant",
                "Caractéristique" };
        DefaultTableModel ueTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Empêche l'édition directe dans la table
            }
        };

        JTable ueTable = new JTable(ueTableModel);
        ueTable.setRowHeight(30);
        ueTable.setIntercellSpacing(new Dimension(5, 5));
        ueTable.setShowGrid(true);
        ueTable.setGridColor(new Color(230, 230, 230));
        ueTable.setSelectionBackground(PRIMARY_COLOR);
        ueTable.setSelectionForeground(Color.WHITE);
        ueTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ueTable.getTableHeader().setBackground(new Color(220, 220, 220));
        ueTable.getTableHeader().setForeground(TEXT_COLOR);
        ueTable.setFont(new Font("Arial", Font.PLAIN, 13));
        ueTable.getTableHeader().setReorderingAllowed(false);
        ueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ueTable.setFillsViewportHeight(true);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                // Appliquer le filtre à la table en fonction du texte recherché
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(ueTableModel);
                ueTable.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter(query));
            }
        });

        // Ajouter le champ de recherche à l'en-tête
        headerPanel.add(searchPanel, BorderLayout.NORTH); // Ajouter à l'est (droite)
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        gestionUEPanel.add(headerPanel, BorderLayout.NORTH);

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
        ueTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Caractéristique
        ueTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Caractéristique

        // Récupérer les UEs liées à cette formation
        List<UE> formationUEs = ueService.getUEsByFormation(formation.getId());
        for (UE ue : formationUEs) {
            ueTableModel.addRow(new Object[] {
                    ue.getCode(),
                    ue.getNom(),
                    ue.getCredits(),
                    ue.getCoefficient(),
                    ue.getVolumeHoraire(),
                    (ue.getEnseignant() == null) ? "Enseignant Non defini" : ue.getEnseignant(),
                    // ue.getDescription(), // Utiliser getDescription ici
                    ue.isObligatoire() ? "obligatoire" : "optionnel"
            });
        }

        // Filtrage en temps réel
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(ueTableModel, searchField.getText(), formationUEs);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(ueTableModel, searchField.getText(), formationUEs);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(ueTableModel, searchField.getText(), formationUEs);
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
        JButton etudButton = createStyledButton("Lister Etudiant", new Color(70, 130, 180));
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
            addDialog.setSize(screenSize);
            addDialog.setLocationRelativeTo(dialogRef[0]);
            addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addDialog.setVisible(true);
        });

        etudButton.addActionListener(e -> {
            int selectedRow = ueTable.getSelectedRow();
            if (selectedRow != -1) {
                String ueCode = (String) ueTableModel.getValueAt(selectedRow, 0);
                UE selectedUE = ueService.findUEByCode(formationUEs, ueCode);
                if (selectedUE != null) {

                    showEtudiantsList(parent, selectedUE);

                }
            } else {
                JOptionPane.showMessageDialog(dialogRef[0],
                        "Veuillez sélectionner une UE",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
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
                    modifyDialog.setSize(screenSize);
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

        buttonsPanel.add(etudButton);
        buttonsPanel.add(addUEButton);
        buttonsPanel.add(modifyUEButton);
        buttonsPanel.add(deleteUEButton);
        gestionUEPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Création d'une boite de dialogue personnalisée
        JDialog dialog = new JDialog(parent, "Gestion des UE", true);
        dialogRef[0] = dialog;
        dialog.setContentPane(gestionUEPanel);
        dialog.setSize(screenSize);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public void showEtudiantsList(FenetrePrincipal parent, UE ue) {
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
        JLabel titleLabel = new JLabel("Liste des Étudiants de l'UE  " + ue.getNom(), JLabel.CENTER);
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
            List<Etudiant> etudiantsByUe = etudiantService.getEtudiantsByUE(ue);
            // Dans votre code principal:
            for (Etudiant etudiant : etudiantsByUe) {
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
                    filterTableEtudiant(etudiantsTableModel, etudiantsTable, searchField.getText(), etudiantsByUe);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterTableEtudiant(etudiantsTableModel, etudiantsTable, searchField.getText(), etudiantsByUe);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterTableEtudiant(etudiantsTableModel, etudiantsTable, searchField.getText(), etudiantsByUe);
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
        JButton genererPDFButton = new JButton("Generer PDF ");
        JButton annulerButton = new JButton("ANNULER ");
        // Style buttons
        styleButton(genererPDFButton, SUCCESS_COLOR);
        styleButton(annulerButton, DANGER_COLOR);

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
                    List<Etudiant> etudiantByUE = etudiantService.getEtudiantsByUE(ue);
                    // List<> formations = formationService.findAll();
                    // Trouver la formation correspondante
                    for (Etudiant etudiant : etudiantByUE) {
                        if (etudiant.getIne().equals(code)) {
                            selectedEtudiant[0] = etudiant;
                            break;
                        }
                    }

                    // Remplir les champs
                    if (selectedEtudiant[0] != null && !selectedEtudiant[0].isInscriptionValidee()) {

                        // Activer les boutons
                        // saveButton.setEnabled(true);
                        // deleteButton.setEnabled(true);
                        // Activer les boutons
                        // genererPDFButton.setEnabled(true);
                        annulerButton.setEnabled(false);

                    } else {
                        // genererPDFButton.setEnabled(false);
                        annulerButton.setEnabled(true);
                    }
                }
            }
        });

        genererPDFButton.addActionListener(e -> {

            // Animation pour le bouton
            genererPDFButton.setBackground(new Color(39, 174, 96));
            Timer timer = new Timer(300, event -> {
                genererPDFButton.setBackground(SUCCESS_COLOR);
                ((Timer) event.getSource()).stop();
            });
            timer.start();

            // Récupérer la liste des étudiants de l'UE sélectionnée
            List<Etudiant> etudiants = etudiantService.getEtudiantsByUE(ue);

            // Générer le PDF dans un thread séparé
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    generateStudentListPDF(etudiants, ue.getNom());
                    return null;
                }

                @Override
                protected void done() {
                    showSuccessDialog("PDF généré avec succès!");
                }
            };
            worker.execute();
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

        // buttonsPanel.add(annulerButton, BorderLayout.WEST);
        buttonsPanel.add(genererPDFButton, BorderLayout.EAST);

        // Création d'une boite de dialogue personnalisée
        JDialog dialog = new JDialog(parent, "Gestion des Étudiants", true);
        dialog.setContentPane(etudiantsPanel);
        dialog.setSize(screenSize);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    // Méthode pour ajouter un en-tête de tableau
    private void addTableHeader(PdfPTable table, String columnTitle, com.itextpdf.text.Font font) {
        PdfPCell header = new PdfPCell(new Phrase(columnTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(header);
    }

    // Méthode pour ajouter une cellule de tableau
    private void addTableCell(PdfPTable table, String text, com.itextpdf.text.Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // Boîte de dialogue de confirmation
    private void showSuccessDialog(String message) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", SUCCESS_COLOR);
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);

        JOptionPane.showMessageDialog(null, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

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

    private void filterTableEtudiant(DefaultTableModel tableModel, JTable etudiantsTable, String searchText,
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

    // Fonction pour filtrer la table en fonction du texte de recherche
    private void filterTable(DefaultTableModel tableModel, String searchText, List<UE> formationUEs) {
        tableModel.setRowCount(0); // Réinitialise la table
        for (UE ue : formationUEs) {
            if (ue.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                    ue.getCode().toLowerCase().contains(searchText.toLowerCase())) {
                tableModel.addRow(new Object[] {
                        ue.getCode(),
                        ue.getNom(),
                        ue.getCredits(),
                        ue.getCoefficient(),
                        ue.getVolumeHoraire(),
                        (ue.getEnseignant() == null) ? "Enseignant Non defini" : ue.getEnseignant(),
                        ue.isObligatoire() ? "obligatoire" : "optionnel"
                });
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
     * Création d'un panneau pour ajouter une nouvelle UE
     */
    public JPanel createNouvelleUEPanelExterne(PanelSwitcher panelSwitcher, String DASHBOARD_PANEL) {

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
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Coefficient:", "Volume Horaire",
                "Enseigant:",
                "Caractéristique:" };
        JTextField codeField = new JTextField(20);
        JTextField intituleField = new JTextField(20);

        // Liste déroulante des formations
        DefaultComboBoxModel<Formation> formationModel = new DefaultComboBoxModel<>();
        // Remplir le modèle avec les formations disponibles
        // for (Formation formation : DataStore.formationService.getAll()) {
        for (Formation formation : formationService.findAll()) {
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
                    setText(formation.getCode() + " - " + formation.getNom());
                }
                return this;
            }
        });

        SpinnerModel creditModel = new SpinnerNumberModel(3, 1, 10, 1);
        JSpinner creditSpinner = new JSpinner(creditModel);
        SpinnerModel volumeHoraireModel = new SpinnerNumberModel(30, 5, 100, 5);
        JSpinner volumeHoraireSpinner = new JSpinner(volumeHoraireModel);

        SpinnerModel coefficientModel = new SpinnerNumberModel(1.0, 0.5, 5.0, 0.5);

        JSpinner coefficientSpinner = new JSpinner(coefficientModel);

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
                    setText(resp.toString());
                }
                return this;
            }
        });

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
                volumeHoraireSpinner, coefficientSpinner, respCombo, caracterePanel
        };

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
                JOptionPane.showMessageDialog(null,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Formation selectedFormation = (Formation) formationComboBox.getSelectedItem();

            UE nouvelleUE = new UE();
            nouvelleUE.setCode(codeField.getText());
            nouvelleUE.setNom(intituleField.getText());
            nouvelleUE.setCredits((int) creditSpinner.getValue());
            nouvelleUE.setEnseignant((Enseignant) respCombo.getSelectedItem());

            nouvelleUE.setCoefficient((Double) coefficientSpinner.getValue());
            nouvelleUE.setFormation(selectedFormation);
            nouvelleUE.setVolumeHoraire((int) volumeHoraireModel.getValue());
            nouvelleUE.setObligatoire(obligatoireRadio.isSelected());

            // Ajouter la caractéristique si disponible
            // DataStore.addUE(nouvelleUE);
            if (ueService.create(nouvelleUE)) {
                formationComboBox.addItem(selectedFormation);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Une erreur s'est produite lors de l'ajout de l'UE.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null,
                    "Unité d'enseignement créée avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Revenir au tableau de bord via la méthode fournie
            panelSwitcher.showPanel(DASHBOARD_PANEL);
        });

        annulerButton.addActionListener(e -> {
            // Revenir au tableau de bord via la méthode fournie
            panelSwitcher.showPanel(DASHBOARD_PANEL);
        });

        return panel;

    }

    public JPanel createNouvelleUEPanel(Formation preselectedFormation, JDialog parentDialog,
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
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Coefficient:", "Volume Horaire",
                "Enseigant:",
                "Caractéristique:" };
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
        SpinnerModel volumeHoraireModel = new SpinnerNumberModel(30, 5, 100, 5);
        JSpinner volumeHoraireSpinner = new JSpinner(volumeHoraireModel);

        SpinnerModel coefficientModel = new SpinnerNumberModel(1.0, 0.5, 5.0, 0.5);

        JSpinner coefficientSpinner = new JSpinner(coefficientModel);

        JTextField responsableField = new JTextField(20);

        // Boutons radio pour le caractère de l'UE
        JRadioButton obligatoireRadio = new JRadioButton("Obligatoire", true);
        JRadioButton optionnelRadio = new JRadioButton("Optionnel");
        ButtonGroup caractereGroup = new ButtonGroup();
        caractereGroup.add(obligatoireRadio);
        caractereGroup.add(optionnelRadio);
        JPanel caracterePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        caracterePanel.add(obligatoireRadio);
        caracterePanel.add(optionnelRadio);

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
                    setText(resp.toString());
                }
                return this;
            }
        });

        JComponent[] fields = {
                codeField, intituleField, formationComboBox, creditSpinner,
                volumeHoraireSpinner, coefficientSpinner, respCombo, caracterePanel
        };

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
            if (respCombo.getItemCount() > 0 && respCombo.getSelectedItem() != null) {

                nouvelleUE.setEnseignant((Enseignant) respCombo.getSelectedItem());
            }

            nouvelleUE.setCoefficient((Double) coefficientSpinner.getValue());
            nouvelleUE.setFormation(selectedFormation);
            nouvelleUE.setVolumeHoraire((int) volumeHoraireModel.getValue());
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
                    nouvelleUE.getCoefficient(),
                    nouvelleUE.getVolumeHoraire(),
                    (nouvelleUE.getEnseignant() == null) ? "Enseignant Non defini"
                            : nouvelleUE.getEnseignant(),
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
        String[] labels = { "Code UE:", "Intitulé:", "Formation:", "Crédits:", "Coefficient:", "Volume Horaire",
                "Enseigant:",
                "Caractéristique:" };
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
        SpinnerModel volumeHoraireModel = new SpinnerNumberModel((int) ue.getVolumeHoraire(), 5, 100, 5);
        JSpinner volumeHoraireSpinner = new JSpinner(volumeHoraireModel);

        SpinnerModel coefficientModel = new SpinnerNumberModel((double) ue.getCoefficient(), 0.5, 5.0, 0.5);

        JSpinner coefficientSpinner = new JSpinner(coefficientModel);

        JTextField responsableField = new JTextField(ue.getEnseignant().toString(), 20);

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

        JComponent[] fields = {
                codeField, intituleField, formationComboBox, creditSpinner,
                coefficientSpinner, volumeHoraireSpinner, respCombo, caracterePanel
        };

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

            ue.setEnseignant(null);
            ue.setCoefficient((Double) coefficientSpinner.getValue());
            ue.setVolumeHoraire((int) volumeHoraireModel.getValue());
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
            tableModel.setValueAt(ue.getCoefficient(), rowIndex, 3);
            tableModel.setValueAt(ue.getVolumeHoraire(), rowIndex, 4);
            if (respCombo.getItemCount() > 0) {
                tableModel.setValueAt(ue.getEnseignant(), rowIndex, 5);
            } else {
                // Optionnel : gérer le cas où il n'y a pas d'éléments
                System.out.println("Le JComboBox Responsable est vide.");
            }

            tableModel.setValueAt(ue.isObligatoire() ? "obligatoire" : "optionnel", rowIndex, 6);
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
        dialog.setSize(screenSize);
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

    public void generateStudentListPDF(List<Etudiant> etudiants, String ueName) {
        Document document = new Document(PageSize.A4.rotate());
        try {
            String filePath = "Liste_Etudiants_" + ueName.replaceAll("\\s+", "_") + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Utiliser une police standard pour éviter les problèmes de chargement
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(baseFont, 14, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(baseFont, 9, com.itextpdf.text.Font.NORMAL);

            // Titre
            Paragraph title = new Paragraph("Liste des étudiants inscrits à l'UE " + ueName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Création du tableau
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(8f);
            table.setSpacingAfter(8f);
            table.setWidths(new float[] { 3f, 1.5f, 1.5f, 4f, 1f, 1.5f, 4f });

            // Ajouter les en-têtes
            addTableHeader(table, "INE", headerFont);
            addTableHeader(table, "Prénom", headerFont);
            addTableHeader(table, "Nom", headerFont);
            addTableHeader(table, "Email", headerFont);
            addTableHeader(table, "Sexe", headerFont);
            addTableHeader(table, "Date Naissance", headerFont);
            addTableHeader(table, "Adresse", headerFont);

            // Ajouter les étudiants
            for (Etudiant etudiant : etudiants) {
                addTableCell(table, etudiant.getIne(), cellFont);
                addTableCell(table, etudiant.getPrenom(), cellFont);
                addTableCell(table, etudiant.getNom(), cellFont);
                addTableCell(table, etudiant.getEmail(), cellFont);
                addTableCell(table, etudiant.getSexe().getPremiereLettre(), cellFont);
                addTableCell(table, formatDate(etudiant.getDateNaissance()), cellFont);
                addTableCell(table, etudiant.getAdresse(), cellFont);
            }
            document.add(table);
            document.close();
            System.out.println("PDF généré avec succès : " + filePath);

            // Ouvrir automatiquement le fichier PDF (facultatif)
            File pdfFile = new File(filePath);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour formater la date de façon plus lisible
    private String formatDate(LocalDate date) {
        if (date == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

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