package sn.uasz.m1.projet.fenetre;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Gestion Pédagogique");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGestion = new JMenu("Gestion");
        JMenuItem menuFormations = new JMenuItem("Formations");
        JMenuItem menuUEs = new JMenuItem("Unités d'Enseignement");
        JMenuItem menuEtudiants = new JMenuItem("Étudiants");
        JMenuItem menuGroupes = new JMenuItem("Groupes TD/TP");
        
        menuGestion.add(menuFormations);
        menuGestion.add(menuUEs);
        menuGestion.add(menuEtudiants);
        menuGestion.add(menuGroupes);
        menuBar.add(menuGestion);
        setJMenuBar(menuBar);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Bienvenue dans l'application de gestion pédagogique", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}
