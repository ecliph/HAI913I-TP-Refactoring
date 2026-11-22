/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Sous-classe Commercial après refactoring dans Eclipse.
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

    @Override
    public double calculerPrime() {
        return getChiffreAffaires() * getTauxCommission();
    }
    
    @Override
    public String genererDescriptionBilan() {
        return "[Commercial] " + getNom()
                + " (CA : " + getChiffreAffaires()
                + " €, Taux : " + (getTauxCommission() * 100) + "%)";
    }
}

