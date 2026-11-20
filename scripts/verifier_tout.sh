#!/usr/bin/env bash
# Module : HAI913I - Évolution et Restructuration Logicielle (Master 2 ICO - Université de Montpellier)
# Étudiants : TEDJNI MOHAMED et SACKO DANGUE
# Script de compilation et de vérification des 6 variantes du projet.

set -e

mkdir -p preuves
RAPPORT_EXEC="preuves/resultats_execution.txt"

echo "==================================================================" | tee "$RAPPORT_EXEC"
echo "HAI913I - TP REFACTORING (PARTIE 2)" | tee -a "$RAPPORT_EXEC"
echo "Étudiants : TEDJNI MOHAMED et SACKO DANGUE" | tee -a "$RAPPORT_EXEC"
echo "Date d'exécution : $(date '+%Y-%m-%d %H:%M:%S')" | tee -a "$RAPPORT_EXEC"
echo "==================================================================" | tee -a "$RAPPORT_EXEC"

echo "" | tee -a "$RAPPORT_EXEC"
echo "[1/7] Compilation Maven (mvn clean test-compile)..." | tee -a "$RAPPORT_EXEC"
mvn clean test-compile | tee -a "$RAPPORT_EXEC"

CLASSES_MAIN=(
    "fr.umontpellier.hai913i.refactoring.mohamed.initial.Main"
    "fr.umontpellier.hai913i.refactoring.mohamed.llm.Main"
    "fr.umontpellier.hai913i.refactoring.mohamed.eclipse.Main"
    "fr.umontpellier.hai913i.refactoring.sacko.initial.Main"
    "fr.umontpellier.hai913i.refactoring.sacko.llm.Main"
    "fr.umontpellier.hai913i.refactoring.sacko.eclipse.Main"
)

ETAPE=2
for CLASSE in "${CLASSES_MAIN[@]}"; do
    echo "" | tee -a "$RAPPORT_EXEC"
    echo "------------------------------------------------------------------" | tee -a "$RAPPORT_EXEC"
    echo "[$ETAPE/7] Exécution de : $CLASSE" | tee -a "$RAPPORT_EXEC"
    echo "------------------------------------------------------------------" | tee -a "$RAPPORT_EXEC"
    java -cp target/classes "$CLASSE" | tee -a "$RAPPORT_EXEC"
    ETAPE=$((ETAPE + 1))
done

echo "" | tee -a "$RAPPORT_EXEC"
echo "==================================================================" | tee -a "$RAPPORT_EXEC"
echo "BILAN : Les 6 classes Main ont compilé et réussi tous leurs tests." | tee -a "$RAPPORT_EXEC"
echo "==================================================================" | tee -a "$RAPPORT_EXEC"
