package sn.uasz.m1.projet.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.interfaces.IEtudiantDAO;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.utils.JPAUtil;

@NoArgsConstructor
public class EtudiantDAO implements GenericService<Etudiant>, IEtudiantDAO {

    @Override
    public boolean create(Etudiant etudiant) {
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
    public boolean update(Etudiant etudiant) {
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
            Etudiant etudiant = em.find(Etudiant.class, etudiantId);
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
    public Etudiant findById(Long etudiantId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Etudiant etudiant = em.find(Etudiant.class, etudiantId);
            em.close();
            return etudiant;
        }
    }

    @Override
    public List<Etudiant> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Etudiant> query = em.createQuery("SELECT e FROM Etudiant e", Etudiant.class);
            List<Etudiant> etudiants = query.getResultList();
            em.close();
            return etudiants;
        }
    }

    @Override
    public boolean isInscriptionValidee(Long etudiantId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Boolean> query = em.createQuery(
                    "SELECT e.inscriptionValidee FROM Etudiant e WHERE e.id = :etudiantId",
                    Boolean.class);
            query.setParameter("etudiantId", etudiantId);
            return query.getSingleResult() != null && query.getSingleResult();
        }
    }

    @Override
    public boolean validerInscriptionEtudiant(Long etudiantId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Récupérer l'étudiant par son ID
            Etudiant etudiant = em.find(Etudiant.class, etudiantId);

            if (etudiant != null) {
                etudiant.setInscriptionValidee(true); // Modifier l'attribut
                em.merge(etudiant); // Sauvegarder la modification
                transaction.commit();
                return true;
            } else {
                transaction.rollback(); // Annuler si l'étudiant n'existe pas
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean inscrireUEsOptionelles(Long etudiantId, List<Long> selectedUEIds) {
        try (EntityManager em = JPAUtil.getEntityManager();) {
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

    @Override
    public List<Etudiant> getEtudiantsByFormation(Formation formation) {
        if (formation == null) {
            return new ArrayList<>();
        }
        try (EntityManager em = JPAUtil.getEntityManager();) {
            // Requête JPQL pour récupérer les étudiants inscrits à au moins une UE de cette
            // formation
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT DISTINCT e FROM Etudiant e JOIN e.ues ue WHERE ue.formation = :formation",
                    Etudiant.class);
            query.setParameter("formation", formation);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}
