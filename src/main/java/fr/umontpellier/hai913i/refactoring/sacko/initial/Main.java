/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE ("Mixing return codes, nulls, and exceptions creates confusion and bugs.")
 * Nature du fichier : Programme principal de test (version initiale).
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 */
package fr.umontpellier.hai913i.refactoring.sacko.initial;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== TEST PROGRAMME INITIAL SACKO (Mélange codes d'erreur, null et exceptions) ===");

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

        // 2. Cas d'erreur signalé par un code de retour (-1 : salle introuvable) et par null
        System.out.println("\n--- 2. Erreur salle introuvable (Code -1 / retour null) ---");
        int codeSalleInconnue = service.verifierEtReserver("Club Robotique", "Z999", 5);
        System.out.println("Code retour verifierEtReserver(Z999) : " + codeSalleInconnue);
        verifierEgaliteInt("Code erreur salle introuvable (-1)", ServiceReservation.ERREUR_SALLE_INTROUVABLE, codeSalleInconnue);

        Reservation rSalleInconnue = service.creerReservation("Club Robotique", "Z999", 5);
        verifierCondition("creerReservation(Z999) retourne null", rSalleInconnue == null);

        // 3. Cas d'erreur signalé par un code de retour (-2 : capacité dépassée) et par null
        System.out.println("\n--- 3. Erreur capacité insuffisante (Code -2 / retour null) ---");
        int codeCapacite = service.verifierEtReserver("Conférence IA", "A101", 15); // Il ne reste que 10 places
        System.out.println("Code retour verifierEtReserver(A101, 15 places) : " + codeCapacite);
        verifierEgaliteInt("Code erreur capacité insuffisante (-2)", ServiceReservation.ERREUR_CAPACITE_INSUFFISANTE, codeCapacite);

        Reservation rCapacite = service.creerReservation("Conférence IA", "A101", 15);
        verifierCondition("creerReservation(A101, 15) retourne null", rCapacite == null);

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

        // 5. Recherche et annulation (mélange retour null et code d'erreur -3)
        System.out.println("\n--- 5. Recherche et annulation ---");
        Reservation trouvee = service.chercherReservation("RES-1");
        verifierCondition("Recherche RES-1 trouvée", trouvee != null && "Groupe M2 ICO".equals(trouvee.getNomClient()));

        Reservation introuvable = service.chercherReservation("RES-999");
        verifierCondition("Recherche RES-999 retourne null", introuvable == null);

        int codeAnnulationInconnue = service.annulerReservation("RES-999");
        System.out.println("Code retour annulerReservation(RES-999) : " + codeAnnulationInconnue);
        verifierEgaliteInt("Code erreur annulation introuvable (-3)", ServiceReservation.ERREUR_RESERVATION_INTROUVABLE, codeAnnulationInconnue);

        int codeAnnulationOk = service.annulerReservation("RES-1");
        System.out.println("Code retour annulerReservation(RES-1) : " + codeAnnulationOk);
        verifierEgaliteInt("Code succès annulation RES-1 (0)", ServiceReservation.SUCCES, codeAnnulationOk);
        verifierEgaliteInt("Places disponibles dans A101 après annulation", 30, salleA.getPlacesDisponibles());
        verifierEgaliteInt("Nombre de réservations actives finales", 1, service.getNombreReservationsActives());

        System.out.println("\n[SUCCÈS] Tous les tests du programme initial de Sacko sont validés.");
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

