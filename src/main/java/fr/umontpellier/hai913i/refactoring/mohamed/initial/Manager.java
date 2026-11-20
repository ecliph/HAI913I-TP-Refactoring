/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Sous-classe représentant un manager (version initiale).
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.initial;

public class Manager extends Employe {
    private int tailleEquipe;
    private boolean objectifsAtteints;

    public Manager(String nom, double salaireBase, int tailleEquipe, boolean objectifsAtteints) {
        super(nom, salaireBase);
        this.tailleEquipe = tailleEquipe;
        this.objectifsAtteints = objectifsAtteints;
    }

    public int getTailleEquipe() {
        return tailleEquipe;
    }

    public boolean isObjectifsAtteints() {
        return objectifsAtteints;
    }
}

