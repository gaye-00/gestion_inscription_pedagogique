package sn.uasz.m1.projet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Etat {
    Long formmations;
    Long enseignants;
    Long inscriptions;
    Long etudiants;

}
