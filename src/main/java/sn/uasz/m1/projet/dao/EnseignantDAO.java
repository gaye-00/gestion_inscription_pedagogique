package sn.uasz.m1.projet.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.utils.JPAUtil;

public class EnseignantDAO implements GenericService<Enseignant> {

    @Override
    public boolean create(Enseignant enseignant) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(enseignant);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Enseignant enseignant) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(enseignant);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Long etudiantId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            Enseignant enseignant = em.find(Enseignant.class, etudiantId);
            if (enseignant != null) {
                em.remove(enseignant);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();

                return false;
            }
        } catch (Exception e) {

            return false;
        }

    }

    public boolean deleteByMatricule(String matricule) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Enseignant> query = em.createQuery("SELECT e FROM Enseignant e WHERE e.matricule = :matricule",
                    Enseignant.class);
            query.setParameter("matricule", matricule);
            Enseignant enseignant = query.getSingleResult();
            if (enseignant != null) {
                em.remove(enseignant);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();

                return false;
            }
        } catch (Exception e) {

            return false;
        }

    }

    @Override
    public Enseignant findById(Long etudiantId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Enseignant enseignant = em.find(Enseignant.class, etudiantId);
            em.close();
            return enseignant;
        }
    }

    public Enseignant findByMatricule(String matricule) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Enseignant> query = em.createQuery("SELECT e FROM Enseignant e WHERE e.matricule = :matricule",
                    Enseignant.class);
            query.setParameter("matricule", matricule);
            Enseignant enseignant = query.getSingleResult();
            em.close();
            return enseignant;
        }
    }

    @Override
    public List<Enseignant> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Enseignant> query = em.createQuery("SELECT e FROM Enseignant e", Enseignant.class);
            List<Enseignant> etudiants = query.getResultList();
            em.close();
            return etudiants;
        }
    }
    
}
