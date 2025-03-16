package sn.uasz.m1.projet.model.formation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.uasz.m1.projet.model.person.Enseignant;
import sn.uasz.m1.projet.model.person.Etudiant;

@EqualsAndHashCode(exclude = { "etudiants" })
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ue")
public class UE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String nom;
    private Integer volumeHoraire;
    private Double coefficient;
    private Integer credits;
    private String nomResponsable;

    @ManyToMany(mappedBy = "ues", fetch = FetchType.EAGER)
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    private boolean obligatoire;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UE ue = (UE) o;
        return Objects.equals(id, ue.id); // Utilisez un identifiant unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Assurez-vous que 'id' est unique et non nul
    }

    @Override
    public String toString() {
        return "UE{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", volumeHoraire=" + volumeHoraire +
                '}';
    }

}
