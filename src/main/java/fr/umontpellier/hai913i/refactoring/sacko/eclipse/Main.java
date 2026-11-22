/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Programme principal de test de la version refactorée avec Eclipse.
 */
package fr.umontpellier.hai913i.refactoring.sacko.eclipse;

public class Main {

    public static void main(String[] args) {
    	System.out.println("=== TEST PROGRAMME SACKO - VERSION REFACTORÉE AVEC ECLIPSE ===");
        ServiceReservation service = new ServiceReservation();
        Salle salleA = new Salle("A101", 30);
        Salle salleB = new Salle("B202", 10);
        service.ajouterSalle(salleA);
        service.ajouterSalle(salleB);

        // 1. Cas normaux : réservations valides
        System.out.println("\n--- 1. Cas normaux ---");
        Reservation r1 = service.creerReservation("Groupe M2 ICO", "A101", 20);
        System.out.println("Créée : " + r1);
        verifierCondition("Réservation RES-1 créée", r1 != null && "RES-1".equals(r1.getReference()));
        verifierEgaliteInt("Places disponibles dans A101 après RES-1", 10, salleA.getPlacesDisponibles());

        Reservation r2 = service.creerReservation("Soutenance Stage", "B202", 8);
        System.out.println("Créée : " + r2);
        verifierCondition("Réservation RES-2 créée", r2 != null && "RES-2".equals(r2.getReference()));
        verifierEgaliteInt("Places disponibles dans B202 après RES-2", 2, salleB.getPlacesDisponibles());

     // 2. Salle introuvable : vérification des exceptions
        System.out.println("\n--- 2. Erreur salle introuvable ---");

        boolean exceptionSalle1 = false;
        try {
            service.verifierEtReserver("Club Robotique", "Z999", 5);
        } catch (IllegalArgumentException e) {
            exceptionSalle1 = true;
            System.out.println("Exception capturée : " + e.getMessage());
        }
        verifierCondition("Salle introuvable : exception levée", exceptionSalle1);

        boolean exceptionSalle2 = false;
        try {
            service.creerReservation("Club Robotique", "Z999", 5);
        } catch (IllegalArgumentException e) {
            exceptionSalle2 = true;
        }
        verifierCondition("Création impossible : exception levée", exceptionSalle2);

     // 3. Capacité insuffisante : vérification des exceptions
        System.out.println("\n--- 3. Erreur capacité insuffisante ---");

        boolean exceptionCapacite1 = false;
        try {
            service.verifierEtReserver("Conférence IA", "A101", 15);
        } catch (IllegalStateException e) {
            exceptionCapacite1 = true;
            System.out.println("Exception capturée : " + e.getMessage());
        }
        verifierCondition("Capacité insuffisante : exception levée", exceptionCapacite1);

        boolean exceptionCapacite2 = false;
        try {
            service.creerReservation("Conférence IA", "A101", 15);
        } catch (IllegalStateException e) {
            exceptionCapacite2 = true;
        }
        verifierCondition("Création impossible : capacité insuffisante", exceptionCapacite2);

        // 4. Cas d'erreur signalés par une exception (IllegalArgumentException)
        System.out.println("\n--- 4. Erreurs signalées par exception (paramètres invalides) ---");
        boolean exceptionClientVide = false;
        try {
            service.creerReservation("   ", "A101", 5);
        } catch (IllegalArgumentException e) {
            exceptionClientVide = true;
            System.out.println("Exception capturée (client vide) : " + e.getMessage());
        }
        verifierCondition("Exception levée pour nomClient vide", exceptionClientVide);

        boolean exceptionNbPersonnes = false;
        try {
            service.creerReservation("Association Étudiante", "A101", 0);
        } catch (IllegalArgumentException e) {
            exceptionNbPersonnes = true;
            System.out.println("Exception capturée (nbPersonnes <= 0) : " + e.getMessage());
        }
        verifierCondition("Exception levée pour nbPersonnes <= 0", exceptionNbPersonnes);

     // 5. Recherche et annulation
        System.out.println("\n--- 5. Recherche et annulation ---");

        // Recherche d'une réservation existante
        Reservation trouvee = service.chercherReservation("RES-1");
        verifierCondition("Recherche RES-1 trouvée",
                "Groupe M2 ICO".equals(trouvee.getNomClient()));

        // Recherche d'une réservation inexistante
        boolean exceptionRecherche = false;
        try {
            service.chercherReservation("RES-999");
        } catch (IllegalArgumentException e) {
            exceptionRecherche = true;
            System.out.println("Exception capturée (recherche) : " + e.getMessage());
        }
        verifierCondition("Recherche introuvable : exception levée", exceptionRecherche);

        // Annulation d'une réservation inexistante
        boolean exceptionAnnulation = false;
        try {
            service.annulerReservation("RES-999");
        } catch (IllegalArgumentException e) {
            exceptionAnnulation = true;
            System.out.println("Exception capturée (annulation) : " + e.getMessage());
        }
        verifierCondition("Annulation introuvable : exception levée", exceptionAnnulation);

        // Annulation d'une réservation existante
        service.annulerReservation("RES-1");
        System.out.println("Annulation RES-1 effectuée avec succès.");

        verifierEgaliteInt("Places disponibles dans A101 après annulation",
                30, salleA.getPlacesDisponibles());

        verifierEgaliteInt("Nombre de réservations actives finales",
                1, service.getNombreReservationsActives());

        System.out.println("\n[SUCCÈS] Tous les tests de la version Eclipse (Sacko) sont validés.");
    }

    private static void verifierEgaliteInt(String description, int attendu, int obtenu) {
        if (attendu != obtenu) {
            throw new IllegalStateException("ÉCHEC TEST [" + description + "] : attendu = " + attendu + ", obtenu = " + obtenu);
        }
        System.out.println("[OK] " + description + " -> " + obtenu);
    }

    private static void verifierCondition(String description, boolean condition) {
        if (!condition) {
            throw new IllegalStateException("ÉCHEC TEST [" + description + "]");
        }
        System.out.println("[OK] " + description);
    }
}

