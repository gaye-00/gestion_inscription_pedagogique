package sn.uasz.m1.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.utils.JPAUtil;
import java.util.List;

public class EtudiantService implements GenericService<Etudiant> {

    @Override
    public void add(Etudiant etudiant) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(etudiant);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Etudiant etudiant) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(etudiant);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Long etudiantId) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Etudiant etudiant = em.find(Etudiant.class, etudiantId);
        if (etudiant != null) {
            em.remove(etudiant);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Etudiant getById(Long etudiantId) {
        EntityManager em = JPAUtil.getEntityManager();
        Etudiant etudiant = em.find(Etudiant.class, etudiantId);
        em.close();
        return etudiant;
    }

    @Override
    public List<Etudiant> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Etudiant> query = em.createQuery("SELECT e FROM Etudiant e", Etudiant.class);
        List<Etudiant> etudiants = query.getResultList();
        em.close();
        return etudiants;
    }
}
