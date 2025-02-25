package sn.uasz.m1.projet.model.formation;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private Set<UE> ues = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsablePedagogique responsable;

    // Méthodes helper
    public void addUE(UE ue) {
        this.ues.add(ue);
        ue.setFormation(this);
    }

    public void removeUE(UE ue) {
        this.ues.remove(ue);
        ue.setFormation(null);
    }
}
