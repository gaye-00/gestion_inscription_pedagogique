package sn.uasz.m1.projet.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;

public class EtudiantDAO {

    private static final String PERSISTENCE_UNIT_NAME = "gestion_inscription_pedagogiquePU";
    private static EntityManagerFactory factory;

    public EtudiantDAO() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    // Create
    public void create(Etudiant etudiant) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(etudiant);
            em.getTransaction().commit();
            em.close();
        }
    }

    // Read by ID
    public Etudiant findById(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            Etudiant etudiant = em.find(Etudiant.class, id);
            em.close();
            return etudiant;
        }
    }

    // Read all
    public List<Etudiant> findAll() {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery("SELECT e FROM Etudiant e", Etudiant.class);
            List<Etudiant> etudiants = query.getResultList();
            em.close();
            return etudiants;
        }
    }

    // Update
    public void update(Etudiant etudiant) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(etudiant);
            em.getTransaction().commit();
            em.close();
        }
    }

    // Delete
    public void delete(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            Etudiant etudiant = em.find(Etudiant.class, id);
            if (etudiant != null) {
                em.remove(etudiant);
            }
            em.getTransaction().commit();
            em.close();
        }
    }

    // Vérifie si l'inscription de l'étudiant est validée
    public boolean isInscriptionValidee(Long etudiantId) {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<Boolean> query = em.createQuery(
                "SELECT e.inscriptionValidee FROM Etudiant e WHERE e.id = :etudiantId",
                Boolean.class
            );
            query.setParameter("etudiantId", etudiantId);
            return query.getSingleResult() != null && query.getSingleResult();
        }
    }

    // Inscrit un étudiant aux UEs optionnelles sélectionnées
    public boolean inscrireUEsOptionelles(Long etudiantId, List<Long> selectedUEIds) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();

            // Récupérer l'étudiant depuis la base de données
            Etudiant etudiant = em.find(Etudiant.class, etudiantId);
            if (etudiant == null) {
                System.out.println("Étudiant non trouvé.");
                return false;
            }

            // Récupérer les UEs sélectionnées
            TypedQuery<UE> query = em.createQuery(
                "SELECT ue FROM UE ue WHERE ue.id IN :ueIds AND ue.obligatoire = false", UE.class);
            query.setParameter("ueIds", selectedUEIds);
            List<UE> selectedUEs = query.getResultList();

            // Ajouter les UEs optionnelles à l'étudiant
            for (UE ue : selectedUEs) {
                etudiant.getUes().add(ue);
                ue.getEtudiants().add(etudiant);
            }

            em.merge(etudiant); // Mettre à jour l'étudiant avec les nouvelles UEs
            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'inscription des UEs optionnelles : " + e.getMessage());
            return false;
        }
    }
}

