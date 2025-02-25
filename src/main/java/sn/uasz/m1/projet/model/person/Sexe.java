package sn.uasz.m1.projet.model.person;

public enum Sexe {
    HOMME,
    FEMME;

    public String getLabel() {
        return this.name();
    }
}
