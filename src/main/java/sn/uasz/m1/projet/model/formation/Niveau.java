package sn.uasz.m1.projet.model.formation;

public enum Niveau {
    L1, L2, L3, M1, M2, D1, D2, D3;

    public String getLabel() {
        return this.name();
    }
}
