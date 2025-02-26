package sn.uasz.m1.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.utils.JPAUtil;

import java.util.List;

public class FormationService implements GenericService<Formation> {
    @Override
    public void add(Formation formation) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(formation);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void update(Formation formation) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(formation);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void delete(Long formationId) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Formation formation = em.find(Formation.class, formationId);
        if (formation != null) {
            em.remove(formation);
        }
        em.getTransaction().commit();
        em.close();
    }
    @Override

    public Formation getById(Long formationId) {
        EntityManager em = JPAUtil.getEntityManager();
        Formation formation = em.find(Formation.class, formationId);
        em.close();
        return formation;
    }

    @Override
    public List<Formation> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Formation> query = em.createQuery("SELECT f FROM Formation f", Formation.class);
        List<Formation> formations = query.getResultList();
        em.close();
        return formations;
    }
}
