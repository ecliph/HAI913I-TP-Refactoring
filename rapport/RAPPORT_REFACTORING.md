# Rapport de Refactoring — Partie 2 (Mise en œuvre pratique)

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Enseignante :** Marianne Huchard  
**Étudiants :**
- **TEDJNI MOHAMED**
- **SACKO DANGUE**

---

## Introduction et organisation du binôme

Dans cette seconde partie du TP, nous étudions concrètement comment corriger deux défauts de conception (*code smells* / *anti-patterns*) en comparant deux approches :
1. **L'utilisation des opérations de refactoring d'un IDE (Eclipse)**, complétées manuellement lorsque l'outil ne propose pas d'automatisation directe.
2. **L'utilisation d'un LLM**, sollicité pour refactorer intégralement le programme en nommant et expliquant les opérations appliquées.

Conformément au sujet (sections 2.1 et 2.2), nous avons réparti le travail en croisant les rôles :
- **Programme 1** : créé initialement par **TEDJNI MOHAMED** sur le défaut *"Code frequently casts to concrete types, breaking polymorphism and adding fragility"*, puis confié à **SACKO DANGUE** pour le refactoring sous Eclipse et avec le LLM.
- **Programme 2** : créé initialement par **SACKO DANGUE** sur le défaut *"Mixing return codes, nulls, and exceptions creates confusion and bugs"*, puis confié à **TEDJNI MOHAMED** pour le refactoring sous Eclipse et avec le LLM.

---

## Partie A — Programme 1 : Conversions explicites vers des types concrets

- **Auteur du programme initial :** TEDJNI MOHAMED (`fr.umontpellier.hai913i.refactoring.mohamed.initial`)
- **Auteur du refactoring :** SACKO DANGUE (`...mohamed.eclipse` et `...mohamed.llm`)

### 1. Défaut attribué
> *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*

### 2. Explication simple du défaut
En programmation orientée objet, lorsqu'une classe mère (`Employe`) possède plusieurs sous-classes (`Developpeur`, `Commercial`, `Manager`), le polymorphisme permet d'appeler une même méthode sur n'importe quel employé sans avoir à connaître sa sous-classe exacte.  
Le défaut apparaît lorsqu'une classe externe vérifie manuellement le type concret de l'objet avec l'opérateur `instanceof`, puis force sa conversion (*downcast*) vers la sous-classe concrète, par exemple `(Developpeur) employe`, pour accéder à ses méthodes spécifiques. On retrouve aussi ce défaut lorsqu'une méthode déclare un type de retour trop général (`Object`) alors qu'elle manipule un type plus précis (`Employe`), obligeant le code appelant à faire lui-même un cast `(Employe)`.

### 3. Présentation du programme initial
Le programme initial gère le calcul des primes annuelles et l'édition des lignes de bilan salarial pour une équipe d'employés :
- `Employe` : classe mère contenant le `nom` et le `salaireBase`.
- `Developpeur` : sous-classe ajoutant `nbProjetsTermines` et `langagePrincipal`.
- `Commercial` : sous-classe ajoutant `chiffreAffaires` et `tauxCommission`.
- `Manager` : sous-classe ajoutant `tailleEquipe` et `objectifsAtteints`.
- `CalculateurPrime` : classe réalisant les calculs et contenant volontairement le défaut dans trois méthodes :
  1. `calculerPrime(Employe employe)` : enchaîne trois `if / else if (employe instanceof ...)` suivis de casts explicites `(Developpeur)`, `(Commercial)` et `(Manager)`.
  2. `genererLigneBilan(Employe employe)` : répète exactement la même structure `instanceof` + cast pour formater la chaîne de caractères décrivant chaque employé.
  3. `trouverPremierParNom(List<Employe> employes, String nomRecherche)` : retourne `Object` au lieu de `Employe`, ce qui oblige `Main` à écrire `(Employe) calculateur.trouverPremierParNom(...)`.
- `Main` : teste 5 employés couvrant toutes les catégories et cas limites (0 projet, objectifs atteints ou non, employé trouvé ou absent).

### 4. Conséquences du défaut
- **Rupture du polymorphisme et couplage fort** : `CalculateurPrime` doit connaître toutes les sous-classes concrètes d'`Employe` et leurs détails internes (*Feature Envy* / *Switch Statements*, diapositive 26 du cours).
- **Fragilité lors des évolutions** : si l'on ajoute une nouvelle catégorie d'employé (par exemple `Stagiaire` ou `Technicien`), il faut retrouver et modifier toutes les méthodes contenant des chaînes de `if (employe instanceof ...)`. Si l'on en oublie une, le programme compile mais produit un résultat faux (prime à `0.0` par défaut).
- **Risque de `ClassCastException`** : multiplier les conversions explicites dans le code client augmente le risque d'erreur de cast à l'exécution.

### 5. Refactorings envisagés et leur intérêt
En nous appuyant sur le catalogue de Fowler présenté dans les diapositives du cours :
1. **Replace Conditional with Polymorphism** (diapositives 18 et 26) : remplacer les structures conditionnelles basées sur le type (`instanceof` + cast) par des méthodes polymorphes déclarées dans la classe mère `Employe` (`calculerPrime()` et `getDetailsCategorie()`) et redéfinies dans chaque sous-classe (`Developpeur`, `Commercial`, `Manager`).
2. **Move Method / Pull Up Method** (diapositives 13 et 21) : déplacer la logique propre aux employés dans la hiérarchie `Employe`. La génération de la ligne de bilan (`genererLigneBilan()`) peut ainsi être placée dans `Employe` en s'appuyant sur `calculerPrime()` et `getDetailsCategorie()`.
3. **Encapsulate Downcast** (diapositive 19) : modifier le type de retour de `trouverPremierParNom()` pour qu'elle retourne directement `Employe` au lieu de `Object`, supprimant ainsi le cast `(Employe)` dans `Main`.

**Intérêt :** chaque sous-classe devient responsable de sa propre formule de prime et de sa description. `CalculateurPrime` ne contient plus aucun `instanceof` ni aucun cast, et l'ajout futur d'une nouvelle sous-classe d'`Employe` ne nécessite plus de modifier `CalculateurPrime`.

### 6. Opérations proposées pour Eclipse
Dans Eclipse (menu `Refactor`), toutes ces transformations ne sont pas automatisées en une seule commande :
- **Opérations disponibles dans le menu `Refactor` d'Eclipse :**
  - `Extract Method` (`Alt+Shift+M`) : permet d'isoler chaque branche du `if (employe instanceof ...)` dans une méthode dédiée prenant directement le type concret ou d'extraire le formatage spécifique.
  - `Move...` (`Alt+Shift+V`) : une fois une méthode extraite avec un paramètre de type `Developpeur`, `Commercial` ou `Manager`, Eclipse sait la déplacer automatiquement dans la sous-classe correspondante.
  - `Pull Up...` : permet de faire remonter une déclaration de méthode (ou de la déclarer abstraite) d'une sous-classe vers la classe mère `Employe`.
  - `Change Method Signature...` (`Alt+Shift+C`) : permet de changer le type de retour de `trouverPremierParNom` de `Object` vers `Employe`.
- **Opérations non automatisées directement par Eclipse :**
  - Eclipse ne possède pas de commande automatique *"Replace Conditional with Polymorphism"* ni *"Encapsulate Downcast"*.
  - La suppression du bloc conditionnel `if / else if (instanceof)` et des variables castées (`Developpeur dev = (Developpeur) employe;`), ainsi que le remplacement par l'appel polymorphe `employe.calculerPrime()`, doivent être achevés à la main en combinant `Extract Method`, `Move`, `Pull Up` et une édition manuelle.

*(La procédure exacte pas à pas est détaillée dans `rapport/GUIDE_ECLIPSE.md`.)*

### 7. Opérations réellement réalisées par le LLM
Dans le package `fr.umontpellier.hai913i.refactoring.mohamed.llm`, le LLM a appliqué les transformations suivantes :
1. Transformation de `Employe` en classe abstraite (`public abstract class Employe`) avec ajout des signatures abstraites `public abstract double calculerPrime();` et `public abstract String getDetailsCategorie();`.
2. Ajout de la méthode commune `genererLigneBilan()` dans `Employe`, factorisant l'assemblage `getDetailsCategorie() + " -> Salaire : ... Prime : ... Total : ..."`.
3. Redéfinition (`@Override`) de `calculerPrime()` et `getDetailsCategorie()` dans `Developpeur`, `Commercial` et `Manager` en utilisant directement leurs attributs privés, sans aucun cast.
4. Simplification de `CalculateurPrime` : `calculerPrime(Employe)` appelle `employe.calculerPrime()`, et `genererLigneBilan(Employe)` appelle `employe.genererLigneBilan()`. Tous les `instanceof` et tous les casts vers les sous-classes ont disparu.
5. Application de *Encapsulate Downcast* sur `trouverPremierParNom(...)` dont le type de retour passe de `Object` à `Employe`.
6. Suppression du cast `(Employe)` aux lignes d'appel de `trouverPremierParNom` dans `Main.java`.

### 8. Résultats des tests et modifications de `Main` (Programme 1)
- **Modification de `Main` :** la seule modification dans `mohamed.llm.Main` est la suppression du cast devenu inutile `(Employe)` devant `calculateur.trouverPremierParNom(...)` :
  - Avant : `Employe trouve = (Employe) calculateur.trouverPremierParNom(employes, "Chloé Bernard");`
  - Après : `Employe trouve = calculateur.trouverPremierParNom(employes, "Chloé Bernard");`
- **Résultats observables :** toutes les lignes de bilan affichées, toutes les primes individuelles (`1200.0`, `0.0`, `2500.0`, `1400.0`, `450.0`), le total des primes (`5550.0 €`) et les résultats de recherche sont **strictement identiques** entre la version initiale et la version LLM (voir `preuves/resultats_execution.txt`).

### 9. Retour d'expérience sur la manipulation sous Eclipse (Programme 1)
> **À compléter après manipulation Eclipse par SACKO DANGUE :**
> - *Indiquer ici si les étapes `Extract Method` -> `Move` -> `Pull Up` ont fonctionné comme prévu dans Eclipse.*
> - *Préciser quelles parties ont dû être modifiées à la main.*
> - *Joindre les références aux captures d'écran placées dans `preuves/eclipse/mohamed/`.*
> - *Confirmer que l'exécution de `mohamed.eclipse.Main` après refactoring affiche `[SUCCÈS]`.*

---

## Partie B — Programme 2 : Mélange incohérent de codes de retour, `null` et exceptions

- **Auteur du programme initial :** SACKO DANGUE (`fr.umontpellier.hai913i.refactoring.sacko.initial`)
- **Auteur du refactoring :** TEDJNI MOHAMED (`...sacko.eclipse` et `...sacko.llm`)

### 1. Défaut attribué
> *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*

### 2. Explication simple du défaut
Lorsqu'une opération échoue dans un programme Java (paramètre invalide, ressource introuvable, règle métier non respectée), le concepteur doit choisir une convention claire pour signaler l'erreur.  
Le défaut consiste à mélanger dans une même classe trois mécanismes différents :
- retourner un **code entier spécial** (`-1`, `-2`, `-3` pour les erreurs, `0` pour le succès) ;
- retourner **`null`** lorsqu'un problème survient ;
- lever une **exception** (`IllegalArgumentException`) pour d'autres erreurs.

L'appelant ne sait plus comment tester le bon déroulement d'un appel : il doit à la fois entourer ses appels de `try / catch`, vérifier si le résultat vaut `null` et comparer des entiers avec des constantes d'erreur.

### 3. Présentation du programme initial
Le programme initial gère la réservation de salles à l'université :
- `Salle` : représente une salle avec son `code`, sa `capaciteMax` et ses `placesOccupees`.
- `Reservation` : représente une réservation validée (`reference`, `nomClient`, `codeSalle`, `nbPersonnes`).
- `ServiceReservation` : centralise les opérations et contient volontairement le mélange des trois mécanismes d'erreur :
  1. **Exceptions** : si `nomClient` est vide ou si `nbPersonnes <= 0`, `verifierEtReserver` lève une `IllegalArgumentException`.
  2. **Codes de retour entiers** : `verifierEtReserver` retourne `-1` (`ERREUR_SALLE_INTROUVABLE`) si la salle n'existe pas, `-2` (`ERREUR_CAPACITE_INSUFFISANTE`) s'il manque des places, et `0` (`SUCCES`) si tout va bien. De même, `annulerReservation` retourne `-3` (`ERREUR_RESERVATION_INTROUVABLE`) ou `0`.
  3. **Retours `null`** : `creerReservation` appelle `verifierEtReserver` et convertit tout code différent de `0` en retour `null` (sans préciser si l'échec vient de la salle ou de la capacité). `chercherReservation` retourne également `null` si la référence est vide ou introuvable.
- `Main` : teste les réservations valides, une salle inconnue, un dépassement de capacité, un nom de client vide, un nombre de personnes nul, ainsi que la recherche et l'annulation d'une réservation existante ou inexistante.

### 4. Conséquences du défaut
- **Confusion et perte d'information** : lorsque `creerReservation` retourne `null`, l'appelant a perdu la cause exacte de l'erreur (était-ce une salle inexistante `-1` ou une capacité insuffisante `-2` ?).
- **Risque élevé de bugs (`NullPointerException` ou erreurs silencieuses)** : si le développeur appelant oublie de tester le code de retour d'`annulerReservation` ou oublie un `if (res != null)` après `creerReservation`, le programme continue son exécution dans un état incohérent ou plante plus loin.
- **Code client lourd et répétitif** : `Main` est obligé de combiner trois styles de vérification différents.

### 5. Refactorings envisagés et leur intérêt
En nous appuyant sur les diapositives du cours :
1. **Replace Error Code with Exception** (diapositive 20 : catégorie *Making Method Calls Simpler*) :
   - Remplacer les retours de codes entiers (`-1`, `-2`, `-3`, `0`) dans `verifierEtReserver` et `annulerReservation` par un type de retour `void` et le lancement d'exceptions lorsqu'une condition d'erreur est détectée.
   - Remplacer les retours `null` d'erreur dans `creerReservation`, `chercherReservation` et `trouverSalle` par des exceptions explicites.
2. **Choix d'exceptions standards de Java** (sans ajouter d'architecture complexe d'exceptions personnalisées) :
   - `IllegalArgumentException` : lorsque les arguments fournis sont invalides ou font référence à un identifiant inexistant (nom vide, nombre de personnes `<= 0`, code salle introuvable, référence de réservation introuvable).
   - `IllegalStateException` : lorsque la salle existe et que les arguments sont syntaxiquement valides, mais que l'état courant de la salle (places disponibles insuffisantes) ne permet pas d'accepter la réservation.

**Intérêt :** le contrat de `ServiceReservation` devient uniforme. Soit une méthode réussit et renvoie toujours un objet valide (ou se termine normalement si elle est `void`), soit elle lève une exception portant un message explicite sur la cause de l'échec.

### 6. Opérations proposées pour Eclipse
- **Opérations disponibles dans le menu `Refactor` d'Eclipse :**
  - `Change Method Signature...` (`Alt+Shift+C`) : permet de modifier le type de retour de `verifierEtReserver` et `annulerReservation` de `int` à `void`.
- **Opérations non automatisées directement par Eclipse :**
  - Eclipse ne propose pas d'opération automatique *"Replace Error Code with Exception"*.
  - Lorsque l'on change le type de retour de `int` à `void` avec `Change Method Signature`, Eclipse met à jour la signature mais signale ensuite des erreurs de compilation sur les instructions `return ERREUR_...;` et sur les affectations `int code = service.verifierEtReserver(...);` dans `Main`.
  - Il faut donc remplacer manuellement les `return ERREUR_...;` et `return null;` par des instructions `throw new IllegalArgumentException(...)` ou `throw new IllegalStateException(...)`, supprimer les constantes entières devenues inutiles, et adapter les appels dans `Main` avec des blocs `try / catch`.

*(La procédure détaillée étape par étape figure dans `rapport/GUIDE_ECLIPSE.md`.)*

### 7. Opérations réellement réalisées par le LLM
Dans le package `fr.umontpellier.hai913i.refactoring.sacko.llm`, le LLM a effectué les transformations suivantes :
1. Suppression des quatre constantes de codes de retour (`SUCCES`, `ERREUR_SALLE_INTROUVABLE`, `ERREUR_CAPACITE_INSUFFISANTE`, `ERREUR_RESERVATION_INTROUVABLE`).
2. Modification de `trouverSalle(String)` et `chercherReservation(String)` pour qu'elles lèvent une `IllegalArgumentException` avec un message précis lorsque le code ou la référence est vide ou introuvable, au lieu de retourner `null`.
3. Application de *Replace Error Code with Exception* sur `creerReservation` et `verifierEtReserver` :
   - `verifierEtReserver` passe d'un type de retour `int` à `void` et délègue à `creerReservation`.
   - Si la capacité disponible est inférieure au nombre de personnes demandé, `creerReservation` lève désormais une `IllegalStateException` explicite au lieu de renvoyer `-2` ou `null`.
   - En cas de succès, `creerReservation` retourne directement l'objet `Reservation` nouvellement créé (jamais `null`).
4. Application de *Replace Error Code with Exception* sur `annulerReservation(String)` : le type de retour passe de `int` à `void`, et l'appel interne à `chercherReservation(reference)` lève automatiquement une `IllegalArgumentException` si la réservation à annuler n'existe pas.

### 8. Résultats des tests et modifications de `Main` (Programme 2)
- **Modification de `Main` :** contrairement au programme 1 où la signature publique ne changeait presque pas, l'opération *Replace Error Code with Exception* modifie volontairement le contrat public de `ServiceReservation` (`int` -> `void`, et exceptions au lieu de codes `-1/-2/-3` ou de `null`).  
  La méthode `main` de `sacko.llm.Main` a donc été adaptée pour vérifier la capture des exceptions (`try / catch (IllegalArgumentException e)` et `try / catch (IllegalStateException e)`) sur les cas d'erreur 2, 3 et 5, tout en conservant **exactement les mêmes scénarios fonctionnels et les mêmes données de test** (`A101`, `B202`, `Z999`, `RES-1`, `RES-2`, `RES-999`).
- **Résultats observables :** l'état des salles (`10` places restantes dans `A101`, `2` dans `B202`, puis retour à `30` places dans `A101` après annulation de `RES-1`) et le nombre final de réservations actives (`1`) sont **strictement identiques** entre la version initiale et la version LLM (voir `preuves/resultats_execution.txt`).

### 9. Retour d'expérience sur la manipulation sous Eclipse (Programme 2)
> **À compléter après manipulation Eclipse par TEDJNI MOHAMED :**
> - *Indiquer ici l'utilisation de `Change Method Signature` dans Eclipse pour passer les méthodes de `int` à `void`.*
> - *Expliquer comment les erreurs de compilation signalées par Eclipse ont guidé le remplacement manuel des `return -1/-2/-3` et `return null` par des `throw new ...` et la mise à jour de `Main`.*
> - *Joindre les références aux captures d'écran placées dans `preuves/eclipse/sacko/`.*
> - *Confirmer que l'exécution de `sacko.eclipse.Main` après refactoring affiche `[SUCCÈS]`.*

---

## Partie C — Comparaison structurée : Refactoring sous Eclipse vs Refactoring par LLM

### 1. Tableau comparatif

| Critère de comparaison | Refactoring assisté par l'IDE (Eclipse) | Refactoring intégral par un LLM |
| :--- | :--- | :--- |
| **Nature des opérations prises en charge** | Très efficace sur les refactorings structurels atomiques (*Rename*, *Extract Method*, *Move Method*, *Pull Up*, *Change Method Signature*). | Capable de réaliser des refactorings conceptuels ou composés (*Replace Conditional with Polymorphism*, *Replace Error Code with Exception*) en une seule étape. |
| **Compréhension de l'intention métier** | Aucune : l'IDE manipule l'arbre syntaxique (AST) selon les instructions explicites de l'utilisateur. | Élevée : le LLM identifie le *code smell*, choisit l'exception standard appropriée (`IllegalStateException` vs `IllegalArgumentException`) et reformule les méthodes. |
| **Garantie de préservation syntaxique** | Très forte sur les opérations natives : Eclipse vérifie les préconditions et met à jour les références automatiquement. | Non garantie par construction : dépend de la vérification par compilation (`mvn test-compile`) et par exécution des tests (`Main`). |
| **Adaptation du code appelant (`Main`)** | Partielle : `Change Method Signature` met à jour l'appel, mais ne transforme pas automatiquement un test `if (code == -1)` en bloc `try / catch`. | Complète : le LLM réécrit simultanément la classe refactorée et adapte les tests dans `Main` au nouveau mécanisme d'exception. |
| **Effort demandé au développeur** | Effort de décomposition en petites étapes atomiques + édition manuelle pour les parties non automatisées. | Effort de spécification (prompt précis), de relecture critique et de validation par les tests. |

### 2. Avantages et limites de l'approche Eclipse
- **Avantages :**
  - Les transformations atomiques du menu `Refactor` (`Move`, `Pull Up`, `Extract Method`, `Change Method Signature`) sont déterministes et sûres.
  - Le compilateur incrémental d'Eclipse signale immédiatement chaque ligne impactée par un changement de signature, ce qui guide le développeur pas à pas.
  - Favorise une bonne compréhension de la mécanique interne du refactoring (comme décrit par Fowler).
- **Inconvénients et limites :**
  - Plusieurs refactorings classiques du cours (*Replace Conditional with Polymorphism*, *Encapsulate Downcast*, *Replace Error Code with Exception*) n'existent pas sous forme de bouton unique dans le menu `Refactor`.
  - Pour passer d'une chaîne de `if (instanceof)` au polymorphisme, il faut enchaîner manuellement plusieurs opérations (`Extract Method`, puis `Move`, puis `Pull Up`) et nettoyer soi-même la condition résiduelle.

### 3. Avantages et limites de l'approche LLM
- **Avantages :**
  - Rapidité et vision globale : le LLM comprend immédiatement le lien entre le *code smell* et la cible de conception (polymorphisme ou unification des exceptions).
  - Il réalise de bout en bout les refactorings composés qui demandent à la fois de créer des méthodes abstraites, de déplacer la logique dans plusieurs sous-classes et de nettoyer la classe appelante.
  - Il explique clairement les transformations réalisées en utilisant le vocabulaire du catalogue de Fowler.
- **Inconvénients et limites :**
  - Comme souligné dans les diapositives du cours (diapositives 4, 7 et 30 — travaux de Liu et al. 2025, Xu et al. 2026), un LLM n'offre pas de garantie formelle de préservation du comportement : il peut modifier involontairement un format d'affichage ou une règle de calcul si l'on ne vérifie pas son code.
  - Il est indispensable de disposer de tests déterministes (comme nos vérifications dans `Main`) et de relire chaque modification pour s'assurer qu'aucune fonctionnalité n'a été altérée.

### 4. Conclusion générale
Ce TP montre que les deux approches sont complémentaires :
- **Eclipse** apporte la rigueur mécanique et la sécurité sur les opérations élémentaires de restructuration.
- **Le LLM** permet d'automatiser des refactorings de plus haut niveau que l'IDE ne sait pas exécuter seul (comme le passage au polymorphisme ou l'harmonisation de la gestion d'erreurs par exceptions), à condition que le développeur maîtrise les concepts du refactoring pour guider l'outil, contrôler les changements de signatures publiques et valider la préservation du comportement par des tests.
