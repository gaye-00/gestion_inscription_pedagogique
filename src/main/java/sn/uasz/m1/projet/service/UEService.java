package sn.uasz.m1.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.utils.JPAUtil;

import java.util.List;

public class UEService implements GenericService<UE> {
    @Override
    public void add(UE ue) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(ue);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(UE ue) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(ue);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Long ueId) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        UE ue = em.find(UE.class, ueId);
        if (ue != null) {
            em.remove(ue);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public UE getById(Long ueId) {
        EntityManager em = JPAUtil.getEntityManager();
        UE ue = em.find(UE.class, ueId);
        em.close();
        return ue;
    }

    @Override
    public List<UE> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<UE> query = em.createQuery("SELECT u FROM UE u", UE.class);
        List<UE> ues = query.getResultList();
        em.close();
        return ues;
    }

    public List<UE> getUEsByFormation(Long formationId) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<UE> query = em.createQuery("SELECT u FROM UE u WHERE u.formation.id = :formationId", UE.class);
        query.setParameter("formationId", formationId);
        List<UE> ues = query.getResultList();
        em.close();
        return ues;
    }
}
