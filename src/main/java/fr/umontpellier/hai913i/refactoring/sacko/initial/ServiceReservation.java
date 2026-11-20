/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Système de réservation illustrant le défaut de gestion incohérente des erreurs.
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 *
 * EXPLICATION DU DÉFAUT :
 * Ce code présente le défaut "Mixing return codes, nulls, and exceptions creates confusion and bugs".
 * Les méthodes du service mélangent trois mécanismes différents pour signaler les erreurs :
 * 1. Des codes de retour entiers négatifs (-1, -2, -3) dans verifierEtReserver() et annulerReservation(),
 *    alors que 0 signifie un succès.
 * 2. Des retours de valeur null dans creerReservation() et chercherReservation() lorsqu'une salle
 *    est introuvable, que la capacité est dépassée ou que la référence n'existe pas.
 * 3. Des exceptions (IllegalArgumentException) lorsque le nom du client est vide ou que le nombre
 *    de personnes est inférieur ou égal à 0.
 *
 * Ce mélange oblige l'appelant (Main) à combiner des blocs try/catch, des tests de codes entiers (< 0)
 * et des vérifications de nullité (!= null), ce qui alourdit le code et favorise les oublis ou NullPointerException.
 *
 * REFACTORING VISÉ (à réaliser par TEDJINI MOHAMED) :
 * - Replace Error Code with Exception (diapositive 20 du cours) : remplacer les codes d'erreur (-1, -2, -3)
 *   et les retours null d'erreur par le lancement cohérent d'exceptions standards Java explicites
 *   (IllegalArgumentException pour les paramètres ou références invalides, IllegalStateException pour
 *   un dépassement de capacité).
 */
package fr.umontpellier.hai913i.refactoring.sacko.initial;

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

    // DÉFAUT : Mélange d'exceptions (IllegalArgumentException) et de codes de retour (-1, -2, 0)
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

    // DÉFAUT : Mélange d'exceptions, d'appels à un code de retour et de retour null en cas d'échec
    public Reservation creerReservation(String nomClient, String codeSalle, int nbPersonnes) {
        int codeResultat = verifierEtReserver(nomClient, codeSalle, nbPersonnes);
        if (codeResultat != SUCCES) {
            return null; // Retourne null si le code d'erreur est -1 ou -2
        }
        return reservations.get(reservations.size() - 1);
    }

    // DÉFAUT : Retourne null si la référence est vide ou si la réservation n'existe pas
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

    // DÉFAUT : Utilise un code de retour (-3 ou 0) au lieu d'une exception si la réservation est introuvable
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

