/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE — Refactoring réalisé par : TEDJINI MOHAMED
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 */
package fr.umontpellier.hai913i.refactoring.sacko.eclipse;

public class Reservation {
    private String reference;
    private String nomClient;
    private String codeSalle;
    private int nbPersonnes;

    public Reservation(String reference, String nomClient, String codeSalle, int nbPersonnes) {
        this.reference = reference;
        this.nomClient = nomClient;
        this.codeSalle = codeSalle;
        this.nbPersonnes = nbPersonnes;
    }

    public String getReference() {
        return reference;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    @Override
    public String toString() {
        return "Réservation [" + reference + "] - Client : " + nomClient
                + ", Salle : " + codeSalle + ", Personnes : " + nbPersonnes;
    }
}

