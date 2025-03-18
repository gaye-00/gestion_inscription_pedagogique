package sn.uasz.m1.projet.dao;

import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
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
        System.out.println(
                "f$$$$$$$$$$$$$$$$$$$$$$$ code " + formation.getCode() + " et identifiant" + formation.getId());
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
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;

        try {
            Formation formation = em.find(Formation.class, formationId);
            if (formation == null) {
                return false;
            }

            // Étape 1 : Supprimer les relations en plusieurs transactions
            detachRelations(formation);
            removeFormation(formationId);

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return success;
    }

    // Étape 1 : Supprimer les relations
    private void detachRelations(Formation formation) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // Dissocier les UE
            for (UE ue : new HashSet<>(formation.getUes())) {
                ue.setFormation(null);
                em.merge(ue);
            }
            formation.getUes().clear();

            // Dissocier les Étudiants
            for (Etudiant etudiant : new HashSet<>(formation.getEtudiants())) {
                etudiant.setFormation(null);
                em.merge(etudiant);
            }
            formation.getEtudiants().clear();

            // Dissocier les Groupes
            for (Groupe groupe : new HashSet<>(formation.getGroupes())) {
                groupe.setFormation(null);
                em.merge(groupe);
            }
            formation.getGroupes().clear();

            // Dissocier les responsables
            formation.setResponsable(null);
            formation.setResponsableFormation(null);

            em.merge(formation);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Étape 2 : Supprimer la formation dans une transaction distincte
    private void removeFormation(Long formationId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            Formation formation = em.find(Formation.class, formationId);
            if (formation != null) {
                em.remove(formation);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
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
            // "SELECT f FROM Formation f JOIN f.ues u JOIN u.etudiants e WHERE e.id =
            // :etudiantId",
            // Formation.class
            // );
            TypedQuery<Formation> query = em.createQuery(
                    "SELECT f FROM Formation f JOIN Etudiant e ON f.id = e.formation.id WHERE e.id = :etudiantId",
                    Formation.class);
            query.setParameter("etudiantId", etudiantId);
            return query.getResultStream().findFirst().orElse(null);
        }
    }

}
