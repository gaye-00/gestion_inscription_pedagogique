package sn.uasz.m1.projet.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.interfaces.IEtudiantDAO;
import sn.uasz.m1.projet.interfaces.IGroupeDAO;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;

import sn.uasz.m1.projet.utils.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.logging.Logger;

public class GroupeDAO implements GenericService<Groupe>, IGroupeDAO {
    private static final Logger LOGGER = Logger.getLogger(GroupeDAO.class.getName());

    public GroupeDAO() {

    }

    @Override
    public boolean create(Groupe etudiant) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(etudiant);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Groupe etudiant) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(etudiant);
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
            Groupe etudiant = em.find(Groupe.class, etudiantId);
            if (etudiant != null) {
                em.remove(etudiant);
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
    public Groupe findById(Long etudiantId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Groupe etudiant = em.find(Groupe.class, etudiantId);
            em.close();
            return etudiant;
        }
    }

    @Override
    public List<Groupe> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Groupe> query = em.createQuery("SELECT e FROM Groupe e", Groupe.class);
            List<Groupe> etudiants = query.getResultList();
            em.close();
            return etudiants;
        }
    }

    @Override
    public List<Etudiant> getEtudiantsByGroupe(Groupe groupe) {
        if (groupe == null) {
            return new ArrayList<>();
        }
        try (EntityManager em = JPAUtil.getEntityManager();) {

            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Groupe e WHERE e.formation = :formation",
                    Etudiant.class);
            query.setParameter("formation", groupe);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Groupe> getGroupesByFormation(Formation formation) {
        if (formation == null) {
            return new ArrayList<>();
        }
        try (EntityManager em = JPAUtil.getEntityManager();) {

            TypedQuery<Groupe> query = em.createQuery(
                    "SELECT e FROM Groupe e WHERE e.formation = :formation",
                    Groupe.class);
            query.setParameter("formation", formation);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
