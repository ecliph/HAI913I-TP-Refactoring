# Documentation sur l'utilisation de l'IA Générative (LLM)

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Enseignante :** Marianne Huchard  
**Étudiants :**
- **TEDJINI MOHAMED**
- **SACKO DANGUE**

Conformément aux consignes des sections 2.1 et 2.2 du sujet de TP, ce document précise l'outil d'IA utilisé, son rôle dans la création des programmes initiaux et des versions refactorées `.llm`, les prompts employés, ainsi que notre analyse critique et les vérifications effectuées après réalisation des refactorings dans Eclipse.

---

## 1. Outil et modèle utilisés

- **Environnement :** Antigravity (environnement de développement basé sur VS Code sous Ubuntu Linux).
- **Modèle LLM utilisé :** Assistant IA Antigravity (modèle Gemini 4 Argon Medium).

---

## 2. Rôle de l'IA et répartition avec le travail manuel sous Eclipse

L'IA a été sollicitée pour trois tâches :

1. **Section 2.1 — Aide à la création des deux programmes initiaux contenant les défauts attribués :**
   - Programme 1 (`fr.umontpellier.hai913i.refactoring.mohamed.initial`) illustrant le défaut attribué à **TEDJINI MOHAMED** : *"Code frequently casts to concrete types, breaking polymorphism and adding fragility"* (calcul de primes d'employés avec `Employe`, `Developpeur`, `Commercial`, `Manager`, `CalculateurPrime`, `Main`).
   - Programme 2 (`fr.umontpellier.hai913i.refactoring.sacko.initial`) illustrant le défaut attribué à **SACKO DANGUE** : *"Mixing return codes, nulls, and exceptions creates confusion and bugs"* (système de réservation de salles avec `Salle`, `Reservation`, `ServiceReservation`, `Main`).

2. **Section 2.2 (point 2) — Refactoring intégral avec un LLM :**
   - Génération de la version refactorée `fr.umontpellier.hai913i.refactoring.mohamed.llm` à partir du programme initial de Mohamed.
   - Génération de la version refactorée `fr.umontpellier.hai913i.refactoring.sacko.llm` à partir du programme initial de Sacko.

3. **Vérification et mise en forme de la documentation finale :**
   - Après que **TEDJINI MOHAMED** a réalisé personnellement les opérations de refactoring sous **Eclipse** dans les packages `mohamed.eclipse` et `sacko.eclipse` et pris les 23 captures d'écran dans `preuves/eclipse/`, l'assistant IA a été utilisé pour exécuter la batterie de tests sur les 6 versions, comparer le code refactoré dans Eclipse avec le code refactoré par le LLM, et mettre à jour le rapport final.

---

## 3. Synthèse des prompts fournis au LLM

### A. Prompt de création des programmes initiaux
Nous avons fourni au LLM le contexte du TP, les documents de cours (`TP_refactoring_2026.pdf`, `2026_diaposRefactoring.pdf`, `AffectationSmells.xlsx`) et les deux défauts exacts :
- Sujet de TEDJINI MOHAMED : *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*
- Sujet de SACKO DANGUE : *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*
Nous avons demandé un code Java 17 simple, sans bibliothèque externe, avec une classe `Main` testant de façon déterministe les cas normaux et les cas d'erreur.

### B. Demande de refactoring adressée au LLM pour le programme 1 (`mohamed.llm`)
> *"À partir du programme initial `fr.umontpellier.hai913i.refactoring.mohamed.initial`, réalise intégralement la correction du code smell 'Code frequently casts to concrete types, breaking polymorphism and adding fragility' dans le package `fr.umontpellier.hai913i.refactoring.mohamed.llm`. Indique quels refactorings du catalogue de Fowler tu appliques, préserve le comportement observable et vérifie que `Main` fonctionne."*

**Opérations appliquées par le LLM :**
- **Replace Conditional with Polymorphism** : transformation de `Employe` en classe abstraite déclarant `calculerPrime()` et `getDetailsCategorie()`, redéfinies dans `Developpeur`, `Commercial` et `Manager`.
- **Move Method / Pull Up Method** : déplacement de `genererLigneBilan()` dans `Employe`, supprimant tous les `instanceof` et casts `(Developpeur)`, `(Commercial)`, `(Manager)` dans `CalculateurPrime`.
- **Encapsulate Downcast** : spécialisation du type de retour de `trouverPremierParNom` de `Object` vers `Employe`, supprimant le cast `(Employe)` dans `Main`.

### C. Demande de refactoring adressée au LLM pour le programme 2 (`sacko.llm`)
> *"À partir du programme initial `fr.umontpellier.hai913i.refactoring.sacko.initial`, réalise intégralement la correction du code smell 'Mixing return codes, nulls, and exceptions creates confusion and bugs' dans le package `fr.umontpellier.hai913i.refactoring.sacko.llm` en t'appuyant sur 'Replace Error Code with Exception'. N'ajoute pas une hiérarchie complexe d'exceptions personnalisées si les exceptions standards de Java suffisent. Indique les refactorings appliqués et adapte `Main` aux nouvelles signatures."*

**Opérations appliquées par le LLM :**
- **Replace Error Code with Exception** : suppression des constantes entières `0`, `-1`, `-2`, `-3` et passage des méthodes `verifierEtReserver` et `annulerReservation` d'un type de retour `int` à `void`.
- **Élimination des retours `null` d'erreur** dans `trouverSalle`, `creerReservation` et `chercherReservation`.
- **Harmonisation avec les exceptions standards Java** : `IllegalArgumentException` (paramètre invalide ou salle/réservation introuvable) et `IllegalStateException` (capacité de salle insuffisante).

---

## 4. Analyse critique : comparaison entre les réponses du LLM et nos manipulations sous Eclipse

1. **Création des programmes initiaux (Section 2.1) :**
   - Les deux programmes initiaux générés avec l'aide du LLM étaient directement fonctionnels et mettaient bien en évidence les défauts attendus. Nous n'avons pas eu besoin de modifier leur logique métier.
2. **Comparaison avec le refactoring manuel sous Eclipse (Section 2.2) :**
   - Sur le **Programme 1 (Mohamed)**, la version réalisée dans Eclipse (`mohamed.eclipse`) utilise `genererDescriptionBilan()` dans `Employe` (avec une implémentation par défaut `"[Employé] " + getNom()`) et conserve l'assemblage du salaire et du total dans `CalculateurPrime.genererLigneBilan(Employe)` ainsi qu'une vérification `if (employe == null) return 0.0;`. Le LLM (`mohamed.llm`) a quant à lui déplacé tout `genererLigneBilan()` dans `Employe` sans garde contre `null`. La confrontation des deux versions nous a permis de constater que la version Eclipse répartit mieux les responsabilités entre `Employe` et `CalculateurPrime`.
   - Sur le **Programme 2 (Sacko)**, la version réalisée dans Eclipse (`sacko.eclipse`) et la version du LLM (`sacko.llm`) aboutissent aux mêmes signatures publiques et lèvent exactement les mêmes types d'exceptions (`IllegalArgumentException` et `IllegalStateException`) sur les mêmes cas de test.

---

## 5. Vérifications effectuées

Les 6 versions (`mohamed.initial`, `mohamed.eclipse`, `mohamed.llm`, `sacko.initial`, `sacko.eclipse`, `sacko.llm`) ont été compilées avec Maven (`mvn clean test-compile` avec `--release 17`) et exécutées via `./scripts/verifier_tout.sh`.  
Les traces complètes d'exécution figurent dans [`preuves/resultats_execution.txt`](../preuves/resultats_execution.txt).
