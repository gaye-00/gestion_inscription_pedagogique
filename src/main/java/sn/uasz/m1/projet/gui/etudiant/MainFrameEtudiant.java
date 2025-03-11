package sn.uasz.m1.projet.gui.etudiant;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

public class MainFrameEtudiant extends JFrame {
    private final EtudiantDashboardPanel dashboardPanel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private Long etudiantId = 12L; // ID par défaut ou à récupérer dynamiquement

    public MainFrameEtudiant() {
        setTitle("Gestion de l'inscription pédagogique - Espace Étudiant");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        dashboardPanel = new EtudiantDashboardPanel(this);
        contentPanel.add(dashboardPanel, "DASHBOARD");
        
        add(contentPanel);
        
        FontIcon appIcon = FontIcon.of(MaterialDesign.MDI_SCHOOL, 24, new Color(41, 128, 185));
        setIconImage(appIcon.toImageIcon().getImage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    MainFrameEtudiant.this,
                    "Êtes-vous sûr de vouloir quitter l'application ?",
                    "Confirmation de sortie",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        // Appel direct au tableau de bord
        showDashboard(etudiantId);
    }
    
    public void showDashboard(Long etudiantId) {
        this.etudiantId = etudiantId;
        dashboardPanel.loadEtudiantData(etudiantId);
        cardLayout.show(contentPanel, "DASHBOARD");
    }
    
    public Long getEtudiantId() {
        return etudiantId;
    }
    
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'application du Look and Feel de Nimbus : " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            MainFrameEtudiant frame = new MainFrameEtudiant();
            frame.setVisible(true);
        });
    }
}
