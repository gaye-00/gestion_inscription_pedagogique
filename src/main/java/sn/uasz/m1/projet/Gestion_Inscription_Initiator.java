package sn.uasz.m1.projet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;

import com.github.javafaker.Faker;

import sn.uasz.m1.projet.dao.EnseignantDAO;
import sn.uasz.m1.projet.dao.EtudiantDAO;
import sn.uasz.m1.projet.dao.FormationDAO;
import sn.uasz.m1.projet.dao.GroupeDAO;
import sn.uasz.m1.projet.dao.ResponsableDAO;
import sn.uasz.m1.projet.dao.UEDAO;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.Niveau;
import sn.uasz.m1.projet.model.formation.TypeGroupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;
import sn.uasz.m1.projet.model.person.Sexe;

public class Gestion_Inscription_Initiator {
    
    // private EntityManager entityManager;
    private final Faker faker;
    private final Random random;
    
    // DAOs
    private final EtudiantDAO etudiantDAO;
    private final ResponsableDAO responsablePedagogiqueDAO;
    private final FormationDAO formationDAO;
    private final EnseignantDAO enseignantDAO;
    // private UtilisateurDAO utilisateurDAO;
    private final UEDAO ueDAO;
    private final GroupeDAO groupeDAO;
    
    public Gestion_Inscription_Initiator() {
        // EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion_inscription_pedagogiquePU"); // À modifier selon votre configuration
        // this.entityManager = emf.createEntityManager();
        this.faker = new Faker();
        this.random = new Random();
        
        // Initialiser les DAOs
        this.etudiantDAO = new EtudiantDAO();
        this.formationDAO = new FormationDAO();
        this.enseignantDAO = new EnseignantDAO();
        this.responsablePedagogiqueDAO = new ResponsableDAO();
        // this.utilisateurDAO = new UtilisateurDAO(entityManager);
        this.ueDAO = new UEDAO();
        this.groupeDAO = new GroupeDAO();
    }
    
    public void initialiserDonnees() {
        try {
            // entityManager.getTransaction().begin();
            
            // Création des enseignants
            creerEnseignants();
            
            // Création des responsables pédagogiques
            creerResponsables();
            
            // Création des formations
            creerFormations();
            
            // Création des UEs
            creerUEs();
            
            // Création des groupes
            creerGroupes();
            
            // Création des étudiants
            creerEtudiants();
            
            // Inscription des étudiants aux UEs
            inscrireEtudiantsAuxUEs();
            
            // entityManager.getTransaction().commit();
            // System.out.println("Initialisation des données terminée avec succès !");
            
        } catch (Exception e) {
            // if (entityManager.getTransaction().isActive()) {
            //     entityManager.getTransaction().rollback();
            // }
            System.err.println("Erreur lors de l'initialisation des données : " + e.getMessage());
        } 
        // finally {
        //     entityManager.close();
        // }
    }
    
    private void creerEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        
        // Ajouter les enseignants existants dans le SQL
        enseignants.add(new Enseignant(null, "ENS001", "Diallo", "Amadou", "amadou.diallo@uasz.sn", "771234567", null));
        enseignants.add(new Enseignant(null, "ENS002", "Diop", "Fatou", "fatou.diop@uasz.sn", "778765432", null));
        enseignants.add(new Enseignant(null, "ENS003", "Ndiaye", "Moussa", "moussa.ndiaye@uasz.sn", "773456789", null));
        enseignants.add(new Enseignant(null, "ENS004", "Sarr", "Awa", "awa.sarr@uasz.sn", "776543210", null));
        enseignants.add(new Enseignant(null, "ENS005", "Faye", "Oumar", "oumar.faye@uasz.sn", "770987654", null));
        
        // Générer 5 nouveaux enseignants avec Faker
        for (int i = 6; i <= 10; i++) {
            String matricule = String.format("ENS%03d", i);
            String nom = faker.name().lastName();
            String prenom = faker.name().firstName();
            String email = prenom.toLowerCase() + "." + nom.toLowerCase() + "@uasz.sn";
            String telephone = "77" + faker.number().digits(7);
            
            enseignants.add(new Enseignant(null, matricule, nom, prenom, email, telephone, null));
        }
        
        // Persister les enseignants
        for (Enseignant enseignant : enseignants) {
            enseignantDAO.create(enseignant);
        }
        
        System.out.println(enseignants.size() + " enseignants créés.");
    }
    
    private void creerResponsables() {
        List<ResponsablePedagogique> responsables = new ArrayList<>();
        
        // Ajouter les responsables existants dans le SQL
        ResponsablePedagogique resp1 = new ResponsablePedagogique();
        resp1.setNom("Tamba");
        resp1.setPrenom("Mbaye");
        resp1.setDateNaissance(LocalDate.of(2004, 3, 30));
        resp1.setSexe(Sexe.FEMININ);
        resp1.setAdresse("Tamba");
        resp1.setEmail("r");
        resp1.setPassword(BCrypt.hashpw("r", BCrypt.gensalt()));
        // resp1.setRole("ResponsablePedagogique");
        responsables.add(resp1);
        
        ResponsablePedagogique resp2 = new ResponsablePedagogique();
        resp2.setNom("Sow");
        resp2.setPrenom("Fatou");
        resp2.setDateNaissance(LocalDate.of(2001, 3, 30));
        resp2.setSexe(Sexe.FEMININ);
        resp2.setAdresse("Ziguinchor");
        resp2.setEmail("fatou.sow@example.com");
        resp2.setPassword(BCrypt.hashpw("password321", BCrypt.gensalt()));
        // resp2.setRole("ResponsablePedagogique");
        responsables.add(resp2);
        
        ResponsablePedagogique resp3 = new ResponsablePedagogique();
        resp3.setNom("Ndoye");
        resp3.setPrenom("Cheikh");
        resp3.setDateNaissance(LocalDate.of(1997, 7, 5));
        resp3.setSexe(Sexe.MASCULIN);
        resp3.setAdresse("Kaolack");
        resp3.setEmail("cheikh.ndoye@example.com");
        resp3.setPassword(BCrypt.hashpw("password654", BCrypt.gensalt()));
        // resp3.setRole("ResponsablePedagogique");
        responsables.add(resp3);
        
        // Générer 2 nouveaux responsables avec Faker
        for (int i = 0; i < 2; i++) {
            ResponsablePedagogique responsable = new ResponsablePedagogique();
            responsable.setNom(faker.name().lastName());
            responsable.setPrenom(faker.name().firstName());
            
            // Générer une date de naissance entre 1980 et 2000
            Date fakeDate = faker.date().birthday(25, 45);
            LocalDate dateNaissance = fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            responsable.setDateNaissance(dateNaissance);
            
            responsable.setSexe(random.nextBoolean() ? Sexe.MASCULIN : Sexe.FEMININ);
            responsable.setAdresse(faker.address().city());
            String email = responsable.getPrenom().toLowerCase() + "." + responsable.getNom().toLowerCase() + "@example.com";
            responsable.setEmail(email);
            
            // Utiliser BCrypt pour le hachage du mot de passe
            String rawPassword = "password" + faker.number().digits(3);
            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            responsable.setPassword(hashedPassword);
            
            // responsable.setRole("ResponsablePedagogique");
            responsables.add(responsable);
        }
        
        // Persister les responsables
        for (ResponsablePedagogique responsable : responsables) {
            // utilisateurDAO.create(responsable);
            responsablePedagogiqueDAO.create(responsable);
        }
        
        System.out.println(responsables.size() + " responsables pédagogiques créés.");
    }
    
    private void creerFormations() {
        List<Formation> formations = new ArrayList<>();
        
        // Récupérer les responsables pour les associer aux formations
        // List<ResponsablePedagogique> responsables = utilisateurDAO.findResponsables();
        List<ResponsablePedagogique> responsables = responsablePedagogiqueDAO.findAll();
        
        // Ajouter les formations existantes dans le SQL
        Formation f1 = new Formation();
        f1.setNom("Licence 3 Informatique");
        f1.setCode("L3INFO");
        f1.setNombreOptionsRequis(2);
        f1.setNiveau(Niveau.L3);
        f1.setResponsable(responsables.get(0)); // Associer le premier responsable
        f1.setMaxEffectifGroupe(3);
        formations.add(f1);
        
        Formation f2 = new Formation();
        f2.setNom("Master 1 Informatique");
        f2.setCode("M1INFO");
        f2.setNombreOptionsRequis(3);
        f2.setNiveau(Niveau.M1);
        f2.setResponsable(responsables.get(1)); // Associer le deuxième responsable
        f2.setMaxEffectifGroupe(4);
        formations.add(f2);
        
        // Générer des nouvelles formations avec Faker
        String[] niveaux = {"L1", "L2", "L3", "M1", "M2"};
        String[] domaines = {"Mathématiques", "Physique", "Chimie", "Biologie", "Géographie", "Économie", "Droit"};
        
        for (int i = 0; i < 5; i++) {
            Formation formation = new Formation();
            String niveau = niveaux[random.nextInt(niveaux.length)];
            String domaine = domaines[random.nextInt(domaines.length)];
            
            formation.setNom(niveau + " " + domaine);
            formation.setCode(niveau + domaine.substring(0, 3).toUpperCase());
            formation.setNombreOptionsRequis(random.nextInt(4) + 1);
            formation.setNiveau(Niveau.valueOf(niveau));
            
            // Associer un responsable aléatoire
            formation.setResponsable(responsables.get(random.nextInt(responsables.size())));
            formation.setMaxEffectifGroupe(random.nextInt(5) + 2);
            
            formations.add(formation);
        }
        
        // Persister les formations
        for (Formation formation : formations) {
            formationDAO.create(formation);
        }
        
        System.out.println(formations.size() + " formations créées.");
    }
    
    private void creerUEs() {
        List<UE> ues = new ArrayList<>();
        
        // Récupérer les enseignants et formations pour les associer aux UEs
        List<Enseignant> enseignants = enseignantDAO.findAll();
        List<Formation> formations = formationDAO.findAll();
        
        // Ajouter les UEs existantes dans le SQL (notez que je corrige les IDs qui semblent incorrects dans votre SQL)
        UE ue1 = new UE();
        ue1.setCode("INFO101");
        ue1.setNom("Algorithmique Avancée");
        ue1.setVolumeHoraire(60);
        ue1.setCoefficient(1.5);
        ue1.setCredits(6);
        ue1.setFormation(formations.get(0)); // Première formation (L3INFO)
        ue1.setEnseignant(enseignants.get(0)); // Premier enseignant
        ue1.setObligatoire(true);
        ues.add(ue1);
        
        UE ue2 = new UE();
        ue2.setCode("INFO102");
        ue2.setNom("Bases de Données");
        ue2.setVolumeHoraire(45);
        ue2.setCoefficient(1.0);
        ue2.setCredits(4);
        ue2.setFormation(formations.get(0)); // Première formation (L3INFO)
        ue2.setEnseignant(enseignants.get(1)); // Deuxième enseignant
        ue2.setObligatoire(true);
        ues.add(ue2);
        
        UE ue3 = new UE();
        ue3.setCode("INFO103");
        ue3.setNom("Programmation Web");
        ue3.setVolumeHoraire(30);
        ue3.setCoefficient(1.0);
        ue3.setCredits(4);
        ue3.setFormation(formations.get(0)); // Première formation (L3INFO)
        ue3.setEnseignant(enseignants.get(1)); // Deuxième enseignant
        ue3.setObligatoire(false);
        ues.add(ue3);
        
        UE ue4 = new UE();
        ue4.setCode("INFO201");
        ue4.setNom("Intelligence Artificielle");
        ue4.setVolumeHoraire(60);
        ue4.setCoefficient(1.5);
        ue4.setCredits(6);
        ue4.setFormation(formations.get(0)); // Première formation (M1INFO selon votre SQL)
        ue4.setEnseignant(enseignants.get(0)); // Premier enseignant
        ue4.setObligatoire(true);
        ues.add(ue4);
        
        UE ue5 = new UE();
        ue5.setCode("INFO202");
        ue5.setNom("Big Data");
        ue5.setVolumeHoraire(45);
        ue5.setCoefficient(1.0);
        ue5.setCredits(4);
        ue5.setFormation(formations.get(0)); // Première formation (M1INFO selon votre SQL)
        ue5.setEnseignant(enseignants.get(1)); // Deuxième enseignant
        ue5.setObligatoire(false);
        ues.add(ue5);
        
        // Générer de nouvelles UEs avec Faker
        String[] sujets = {
            "Programmation Orientée Objet", "Réseaux", "Systèmes d'Exploitation", 
            "Architecture des Ordinateurs", "Génie Logiciel", "Sécurité Informatique",
            "Cloud Computing", "DevOps", "Machine Learning", "Data Mining",
            "Anglais Technique", "Communication", "Gestion de Projet"
        };
        
        for (int i = 0; i < 10; i++) {
            UE ue = new UE();
            String prefix = formations.get(i % formations.size()).getCode().substring(0, 2);
            int codeNum = 300 + i;
            ue.setCode(prefix + "INF" + codeNum);
            ue.setNom(sujets[i % sujets.length]);
            ue.setVolumeHoraire(30 + random.nextInt(4) * 15); // 30, 45, 60, ou 75
            ue.setCoefficient(0.5f + random.nextInt(3) * 0.5); // 0.5, 1.0, 1.5, ou 2.0
            ue.setCredits(2 + random.nextInt(5)); // Entre 2 et 6
            ue.setFormation(formations.get(i % formations.size()));
            ue.setEnseignant(enseignants.get(i % enseignants.size()));
            ue.setObligatoire(random.nextBoolean());
            ues.add(ue);
        }
        
        // Persister les UEs
        for (UE ue : ues) {
            ueDAO.create(ue);
        }
        
        System.out.println(ues.size() + " UEs créées.");
    }
    
    private void creerGroupes() {
        List<Groupe> groupes = new ArrayList<>();
        List<Formation> formations = formationDAO.findAll();
        
        // Ajouter les groupes existants dans le SQL
        groupes.add(new Groupe(null, 1, TypeGroupe.TD, null, formations.get(0)));
        groupes.add(new Groupe(null, 2, TypeGroupe.TD, null, formations.get(0)));
        groupes.add(new Groupe(null, 1, TypeGroupe.TP, null, formations.get(1)));
        groupes.add(new Groupe(null, 2, TypeGroupe.TP, null, formations.get(1)));
        
        // Générer des nouveaux groupes
        String[] typesGroupe = {"TD", "TP"};
        
        for (int i = 3; i <= 6; i++) {
            for (String type : typesGroupe) {
                if (!(i == 3 && (type.equals("TD") || type.equals("TP")))) { // Éviter les doublons avec les groupes existants
                    groupes.add(new Groupe(null, i, TypeGroupe.valueOf(type), null, null));
                }
            }
        }
        
        // Persister les groupes
        for (Groupe groupe : groupes) {
            groupeDAO.create(groupe);
        }
        
        System.out.println(groupes.size() + " groupes créés.");
    }
    
    private void creerEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        
        // Ajouter les étudiants existants dans le SQL
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNom("Ngor");
        etudiant1.setPrenom("Fall");
        etudiant1.setDateNaissance(LocalDate.of(2000, 5, 15));
        etudiant1.setIne("INE001");
        etudiant1.setSexe(Sexe.MASCULIN);
        etudiant1.setAdresse("Dakar");
        etudiant1.setEmail("e");
        etudiant1.setInscriptionValidee(false);
        etudiant1.setPassword(BCrypt.hashpw("e", BCrypt.gensalt()));
        // etudiant1.setRole("ETUDIANT");
        etudiants.add(etudiant1);
        
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNom("Gaye");
        etudiant2.setPrenom("Abdoulaye");
        etudiant2.setDateNaissance(LocalDate.of(2000, 5, 15));
        etudiant2.setIne("INE002");
        etudiant2.setSexe(Sexe.MASCULIN);
        etudiant2.setAdresse("Dakar");
        etudiant2.setEmail("abdoulaye.gaye@example.com");
        etudiant2.setInscriptionValidee(false);
        etudiant2.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));
        // etudiant2.setRole("ETUDIANT");
        etudiants.add(etudiant2);
        
        Etudiant etudiant3 = new Etudiant();
        etudiant3.setNom("Diop");
        etudiant3.setPrenom("Aminata");
        etudiant3.setDateNaissance(LocalDate.of(1998, 8, 22));
        etudiant3.setIne("INE003");
        etudiant3.setSexe(Sexe.FEMININ);
        etudiant3.setAdresse("Thiès");
        etudiant3.setEmail("aminata.diop@example.com");
        etudiant3.setInscriptionValidee(false);
        etudiant3.setPassword(BCrypt.hashpw("password456", BCrypt.gensalt()));
        // etudiant3.setRole("ETUDIANT");
        etudiants.add(etudiant3);
        
        Etudiant etudiant4 = new Etudiant();
        etudiant4.setNom("Faye");
        etudiant4.setPrenom("Mamadou");
        etudiant4.setDateNaissance(LocalDate.of(1999, 12, 10));
        etudiant4.setIne("INE004");
        etudiant4.setSexe(Sexe.MASCULIN);
        etudiant4.setAdresse("Saint-Louis");
        etudiant4.setEmail("mamadou.faye@example.com");
        etudiant4.setInscriptionValidee(false);
        etudiant4.setPassword(BCrypt.hashpw("password789", BCrypt.gensalt()));
        // etudiant4.setRole("ETUDIANT");
        etudiants.add(etudiant4);
        
        // Générer 20 nouveaux étudiants avec Faker
        List<Formation> formations = formationDAO.findAll();
        
        for (int i = 5; i <= 24; i++) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom(faker.name().lastName());
            etudiant.setPrenom(faker.name().firstName());
            
            // Générer une date de naissance entre 1995 et 2002
            Date fakeDate = faker.date().birthday(18, 30);
            LocalDate dateNaissance = fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            etudiant.setDateNaissance(dateNaissance);
            
            etudiant.setIne("INE" + String.format("%03d", i));
            etudiant.setSexe(random.nextBoolean() ? Sexe.MASCULIN : Sexe.FEMININ);
            etudiant.setAdresse(faker.address().city());
            String email = etudiant.getPrenom().toLowerCase() + "." + etudiant.getNom().toLowerCase() + "@example.com";
            etudiant.setEmail(email);
            etudiant.setInscriptionValidee(random.nextBoolean());
            
            // Utiliser BCrypt pour le hachage du mot de passe
            String rawPassword = "etudiant" + faker.number().digits(3);
            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            etudiant.setPassword(hashedPassword);
            
            // etudiant.setRole("ETUDIANT");
            
            // Associer l'étudiant à une formation
            Formation formation = formations.get(random.nextInt(formations.size()));
            etudiant.setFormation(formation);
            
            etudiants.add(etudiant);
        }
        
        // Persister les étudiants
        for (Etudiant etudiant : etudiants) {
            etudiantDAO.create(etudiant);
        }
        
        System.out.println(etudiants.size() + " étudiants créés.");
    }
    
    private void inscrireEtudiantsAuxUEs() {
        // Récupérer tous les étudiants et UEs
        List<Etudiant> etudiants = etudiantDAO.findAll();
        List<UE> ues = ueDAO.findAll();
        
        // Pour chaque étudiant, l'inscrire à au moins 2 UEs aléatoires
        for (Etudiant etudiant : etudiants) {
            // Déterminer le nombre d'UEs pour cet étudiant (entre 2 et 5)
            int nbUEs = 2 + random.nextInt(4);
            
            for (int i = 0; i < nbUEs; i++) {
                UE ue = ues.get(random.nextInt(ues.size()));
                
                // Vérifier si l'étudiant n'est pas déjà inscrit à cette UE
                if (!etudiant.getUes().contains(ue)) {
                    etudiant.getUes().add(ue);
                    ue.getEtudiants().add(etudiant);
                }
            }
            
            // Mettre à jour l'étudiant
            etudiantDAO.update(etudiant);
        }
        
        System.out.println("Inscription des étudiants aux UEs terminée.");
    }
    
    public static void main(String[] args) {
        Gestion_Inscription_Initiator initiator = new Gestion_Inscription_Initiator();
        initiator.initialiserDonnees();
    }
}
