package sn.uasz.m1.projet.model.person;

public enum Sexe {
    MASCULIN, FEMININ;

    public String getLabel() {
        return this.name();
    }
}
