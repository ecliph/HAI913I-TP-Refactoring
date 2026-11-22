# Guide et Trace des Manipulations Réalisées dans Eclipse

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Étudiants :** TEDJINI MOHAMED et SACKO DANGUE

Ce document décrit pas à pas les manipulations qui ont été réalisées dans **Eclipse** sur les deux packages dédiés :
- `fr.umontpellier.hai913i.refactoring.mohamed.eclipse`
- `fr.umontpellier.hai913i.refactoring.sacko.eclipse`

Toutes les étapes ci-dessous ont été exécutées dans Eclipse et sont illustrées par les captures d'écran enregistrées dans `preuves/eclipse/mohamed/` et `preuves/eclipse/sacko/`.

---

## Étape préliminaire : Importation du projet Maven sous Eclipse

1. Lancer **Eclipse**.
2. Cliquer sur **File → Import... → Maven → Existing Maven Projects → Next >**.
3. Sélectionner le dossier racine du projet `HAI913I-TP-Refactoring`, vérifier que `pom.xml` est coché, puis cliquer sur **Finish**.
4. Dans le **Package Explorer**, dérouler `src/main/java` pour accéder aux 6 packages.

---

## Partie 1 — Refactoring du programme 1 dans Eclipse (`mohamed.eclipse`)

**Package :** `fr.umontpellier.hai913i.refactoring.mohamed.eclipse`  
**Défaut corrigé :** *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*  
**Classes modifiées :** `CalculateurPrime.java`, `Employe.java`, `Developpeur.java`, `Commercial.java`, `Manager.java`, `Main.java`.

### A. Exécution initiale avant refactoring
1. Exécution de `mohamed.eclipse.Main` (**Run As → Java Application**) pour vérifier que tous les tests passent sur le code initial (**Capture** `00_mohamed_tests_avant.png`).
2. Inspection des chaînes `if (employe instanceof ...)` et des casts dans `CalculateurPrime.java` (**Capture** `01_mohamed_code_initial.png`).

### B. Opération 1 : *Encapsulate Downcast* sur `trouverPremierParNom` via `Change Method Signature`
1. Dans `CalculateurPrime.java`, sélection de la méthode `trouverPremierParNom`.
2. **Clic droit → Refactor → Change Method Signature...** (`Alt+Shift+C`).
3. Modification du **Return type** de `Object` à `Employe` (**Capture** `02_mohamed_change_signature_return_type.png`), puis validation par **OK**.
4. Dans `Main.java` (`mohamed.eclipse`), suppression du cast `(Employe)` devenu inutile lors des appels à `calculateur.trouverPremierParNom(...)`.

### C. Opération 2 : *Replace Conditional with Polymorphism* (combinaison de `Extract Method`, `Move`, `Pull Up` et finalisation manuelle)

#### 1. Extraction et déplacement du calcul pour `Developpeur`
1. Dans `CalculateurPrime.calculerPrime(Employe)`, sélection de l'expression `dev.getNbProjetsTermines() * 300.0`.
2. **Clic droit → Refactor → Extract Method...** (`Alt+Shift+M`) pour extraire la méthode `calculerPrimeDeveloppeur(Developpeur dev)` (**Capture** `03_mohamed_extract_method.png`).
3. **Clic droit → Refactor → Move...** (`Alt+Shift+V`) sur la méthode extraite pour la déplacer vers `Developpeur` (`Receiver : dev`) (**Captures** `04_mohamed_move_method_developpeur.png` et `04_mohamed_move_method_developpeur_v2.png`), puis renommage en `calculerPrime()`.

#### 2. Remontée de la déclaration abstraite dans `Employe` avec `Pull Up`
1. Dans `Developpeur.java`, **clic droit → Refactor → Pull Up...**.
2. Sélection de `calculerPrime()` avec l'action **declare abstract in destination** vers `Employe` (**Capture** `05_mohamed_pull_up_method.png`).
3. Vérification dans la fenêtre de prévisualisation (**Captures** `05_mohamed_pull_up_method_v2.png` à `v5.png`) :
   - Eclipse transforme `Employe` en classe abstraite (`public abstract class Employe`) et déclare `public abstract double calculerPrime();`.
   - Eclipse génère automatiquement les méthodes `@Override public double calculerPrime()` dans `Commercial` et `Manager`.

#### 3. Finalisation manuelle du polymorphisme dans les sous-classes et `CalculateurPrime`
1. Dans `Commercial.java` et `Manager.java`, remplacement du `// TODO Auto-generated method stub` par les formules respectives de calcul de prime (**Captures** `05_mohamed_commercial_refactore.png` et `05_mohamed_manager_refactore.png`).
2. Ajout de la méthode polymorphe `genererDescriptionBilan()` dans `Employe` et redéfinition (`@Override`) dans `Developpeur`, `Commercial` et `Manager`.
3. Dans `CalculateurPrime.java`, remplacement des chaînes de `if (employe instanceof ...)` par les appels polymorphes `employe.calculerPrime()` et `employe.genererDescriptionBilan()` (**Capture** `06_mohamed_code_refactore.png`).

### D. Vérification finale dans Eclipse
1. Exécution de `mohamed.eclipse.Main` (**Run As → Java Application**).
2. Tous les tests réussissent avec les mêmes résultats numériques (`5550.0 €`) (**Capture** `07_mohamed_console_tests_ok.png`).

---

## Partie 2 — Refactoring du programme 2 dans Eclipse (`sacko.eclipse`)

**Package :** `fr.umontpellier.hai913i.refactoring.sacko.eclipse`  
**Défaut corrigé :** *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*  
**Classes modifiées :** `ServiceReservation.java`, `Main.java`.

### A. Exécution initiale avant refactoring
1. Exécution de `sacko.eclipse.Main` avant modification (**Capture** `00_sacko_tests_avant.png`).
2. Ouverture de `ServiceReservation.java` montrant les constantes de codes d'erreur (`0`, `-1`, `-2`, `-3`) et les retours `null` (**Capture** `01_sacko_code_initial.png`).

### B. Opération 1 : Changement des signatures avec `Change Method Signature`
1. Sur `verifierEtReserver` dans `ServiceReservation.java` : **Clic droit → Refactor → Change Method Signature...** (`Alt+Shift+C`), passage du **Return type** de `int` à `void` (**Captures** `02_sacko_change_signature_verifierEtReserver.png` et `03_sacko_previsualisation_refactoring.png`).
2. Sur `annulerReservation` : **Clic droit → Refactor → Change Method Signature...**, passage du **Return type** de `int` à `void` (**Capture** `04_sacko_previsualisation_annulerReservation.png`).

### C. Opération 2 : Remplacement manuel des codes d'erreur et des retours `null` par des exceptions (*Replace Error Code with Exception*)
1. Suppression des constantes `SUCCES`, `ERREUR_SALLE_INTROUVABLE`, `ERREUR_CAPACITE_INSUFFISANTE`, `ERREUR_RESERVATION_INTROUVABLE`.
2. Dans `trouverSalle` et `chercherReservation`, remplacement des `return null;` par `throw new IllegalArgumentException(...)`.
3. Dans `verifierEtReserver`, remplacement du code `-2` par `throw new IllegalStateException("Capacité insuffisante dans la salle " + codeSalle);`.
4. Dans `creerReservation`, appel direct à `verifierEtReserver(nomClient, codeSalle, nbPersonnes)` suivi du retour de la réservation créée (**Capture** `04_sacko_code_service_refactore.png`).

### D. Adaptation de `Main.java` et vérification finale dans Eclipse
1. Dans `sacko.eclipse.Main`, adaptation des tests d'erreur avec des blocs `try / catch` capturant `IllegalArgumentException` et `IllegalStateException` (**Capture** `05_sacko_main_adapte.png`).
2. Exécution de `sacko.eclipse.Main` (**Run As → Java Application**) confirmant le passage de tous les tests (**Capture** `06_sacko_console_tests_ok.png`).
