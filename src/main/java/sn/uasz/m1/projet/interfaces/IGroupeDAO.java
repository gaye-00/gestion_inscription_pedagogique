package sn.uasz.m1.projet.interfaces;

import java.util.List;

import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;
import sn.uasz.m1.projet.model.person.Etudiant;

public interface IGroupeDAO {
  
    public List<Etudiant> getEtudiantsByGroupe(Groupe groupe) ;
    public List<Groupe> getGroupesByFormation(Formation groupe) ;
    
}
