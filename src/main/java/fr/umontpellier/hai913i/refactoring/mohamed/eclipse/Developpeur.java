/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED — Refactoring réalisé par : SACKO DANGUE
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

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
