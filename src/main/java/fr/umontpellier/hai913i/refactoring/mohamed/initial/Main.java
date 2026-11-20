/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Programme principal de test (version initiale).
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.initial;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== TEST PROGRAMME INITIAL MOHAMED (Conversions explicites / Casts) ===");

        CalculateurPrime calculateur = new CalculateurPrime();
        List<Employe> employes = new ArrayList<>();

        Developpeur dev1 = new Developpeur("Alice Martin", 2500.0, 4, "Java");
        Developpeur dev2 = new Developpeur("Benoit Blanc", 2300.0, 0, "Python");
        Commercial com1 = new Commercial("Chloé Bernard", 2200.0, 50000.0, 0.05);
        Manager man1 = new Manager("David Petit", 3200.0, 6, true);
        Manager man2 = new Manager("Emma Leroy", 3000.0, 3, false);

        employes.add(dev1);
        employes.add(dev2);
        employes.add(com1);
        employes.add(man1);
        employes.add(man2);

        System.out.println("\n--- Bilans individuels ---");
        for (Employe emp : employes) {
            System.out.println(calculateur.genererLigneBilan(emp));
        }

        // Vérifications déterministes des primes individuelles
        verifierEgaliteDouble("Prime Alice (Développeur, 4 projets)", 1200.0, calculateur.calculerPrime(dev1));
        verifierEgaliteDouble("Prime Benoit (Développeur, 0 projet)", 0.0, calculateur.calculerPrime(dev2));
        verifierEgaliteDouble("Prime Chloé (Commercial, 50000 * 5%)", 2500.0, calculateur.calculerPrime(com1));
        verifierEgaliteDouble("Prime David (Manager, équipe 6, obj atteints)", 1400.0, calculateur.calculerPrime(man1));
        verifierEgaliteDouble("Prime Emma (Manager, équipe 3, obj non atteints)", 450.0, calculateur.calculerPrime(man2));

        double totalPrimes = calculateur.calculerTotalPrimes(employes);
        System.out.println("\nTotal des primes versées : " + totalPrimes + " €");
        verifierEgaliteDouble("Total des primes", 5550.0, totalPrimes);

        // Test de la recherche avec downcast côté appelant (défaut illustrant Encapsulate Downcast)
        System.out.println("\n--- Recherche d'employé ---");
        Employe trouve = (Employe) calculateur.trouverPremierParNom(employes, "Chloé Bernard");
        if (trouve != null) {
            System.out.println("Employé trouvé : " + trouve.getNom() + " (Salaire de base : " + trouve.getSalaireBase() + " €)");
        }
        verifierCondition("Recherche Chloé Bernard trouvée", trouve != null && "Chloé Bernard".equals(trouve.getNom()));

        Employe absent = (Employe) calculateur.trouverPremierParNom(employes, "Inconnu");
        verifierCondition("Recherche employé inexistant renvoie null", absent == null);

        System.out.println("\n[SUCCÈS] Tous les tests du programme initial de Mohamed sont validés.");
    }

    private static void verifierEgaliteDouble(String description, double attendu, double obtenu) {
        if (Math.abs(attendu - obtenu) > 0.001) {
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
