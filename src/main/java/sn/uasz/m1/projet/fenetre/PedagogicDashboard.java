package sn.uasz.m1.projet.fenetre;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PedagogicDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public PedagogicDashboard() {
        setTitle("Gestion Pédagogique");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Sidebar panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setBackground(Color.LIGHT_GRAY);
        
        JButton btnFormations = new JButton("Gérer Formations");
        JButton btnUE = new JButton("Gérer UEs");
        JButton btnInscriptions = new JButton("Inscriptions");
        JButton btnGroupes = new JButton("Gérer Groupes");
        JButton btnEtudiants = new JButton("Liste Étudiants");
        
        sidebar.add(btnFormations);
        sidebar.add(btnUE);
        sidebar.add(btnInscriptions);
        sidebar.add(btnGroupes);
        sidebar.add(btnEtudiants);

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        contentPanel.add(new JLabel("Bienvenue dans la gestion pédagogique"), "Home");
        contentPanel.add(new JLabel("Gestion des formations"), "Formations");
        contentPanel.add(new JLabel("Gestion des UEs"), "UEs");
        contentPanel.add(new JLabel("Validation des inscriptions"), "Inscriptions");
        contentPanel.add(new JLabel("Gestion des groupes"), "Groupes");
        contentPanel.add(new JLabel("Liste des étudiants"), "Etudiants");
        
        // Button actions to switch views
        btnFormations.addActionListener(e -> cardLayout.show(contentPanel, "Formations"));
        btnUE.addActionListener(e -> cardLayout.show(contentPanel, "UEs"));
        btnInscriptions.addActionListener(e -> cardLayout.show(contentPanel, "Inscriptions"));
        btnGroupes.addActionListener(e -> cardLayout.show(contentPanel, "Groupes"));
        btnEtudiants.addActionListener(e -> cardLayout.show(contentPanel, "Etudiants"));
        
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PedagogicDashboard::new);
    }
}
