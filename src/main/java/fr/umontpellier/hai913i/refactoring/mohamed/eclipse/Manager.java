/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJNI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJNI MOHAMED — Refactoring réalisé par : SACKO DANGUE
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

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
