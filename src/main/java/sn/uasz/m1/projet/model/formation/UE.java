package sn.uasz.m1.projet.model.formation;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import sn.uasz.m1.projet.model.person.Etudiant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @ManyToMany(mappedBy = "ues")
    private Set<Etudiant> etudiants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

}
