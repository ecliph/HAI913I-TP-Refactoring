/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Classe de calcul des primes et bilans illustrant le défaut de conversions explicites vers des types concrets.
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 *
 * EXPLICATION DU DÉFAUT :
 * Ce code présente le défaut "Code frequently casts to concrete types, breaking polymorphism and adding fragility".
 * Les méthodes calculerPrime() et genererLigneBilan() multiplient les tests de type (instanceof) suivis de
 * conversions explicites (casts) vers les sous-classes concrètes Developpeur, Commercial et Manager.
 * De plus, trouverPremierParNom() renvoie Object au lieu de Employe, obligeant le code appelant à faire un downcast.
 * Cela brise le polymorphisme et rend le code fragile : l'ajout d'une nouvelle catégorie d'employé oblige
 * à modifier toutes les chaînes de conditions.
 *
 * REFACTORING VISÉ (à réaliser par SACKO DANGUE) :
 * - Replace Conditional with Polymorphism (et Move Method / Pull Up Method) : déplacer le calcul de la prime
 *   et la description spécifique directement dans la hiérarchie Employe / sous-classes.
 * - Encapsulate Downcast : faire en sorte que trouverPremierParNom() effectue le cast en interne et retourne
 *   directement un type Employe (voire typer la liste en List<Employe>).
 */
package fr.umontpellier.hai913i.refactoring.mohamed.initial;

import java.util.List;

public class CalculateurPrime {

    // DÉFAUT : Tests instanceof répétés et conversions explicites (casts) vers les types concrets
    public double calculerPrime(Employe employe) {
        if (employe instanceof Developpeur) {
            Developpeur dev = (Developpeur) employe;
            return dev.getNbProjetsTermines() * 300.0;
        } else if (employe instanceof Commercial) {
            Commercial com = (Commercial) employe;
            return com.getChiffreAffaires() * com.getTauxCommission();
        } else if (employe instanceof Manager) {
            Manager man = (Manager) employe;
            double primeEquipe = man.getTailleEquipe() * 150.0;
            if (man.isObjectifsAtteints()) {
                primeEquipe += 500.0;
            }
            return primeEquipe;
        }
        return 0.0;
    }

    // DÉFAUT : Même structure de tests instanceof et casts répétés une seconde fois
    public String genererLigneBilan(Employe employe) {
        double prime = calculerPrime(employe);
        double total = employe.getSalaireBase() + prime;

        if (employe instanceof Developpeur) {
            Developpeur dev = (Developpeur) employe;
            return "[Développeur] " + dev.getNom()
                    + " (Langage : " + dev.getLangagePrincipal() + ", Projets : " + dev.getNbProjetsTermines() + ")"
                    + " -> Salaire : " + dev.getSalaireBase() + " €, Prime : " + prime + " €, Total : " + total + " €";
        } else if (employe instanceof Commercial) {
            Commercial com = (Commercial) employe;
            return "[Commercial] " + com.getNom()
                    + " (CA : " + com.getChiffreAffaires() + " €, Taux : " + (com.getTauxCommission() * 100) + "%)"
                    + " -> Salaire : " + com.getSalaireBase() + " €, Prime : " + prime + " €, Total : " + total + " €";
        } else if (employe instanceof Manager) {
            Manager man = (Manager) employe;
            String statutObj = man.isObjectifsAtteints() ? "Oui" : "Non";
            return "[Manager] " + man.getNom()
                    + " (Équipe : " + man.getTailleEquipe() + ", Objectifs atteints : " + statutObj + ")"
                    + " -> Salaire : " + man.getSalaireBase() + " €, Prime : " + prime + " €, Total : " + total + " €";
        }
        return "[Employé] " + employe.getNom()
                + " -> Salaire : " + employe.getSalaireBase() + " €, Prime : " + prime + " €, Total : " + total + " €";
    }

    public double calculerTotalPrimes(List<Employe> employes) {
        double total = 0.0;
        for (Employe emp : employes) {
            total += calculerPrime(emp);
        }
        return total;
    }

    // DÉFAUT : Retourne Object au lieu de Employe, obligeant l'appelant à réaliser un downcast explicite
    public Object trouverPremierParNom(List<Employe> employes, String nomRecherche) {
        for (Employe emp : employes) {
            if (emp.getNom().equalsIgnoreCase(nomRecherche)) {
                return emp;
            }
        }
        return null;
    }
}
