/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED ("Code frequently casts to concrete types, breaking polymorphism and adding fragility.")
 * Nature du fichier : Sous-classe Developpeur après refactoring par le LLM (polymorphisme).
 */
package fr.umontpellier.hai913i.refactoring.mohamed.llm;

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

    @Override
    public double calculerPrime() {
        return nbProjetsTermines * 300.0;
    }

    @Override
    public String getDetailsCategorie() {
        return "[Développeur] " + getNom()
                + " (Langage : " + langagePrincipal + ", Projets : " + nbProjetsTermines + ")";
    }
}

