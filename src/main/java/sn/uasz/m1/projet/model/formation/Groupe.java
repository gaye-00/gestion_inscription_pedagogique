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
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;

    @Enumerated(EnumType.STRING)
    private TypeGroupe typeGroupe;

    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL)
    private Set<Etudiant> etudiants = new HashSet<>();

    // Méthodes helper
    public void addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setGroupe(this);
    }

    public void removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setGroupe(null);
    }
}