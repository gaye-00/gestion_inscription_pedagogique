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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.uasz.m1.projet.model.formation.Formation;
import sn.uasz.m1.projet.model.formation.Groupe;
import sn.uasz.m1.projet.model.formation.UE;

// @EqualsAndHashCode(callSuper = true)
// @EqualsAndHashCode(callSuper = true, exclude = {"ues"})
@EqualsAndHashCode(callSuper = true, exclude = {"ues", "groupe", "formation"}) // Exclure toutes les relations
@ToString(exclude = {"ues", "groupe", "formation"}) // Éviter les cycles dans toString aussi
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("ETUDIANT")
public class Etudiant extends Utilisateur {

    @Column(unique = true)
    private String ine;

    @ManyToOne
    @JoinColumn(name = "groupe_id")
    private Groupe groupe;

    // @ManyToMany(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "etudiant_ue", joinColumns = @JoinColumn(name = "etudiant_id"), inverseJoinColumns = @JoinColumn(name = "ue_id"))
    private Set<UE> ues = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    // @Default(false)
    private boolean inscriptionValidee;
}
