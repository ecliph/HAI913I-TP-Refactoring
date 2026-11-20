/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE — Refactoring réalisé par : TEDJNI MOHAMED
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 *
 * NOTE IMPORTANTE :
 * Ce package contient une copie fonctionnelle du code initial de SACKO DANGUE destinée à être
 * refactorée manuellement dans Eclipse par TEDJNI MOHAMED (voir le fichier rapport/GUIDE_ECLIPSE.md).
 */
package fr.umontpellier.hai913i.refactoring.sacko.eclipse;

import java.util.ArrayList;
import java.util.List;

public class ServiceReservation {
    public static final int SUCCES = 0;
    public static final int ERREUR_SALLE_INTROUVABLE = -1;
    public static final int ERREUR_CAPACITE_INSUFFISANTE = -2;
    public static final int ERREUR_RESERVATION_INTROUVABLE = -3;

    private List<Salle> salles = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private int compteurReference = 1;

    public void ajouterSalle(Salle salle) {
        salles.add(salle);
    }

    public Salle trouverSalle(String codeSalle) {
        for (Salle s : salles) {
            if (s.getCode().equalsIgnoreCase(codeSalle)) {
                return s;
            }
        }
        return null;
    }

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Mélange d'exceptions et de codes de retour (-1, -2, 0)
    public int verifierEtReserver(String nomClient, String codeSalle, int nbPersonnes) {
        if (nomClient == null || nomClient.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide.");
        }
        if (nbPersonnes <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être strictement positif.");
        }

        Salle salle = trouverSalle(codeSalle);
        if (salle == null) {
            return ERREUR_SALLE_INTROUVABLE; // Code d'erreur -1
        }
        if (salle.getPlacesDisponibles() < nbPersonnes) {
            return ERREUR_CAPACITE_INSUFFISANTE; // Code d'erreur -2
        }

        salle.occuperPlaces(nbPersonnes);
        String ref = "RES-" + compteurReference++;
        reservations.add(new Reservation(ref, nomClient, salle.getCode(), nbPersonnes));
        return SUCCES; // Code 0
    }

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Mélange d'exceptions, de code de retour et de retour null
    public Reservation creerReservation(String nomClient, String codeSalle, int nbPersonnes) {
        int codeResultat = verifierEtReserver(nomClient, codeSalle, nbPersonnes);
        if (codeResultat != SUCCES) {
            return null;
        }
        return reservations.get(reservations.size() - 1);
    }

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Retourne null si la référence est vide ou introuvable
    public Reservation chercherReservation(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            return null;
        }
        for (Reservation r : reservations) {
            if (r.getReference().equalsIgnoreCase(reference)) {
                return r;
            }
        }
        return null;
    }

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Code de retour (-3 ou 0) au lieu d'une exception
    public int annulerReservation(String reference) {
        Reservation res = chercherReservation(reference);
        if (res == null) {
            return ERREUR_RESERVATION_INTROUVABLE; // Code d'erreur -3
        }
        Salle salle = trouverSalle(res.getCodeSalle());
        if (salle != null) {
            salle.libererPlaces(res.getNbPersonnes());
        }
        reservations.remove(res);
        return SUCCES;
    }

    public int getNombreReservationsActives() {
        return reservations.size();
    }
}
