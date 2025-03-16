package sn.uasz.m1.projet.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.utils.JPAUtil;

@NoArgsConstructor
public class FormationDAO implements GenericService<Formation> {
    @Override
    public boolean create(Formation formation) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(formation);
            em.getTransaction().commit();

            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Formation formation) {
        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("f$$$$$$$$$$$$$$$$$$$$$$$ code "+formation.getCode()+ " et identifiant"+formation.getId());
        try {
            em.getTransaction().begin();

            // Vérifier si l'entité existe déjà
            Formation existingFormation = em.find(Formation.class, formation.getId());
            if (existingFormation == null) {
                System.out.println(
                        "######################entité n'existe pas en base, on ne peut pas faire de mise à jour####");
                // L'entité n'existe pas en base, on ne peut pas faire de mise à jour
                em.getTransaction().rollback();
                return false;
            }

            // Mise à jour si l'entité existe
            em.merge(formation);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Long formationId) {

        try (EntityManager em = JPAUtil.getEntityManager()) {

            em.getTransaction().begin();
            Formation formation = em.find(Formation.class, formationId);
            if (formation != null) {
                em.remove(formation);
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
    public Formation findById(Long formationId) {
        Formation formation;
        try (EntityManager em = JPAUtil.getEntityManager()) {
            formation = em.find(Formation.class, formationId);
        }
        return formation;
    }

    @Override
    public List<Formation> findAll() {
        List<Formation> formations;
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            TypedQuery<Formation> query = em.createQuery(
                    "SELECT f FROM Formation f LEFT JOIN FETCH f.ues", Formation.class);
            formations = query.getResultList();
            transaction.commit();
        }
        return formations;
    }

    public Formation findFormationByEtudiant(Long etudiantId) {
        // try (EntityManager em = factory.createEntityManager()) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            // TypedQuery<Formation> query = em.createQuery(
            //     "SELECT f FROM Formation f JOIN f.ues u JOIN u.etudiants e WHERE e.id = :etudiantId", 
            //     Formation.class
            // );
            TypedQuery<Formation> query = em.createQuery(
                "SELECT f FROM Formation f JOIN Etudiant e ON f.id = e.formation.id WHERE e.id = :etudiantId", 
                Formation.class
            );
            query.setParameter("etudiantId", etudiantId);
            return query.getResultStream().findFirst().orElse(null);
        }
    }

}
