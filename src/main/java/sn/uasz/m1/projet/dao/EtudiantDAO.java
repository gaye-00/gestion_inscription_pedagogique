package sn.uasz.m1.projet.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import sn.uasz.m1.projet.email.EmailService;
import sn.uasz.m1.projet.email.EmailServiceImpl;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.interfaces.IEtudiantDAO;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.TypeGroupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.utils.JPAUtil;

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

                // Ajouter l'étudiant dans un groupe
                ajouterDansGroupe(etudiant); 
                
                return true;
            } else {
                LOGGER.log(Level.WARNING, "\nEchec de la validation : Etudiant introuvable (ID: {0})\n", etudiantId);
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // LOGGER.severe("Erreur lors de la validation de l'inscription : " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erreur lors de la validation de l''inscription : {0}", e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Ajoute un étudiant dans un groupe approprié de sa formation.
     * Si aucun groupe n'existe ou si tous les groupes existants sont pleins,
     * un nouveau groupe est créé pour accueillir l'étudiant.
     * 
     * @param etudiant L'étudiant à ajouter dans un groupe
     */
    public void ajouterDansGroupe(Etudiant etudiant) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Récupérer l'étudiant et sa formation depuis la base de données pour avoir les données à jour
            etudiant = em.find(Etudiant.class, etudiant.getId());
            Formation formation = etudiant.getFormation();
            
            if (formation == null) {
                LOGGER.log(Level.WARNING, "\nImpossible d'ajouter l'étudiant dans un groupe: formation non définie (ID: {0})\n", etudiant.getId());
                transaction.rollback();
                return;
            }
            
            // Rafraîchir la formation pour avoir les groupes à jour
            formation = em.find(Formation.class, formation.getId());
            Integer maxEffectif = formation.getMaxEffectifGroupe();
            
            if (maxEffectif == null || maxEffectif <= 0) {
                LOGGER.log(Level.WARNING, "\nLa formation n'a pas défini de taille maximale de groupe valide (ID: {0})\n", formation.getId());
                transaction.rollback();
                return;
            }
            
            // Chercher un groupe disponible
            Groupe groupeDisponible = null;
            TypeGroupe typeGroupe = TypeGroupe.TD; // Supposons que nous ajoutons dans des groupes de TD
            
            // Vérifier si la formation a des groupes
            if (formation.getGroupes() != null && !formation.getGroupes().isEmpty()) {
                // Chercher un groupe qui n'a pas atteint la capacité maximale
                for (Groupe groupe : formation.getGroupes()) {
                    if (groupe.getTypeGroupe() == typeGroupe && groupe.getEtudiants().size() < maxEffectif) {
                        groupeDisponible = groupe;
                        break;
                    }
                }
            }
            
            // Si aucun groupe disponible, créer un nouveau groupe
            if (groupeDisponible == null) {
                groupeDisponible = new Groupe();
                groupeDisponible.setTypeGroupe(typeGroupe);
                groupeDisponible.setFormation(formation);
                
                // Déterminer le numéro du nouveau groupe
                int numeroGroupe = 1;
                if (formation.getGroupes() != null && !formation.getGroupes().isEmpty()) {
                    // Trouver le plus grand numéro de groupe et ajouter 1
                    for (Groupe g : formation.getGroupes()) {
                        if (g.getTypeGroupe() == typeGroupe && g.getNumero() != null && g.getNumero() >= numeroGroupe) {
                            numeroGroupe = g.getNumero() + 1;
                        }
                    }
                }
                groupeDisponible.setNumero(numeroGroupe);
                
                // Ajouter le groupe à la formation
                formation.getGroupes().add(groupeDisponible);
                em.persist(groupeDisponible);
                LOGGER.log(Level.INFO, "Nouveau groupe créé: {0} pour la formation {1}", 
                        new Object[]{groupeDisponible.toString(), formation.getNom()});
            }
            
            // Ajouter l'étudiant au groupe
            if (etudiant.getGroupe() != null) {
                // Si l'étudiant est déjà dans un groupe, le retirer d'abord
                etudiant.getGroupe().removeEtudiant(etudiant);
            }
            
            groupeDisponible.addEtudiant(etudiant);
            em.merge(etudiant);
            em.merge(groupeDisponible);
            
            transaction.commit();
            LOGGER.log(Level.INFO, "Étudiant {0} {1} ajouté au groupe {2}", 
                    new Object[]{etudiant.getNom(), etudiant.getPrenom(), groupeDisponible.toString()});
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'étudiant dans un groupe: {0}", e.getMessage());
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
                ".container { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                +
                "h2 { color: #2c3e50; }" +
                "p { font-size: 16px; color: #333; line-height: 1.6; }" +
                ".btn { display: inline-block; margin-top: 15px; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px; }"
                +
                ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2>Bienvenue à l'Université Assane Seck de Ziguinchor, " + etudiant.getPrenom() + " "
                + etudiant.getNom() + " 🎓</h2>" +
                "<p>Nous avons le plaisir de vous informer que votre inscription à l'UASZ a été <strong>validée avec succès</strong>. 🎉</p>"
                +
                "<p>Vous pouvez dès à présent accéder à votre espace étudiant pour consulter votre dossier et suivre les prochaines étapes.</p>"
                +

                "<p class='footer'>Pour toute question, contactez le service des inscriptions à <br>" +
                "<a href='mailto:scolarite@uasz.sn'>scolarite@uasz.sn</a> ou appelez le <strong>+221 77 102 61 70</strong>.</p>"
                +
                "<p class='footer'>Cordialement,<br>Le Service des Inscriptions - UASZ</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        // EmailService.envoyerEmail(etudiant.getEmail(), sujet, message, true);

        try {
            EmailService emailService = new EmailServiceImpl("");
            emailService.sendEmail(etudiant.getEmail(), sujet, message);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail de validation : " + e.getMessage());
        }
         catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'e-mail de validation : " + e.getMessage());
        }

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
            TypedQuery<Etudiant> query = em.createQuery(
                    "SELECT DISTINCT e FROM Etudiant e JOIN e.ues ue WHERE ue.formation =:formation",
                    Etudiant.class);
            // TypedQuery<Etudiant> query = em.createQuery(
            // "SELECT e FROM Etudiant e WHERE e.formation = :formation",
            // Etudiant.class);

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
