package sn.uasz.m1.projet.model.person;

public enum Sexe {
    MASCULIN, FEMININ;

    public String getLabel() {
        return this.name();
    }

    public String getPremiereLettre() {
        return this.name().substring(0, 1);
    }

}
