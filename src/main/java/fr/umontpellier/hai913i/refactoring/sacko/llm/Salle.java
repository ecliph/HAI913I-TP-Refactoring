/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Classe Salle (version refactorée par le LLM).
 */
package fr.umontpellier.hai913i.refactoring.sacko.llm;

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

