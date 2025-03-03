package sn.uasz.m1.projet.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.model.formation.Formation;

public class FormationDAO {

    private static final String PERSISTENCE_UNIT_NAME = "gestion_inscription_pedagogiquePU";
    private static EntityManagerFactory factory;

    public FormationDAO() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    // Create
    public void create(Formation formation) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(formation);
            em.getTransaction().commit();
            em.close();
        }
    }

    // Read by ID
    public Formation findById(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            Formation formation = em.find(Formation.class, id);
            em.close();
            return formation;
        }
    }

    // Read all
    public List<Formation> findAll() {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<Formation> query = em.createQuery("SELECT f FROM Formation f", Formation.class);
            List<Formation> formations = query.getResultList();
            em.close();
            return formations;
        }
    }

    // Update
    public void update(Formation formation) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(formation);
            em.getTransaction().commit();
            em.close();
        }
    }

    // Delete
    public void delete(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            Formation formation = em.find(Formation.class, id);
            if (formation != null) {
                em.remove(formation);
            }
            em.getTransaction().commit();
            em.close();
        }
    }

    // Delete
    // public void delete(Long id) {
    //     try (EntityManager em = factory.createEntityManager()) {
    //         em.getTransaction().begin();
    //         em.find(Formation.class, id)
    //         .ifPresent(em::remove);
    //         em.getTransaction().commit();
    //     }
    // }

    // Find Formation by Etudiant ID
    public Formation findFormationByEtudiant(Long etudiantId) {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<Formation> query = em.createQuery(
                "SELECT f FROM Formation f JOIN f.ues u JOIN u.etudiants e WHERE e.id = :etudiantId", 
                Formation.class
            );
            query.setParameter("etudiantId", etudiantId);
            return query.getResultStream().findFirst().orElse(null);
        }
    }
}

