/*
 * Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
 * Étudiants : TEDJINI MOHAMED et SACKO DANGUE
 * Sujet attribué à : TEDJINI MOHAMED — Refactoring réalisé par : SACKO DANGUE (via LLM)
 * Nature du fichier : Sous-classe Manager après refactoring par le LLM (polymorphisme).
 */
package fr.umontpellier.hai913i.refactoring.mohamed.llm;

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

    @Override
    public double calculerPrime() {
        double primeEquipe = tailleEquipe * 150.0;
        if (objectifsAtteints) {
            primeEquipe += 500.0;
        }
        return primeEquipe;
    }

    @Override
    public String getDetailsCategorie() {
        String statutObj = objectifsAtteints ? "Oui" : "Non";
        return "[Manager] " + getNom()
                + " (Équipe : " + tailleEquipe + ", Objectifs atteints : " + statutObj + ")";
    }
}

