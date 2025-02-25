package sn.uasz.m1.projet.model.person;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;

@Entity
@DiscriminatorValue("ETUDIANT")
public class Etudiant extends Utilisateur {
    
    @Column(unique = true)
    private String ine;
    
    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "etudiant_ue",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "ue_id")
    )
    private Set<UE> ues = new HashSet<>();
    
    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Set<UE> getUes() {
        return ues;
    }

    public void setUes(Set<UE> ues) {
        this.ues = ues;
    }
    
    // Méthodes helper
    public void addUE(UE ue) {
        this.ues.add(ue);
    }
    
    public void removeUE(UE ue) {
        this.ues.remove(ue);
    }
}
