package sn.uasz.m1.projet.interfaces;

import java.util.List;

import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;

public interface IEtudiantDAO {
    boolean isInscriptionValidee(Long etudiantId);

    boolean validerInscription(Long etudiantId);
    boolean invaliderInscription(Long etudiantId);

    boolean inscrireUEsOptionelles(Long etudiantId, List<Long> selectedUEIds);

    /**
     * Récupère les étudiants inscrits à une formation spécifique.
     * Un étudiant est considéré comme inscrit à une formation s'il est inscrit à au
     * moins une UE
     * appartenant à cette formation.
     * 
     * @param formation La formation dont on veut récupérer les étudiants inscrits
     * @return La liste des étudiants inscrits à cette formation, ou une liste vide
     *         si aucun étudiant n'est trouvé
     */
    public List<Etudiant> getEtudiantsByFormation(Formation formation);
    public List<Etudiant> getEtudiantsByUE(UE ue);
    public List<Etudiant> getEtudiantsByGroupe(Groupe groupe);
}
