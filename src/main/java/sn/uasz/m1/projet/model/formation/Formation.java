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
import sn.uasz.m1.projet.model.person.ResponsablePedagogique;

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
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Set<UE> getUes() {
        return ues;
    }

    public void setUes(Set<UE> ues) {
        this.ues = ues;
    }

    public ResponsablePedagogique getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsablePedagogique responsable) {
        this.responsable = responsable;
    }
    
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
