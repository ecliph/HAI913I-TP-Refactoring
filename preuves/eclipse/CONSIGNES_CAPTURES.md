# Inventaire des Captures d'Écran Eclipse

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Étudiants :** TEDJINI MOHAMED et SACKO DANGUE

La section 2.2 du sujet demande d'illustrer par des **copies d'écran des différentes étapes** les procédures de refactoring appliquées sous Eclipse.  
Ce document répertorie et explique chacune des **23 captures d'écran** enregistrées lors des manipulations dans Eclipse.

---

## 1. Dossier `preuves/eclipse/mohamed/` (15 captures)
*(Refactoring dans Eclipse du programme de gestion des primes d'employés — Sujet de TEDJINI MOHAMED)*

| Fichier PNG | Étape documentée dans Eclipse |
| :--- | :--- |
| `00_mohamed_tests_avant.png` | Exécution initiale de `mohamed.eclipse.Main` dans Eclipse avant tout refactoring (console affichant les bilans et le total de 5550.0 €). |
| `01_mohamed_code_initial.png` | `CalculateurPrime.java` ouvert dans Eclipse avant refactoring, montrant les chaînes de `if (employe instanceof ...)` et les casts `(Developpeur)`, `(Commercial)`, `(Manager)`. |
| `02_mohamed_change_signature_return_type.png` | Opération **Refactor → Change Method Signature** sur `trouverPremierParNom`, illustrant le passage du type de retour de `Object` à `Employe` (*Encapsulate Downcast*). |
| `03_mohamed_extract_method.png` | Opération **Refactor → Extract Method** dans `CalculateurPrime` pour extraire le calcul de la prime du développeur dans une méthode `calculerPrimeDeveloppeur(Developpeur dev)`. |
| `04_mohamed_move_method_developpeur.png` | Boîte de dialogue **Refactor → Move Method** déplaçant la méthode extraite vers la classe `Developpeur` (`Receiver : dev`). |
| `04_mohamed_move_method_developpeur_v2.png` | Fenêtre **Preview** de **Move Method** montrant le remplacement de `calculerPrimeDeveloppeur(dev)` par `dev.calculerPrimeDeveloppeur()`. |
| `05_mohamed_pull_up_method.png` | Assistant **Refactor → Pull Up** depuis `Developpeur` vers `Employe`, avec l'action **declare abstract in destination** sélectionnée pour `calculerPrime()`. |
| `05_mohamed_pull_up_method_v2.png` | Prévisualisation (**Pull Up**) de l'impact sur `CalculateurPrime.java` (le type de la variable locale passe à `Employe`). |
| `05_mohamed_pull_up_method_v3.png` | Prévisualisation (**Pull Up**) sur `Employe.java` montrant le passage automatique de la classe en `public abstract class Employe` et l'ajout de la signature abstraite. |
| `05_mohamed_pull_up_method_v4.png` | Prévisualisation (**Pull Up**) sur `Commercial.java` montrant la génération automatique du stub `@Override public double calculerPrime()`. |
| `05_mohamed_pull_up_method_v5.png` | Prévisualisation (**Pull Up**) sur `Manager.java` montrant la génération automatique du stub `@Override public double calculerPrime()`. |
| `05_mohamed_commercial_refactore.png` | Éditeur Eclipse montrant `Commercial.java` après remplacement du `TODO` par la formule `getChiffreAffaires() * getTauxCommission()`. |
| `05_mohamed_manager_refactore.png` | Éditeur Eclipse montrant `Manager.java` après remplacement du `TODO` par le calcul de la prime d'équipe et du bonus d'objectifs. |
| `06_mohamed_code_refactore.png` | Éditeur Eclipse montrant `CalculateurPrime.java` entièrement refactoré, utilisant les appels polymorphes `employe.calculerPrime()` et `employe.genererDescriptionBilan()` sans aucun `instanceof` ni cast. |
| `07_mohamed_console_tests_ok.png` | Vue **Console** d'Eclipse après exécution de `mohamed.eclipse.Main` refactoré, confirmant `[SUCCÈS] Tous les tests de la version Eclipse (Mohamed) sont validés.`. |

---

## 2. Dossier `preuves/eclipse/sacko/` (8 captures)
*(Refactoring dans Eclipse du programme de gestion des réservations — Sujet de SACKO DANGUE)*

| Fichier PNG | Étape documentée dans Eclipse |
| :--- | :--- |
| `00_sacko_tests_avant.png` | Exécution initiale de `sacko.eclipse.Main` dans Eclipse avant refactoring (console affichant les codes `-1`, `-2`, `-3` et `0`). |
| `01_sacko_code_initial.png` | `ServiceReservation.java` ouvert dans Eclipse avant refactoring, montrant les constantes de codes d'erreur (`SUCCES`, `-1`, `-2`, `-3`) et le retour `null`. |
| `02_sacko_change_signature_verifierEtReserver.png` | Fenêtre **Refactor → Change Method Signature** sur `verifierEtReserver`, passant le type de retour de `int` à `void`. |
| `03_sacko_previsualisation_refactoring.png` | Fenêtre **Preview** de **Change Method Signature** sur `verifierEtReserver` (`public int` -> `public void`). |
| `04_sacko_previsualisation_annulerReservation.png` | Fenêtre **Preview** de **Change Method Signature** sur `annulerReservation` (`public int` -> `public void`). |
| `04_sacko_code_service_refactore.png` | Éditeur Eclipse montrant `ServiceReservation.java` refactoré avec lancement d'`IllegalStateException` et `IllegalArgumentException` (*Replace Error Code with Exception*). |
| `05_sacko_main_adapte.png` | Éditeur Eclipse montrant `Main.java` (`sacko.eclipse`) adapté avec les blocs `try / catch` pour vérifier les exceptions levées. |
| `06_sacko_console_tests_ok.png` | Vue **Console** d'Eclipse après exécution de `sacko.eclipse.Main` refactoré, confirmant `[SUCCÈS] Tous les tests de la version Eclipse (Sacko) sont validés.`. |
