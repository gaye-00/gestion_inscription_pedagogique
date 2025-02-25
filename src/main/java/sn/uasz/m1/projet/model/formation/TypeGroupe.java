package sn.uasz.m1.projet.model.formation;

public enum TypeGroupe {
    TD, TP;

    public String getLabel() {
        return this.name();
    }
}
