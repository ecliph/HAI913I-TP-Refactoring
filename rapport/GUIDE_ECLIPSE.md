# Guide Pas à Pas — Réalisation des Refactorings dans Eclipse

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Étudiants :** TEDJNI MOHAMED et SACKO DANGUE

Ce guide détaille étape par étape comment réaliser dans **Eclipse** les opérations de refactoring sur les deux packages dédiés :
- `fr.umontpellier.hai913i.refactoring.mohamed.eclipse` (à réaliser par **SACKO DANGUE**)
- `fr.umontpellier.hai913i.refactoring.sacko.eclipse` (à réaliser par **TEDJNI MOHAMED**)

> **Important :** Les packages `.initial` et `.llm` ne doivent pas être modifiés. Toutes les manipulations Eclipse se font uniquement dans les packages `.eclipse`.

---

## Étape préliminaire : Importer le projet et vérifier l'état initial sous Eclipse

1. Lancer **Eclipse**.
2. Cliquer sur **File → Import... → Maven → Existing Maven Projects → Next >**.
3. Sélectionner le dossier racine du projet `HAI913I-TP-Refactoring`, vérifier que `pom.xml` est coché, puis cliquer sur **Finish**.
4. Dans le **Package Explorer** (à gauche), dérouler `src/main/java`.
5. Vérifier que les 6 packages apparaissent sans aucune erreur de compilation.

---

## Partie 1 — Refactoring du programme de Mohamed dans Eclipse (par SACKO DANGUE)

**Package concerné :** `fr.umontpellier.hai913i.refactoring.mohamed.eclipse`  
**Défaut à corriger :** *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*  
**Classes concernées :** `CalculateurPrime.java`, `Employe.java`, `Developpeur.java`, `Commercial.java`, `Manager.java`, `Main.java`.

### A. Avant de commencer (Exécution initiale et capture 1)
1. Dans `fr.umontpellier.hai913i.refactoring.mohamed.eclipse`, faire un **clic droit sur `Main.java` → Run As → Java Application**.
2. Vérifier que la console affiche `[SUCCÈS] Tous les tests de la version Eclipse (Mohamed) sont validés.`.
3. Ouvrir `CalculateurPrime.java` dans l'éditeur et prendre la **Capture 1** (`01_mohamed_code_initial.png`) montrant les `instanceof` et les casts.

### B. Opération 1 : *Encapsulate Downcast* sur `trouverPremierParNom` via `Change Method Signature`
Dans `CalculateurPrime.java`, la méthode `trouverPremierParNom` retourne `Object`, ce qui oblige `Main` à faire un cast `(Employe)`.
Eclipse permet de modifier ce type de retour via son menu `Refactor` :

1. Dans `CalculateurPrime.java`, cliquer sur le nom de la méthode `trouverPremierParNom` (ligne 66).
2. Faire un **clic droit → Refactor → Change Method Signature...** (ou `Alt+Shift+C`).
3. Dans la fenêtre qui s'ouvre, changer le champ **Return type** de `Object` à `Employe`.
4. Prendre la **Capture 2** (`02_mohamed_change_signature_return_type.png`) avec la fenêtre *Change Method Signature* ouverte (ou cliquer sur **Preview >** pour capturer l'aperçu).
5. Cliquer sur **OK**.
6. Ouvrir `Main.java` du package `mohamed.eclipse` (lignes 49 et 55) : le cast `(Employe)` devant `calculateur.trouverPremierParNom(...)` est maintenant superflu. Supprimer `(Employe)` sur ces deux lignes et sauvegarder (`Ctrl+S`).

### C. Opération 2 : *Replace Conditional with Polymorphism* (combinaison de `Extract Method`, `Move`, `Pull Up` et édition manuelle)
> **Limitation d'Eclipse à noter dans le rapport :** Eclipse ne possède pas de commande automatique *"Replace Conditional with Polymorphism"*. En revanche, on peut utiliser ses refactorings natifs **Extract Method**, **Move** et **Pull Up** pour déplacer le calcul de la prime dans les sous-classes avant de nettoyer le `if / else if`.

#### Étape C.1 — Extraire et déplacer le calcul de la prime pour `Developpeur`
1. Dans `CalculateurPrime.java`, à l'intérieur de `calculerPrime(Employe employe)`, sélectionner l'expression de calcul de la prime du développeur :
   ```java
   dev.getNbProjetsTermines() * 300.0
   ```
2. Faire un **clic droit → Refactor → Extract Method...** (`Alt+Shift+M`).
3. Nommer la nouvelle méthode `calculerPrime` (ou `calculerPrimeSpecifique`), décocher `private` pour choisir **public**, puis prendre la **Capture 3** (`03_mohamed_extract_method.png`) et cliquer sur **OK**.
   Eclipse génère une méthode `public double calculerPrime(Developpeur dev)`.
4. Placer le curseur sur le nom de cette nouvelle méthode `calculerPrime(Developpeur dev)`.
5. Faire un **clic droit → Refactor → Move...** (`Alt+Shift+V`).
6. Eclipse détecte automatiquement que la méthode utilise `Developpeur` et propose comme destination **`Receiver : dev (Developpeur)`**.
7. Prendre la **Capture 4** (`04_mohamed_move_method_developpeur.png`) de la boîte de dialogue **Move Method**, puis cliquer sur **OK**.
   La méthode `public double calculerPrime()` est maintenant déplacée dans `Developpeur.java` !

#### Étape C.2 — Faire remonter la déclaration dans `Employe` avec `Pull Up`
1. Ouvrir `Developpeur.java` dans `mohamed.eclipse`.
2. Faire un **clic droit dans la classe → Refactor → Pull Up...**.
3. Dans la fenêtre **Pull Up** :
   - Vérifier que **Destination** est bien `fr.umontpellier.hai913i.refactoring.mohamed.eclipse.Employe`.
   - Dans la colonne **Action** en face de `calculerPrime()`, remplacer *pull up* par **declare abstract in destination** (ou laisser *pull up* si vous souhaitez une méthode par défaut retournant `0.0` dans `Employe`).
   - *Astuce :* Si vous choisissez **declare abstract in destination**, Eclipse transformera automatiquement `Employe` en classe `abstract` et proposera d'ajouter un stub dans les autres sous-classes. Si vous préférez garder `Employe` concrète ou abstraite, les deux choix sont valides. Pour obtenir exactement la même structure que la version LLM, choisissez **declare abstract in destination**.
4. Prendre la **Capture 5** (`05_mohamed_pull_up_method.png`) de la fenêtre **Pull Up**, puis cliquer sur **Next >** et **Finish**.

#### Étape C.3 — Implémenter les méthodes polymorphes dans `Commercial` et `Manager` et nettoyer `CalculateurPrime` (étape manuelle)
Comme Eclipse n'automatise pas la suppression des blocs `if (employe instanceof ...)`, terminer manuellement la transformation :

1. Dans `Employe.java` (`mohamed.eclipse`), vérifier ou ajouter les méthodes polymorphes :
   ```java
   public abstract double calculerPrime();

   public abstract String getDetailsCategorie();

   public String genererLigneBilan() {
       double prime = calculerPrime();
       double total = salaireBase + prime;
       return getDetailsCategorie()
               + " -> Salaire : " + salaireBase + " €, Prime : " + prime + " €, Total : " + total + " €";
   }
   ```
2. Dans `Developpeur.java` (`mohamed.eclipse`), vérifier `calculerPrime()` et ajouter `getDetailsCategorie()` :
   ```java
   @Override
   public double calculerPrime() {
       return nbProjetsTermines * 300.0;
   }

   @Override
   public String getDetailsCategorie() {
       return "[Développeur] " + getNom()
               + " (Langage : " + langagePrincipal + ", Projets : " + nbProjetsTermines + ")";
   }
   ```
3. Dans `Commercial.java` (`mohamed.eclipse`), implémenter les deux méthodes :
   ```java
   @Override
   public double calculerPrime() {
       return chiffreAffaires * tauxCommission;
   }

   @Override
   public String getDetailsCategorie() {
       return "[Commercial] " + getNom()
               + " (CA : " + chiffreAffaires + " €, Taux : " + (tauxCommission * 100) + "%)";
   }
   ```
4. Dans `Manager.java` (`mohamed.eclipse`), implémenter les deux méthodes :
   ```java
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
   ```
5. Dans `CalculateurPrime.java` (`mohamed.eclipse`), remplacer les chaînes de `if (employe instanceof ...)` par les appels polymorphes :
   ```java
   public double calculerPrime(Employe employe) {
       return employe.calculerPrime();
   }

   public String genererLigneBilan(Employe employe) {
       return employe.genererLigneBilan();
   }
   ```
6. Sauvegarder tous les fichiers (`Ctrl+Shift+S`).
7. Prendre la **Capture 6** (`06_mohamed_code_refactore.png`) montrant `CalculateurPrime.java` et `Employe.java` sans aucun `instanceof` ni cast.

### D. Vérification finale dans Eclipse (Capture 7)
1. Faire un **clic droit sur `Main.java` (du package `mohamed.eclipse`) → Run As → Java Application**.
2. Vérifier que tous les tests passent dans la console Eclipse.
3. Prendre la **Capture 7** (`07_mohamed_console_tests_ok.png`) montrant l'exécution réussie dans la console d'Eclipse.

---

## Partie 2 — Refactoring du programme de Sacko dans Eclipse (par TEDJNI MOHAMED)

**Package concerné :** `fr.umontpellier.hai913i.refactoring.sacko.eclipse`  
**Défaut à corriger :** *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*  
**Classes concernées :** `ServiceReservation.java`, `Main.java`.

### A. Avant de commencer (Exécution initiale et capture 1)
1. Dans `fr.umontpellier.hai913i.refactoring.sacko.eclipse`, faire un **clic droit sur `Main.java` → Run As → Java Application**.
2. Vérifier que la console affiche `[SUCCÈS] Tous les tests de la version Eclipse (Sacko) sont validés.`.
3. Ouvrir `ServiceReservation.java` dans l'éditeur et prendre la **Capture 1** (`01_sacko_code_initial.png`) montrant les constantes de codes d'erreur (`-1`, `-2`, `-3`) et les retours `null`.

### B. Opération 1 : Changer les signatures avec `Change Method Signature` (Menu `Refactor`)
Le refactoring visé est **Replace Error Code with Exception** (diapositive 20 du cours).
> **Limitation d'Eclipse à noter dans le rapport :** Eclipse ne propose pas d'opération automatisée *"Replace Error Code with Exception"*. Nous utilisons donc **Change Method Signature** dans le menu `Refactor` pour transformer le type de retour `int` en `void` sur `verifierEtReserver` et `annulerReservation`, puis nous remplaçons manuellement les retours de codes d'erreur et de `null` par des `throw new ...`.

1. Dans `ServiceReservation.java` (`sacko.eclipse`), placer le curseur sur la méthode `verifierEtReserver` (ligne 55).
2. Faire un **clic droit → Refactor → Change Method Signature...** (`Alt+Shift+C`).
3. Changer **Return type** de `int` à `void`.
4. Prendre la **Capture 2** (`02_sacko_change_signature_verifierEtReserver.png`) avec la fenêtre ouverte.
5. Cliquer sur **OK**.
6. Faire de même sur la méthode `annulerReservation(String reference)` : **clic droit → Refactor → Change Method Signature...**, passer **Return type** de `int` à `void`, prendre la **Capture 3** (`03_sacko_change_signature_annulerReservation.png`), puis cliquer sur **OK**.

### C. Opération 2 : Remplacement manuel des codes d'erreur et des retours `null` par des exceptions standards
Après le changement de signature, Eclipse souligne en rouge les `return ERREUR_...;` dans `ServiceReservation.java` ainsi que les appels `int code = ...` dans `Main.java`. C'est très pratique car l'IDE indique tous les endroits à convertir en exceptions :

1. Dans `ServiceReservation.java` (`sacko.eclipse`) :
   - Supprimer les 4 constantes `SUCCES`, `ERREUR_SALLE_INTROUVABLE`, `ERREUR_CAPACITE_INSUFFISANTE`, `ERREUR_RESERVATION_INTROUVABLE`.
   - Dans `trouverSalle(String codeSalle)` : au lieu de retourner `null`, lever une `IllegalArgumentException` si le code est vide ou si la salle n'est pas trouvée :
     ```java
     public Salle trouverSalle(String codeSalle) {
         if (codeSalle == null || codeSalle.trim().isEmpty()) {
             throw new IllegalArgumentException("Le code de la salle ne peut pas être vide.");
         }
         for (Salle s : salles) {
             if (s.getCode().equalsIgnoreCase(codeSalle)) {
                 return s;
             }
         }
         throw new IllegalArgumentException("Salle introuvable : " + codeSalle);
     }
     ```
   - Dans `creerReservation` et `verifierEtReserver` : remplacer le retour `-2` / `null` lorsque la capacité est insuffisante par une `IllegalStateException`, et retourner directement la `Reservation` créée :
     ```java
     public void verifierEtReserver(String nomClient, String codeSalle, int nbPersonnes) {
         creerReservation(nomClient, codeSalle, nbPersonnes);
     }

     public Reservation creerReservation(String nomClient, String codeSalle, int nbPersonnes) {
         if (nomClient == null || nomClient.trim().isEmpty()) {
             throw new IllegalArgumentException("Le nom du client ne peut pas être vide.");
         }
         if (nbPersonnes <= 0) {
             throw new IllegalArgumentException("Le nombre de personnes doit être strictement positif.");
         }

         Salle salle = trouverSalle(codeSalle);
         if (salle.getPlacesDisponibles() < nbPersonnes) {
             throw new IllegalStateException("Capacité insuffisante dans la salle " + salle.getCode()
                     + " (demandé : " + nbPersonnes + ", disponible : " + salle.getPlacesDisponibles() + ").");
         }

         salle.occuperPlaces(nbPersonnes);
         String ref = "RES-" + compteurReference++;
         Reservation nouvelle = new Reservation(ref, nomClient, salle.getCode(), nbPersonnes);
         reservations.add(nouvelle);
         return nouvelle;
     }
     ```
   - Dans `chercherReservation(String reference)` : remplacer les deux `return null;` par :
     - `throw new IllegalArgumentException("La référence de réservation ne peut pas être vide.");`
     - `throw new IllegalArgumentException("Réservation introuvable : " + reference);`
   - Dans `annulerReservation(String reference)` : comme `chercherReservation` et `trouverSalle` lèvent désormais une exception si la réservation ou la salle est introuvable, la méthode devient :
     ```java
     public void annulerReservation(String reference) {
         Reservation res = chercherReservation(reference);
         Salle salle = trouverSalle(res.getCodeSalle());
         salle.libererPlaces(res.getNbPersonnes());
         reservations.remove(res);
     }
     ```
2. Prendre la **Capture 4** (`04_sacko_code_service_refactore.png`) montrant `ServiceReservation.java` refactoré avec les exceptions.

### D. Opération 3 : Adapter `Main.java` dans `sacko.eclipse` et vérifier les tests (Captures 5 et 6)
1. Ouvrir `Main.java` dans `fr.umontpellier.hai913i.refactoring.sacko.eclipse`.
2. Remplacer les tests de codes de retour (`-1`, `-2`, `-3`, `0`) et les tests `== null` des sections 2, 3 et 5 par des blocs `try / catch` vérifiant la levée de `IllegalArgumentException` et `IllegalStateException` (vous pouvez vous appuyer sur `fr.umontpellier.hai913i.refactoring.sacko.llm.Main` qui teste exactement les mêmes scénarios).
3. Sauvegarder (`Ctrl+S`) et prendre la **Capture 5** (`05_sacko_main_adapte.png`).
4. Faire un **clic droit sur `Main.java` (du package `sacko.eclipse`) → Run As → Java Application**.
5. Vérifier que la console affiche `[SUCCÈS]` et prendre la **Capture 6** (`06_sacko_console_tests_ok.png`).
