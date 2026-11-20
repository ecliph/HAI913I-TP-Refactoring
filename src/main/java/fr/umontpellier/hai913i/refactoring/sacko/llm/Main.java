/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : SACKO DANGUE — Refactoring réalisé par : TEDJNI MOHAMED (via LLM)
 * Nature du fichier : Programme principal de test (version refactorée par le LLM).
 *
 * ADAPTATION DU MAIN :
 * - Suite à l'opération "Replace Error Code with Exception", les méthodes verifierEtReserver()
 *   et annulerReservation() retournent désormais void au lieu d'un int, et creerReservation() /
 *   chercherReservation() lèvent une exception au lieu de retourner null.
 * - Les tests de Main ont été adaptés pour vérifier la levée des exceptions attendues
 *   (IllegalArgumentException et IllegalStateException) sur exactement les mêmes scénarios fonctionnels.
 */
package fr.umontpellier.hai913i.refactoring.sacko.llm;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== TEST PROGRAMME SACKO - VERSION REFACTORÉE PAR LLM (Exceptions cohérentes) ===");

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

        // 2. Cas d'erreur : salle introuvable (désormais signalé par IllegalArgumentException)
        System.out.println("\n--- 2. Erreur salle introuvable (Exception au lieu de Code -1 / null) ---");
        boolean exceptionSalleInconnue1 = false;
        try {
            service.verifierEtReserver("Club Robotique", "Z999", 5);
        } catch (IllegalArgumentException e) {
            exceptionSalleInconnue1 = true;
            System.out.println("Exception capturée verifierEtReserver(Z999) : " + e.getMessage());
        }
        verifierCondition("Exception IllegalArgumentException pour salle introuvable (verifierEtReserver)", exceptionSalleInconnue1);

        boolean exceptionSalleInconnue2 = false;
        try {
            service.creerReservation("Club Robotique", "Z999", 5);
        } catch (IllegalArgumentException e) {
            exceptionSalleInconnue2 = true;
        }
        verifierCondition("Exception IllegalArgumentException pour salle introuvable (creerReservation)", exceptionSalleInconnue2);

        // 3. Cas d'erreur : capacité dépassée (désormais signalé par IllegalStateException)
        System.out.println("\n--- 3. Erreur capacité insuffisante (Exception au lieu de Code -2 / null) ---");
        boolean exceptionCapacite1 = false;
        try {
            service.verifierEtReserver("Conférence IA", "A101", 15); // Il ne reste que 10 places
        } catch (IllegalStateException e) {
            exceptionCapacite1 = true;
            System.out.println("Exception capturée verifierEtReserver(A101, 15 places) : " + e.getMessage());
        }
        verifierCondition("Exception IllegalStateException pour capacité insuffisante (verifierEtReserver)", exceptionCapacite1);

        boolean exceptionCapacite2 = false;
        try {
            service.creerReservation("Conférence IA", "A101", 15);
        } catch (IllegalStateException e) {
            exceptionCapacite2 = true;
        }
        verifierCondition("Exception IllegalStateException pour capacité insuffisante (creerReservation)", exceptionCapacite2);

        // 4. Cas d'erreur : paramètres invalides (toujours signalés par IllegalArgumentException)
        System.out.println("\n--- 4. Erreurs de paramètres invalides (IllegalArgumentException) ---");
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

        // 5. Recherche et annulation (désormais unifiées par IllegalArgumentException en cas d'erreur)
        System.out.println("\n--- 5. Recherche et annulation ---");
        Reservation trouvee = service.chercherReservation("RES-1");
        verifierCondition("Recherche RES-1 trouvée", "Groupe M2 ICO".equals(trouvee.getNomClient()));

        boolean exceptionRechercheInconnue = false;
        try {
            service.chercherReservation("RES-999");
        } catch (IllegalArgumentException e) {
            exceptionRechercheInconnue = true;
            System.out.println("Exception capturée chercherReservation(RES-999) : " + e.getMessage());
        }
        verifierCondition("Recherche RES-999 lève IllegalArgumentException", exceptionRechercheInconnue);

        boolean exceptionAnnulationInconnue = false;
        try {
            service.annulerReservation("RES-999");
        } catch (IllegalArgumentException e) {
            exceptionAnnulationInconnue = true;
            System.out.println("Exception capturée annulerReservation(RES-999) : " + e.getMessage());
        }
        verifierCondition("Annulation RES-999 lève IllegalArgumentException (au lieu de code -3)", exceptionAnnulationInconnue);

        service.annulerReservation("RES-1");
        System.out.println("Annulation de RES-1 effectuée avec succès.");
        verifierEgaliteInt("Places disponibles dans A101 après annulation", 30, salleA.getPlacesDisponibles());
        verifierEgaliteInt("Nombre de réservations actives finales", 1, service.getNombreReservationsActives());

        System.out.println("\n[SUCCÈS] Tous les tests de la version LLM (Sacko) sont validés.");
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
