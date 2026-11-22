# Rapport de Refactoring — Partie 2 (Mise en œuvre pratique)

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Enseignante :** Marianne Huchard  
**Étudiants :**
- **TEDJINI MOHAMED**
- **SACKO DANGUE**

---

## Introduction

Dans cette seconde partie du TP, nous étudions la correction de deux défauts de conception (*code smells* / *anti-patterns*) en comparant deux méthodes de travail sur un même code initial :
1. **Le refactoring dans l'IDE Eclipse**, en exploitant les opérations automatisées du menu `Refactor` et en complétant manuellement ce que l'IDE ne prend pas en charge.
2. **Le refactoring intégral par un LLM**, à qui l'on demande de corriger le défaut et d'expliquer les opérations du catalogue de Fowler appliquées.

Les deux sujets attribués dans le fichier officiel `AffectationSmells.xlsx` sont :
- **Programme 1 (Sujet attribué à TEDJINI MOHAMED) :** *"Code frequently casts to concrete types, breaking polymorphism and adding fragility"*, illustré par un programme de calcul de primes et de bilans d'employés (`fr.umontpellier.hai913i.refactoring.mohamed.*`).
- **Programme 2 (Sujet attribué à SACKO DANGUE) :** *"Mixing return codes, nulls, and exceptions creates confusion and bugs"*, illustré par un programme de gestion de réservations de salles (`fr.umontpellier.hai913i.refactoring.sacko.*`).

Pour chaque programme, les trois versions (`initial`, `eclipse`, `llm`) sont présentes dans des packages séparés, compilent avec Maven (Java 17) et possèdent leur propre classe `Main` de test. Les manipulations pratiques dans Eclipse ont été effectuées par TEDJINI MOHAMED sur les deux packages `.eclipse`.

---

## Partie A — Programme 1 : Gestion des primes d'employés (Conversions explicites vers des types concrets)

- **Packages :**
  - Version initiale : `fr.umontpellier.hai913i.refactoring.mohamed.initial`
  - Version Eclipse : `fr.umontpellier.hai913i.refactoring.mohamed.eclipse`
  - Version LLM : `fr.umontpellier.hai913i.refactoring.mohamed.llm`

### 1. Défaut attribué et explication
> *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*

Lorsqu'une hiérarchie de classes partage une classe mère (`Employe`) et plusieurs sous-classes (`Developpeur`, `Commercial`, `Manager`), le polymorphisme permet d'appeler une opération commune sur une référence de type `Employe` sans connaître sa classe concrète.  
Le défaut consiste à tester manuellement le type dynamique de l'objet avec `instanceof`, puis à effectuer une conversion explicite (*downcast*) vers la sous-classe concrète (`(Developpeur) employe`, `(Commercial) employe`, `(Manager) employe`) pour calculer un résultat. On retrouve une seconde forme de ce défaut lorsqu'une méthode déclare un type de retour trop général (`Object`) alors qu'elle retourne toujours un `Employe`, obligeant l'appelant à écrire un cast `(Employe)`.

### 2. Présentation du programme initial (`mohamed.initial`)
Le programme initial comporte 6 classes :
- `Employe` : classe mère contenant les attributs `nom` et `salaireBase`.
- `Developpeur`, `Commercial`, `Manager` : trois sous-classes contenant uniquement leurs attributs spécifiques et leurs *getters* (aucune méthode métier).
- `CalculateurPrime` : classe qui concentre toute la logique et présente trois manifestations du défaut :
  1. `calculerPrime(Employe employe)` : enchaîne trois blocs `if / else if (employe instanceof ...)` avec des casts explicites vers `Developpeur`, `Commercial` et `Manager`.
  2. `genererLigneBilan(Employe employe)` : duplique la même chaîne de tests `instanceof` et de casts pour construire la description textuelle de chaque employé.
  3. `trouverPremierParNom(List<Employe> employes, String nomRecherche)` : retourne `Object` au lieu de `Employe`, forçant `Main` à faire un cast `(Employe)`.
- `Main` : teste 5 employés (Alice, Benoit, Chloé, David, Emma), vérifie les primes individuelles, le total des primes (`5550.0 €`) et la recherche par nom.

### 3. Conséquences du défaut
- **Violation du principe Ouvert/Fermé et fragilité** : si l'on ajoute une nouvelle sous-classe d'`Employe` (par exemple `Stagiaire`), il faut modifier toutes les méthodes contenant des chaînes `if (employe instanceof ...)`. Tout oubli produit silencieusement un calcul erroné (`0.0`).
- **Mauvaise répartition des responsabilités (*Feature Envy* / *Switch Statements*, diapositive 26 du cours)** : `CalculateurPrime` manipule directement les données internes des sous-classes au lieu de déléguer le comportement aux objets concernés.
- **Duplication structurelle** : la chaîne de tests de types est répétée à l'identique dans `calculerPrime` et `genererLigneBilan`.

### 4. Refactorings appliqués et intérêt
Les opérations du catalogue de Fowler (présentées dans les diapositives 13, 18, 19, 21 et 26 du cours) appliquées pour corriger ce défaut sont :
1. **Encapsulate Downcast** (diapositive 19) : spécialiser le type de retour de `trouverPremierParNom` de `Object` vers `Employe` pour décharger le code client (`Main`) du cast explicite.
2. **Extract Method + Move Method + Pull Up Method** (diapositives 11, 13 et 21) : extraire le calcul propre à une sous-classe, le déplacer dans cette sous-classe, puis faire remonter la déclaration `public abstract double calculerPrime()` dans la classe mère `Employe`.
3. **Replace Conditional with Polymorphism** (diapositive 18) : remplacer les structures conditionnelles `if / else if (instanceof)` de `CalculateurPrime` par des appels polymorphes sur `Employe` (`employe.calculerPrime()` et la méthode de description du bilan).

### 5. Procédure réellement appliquée sous Eclipse (`mohamed.eclipse`)
Les manipulations ont été réalisées dans Eclipse et illustrées par les 15 captures du dossier `preuves/eclipse/mohamed/` :
1. **Vérification avant refactoring** (`00_mohamed_tests_avant.png` et `01_mohamed_code_initial.png`) : exécution initiale de `Main` dans Eclipse confirmant le bon fonctionnement de départ.
2. **Application de *Encapsulate Downcast* avec `Change Method Signature`** (`02_mohamed_change_signature_return_type.png`) :
   - Menu **Refactor → Change Method Signature...** (`Alt+Shift+C`) sur `trouverPremierParNom` pour remplacer le type de retour `Object` par `Employe`.
   - Suppression manuelle du cast `(Employe)` devenu inutile dans `Main.java`.
3. **Amorçage de *Replace Conditional with Polymorphism* via `Extract Method`, `Move` et `Pull Up`** :
   - Sélection de `dev.getNbProjetsTermines() * 300.0` dans `CalculateurPrime.calculerPrime`, puis **Refactor → Extract Method...** pour créer `calculerPrimeDeveloppeur(Developpeur dev)` (`03_mohamed_extract_method.png`).
   - **Refactor → Move...** sur cette méthode extraite pour la déplacer dans `Developpeur` (`04_mohamed_move_method_developpeur.png` et `04_mohamed_move_method_developpeur_v2.png`), puis renommage en `calculerPrime()`.
   - **Refactor → Pull Up...** depuis `Developpeur` vers `Employe` en sélectionnant l'action **declare abstract in destination** (`05_mohamed_pull_up_method.png`). Comme le montrent les aperçus (`v2`, `v3`, `v4`, `v5`), Eclipse a automatiquement rendu la classe `Employe` abstraite, ajouté `public abstract double calculerPrime();` dans `Employe`, et généré les squelettes `@Override public double calculerPrime()` avec un `TODO` dans `Commercial` et `Manager`.
4. **Partie réalisée manuellement dans Eclipse** :
   - Eclipse ne pouvant pas deviner ni déplacer automatiquement les autres branches du `if / else if` dans les méthodes générées par `Pull Up`, les formules de `Commercial.calculerPrime()` et `Manager.calculerPrime()` ont été complétées à la main (`05_mohamed_commercial_refactore.png`, `05_mohamed_manager_refactore.png`).
   - Pour le bilan, une méthode polymorphe `genererDescriptionBilan()` a été ajoutée dans `Employe` (retournant `"[Employé] " + getNom()`) et redéfinie dans `Developpeur`, `Commercial` et `Manager`.
   - Enfin, dans `CalculateurPrime.java`, les deux chaînes de `if (employe instanceof ...)` ont été remplacées manuellement par `employe.calculerPrime()` (avec une garde `if (employe == null) return 0.0;`) et par `employe.genererDescriptionBilan()` (`06_mohamed_code_refactore.png`).
5. **Validation finale sous Eclipse** (`07_mohamed_console_tests_ok.png`) : l'exécution de `mohamed.eclipse.Main` valide l'ensemble des tests.

### 6. Opérations réalisées par le LLM (`mohamed.llm`)
Le LLM a appliqué directement la cible polymorphe sur l'ensemble des classes :
- `Employe` a été déclarée `abstract` avec deux méthodes abstraites : `calculerPrime()` et `getDetailsCategorie()`.
- Le LLM a également déplacé l'assemblage complet de la ligne de bilan (`genererLigneBilan()`) dans la classe mère `Employe`, tandis que `CalculateurPrime.genererLigneBilan(Employe)` se contente de déléguer à `employe.genererLigneBilan()`.
- `trouverPremierParNom` a vu son type de retour passer de `Object` à `Employe`, et les casts `(Employe)` ont été retirés de `Main`.

### 7. Comparaison détaillée des trois versions (`mohamed.initial`, `mohamed.eclipse`, `mohamed.llm`)

| Critère | `mohamed.initial` | `mohamed.eclipse` | `mohamed.llm` |
| :--- | :--- | :--- | :--- |
| **Nombre de tests `instanceof`** | 6 (3 dans `calculerPrime`, 3 dans `genererLigneBilan`) | **0** | **0** |
| **Nombre de casts explicites** | 8 (6 dans `CalculateurPrime`, 2 dans `Main`) | **0** | **0** |
| **Classe `Employe`** | Concrète, sans méthode métier | Abstraite (`calculerPrime()` abstraite, `genererDescriptionBilan()` concrète par défaut) | Abstraite (`calculerPrime()` et `getDetailsCategorie()` abstraites, `genererLigneBilan()` concrète) |
| **Emplacement du formatage du salaire/total** | Dans chaque branche `if/else` de `CalculateurPrime` (dupliqué 4 fois) | Factorisé une seule fois dans `CalculateurPrime.genererLigneBilan(Employe)` | Factorisé une seule fois dans `Employe.genererLigneBilan()` |
| **Protection contre `employe == null`** | Implicite via `instanceof` (retourne `0.0`) | Explicite (`if (employe == null) return 0.0;`) dans `CalculateurPrime.calculerPrime` | Aucune (lève `NullPointerException` si `employe` est `null`) |
| **Accès aux champs dans les sous-classes** | Depuis l'extérieur via *getters* après cast | Via les *getters* internes (`getChiffreAffaires()`, `getTailleEquipe()`, etc.) | Accès direct aux attributs privés de la sous-classe |

**Analyse comparative (Eclipse vs LLM sur le Programme 1) :**
- La version **Eclipse** n'est pas inférieure à la version **LLM** ; sur deux points précis, elle est même légèrement plus prudente :
  1. Dans `mohamed.eclipse.CalculateurPrime.calculerPrime(Employe)`, la garde `if (employe == null) return 0.0;` préserve exactement le comportement du programme initial dans le cas où une référence `null` serait passée à `calculerPrime`, alors que `mohamed.llm` appellerait directement `employe.calculerPrime()` et lèverait une `NullPointerException`.
  2. Dans `mohamed.eclipse`, `CalculateurPrime` conserve la responsabilité de mettre en forme la partie financière du bilan (`" -> Salaire : ... Prime : ... Total : ..."`), tandis que `Employe` fournit sa description (`genererDescriptionBilan()`). Cela justifie mieux l'existence de la classe `CalculateurPrime` que dans `mohamed.llm`, où `CalculateurPrime` devient un simple passe-plat (*Middle Man*).
- Une différence mineure de chaîne de caractères existe dans `mohamed.eclipse.Manager.genererDescriptionBilan()` : le mot `"Equipe"` a été saisi sans accent lors de l'édition manuelle dans Eclipse (`"(Equipe : 6..."`), contre `"(Équipe : 6..."` dans `initial` et `llm`. Cela n'affecte aucun calcul numérique ni aucun test.

---

## Partie B — Programme 2 : Système de réservation de salles (Mélange de codes de retour, `null` et exceptions)

- **Packages :**
  - Version initiale : `fr.umontpellier.hai913i.refactoring.sacko.initial`
  - Version Eclipse : `fr.umontpellier.hai913i.refactoring.sacko.eclipse`
  - Version LLM : `fr.umontpellier.hai913i.refactoring.sacko.llm`

### 1. Défaut attribué et explication
> *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*

Ce défaut survient lorsqu'un même composant logiciel utilise simultanément trois conventions hétérogènes pour signaler ses erreurs :
- des **codes de retour entiers** (`0` pour le succès, `-1`, `-2`, `-3` pour différentes erreurs) ;
- des **valeurs `null`** retournées à la place d'un objet attendu ;
- des **exceptions** (`IllegalArgumentException`) pour d'autres erreurs.

Cette incohérence complique l'utilisation de l'API : le code appelant doit combiner des tests sur des entiers, des tests `!= null` et des blocs `try / catch`. De plus, lorsqu'une méthode convertit un code d'erreur en retour `null` (comme le fait `creerReservation` dans la version initiale), la cause exacte de l'échec est perdue.

### 2. Présentation du programme initial (`sacko.initial`)
Le programme initial comporte 4 classes (`Salle`, `Reservation`, `ServiceReservation`, `Main`). Dans `ServiceReservation`, les erreurs sont traitées de manière incohérente :
1. **Exceptions** : `verifierEtReserver` lève `IllegalArgumentException` si `nomClient` est vide ou si `nbPersonnes <= 0`.
2. **Codes d'erreur entiers** : `verifierEtReserver` retourne `-1` (`ERREUR_SALLE_INTROUVABLE`) si la salle n'existe pas, `-2` (`ERREUR_CAPACITE_INSUFFISANTE`) s'il manque des places, et `0` (`SUCCES`) sinon. De même, `annulerReservation` retourne `-3` (`ERREUR_RESERVATION_INTROUVABLE`) ou `0`.
3. **Retours `null`** : `trouverSalle`, `creerReservation` et `chercherReservation` retournent `null` en cas d'échec.

### 3. Refactorings appliqués et intérêt
Conformément à la diapositive 20 du cours (*Making Method Calls Simpler*), nous avons appliqué :
- **Replace Error Code with Exception** :
  - Suppression des constantes entières `SUCCES`, `ERREUR_SALLE_INTROUVABLE`, `ERREUR_CAPACITE_INSUFFISANTE` et `ERREUR_RESERVATION_INTROUVABLE`.
  - Changement de signature (*Change Method Signature*) de `verifierEtReserver` et `annulerReservation` : leur type de retour passe de `int` à `void`.
  - Remplacement des retours de codes négatifs et des retours `null` par deux exceptions standards du JDK :
    - `IllegalArgumentException` : paramètre invalide (nom vide, code vide, référence vide, `nbPersonnes <= 0`) ou identifiant introuvable (salle ou réservation inexistante).
    - `IllegalStateException` : capacité disponible de la salle insuffisante pour accueillir le groupe demandé.

### 4. Procédure réellement appliquée sous Eclipse (`sacko.eclipse`)
Les manipulations effectuées sous Eclipse sont documentées par les 8 captures du dossier `preuves/eclipse/sacko/` :
1. **Vérification de l'état initial** (`00_sacko_tests_avant.png` et `01_sacko_code_initial.png`) : exécution de `sacko.eclipse.Main` avant refactoring.
2. **Utilisation du menu `Refactor → Change Method Signature...` (`Alt+Shift+C`)** :
   - Sur `verifierEtReserver` : changement du type de retour de `int` à `void` (`02_sacko_change_signature_verifierEtReserver.png` et prévisualisation `03_sacko_previsualisation_refactoring.png`).
   - Sur `annulerReservation` : changement du type de retour de `int` à `void` (prévisualisation `04_sacko_previsualisation_annulerReservation.png`).
3. **Modifications manuelles dans `ServiceReservation.java`** (`04_sacko_code_service_refactore.png`) :
   - Comme Eclipse ne possède pas de refactoring automatique *"Replace Error Code with Exception"*, le passage de `int` à `void` a immédiatement mis en évidence (par des erreurs de compilation dans l'éditeur Eclipse) les instructions `return ERREUR_...;` et `return SUCCES;` ainsi que les appels `int code = ...` dans `Main`.
   - Les 4 constantes de codes d'erreur ont été supprimées.
   - `trouverSalle` et `chercherReservation` ont été modifiées pour vérifier que l'identifiant n'est pas vide et lever une `IllegalArgumentException` s'il est introuvable (plus aucun retour `null`).
   - Dans `verifierEtReserver`, le test de capacité lève désormais `throw new IllegalStateException("Capacité insuffisante dans la salle " + codeSalle);`.
   - `creerReservation` appelle simplement `verifierEtReserver(nomClient, codeSalle, nbPersonnes)` puis retourne `reservations.get(reservations.size() - 1)`.
4. **Adaptation de `Main.java` et validation des tests** (`05_sacko_main_adapte.png` et `06_sacko_console_tests_ok.png`) :
   - Les vérifications de codes (`-1`, `-2`, `-3`, `0`) et de `null` ont été remplacées par des blocs `try / catch` vérifiant que `IllegalArgumentException` et `IllegalStateException` sont bien levées.
   - L'exécution dans Eclipse affiche `[SUCCÈS] Tous les tests de la version Eclipse (Sacko) sont validés.`.

### 5. Opérations réalisées par le LLM (`sacko.llm`)
Le LLM a appliqué la même stratégie d'harmonisation par exceptions (`IllegalArgumentException` et `IllegalStateException`), mais a inversé le sens de délégation entre `verifierEtReserver` et `creerReservation` :
- Dans `sacko.llm.ServiceReservation`, toute la logique de validation et de création a été placée dans `creerReservation(...)` qui instancie `Reservation nouvelle`, l'ajoute à la liste et la retourne directement ; `verifierEtReserver(...)` se contente d'appeler `creerReservation(...)` en ignorant la valeur de retour.
- Le LLM a également ajouté une garde `if (salle == null) throw new IllegalArgumentException(...)` dans `ajouterSalle(Salle)` et un message d'exception plus détaillé lors d'un dépassement de capacité (`"(demandé : 15, disponible : 10)"`).

### 6. Comparaison détaillée des trois versions (`sacko.initial`, `sacko.eclipse`, `sacko.llm`)

| Critère | `sacko.initial` | `sacko.eclipse` | `sacko.llm` |
| :--- | :--- | :--- | :--- |
| **Mécanismes d'erreur utilisés** | 3 mélangés (codes `-1/-2/-3`, `null`, exceptions) | **1 seul unifié** (exceptions standards) | **1 seul unifié** (exceptions standards) |
| **Nombre de `return null;`** | 4 | **0** | **0** |
| **Nombre d'instructions `throw new`** | 2 (dans `ServiceReservation`) | **6** (dans `ServiceReservation`) | **7** (dans `ServiceReservation`) |
| **Signature de `verifierEtReserver`** | `int verifierEtReserver(...)` | `void verifierEtReserver(...)` | `void verifierEtReserver(...)` |
| **Signature de `annulerReservation`** | `int annulerReservation(String)` | `void annulerReservation(String)` | `void annulerReservation(String)` |
| **Sens de délégation** | `creerReservation` appelle `verifierEtReserver` | `creerReservation` appelle `verifierEtReserver` puis lit le dernier élément de la liste | `verifierEtReserver` appelle `creerReservation` qui retourne directement l'objet `Reservation` créé |

**Analyse comparative (Eclipse vs LLM sur le Programme 2) :**
- La version **Eclipse** (`sacko.eclipse`) montre bien comment un développeur procède à partir du code existant : en conservant la structure de `verifierEtReserver` et en faisant appel à `verifierEtReserver` depuis `creerReservation`, la modification est minimale et très proche de l'historique du code initial.
- En contrepartie, sur ce second programme, l'inversion de délégation proposée par le **LLM** (`creerReservation` crée et retourne directement l'objet `nouvelle`, et `verifierEtReserver` l'appelle) est légèrement plus propre que `return reservations.get(reservations.size() - 1);` conservé dans `sacko.eclipse`.

---

## Partie C — Vérification quantitative et résultats des tests sur les 6 versions

### 1. Métriques quantitatives mesurées sur le code source
Les métriques ci-dessous ont été calculées automatiquement par analyse des 30 fichiers `.java` du projet (en comptant le nombre total de lignes, les lignes de code effectives hors commentaires et lignes vides, ainsi que les occurrences de `instanceof`, `return null;` et `throw new`) :

| Package | Fichiers `.java` | Lignes totales | Lignes de code (LOC hors commentaires) | Occurrences de `instanceof` | Occurrences de `return null;` | Occurrences de `throw new` (service + tests) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| `mohamed.initial` | 6 | 279 | 175 | 6 | 1 | 2 |
| `mohamed.eclipse` | 6 | 288 | 187 | **0** | 1 | 2 |
| `mohamed.llm` | 6 | 291 | 181 | **0** | 1 | 2 |
| `sacko.initial` | 4 | 314 | 209 | 0 | 4 | 4 |
| `sacko.eclipse` | 4 | 319 | 226 | 0 | **0** | **9** |
| `sacko.llm` | 4 | 331 | 230 | 0 | **0** | **10** |

*Remarque sur `return null;` dans le programme 1 (Mohamed) :* l'unique `return null;` conservé dans les trois versions de Mohamed est celui de `trouverPremierParNom` lorsqu'un employé recherché ne figure pas dans la liste (comportement normal testé par `Main`). En revanche, dans le programme 2 (Sacko), les 4 `return null;` qui masquaient des erreurs de réservation ont tous été éliminés (`0` dans `sacko.eclipse` et `sacko.llm`).

### 2. Résultats réels de compilation et d'exécution des 6 méthodes `main`
La commande `./scripts/verifier_tout.sh` exécute `mvn clean test-compile` (avec `--release 17`) puis lance les 6 classes `Main`. Toutes les traces sont enregistrées dans `preuves/resultats_execution.txt` :

1. **`mohamed.initial.Main`** : compile et réussit tous les tests (Alice : `1200.0 €`, Benoit : `0.0 €`, Chloé : `2500.0 €`, David : `1400.0 €`, Emma : `450.0 €`, Total : `5550.0 €`, recherche de Chloé et d'un employé inconnu).
2. **`mohamed.eclipse.Main`** : compile et réussit tous les tests avec **exactement les mêmes résultats numériques** (`1200.0`, `0.0`, `2500.0`, `1400.0`, `450.0`, Total : `5550.0 €`), sans aucun cast ni `instanceof`.
3. **`mohamed.llm.Main`** : compile et réussit tous les tests avec **exactement les mêmes résultats numériques** (`5550.0 €`), sans aucun cast ni `instanceof`.
4. **`sacko.initial.Main`** : compile et réussit tous les tests (création de `RES-1` et `RES-2`, test des codes `-1`, `-2`, `-3`, `0`, des retours `null` et des `IllegalArgumentException`, état final : `30` places dans `A101` après annulation et `1` réservation active).
5. **`sacko.eclipse.Main`** : compile et réussit tous les tests. Les scénarios d'erreur (salle `Z999` inconnue, capacité de `15` places demandée sur `10` disponibles dans `A101`, client vide, `0` personne, recherche et annulation de `RES-999`) lèvent tous les exceptions attendues (`IllegalArgumentException` et `IllegalStateException`), et l'état final des salles (`30` places dans `A101`, `1` réservation active) est identique.
6. **`sacko.llm.Main`** : compile et réussit tous les tests sur les mêmes scénarios fonctionnels et le même état final.

---

## Partie D — Bilan général : IDE (Eclipse) vs LLM

### 1. Ce que montre notre expérimentation sous Eclipse
- **Points forts d'Eclipse :**
  - Les opérations du menu `Refactor` (`Change Method Signature`, `Extract Method`, `Move Method`, `Pull Up`) sont sûres, prévisibles et offrent une fenêtre de prévisualisation (*Preview*) très claire qui montre l'impact exact sur chaque fichier du package.
  - En particulier, l'assistant **Pull Up** avec l'option *declare abstract in destination* s'est révélé très utile sur le programme 1 : il a transformé `Employe` en classe abstraite et créé automatiquement les méthodes `@Override` dans `Commercial` et `Manager`.
  - Sur le programme 2, après `Change Method Signature` (`int` -> `void`), la vérification syntaxique temps réel d'Eclipse a immédiatement signalé toutes les lignes de `ServiceReservation` et de `Main` qui utilisaient encore les anciens codes de retour.
- **Limites d'Eclipse :**
  - Eclipse ne propose pas d'automatisation complète pour les refactorings conceptuels comme *Replace Conditional with Polymorphism* ou *Replace Error Code with Exception*.
  - Lorsque l'on a plusieurs branches dans un `if / else if (instanceof)`, Eclipse ne sait pas fusionner automatiquement les différentes branches dans les sous-classes : après avoir utilisé `Extract Method` -> `Move` -> `Pull Up` sur la première branche (`Developpeur`), il faut recopier manuellement le corps des autres branches dans `Commercial` et `Manager` et supprimer soi-même le bloc `if / else if` dans `CalculateurPrime`.
  - De même, dans `Main`, la transformation d'un test `if (code == -1)` en bloc `try { ... } catch (IllegalArgumentException e) { ... }` doit être écrite manuellement.

### 2. Ce que montre notre expérimentation avec le LLM
- **Points forts du LLM :**
  - Le LLM comprend directement l'intention globale du refactoring : il réalise en une seule passe la création des méthodes abstraites, la répartition des formules dans les sous-classes, le nettoyage des conditions `instanceof`, ou encore le remplacement complet des codes d'erreur et des `null` par des exceptions avec mise à jour simultanée des tests dans `Main`.
  - Il maîtrise le vocabulaire du catalogue de Fowler et justifie ses choix de conception.
- **Limites et précautions avec le LLM :**
  - Le LLM a parfois tendance à pousser le refactoring plus loin que nécessaire (par exemple en déplaçant aussi `genererLigneBilan()` dans `Employe`, ce qui vide presque entièrement `CalculateurPrime` de sa logique, ou en omettant le test `if (employe == null)`).
  - Contrairement aux opérations atomiques d'Eclipse qui garantissent la validité syntaxique sur l'arbre de syntaxe abstraite (AST), le code généré par un LLM doit systématiquement être revalidé par compilation et par des tests de non-régression.

### 3. Conclusion
L'IDE Eclipse et le LLM ne s'opposent pas mais se complètent. Les concepts étudiés en cours (catalogue de Fowler, identification des *code smells*, préservation du comportement observable) restent indispensables dans les deux cas : ils permettent de décomposer proprement une restructuration dans Eclipse, et fournissent le vocabulaire précis ainsi que l'esprit critique nécessaires pour piloter et contrôler un LLM.
