package sn.uasz.m1.projet.model.formation;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String code;
    private Integer nombreOptionsRequis;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private Set<UE> ues = new HashSet<>();

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private Set<Etudiant> etudiants = new HashSet<>();

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
