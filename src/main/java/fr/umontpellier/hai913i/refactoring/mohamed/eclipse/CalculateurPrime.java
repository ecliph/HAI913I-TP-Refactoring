/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED — Refactoring réalisé par : SACKO DANGUE
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 *
 * NOTE IMPORTANTE :
 * Ce package contient une copie fonctionnelle du code initial de TEDJNI MOHAMED destinée à être
 * refactorée manuellement dans Eclipse par SACKO DANGUE (voir le fichier rapport/GUIDE_ECLIPSE.md).
 */
package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

import java.util.List;

public class CalculateurPrime {

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Tests instanceof répétés et conversions explicites (casts)
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

    // DÉFAUT À CORRIGER SOUS ECLIPSE : Tests instanceof et casts répétés
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

    // DÉFAUT À CORRIGER SOUS ECLIPSE (Encapsulate Downcast) : Retourne Object au lieu de Employe
    public Object trouverPremierParNom(List<Employe> employes, String nomRecherche) {
        for (Employe emp : employes) {
            if (emp.getNom().equalsIgnoreCase(nomRecherche)) {
                return emp;
            }
        }
        return null;
    }
}
