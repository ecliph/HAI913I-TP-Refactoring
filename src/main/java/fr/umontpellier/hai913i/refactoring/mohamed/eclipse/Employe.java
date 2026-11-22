/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Classe mère abstraite Employe après refactoring dans Eclipse.
 */
package fr.umontpellier.hai913i.refactoring.mohamed.eclipse;

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
	
	public String genererDescriptionBilan() {
	    return "[Employé] " + getNom();
	}
	
	
}

