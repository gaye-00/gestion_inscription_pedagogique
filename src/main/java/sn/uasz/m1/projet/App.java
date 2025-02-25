package sn.uasz.m1.projet;

import javax.swing.SwingUtilities;

import sn.uasz.m1.projet.gui.Connexion;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        // Créer et afficher l'interface de connexion
        SwingUtilities.invokeLater(() -> {
            Connexion connexion = new Connexion();
            connexion.setVisible(true);
        });
    }
}
