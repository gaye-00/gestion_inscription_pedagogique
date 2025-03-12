package sn.uasz.m1.projet.interfaces;

import java.util.List;

import sn.uasz.m1.projet.model.formation.UE;

public interface IUE {
    public List<UE> findUEsOptionnellesByFormation(Long formationId);
    public List<UE> findUEsObligatoiresByFormation(Long formationId); 
    public UE findUEByCode(List<UE> ues, String code);
    public List<UE> getUEsByFormation(Long formationId);
    
}