package sn.uasz.m1.projet.service_backup;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.model.formation.UE;

public class UEDAO {

    private static final String PERSISTENCE_UNIT_NAME = "gestion_inscription_pedagogiquePU";
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    // Create
    public void create(UE ue) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(ue);
            em.getTransaction().commit();
        }
    }

    // Read by ID
    public UE findById(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            return em.find(UE.class, id);
        }
    }

    // Read all
    public List<UE> findAll() {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<UE> query = em.createQuery("SELECT u FROM UE u", UE.class);
            return query.getResultList();
        }
    }

    // Update
    public void update(UE ue) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(ue);
            em.getTransaction().commit();
        }
    }

    // Delete
    public void delete(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            em.getTransaction().begin();
            UE ue = em.find(UE.class, id);
            if (ue != null) {
                em.remove(ue);
            }
            em.getTransaction().commit();
        }
    }

    // Find UEs Obligatoires by Formation
    public List<UE> findUEsObligatoiresByFormation(Long formationId) {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<UE> query = em.createQuery(
                "SELECT u FROM UE u WHERE u.formation.id = :formationId AND u.obligatoire = true", 
                UE.class
            );
            query.setParameter("formationId", formationId);
            return query.getResultList();
        }
    }

    // Find UEs Optionnelles by Formation
    public List<UE> findUEsOptionnellesByFormation(Long formationId) {
        try (EntityManager em = factory.createEntityManager()) {
            TypedQuery<UE> query = em.createQuery(
                "SELECT u FROM UE u WHERE u.formation.id = :formationId AND u.obligatoire = false", 
                UE.class
            );
            query.setParameter("formationId", formationId);
            return query.getResultList();
        }
    }
}

