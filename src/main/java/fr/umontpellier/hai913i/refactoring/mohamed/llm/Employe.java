/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED — Refactoring réalisé par : SACKO DANGUE (via LLM)
 * Nature du fichier : Classe mère abstraite des employés après refactoring par le LLM.
 *
 * REFACTORINGS APPLIQUÉS :
 * - Replace Conditional with Polymorphism : ajout des méthodes polymorphes calculerPrime()
 *   et getDetailsCategorie() dans Employe, redéfinies par chaque sous-classe.
 * - Move Method / Pull Up Method : déplacement de la génération de la ligne de bilan commune
 *   pour factoriser l'affichage du salaire, de la prime et du total sans aucun test instanceof ni cast.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.llm;

public abstract class Employe {
    private String nom;
    private double salaireBase;

    public Employe(String nom, double salaireBase) {
        this.nom = nom;
        this.salaireBase = salaireBase;
    }

    public String getNom() {
        return nom;
    }

    public double getSalaireBase() {
        return salaireBase;
    }

    public abstract double calculerPrime();

    public abstract String getDetailsCategorie();

    public String genererLigneBilan() {
        double prime = calculerPrime();
        double total = salaireBase + prime;
        return getDetailsCategorie()
                + " -> Salaire : " + salaireBase + " €, Prime : " + prime + " €, Total : " + total + " €";
    }
}
