/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Sous-classe représentant un développeur (version initiale).
 * Création : Programme initial préparé avec l'assistance d'un LLM.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.initial;

public class Developpeur extends Employe {
    private int nbProjetsTermines;
    private String langagePrincipal;

    public Developpeur(String nom, double salaireBase, int nbProjetsTermines, String langagePrincipal) {
        super(nom, salaireBase);
        this.nbProjetsTermines = nbProjetsTermines;
        this.langagePrincipal = langagePrincipal;
    }

    public int getNbProjetsTermines() {
        return nbProjetsTermines;
    }

    public String getLangagePrincipal() {
        return langagePrincipal;
    }
}
