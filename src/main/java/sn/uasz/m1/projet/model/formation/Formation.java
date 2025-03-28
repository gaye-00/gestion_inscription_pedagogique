package sn.uasz.m1.projet.model.formation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "formation")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    @Column(unique = true)
    private String code;
    private Integer nombreOptionsRequis;
    private Integer maxEffectifGroupe;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UE> ues = new HashSet<>();

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Etudiant> etudiants = new HashSet<>();

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Groupe> groupes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsablePedagogique responsable;
    @ManyToOne
    @JoinColumn(name = "responsableFormation_id")
    private Enseignant responsableFormation;

    // Méthodes helper
    public void addUE(UE ue) {
        this.ues.add(ue);
        ue.setFormation(this);
    }

    public void removeUE(UE ue) {
        this.ues.remove(ue);
        ue.setFormation(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Formation formation = (Formation) o;
        return Objects.equals(id, formation.id); // Assurez-vous d'utiliser un champ unique comme ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Assurez-vous que 'id' est unique et non nul
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", responsablePedagogique='"
                + (responsable != null ? responsable.getNom() : "null") + '\'' +
                '}';
    }

}
