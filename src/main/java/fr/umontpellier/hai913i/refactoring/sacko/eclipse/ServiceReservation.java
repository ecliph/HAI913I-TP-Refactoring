/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Classe ServiceReservation après refactoring dans Eclipse (Replace Error Code with Exception).
 */
package fr.umontpellier.hai913i.refactoring.sacko.eclipse;

import java.util.ArrayList;
import java.util.List;

public class ServiceReservation {


    private List<Salle> salles = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private int compteurReference = 1;

    public void ajouterSalle(Salle salle) {
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

    // Refactoring Eclipse (Change Method Signature : int -> void) + remplacement des codes d'erreur par des exceptions
    public void verifierEtReserver(String nomClient, String codeSalle, int nbPersonnes) {
        if (nomClient == null || nomClient.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide.");
        }
        if (nbPersonnes <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être strictement positif.");
        }

        Salle salle = trouverSalle(codeSalle);
        
        if (salle.getPlacesDisponibles() < nbPersonnes) {
        	throw new IllegalStateException("Capacité insuffisante dans la salle " + codeSalle);        }

        salle.occuperPlaces(nbPersonnes);
        String ref = "RES-" + compteurReference++;
        reservations.add(new Reservation(ref, nomClient, salle.getCode(), nbPersonnes));
        }

    // Délègue la vérification et l'enregistrement à verifierEtReserver() et retourne la réservation créée (sans retour null)
    public Reservation creerReservation(String nomClient, String codeSalle, int nbPersonnes) {
    	verifierEtReserver(nomClient, codeSalle, nbPersonnes);
        return reservations.get(reservations.size() - 1);
    }

    // Lève une IllegalArgumentException au lieu de retourner null lorsque la référence est vide ou introuvable
    public Reservation chercherReservation(String reference) {
    	if (reference == null || reference.trim().isEmpty()) {
    	    throw new IllegalArgumentException("La référence de réservation ne peut pas être vide.");
    	}
        for (Reservation r : reservations) {
            if (r.getReference().equalsIgnoreCase(reference)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Réservation introuvable : " + reference);    }

    // Refactoring Eclipse (Change Method Signature : int -> void) : lève une exception si la réservation est introuvable
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

