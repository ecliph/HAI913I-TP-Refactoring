/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Classe CalculateurPrime après refactoring intégral par le LLM.
 *
 * REFACTORINGS APPLIQUÉS :
 * 1. Move Method + Pull Up Method + Replace Conditional with Polymorphism :
 *    Les chaînes de conditions if/else instanceof et les casts vers Developpeur, Commercial et Manager
 *    ont été supprimés. CalculateurPrime délègue désormais directement à employe.calculerPrime()
 *    et employe.genererLigneBilan() grâce au polymorphisme.
 * 2. Encapsulate Downcast :
 *    La méthode trouverPremierParNom() retourne désormais directement le type précis Employe
 *    au lieu de Object, évitant au client (Main) d'effectuer un cast explicite (Employe).
 */
package fr.umontpellier.hai913i.refactoring.mohamed.llm;

import java.util.List;

public class CalculateurPrime {

    public double calculerPrime(Employe employe) {
        return employe.calculerPrime();
    }

    public String genererLigneBilan(Employe employe) {
        return employe.genererLigneBilan();
    }

    public double calculerTotalPrimes(List<Employe> employes) {
        double total = 0.0;
        for (Employe emp : employes) {
            total += emp.calculerPrime();
        }
        return total;
    }

    // Encapsulate Downcast : le type de retour est spécialisé en Employe au lieu de Object
    public Employe trouverPremierParNom(List<Employe> employes, String nomRecherche) {
        for (Employe emp : employes) {
            if (emp.getNom().equalsIgnoreCase(nomRecherche)) {
                return emp;
            }
        }
        return null;
    }
}

