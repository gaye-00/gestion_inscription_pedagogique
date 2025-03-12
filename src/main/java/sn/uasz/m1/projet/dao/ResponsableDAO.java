package sn.uasz.m1.projet.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import sn.uasz.m1.projet.dto.Etat;
import sn.uasz.m1.projet.interfaces.GenericService;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;
import sn.uasz.m1.projet.utils.JPAUtil;
import java.util.List;

@NoArgsConstructor
public class ResponsableDAO implements GenericService<ResponsablePedagogique> {

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
    public ResponsablePedagogique findById(Long etudiantId) {
        EntityManager em = JPAUtil.getEntityManager();
        ResponsablePedagogique responsablePedagogique = em.find(ResponsablePedagogique.class, etudiantId);
        em.close();
        return responsablePedagogique;
    }

    @Override
    public List<ResponsablePedagogique> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<ResponsablePedagogique> query = em.createQuery("SELECT e FROM Etudiant e",
                ResponsablePedagogique.class);
        List<ResponsablePedagogique> responsablePedagogiques = query.getResultList();
        em.close();
        return responsablePedagogiques;
    }

    @Override
    public boolean create(ResponsablePedagogique entity) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(ResponsablePedagogique entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            em.close();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }

    }

    public Etat etatDashboard() {

        EntityManager em = JPAUtil.getEntityManager();

        return new Etat(em.createQuery("SELECT COUNT(f) FROM Formation f", Long.class).getSingleResult(),
                em.createQuery("SELECT COUNT(u) FROM UE u", Long.class).getSingleResult(), em.createQuery(
                        "SELECT COUNT(DISTINCT e) FROM Etudiant e JOIN e.ues u", Long.class)
                        .getSingleResult(),
                em.createQuery("SELECT COUNT(e) FROM Etudiant e", Long.class).getSingleResult());
    }

}
