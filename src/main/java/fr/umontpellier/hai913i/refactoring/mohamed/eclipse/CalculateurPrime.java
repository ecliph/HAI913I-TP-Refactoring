/*
 * Module : HAI913I - Évolution et Restructuration Logicielle
 * Master 2 ICO - Université de Montpellier
 *
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED
 *
 * Version refactorée avec Eclipse.
 */

package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

import java.util.List;

public class CalculateurPrime {

    // Calcul de la prime avec le polymorphisme
    public double calculerPrime(Employe employe) {
        if (employe == null) {
            return 0.0;
        }

        return employe.calculerPrime();
    }

    // Génération du bilan individuel avec le polymorphisme
    public String genererLigneBilan(Employe employe) {
        double prime = calculerPrime(employe);
        double total = employe.getSalaireBase() + prime;

        return employe.genererDescriptionBilan()
                + " -> Salaire : " + employe.getSalaireBase()
                + " €, Prime : " + prime
                + " €, Total : " + total + " €";
    }

    // Calcul du total des primes
    public double calculerTotalPrimes(List<Employe> employes) {
        double total = 0.0;

        for (Employe emp : employes) {
            total += calculerPrime(emp);
        }

        return total;
    }

    // Recherche d'un employé par son nom
    public Employe trouverPremierParNom(List<Employe> employes, String nomRecherche) {
        for (Employe emp : employes) {
            if (emp.getNom().equalsIgnoreCase(nomRecherche)) {
                return emp;
            }
        }

        return null;
    }
}