/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Classe ServiceReservation après refactoring intégral par le LLM.
 *
 * REFACTORINGS APPLIQUÉS :
 * 1. Replace Error Code with Exception (diapositive 20 du cours) :
 *    - Les constantes de codes d'erreur (SUCCES=0, -1, -2, -3) ont été supprimées.
 *    - Les méthodes verifierEtReserver() et annulerReservation() ont désormais un type de retour void
 *      (Change Method Signature) et lèvent des exceptions standards explicites en cas d'erreur :
 *        * IllegalArgumentException : lorsque la salle ou la réservation demandée est introuvable,
 *          ou lorsque les paramètres (nomClient, reference, nbPersonnes) sont invalides.
 *        * IllegalStateException : lorsque la capacité disponible de la salle est insuffisante.
 * 2. Élimination des retours null ambigus :
 *    - creerReservation() et chercherReservation() ne retournent plus jamais null pour masquer une erreur ;
 *      elles garantissent un objet Reservation non-null en sortie ou lèvent une exception explicite.
 */
package fr.umontpellier.hai913i.refactoring.sacko.llm;

import java.util.ArrayList;
import java.util.List;

public class ServiceReservation {

    private List<Salle> salles = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private int compteurReference = 1;

    public void ajouterSalle(Salle salle) {
        if (salle == null) {
            throw new IllegalArgumentException("La salle à ajouter ne peut pas être null.");
        }
        salles.add(salle);
    }

    public Salle trouverSalle(String codeSalle) {
        if (codeSalle == null || codeSalle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le code de la salle ne peut pas être vide.");
        }
        for (Salle s : salles) {
            if (s.getCode().equalsIgnoreCase(codeSalle)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Salle introuvable : " + codeSalle);
    }

    // Replace Error Code with Exception : retourne void au lieu d'un int (-1, -2, 0)
    public void verifierEtReserver(String nomClient, String codeSalle, int nbPersonnes) {
        creerReservation(nomClient, codeSalle, nbPersonnes);
    }

    // Retourne toujours une Reservation valide non-null ou lève une exception explicite
    public Reservation creerReservation(String nomClient, String codeSalle, int nbPersonnes) {
        if (nomClient == null || nomClient.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide.");
        }
        if (nbPersonnes <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être strictement positif.");
        }

        Salle salle = trouverSalle(codeSalle);
        if (salle.getPlacesDisponibles() < nbPersonnes) {
            throw new IllegalStateException("Capacité insuffisante dans la salle " + salle.getCode()
                    + " (demandé : " + nbPersonnes + ", disponible : " + salle.getPlacesDisponibles() + ").");
        }

        salle.occuperPlaces(nbPersonnes);
        String ref = "RES-" + compteurReference++;
        Reservation nouvelle = new Reservation(ref, nomClient, salle.getCode(), nbPersonnes);
        reservations.add(nouvelle);
        return nouvelle;
    }

    // Ne retourne plus null : lève IllegalArgumentException si la référence est invalide ou introuvable
    public Reservation chercherReservation(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new IllegalArgumentException("La référence de réservation ne peut pas être vide.");
        }
        for (Reservation r : reservations) {
            if (r.getReference().equalsIgnoreCase(reference)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Réservation introuvable : " + reference);
    }

    // Replace Error Code with Exception : retourne void au lieu d'un code entier (-3 ou 0)
    public void annulerReservation(String reference) {
        Reservation res = chercherReservation(reference);
        Salle salle = trouverSalle(res.getCodeSalle());
        salle.libererPlaces(res.getNbPersonnes());
        reservations.remove(res);
    }

    public int getNombreReservationsActives() {
        return reservations.size();
    }
}

