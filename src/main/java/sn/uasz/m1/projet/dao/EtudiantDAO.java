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

public class EtudiantDAO implements GenericService<Etudiant>, IEtudiantDAO {
    private static final Logger LOGGER = Logger.getLogger(EtudiantDAO.class.getName());

    public EtudiantDAO() {

    }

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
    public boolean validerInscription(Long etudiantId) {
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

                // Envoyer un e-mail après validation
                envoyerEmailValidation(etudiant);

                

                return true;
            } else {
                LOGGER.warning("Échec de la validation : étudiant introuvable (ID: " + etudiantId + ")");
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.severe("Erreur lors de la validation de l'inscription : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    private void envoyerEmailValidation(Etudiant etudiant) {
        String sujet = "🎓 Validation de votre inscription à l'UASZ";
    
        String message = "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Validation d'inscription - UASZ</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f8f9fa; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; }" +
                "h2 { color: #2c3e50; }" +
                "p { font-size: 16px; color: #333; line-height: 1.6; }" +
                ".btn { display: inline-block; margin-top: 15px; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px; }" +
                ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2>Bienvenue à l'Université Assane Seck de Ziguinchor, " + etudiant.getPrenom()+" "+ etudiant.getNom() + " 🎓</h2>" +
                "<p>Nous avons le plaisir de vous informer que votre inscription à l'UASZ a été <strong>validée avec succès</strong>. 🎉</p>" +
                "<p>Vous pouvez dès à présent accéder à votre espace étudiant pour consulter votre dossier et suivre les prochaines étapes.</p>" +
                
                "<p class='footer'>Pour toute question, contactez le service des inscriptions à <br>" +
                "<a href='mailto:scolarite@uasz.sn'>scolarite@uasz.sn</a> ou appelez le <strong>+221 77 102 61 70</strong>.</p>" +
                "<p class='footer'>Cordialement,<br>Le Service des Inscriptions - UASZ</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    
        EmailService.envoyerEmail(etudiant.getEmail(), sujet, message, true);
    }
    



    @Override
    public boolean invaliderInscription(Long etudiantId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Récupérer l'étudiant par son ID
            Etudiant etudiant = em.find(Etudiant.class, etudiantId);

            if (etudiant != null) {
                etudiant.setInscriptionValidee(false); // Modifier l'attribut
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
            // TypedQuery<Etudiant> query = em.createQuery(
            // "SELECT DISTINCT e FROM Etudiant e JOIN e.ues ue WHERE ue.formation =
            // :formation",
            // Etudiant.class);
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e WHERE e.formation = :formation",
                    Etudiant.class);

            query.setParameter("formation", formation);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override

    public List<Etudiant> getEtudiantsByUE(UE ue) {
        if (ue == null) {
            return Collections.emptyList();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createNativeQuery(
                    "SELECT u.* FROM etudiant_ue e " +
                            "JOIN utilisateur u ON e.etudiant_id = u.id " +
                            "WHERE e.ue_id = ? AND u.inscriptionValidee = true",
                    Etudiant.class);
            query.setParameter(1, ue.getId());

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    @Override

    public List<Etudiant> getEtudiantsByGroupe(Groupe groupe) {
        if (groupe == null) {
            return Collections.emptyList();
        }
    
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT e FROM Etudiant e " +
                    "WHERE e.groupe = :groupe " +
                    "AND e.inscriptionValidee = true",
                    Etudiant.class);
    
            query.setParameter("groupe", groupe);
    
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
     
}
