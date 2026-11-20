/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED — Refactoring réalisé par : SACKO DANGUE
 * Nature du fichier : Version initiale préparée pour refactoring manuel dans Eclipse — refactoring non encore effectué.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

public class Commercial extends Employe {
    private double chiffreAffaires;
    private double tauxCommission;

    public Commercial(String nom, double salaireBase, double chiffreAffaires, double tauxCommission) {
        super(nom, salaireBase);
        this.chiffreAffaires = chiffreAffaires;
        this.tauxCommission = tauxCommission;
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }

    public double getTauxCommission() {
        return tauxCommission;
    }
}

