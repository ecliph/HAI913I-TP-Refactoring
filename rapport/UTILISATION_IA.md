# Documentation sur l'utilisation de l'IA Générative (LLM)

**Module :** HAI913I — Évolution et Restructuration Logicielle (Master 2 Informatique ICO — Université de Montpellier)  
**Enseignante :** Marianne Huchard  
**Étudiants :**
- **TEDJINI MOHAMED**
- **SACKO DANGUE**

Conformément aux consignes des sections 2.1 et 2.2 du sujet de TP, ce document précise l'outil d'IA utilisé, son rôle dans la création des programmes initiaux et des versions refactorées `.llm`, les prompts employés, ainsi que notre analyse critique et les vérifications effectuées.

---

## 1. Outil et modèle utilisés

- **Environnement :** Antigravity (environnement de développement basé sur VS Code sous Ubuntu Linux).
- **Modèle LLM utilisé :** Assistant IA Antigravity (modèle Gemini 4 Argon Medium).

---

## 2. Rôle de l'IA dans notre travail

L'IA a été sollicitée pour deux tâches distinctes prévues par l'énoncé :

1. **Section 2.1 — Aide à la création des deux programmes initiaux contenant les défauts attribués :**
   - Pour **TEDJINI MOHAMED** : génération du programme initial `fr.umontpellier.hai913i.refactoring.mohamed.initial` illustrant le défaut *"Code frequently casts to concrete types, breaking polymorphism and adding fragility"* autour d'un système de calcul de primes d'employés (`Employe`, `Developpeur`, `Commercial`, `Manager`, `CalculateurPrime`, `Main`).
   - Pour **SACKO DANGUE** : génération du programme initial `fr.umontpellier.hai913i.refactoring.sacko.initial` illustrant le défaut *"Mixing return codes, nulls, and exceptions creates confusion and bugs"* autour d'un système de réservation de salles (`Salle`, `Reservation`, `ServiceReservation`, `Main`).

2. **Section 2.2 (point 2) — Refactoring intégral avec un LLM :**
   - Génération de la version refactorée `fr.umontpellier.hai913i.refactoring.mohamed.llm` à partir du programme initial de Mohamed.
   - Génération de la version refactorée `fr.umontpellier.hai913i.refactoring.sacko.llm` à partir du programme initial de Sacko.

> **Important :** Les versions situées dans les packages `.eclipse` n'ont **pas** été refactorées par l'IA ; elles contiennent une copie du code initial afin que chaque étudiant réalise lui-même les opérations de refactoring dans l'IDE Eclipse en suivant `rapport/GUIDE_ECLIPSE.md`.

---

## 3. Synthèse des prompts fournis au LLM

### A. Prompt de création et cadrage général du projet
Nous avons fourni au LLM le contexte complet du TP :
- Nos deux noms (**TEDJINI MOHAMED** et **SACKO DANGUE**) et les trois documents de référence du cours (`TP_refactoring_2026.pdf`, `2026_diaposRefactoring.pdf`, `AffectationSmells.xlsx`).
- Les deux défauts exacts attribués dans le fichier Excel :
  - Mohamed : *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."*
  - Sacko : *"Mixing return codes, nulls, and exceptions creates confusion and bugs."*
- L'exigence d'un code Java 17 simple, lisible, sans framework externe, compréhensible par des étudiants de Master 2, avec des méthodes `main` testant des cas normaux et des cas limites de manière déterministe.
- L'exigence de s'appuyer explicitement sur les opérations du catalogue de Fowler présentées dans les diapositives du cours :
  - *Replace Conditional with Polymorphism*, *Move Method*, *Pull Up Method*, *Encapsulate Downcast* pour le sujet de Mohamed.
  - *Replace Error Code with Exception* pour le sujet de Sacko.

### B. Demande de refactoring adressée au LLM pour le programme 1 (Mohamed -> refactoré par Sacko)
> *"À partir du programme initial `fr.umontpellier.hai913i.refactoring.mohamed.initial`, réalise intégralement la correction du code smell 'Code frequently casts to concrete types, breaking polymorphism and adding fragility' dans le package `fr.umontpellier.hai913i.refactoring.mohamed.llm`. Indique quels refactorings du catalogue de Fowler tu appliques, préserve le comportement observable et vérifie que `Main` fonctionne."*

**Réponse et opérations appliquées par le LLM :**
- **Replace Conditional with Polymorphism** : transformation de `Employe` en classe abstraite déclarant `calculerPrime()` et `getDetailsCategorie()`, redéfinies dans `Developpeur`, `Commercial` et `Manager`.
- **Move Method / Pull Up Method** : déplacement de la construction de la ligne de bilan `genererLigneBilan()` dans `Employe`, ce qui élimine les deux chaînes de `if (employe instanceof ...)` et tous les casts `(Developpeur)`, `(Commercial)`, `(Manager)` dans `CalculateurPrime`.
- **Encapsulate Downcast** : spécialisation du type de retour de `trouverPremierParNom` de `Object` vers `Employe`, ce qui élimine le cast `(Employe)` dans `Main`.

### C. Demande de refactoring adressée au LLM pour le programme 2 (Sacko -> refactoré par Mohamed)
> *"À partir du programme initial `fr.umontpellier.hai913i.refactoring.sacko.initial`, réalise intégralement la correction du code smell 'Mixing return codes, nulls, and exceptions creates confusion and bugs' dans le package `fr.umontpellier.hai913i.refactoring.sacko.llm` en t'appuyant sur 'Replace Error Code with Exception'. N'ajoute pas une hiérarchie complexe d'exceptions personnalisées si les exceptions standards de Java suffisent. Indique les refactorings appliqués et adapte `Main` aux nouvelles signatures."*

**Réponse et opérations appliquées par le LLM :**
- **Replace Error Code with Exception** : suppression des constantes entières `0`, `-1`, `-2`, `-3` et passage des méthodes `verifierEtReserver` et `annulerReservation` d'un type de retour `int` à `void` (*Change Method Signature*).
- **Élimination des retours `null` d'erreur** dans `trouverSalle`, `creerReservation` et `chercherReservation`.
- **Harmonisation avec les exceptions standards Java** :
  - `IllegalArgumentException` pour tout paramètre invalide ou identifiant introuvable (nom vide, nombre de personnes `<= 0`, salle inexistante, référence de réservation inexistante).
  - `IllegalStateException` lorsque la capacité disponible de la salle est insuffisante.

---

## 4. Analyse critique : avons-nous eu à modifier ce qu'a donné le LLM ?

### Pour la création des programmes initiaux (Section 2.1)
- Le LLM a généré du premier coup deux programmes initiaux qui compilent et s'exécutent sans erreur avec Java 17.
- **Vérification importante sur le programme de Mohamed :** nous avons vérifié que le programme initial contenait à la fois des casts liés à des `instanceof` sur les sous-classes (`Developpeur`, `Commercial`, `Manager`) **et** un cas de downcast sur un type de retour (`Object` -> `Employe` dans `trouverPremierParNom`), afin d'illustrer à la fois *Replace Conditional with Polymorphism* et *Encapsulate Downcast* (diapositives 18 et 19 du cours).
- **Vérification importante sur le programme de Sacko :** nous avons vérifié que les trois mécanismes cités dans l'intitulé du smell (*return codes*, *nulls*, *exceptions*) étaient bien présents simultanément dans `ServiceReservation` et testés explicitement dans `Main`.

### Pour le refactoring par le LLM (Section 2.2)
- **Sur le programme de Mohamed (`mohamed.llm`) :** le refactoring produit par le LLM n'a nécessité aucune retouche manuelle corrective. La sortie console des bilans et des primes est au caractère près identique à celle du programme initial, et seule la conversion explicite `(Employe)` a été retirée dans `Main` grâce à *Encapsulate Downcast*.
- **Sur le programme de Sacko (`sacko.llm`) :** le refactoring *Replace Error Code with Exception* change par nature la signature publique des méthodes qui retournaient un code entier (`int` -> `void`) et remplace les retours `null` par des exceptions. Le LLM a correctement adapté la classe `Main` pour capturer `IllegalArgumentException` et `IllegalStateException` avec des blocs `try / catch` tout en conservant exactement les mêmes cas de test et les mêmes vérifications d'état sur les salles.

---

## 5. Vérifications effectuées

Toutes les versions ont été compilées avec Maven (`mvn clean test-compile` avec `--release 17`) et exécutées via le script `./scripts/verifier_tout.sh`.  
Les traces complètes et authentiques d'exécution sont enregistrées dans [`preuves/resultats_execution.txt`](../preuves/resultats_execution.txt).

