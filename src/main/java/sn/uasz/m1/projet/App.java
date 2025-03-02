package sn.uasz.m1.projet;

import java.time.LocalDate;

import javax.swing.SwingUtilities;

import sn.uasz.m1.projet.gui.Connexion;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.Sexe;
import sn.uasz.m1.projet.service.EtudiantService;
import sn.uasz.m1.projet.service.FormationService;
import sn.uasz.m1.projet.service.UEService;
import sn.uasz.m1.projet.utils.JPAUtil;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // Créer et afficher l'interface de connexion
        SwingUtilities.invokeLater(() -> {
            Connexion connexion = new Connexion();
            connexion.setVisible(true);
        });
    }

    public static void TestDOnneAPI() {
        FormationService formationService = new FormationService();
        UEService ueService = new UEService();
        EtudiantService etudiantService = new EtudiantService();

        // Ajouter une formation
        // Formation formation = new Formation(null, "Master Informatique", Niveau.M1, null, null);
        Formation formation = new Formation();
        formation.setNom("Master Informatique");
        formation.setCode("MI");
        formation.setNiveau(Niveau.M1);
        formationService.add(formation);

        // Ajouter une UE
        UE ue = new UE(null, "ALG001", "Algorithmes avancés", 6, 4.9, 4, "resp", null, formation, true);
        ueService.add(ue);

        // Ajouter un étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setAdresse("Adress");
        etudiant.setNom("Gaye");
        etudiant.setPrenom("Abdoulaye");
        etudiant.setSexe(Sexe.MASCULIN);
        etudiant.setEmail("smag@gmail.com");
        etudiantService.add(etudiant);
        etudiant.setDateNaissance(LocalDate.parse("1998-05-15"));

        System.out.println("Liste des formations :");
        formationService.getAll().forEach(f -> System.out.println(f.getNom()));

        // Afficher les UEs
        System.out.println("Liste des UEs :");
        ueService.getAll().forEach(u -> System.out.println(u.getNom()));

        // Afficher les étudiants
        System.out.println("Liste des étudiants :");
        etudiantService.getAll().forEach(e -> System.out.println(e.getNom() + " " + e.getPrenom()));

        // Fermer l'EntityManagerFactory
        JPAUtil.close();
    }
}
