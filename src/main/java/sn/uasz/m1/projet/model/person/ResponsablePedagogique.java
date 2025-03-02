package sn.uasz.m1.projet.model.person;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.uasz.m1.projet.model.formation.Formation;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// @DiscriminatorValue("RESPONSABLE")
@DiscriminatorValue("ResponsablePedagogique")
public class ResponsablePedagogique extends Utilisateur {
    
    // @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Formation> formations = new HashSet<>();
    
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