# Consignes pour les Captures d'Écran Eclipse

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Étudiants :** TEDJINI MOHAMED et SACKO DANGUE

La section 2.2 du sujet demande d'illustrer par des **copies d'écran des différentes étapes** les procédures de refactoring appliquées sous Eclipse.

Aucune fausse capture n'a été générée automatiquement. Lorsque vous réaliserez les manipulations sous Eclipse en suivant [`rapport/GUIDE_ECLIPSE.md`](../../rapport/GUIDE_ECLIPSE.md), enregistrez vos captures au format PNG dans les deux sous-dossiers prévus ci-dessous.

---

## 1. Dossier `preuves/eclipse/mohamed/`
*(Refactoring du programme de TEDJINI MOHAMED réalisé sous Eclipse par SACKO DANGUE)*

| Nom de fichier conseillé | Étape à capturer dans Eclipse |
| :--- | :--- |
| `01_mohamed_code_initial.png` | `CalculateurPrime.java` (package `mohamed.eclipse`) ouvert dans Eclipse avant refactoring, montrant les `instanceof` et les casts `(Developpeur)`, `(Commercial)`, `(Manager)`. |
| `02_mohamed_change_signature_return_type.png` | Fenêtre **Refactor → Change Method Signature** sur `trouverPremierParNom`, montrant le passage du type de retour de `Object` à `Employe` (*Encapsulate Downcast*). |
| `03_mohamed_extract_method.png` | Fenêtre **Refactor → Extract Method** lors de l'extraction du calcul de la prime d'une sous-classe dans `CalculateurPrime`. |
| `04_mohamed_move_method_developpeur.png` | Fenêtre **Refactor → Move Method** montrant le déplacement de la méthode extraite vers la classe `Developpeur`. |
| `05_mohamed_pull_up_method.png` | Fenêtre **Refactor → Pull Up** montrant la remontée de la méthode `calculerPrime()` vers la classe mère `Employe` (*declare abstract in destination*). |
| `06_mohamed_code_refactore.png` | Éditeur Eclipse montrant `CalculateurPrime.java` et `Employe.java` une fois le refactoring polymorphe terminé (sans aucun `instanceof` ni cast). |
| `07_mohamed_console_tests_ok.png` | Vue **Console** d'Eclipse après `Run As → Java Application` sur `mohamed.eclipse.Main`, affichant `[SUCCÈS]`. |

---

## 2. Dossier `preuves/eclipse/sacko/`
*(Refactoring du programme de SACKO DANGUE réalisé sous Eclipse par TEDJINI MOHAMED)*

| Nom de fichier conseillé | Étape à capturer dans Eclipse |
| :--- | :--- |
| `01_sacko_code_initial.png` | `ServiceReservation.java` (package `sacko.eclipse`) ouvert dans Eclipse avant refactoring, montrant les constantes `-1`, `-2`, `-3` et les retours `null`. |
| `02_sacko_change_signature_verifierEtReserver.png` | Fenêtre **Refactor → Change Method Signature** sur `verifierEtReserver`, montrant le changement du type de retour de `int` à `void`. |
| `03_sacko_change_signature_annulerReservation.png` | Fenêtre **Refactor → Change Method Signature** sur `annulerReservation`, montrant le changement du type de retour de `int` à `void`. |
| `04_sacko_code_service_refactore.png` | Éditeur Eclipse montrant `ServiceReservation.java` après remplacement manuel des codes d'erreur et de `null` par `throw new IllegalArgumentException(...)` et `throw new IllegalStateException(...)` (*Replace Error Code with Exception*). |
| `05_sacko_main_adapte.png` | Éditeur Eclipse montrant `Main.java` (package `sacko.eclipse`) adapté avec les blocs `try / catch`. |
| `06_sacko_console_tests_ok.png` | Vue **Console** d'Eclipse après `Run As → Java Application` sur `sacko.eclipse.Main`, affichant `[SUCCÈS]`. |

