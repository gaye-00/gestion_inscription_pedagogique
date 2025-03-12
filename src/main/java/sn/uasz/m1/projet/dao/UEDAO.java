package sn.uasz.m1.projet.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.interfaces.IUE;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.utils.JPAUtil;

import java.util.HashSet;
import java.util.List;

@NoArgsConstructor
public class UEDAO implements GenericService<UE>, IUE {
    @Override
    /**
     * Ajoute une UE dans la base de données.
     * 
     * @param ue L'UE à ajouter
     * @return true si l'ajout a réussi, false sinon
     */

    public boolean create(UE ue) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Si l'UE a une formation associée, s'assurer que la formation est bien gérée
            if (ue.getFormation() != null) {
                Formation formation = em.find(Formation.class, ue.getFormation().getId());
                if (formation != null) {
                    ue.setFormation(formation);
                    formation.getUes().add(ue);
                }
            }

            // Persister l'UE
            em.persist(ue);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;

        } finally {
            em.close();
        }

    }

    /**
     * Met à jour une UE existante dans la base de données.
     * 
     * @param ue L'UE avec les nouvelles valeurs à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    @Override
    public boolean update(UE ue) {
        if (ue == null || ue.getId() == null) {
            return false;
        }

        try (EntityManager em = JPAUtil.getEntityManager();) {
            em.getTransaction().begin();

            // Vérifier si l'UE existe dans la base de données
            UE existingUE = em.find(UE.class, ue.getId());
            if (existingUE == null) {
                em.getTransaction().rollback();
                return false;
            }

            // Vérifier si le nouveau code est déjà utilisé par une autre UE
            if (ue.getCode() != null && !ue.getCode().equals(existingUE.getCode())) {
                TypedQuery<Long> query = em.createQuery(
                        "SELECT COUNT(u) FROM UE u WHERE u.code = :code AND u.id != :id", Long.class);
                query.setParameter("code", ue.getCode());
                query.setParameter("id", ue.getId());
                Long count = query.getSingleResult();

                if (count > 0) {
                    // Code déjà utilisé par une autre UE
                    em.getTransaction().rollback();
                    return false;
                }
            }

            // Mettre à jour les attributs de l'UE existante
            existingUE.setCode(ue.getCode());
            existingUE.setNom(ue.getNom());
            existingUE.setVolumeHoraire(ue.getVolumeHoraire());
            existingUE.setCoefficient(ue.getCoefficient());
            existingUE.setCredits(ue.getCredits());
            existingUE.setNomResponsable(ue.getNomResponsable());
            existingUE.setObligatoire(ue.isObligatoire());

            // Gérer le changement de formation si nécessaire
            if (ue.getFormation() != null &&
                    (existingUE.getFormation() == null ||
                            !existingUE.getFormation().getId().equals(ue.getFormation().getId()))) {

                // Enlever l'UE de l'ancienne formation si elle existe
                if (existingUE.getFormation() != null) {
                    Formation oldFormation = em.find(Formation.class, existingUE.getFormation().getId());
                    if (oldFormation != null) {
                        oldFormation.removeUE(existingUE);
                        em.merge(oldFormation);
                    }
                }

                // Ajouter l'UE à la nouvelle formation
                Formation newFormation = em.find(Formation.class, ue.getFormation().getId());
                if (newFormation != null) {
                    newFormation.addUE(existingUE);
                    em.merge(newFormation);
                }
            } else if (ue.getFormation() == null && existingUE.getFormation() != null) {
                // Retirer l'UE de sa formation actuelle si la nouvelle formation est null
                Formation oldFormation = em.find(Formation.class, existingUE.getFormation().getId());
                if (oldFormation != null) {
                    oldFormation.removeUE(existingUE);
                    em.merge(oldFormation);
                }
                existingUE.setFormation(null);
            }

            // Persister les changements
            em.merge(existingUE);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Supprime une UE de la base de données.
     * 
     * @param ue L'UE à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    @Override
    public boolean delete(Long ueId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            em.getTransaction().begin();
            // Récupérer l'UE depuis la base de données pour s'assurer qu'elle est gérée
            UE ue = em.find(UE.class, ueId);
            if (ue != null) {
                // Si l'UE est associée à une formation, on doit d'abord supprimer cette
                // association
                if (ue.getFormation() != null) {
                    Formation formation = ue.getFormation();
                    formation.removeUE(ue);
                    em.merge(formation);
                }
                // Supprimer les associations avec les étudiants
                for (Etudiant etudiant : new HashSet<>(ue.getEtudiants())) {
                    etudiant.getUes().remove(ue);
                    em.merge(etudiant);
                }

                em.remove(ue);
                em.getTransaction().commit();
                return true;
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
        return false;

    }

    @Override
    public UE findById(Long ueId) {
        EntityManager em = JPAUtil.getEntityManager();
        UE ue = em.find(UE.class, ueId);
        em.close();
        return ue;
    }

    @Override
    public List<UE> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<UE> query = em.createQuery("SELECT u FROM UE u", UE.class);
        List<UE> ues = query.getResultList();
        em.close();
        return ues;
    }

    /**
     * Récupère les UEs liées à une formation spécifique.
     * 
     * @param formationId Id de La formation dont on veut récupérer les UEs
     * @return La liste des UEs liées à cette formation, ou une liste vide si aucune
     *         UE n'est trouvée
     */

    @Override
    public List<UE> getUEsByFormation(Long formationId) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<UE> query = em.createQuery("SELECT u FROM UE u WHERE u.formation.id = :formationId", UE.class);
        query.setParameter("formationId", formationId);
        List<UE> ues = query.getResultList();
        em.close();
        return ues;
    }

    // Find UEs Optionnelles by Formation
    @Override
    public List<UE> findUEsOptionnellesByFormation(Long formationId) {
        try (EntityManager em = JPAUtil.getEntityManager();) {
            TypedQuery<UE> query = em.createQuery(
                    "SELECT u FROM UE u WHERE u.formation.id = :formationId AND u.obligatoire = false",
                    UE.class);
            query.setParameter("formationId", formationId);
            return query.getResultList();
        }
    }

    @Override
    public List<UE> findUEsObligatoiresByFormation(Long formationId) {
        try (EntityManager em = JPAUtil.getEntityManager();) {
            TypedQuery<UE> query = em.createQuery(
                    "SELECT u FROM UE u WHERE u.formation.id = :formationId AND u.obligatoire = true",
                    UE.class);
            query.setParameter("formationId", formationId);
            return query.getResultList();
        }

    }

    /**
     * Trouver une UE par son code
     */

    @Override
    public UE findUEByCode(List<UE> ues, String code) {
        for (UE ue : ues) {
            if (ue.getCode().equals(code)) {
                return ue;
            }
        }
        return null;
    }

}
