package sn.uasz.m1.projet.model.person;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import sn.uasz.m1.projet.model.formation.Formation;

@Entity
@DiscriminatorValue("RESPONSABLE")
public class ResponsablePedagogique extends Utilisateur {
    
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private Set<Formation> formations = new HashSet<>();
    
    public Set<Formation> getFormations() {
        return formations;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }
    
    // Méthodes helper
    public void addFormation(Formation formation) {
        this.formations.add(formation);
        formation.setResponsable(this);
    }
    
    public void removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.setResponsable(null);
    }
}