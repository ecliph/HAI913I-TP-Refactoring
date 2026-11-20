/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE — Refactoring réalisé par : TEDJINI MOHAMED
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 */
package fr.umontpellier.hai913i.refactoring.sacko.eclipse;

public class Salle {
    private String code;
    private int capaciteMax;
    private int placesOccupees;

    public Salle(String code, int capaciteMax) {
        this.code = code;
        this.capaciteMax = capaciteMax;
        this.placesOccupees = 0;
    }

    public String getCode() {
        return code;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public int getPlacesOccupees() {
        return placesOccupees;
    }

    public int getPlacesDisponibles() {
        return capaciteMax - placesOccupees;
    }

    public void occuperPlaces(int nbPersonnes) {
        this.placesOccupees += nbPersonnes;
    }

    public void libererPlaces(int nbPersonnes) {
        this.placesOccupees -= nbPersonnes;
    }
}

