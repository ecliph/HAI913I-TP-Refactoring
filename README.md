# HAI913I — TP de Refactoring (Partie 2 : Mise en œuvre pratique)

**Master 2 Informatique — Parcours ICO — Université de Montpellier (2026)**  
**Module :** HAI913I — Évolution et Restructuration Logicielle (Enseignante : Marianne Huchard)  
**Binôme :**
- **TEDJNI MOHAMED**
- **SACKO DANGUE**

---

## 1. Objectif de la partie 2

Ce projet correspond à la **partie 2 (Mise en œuvre pratique)** du TP de refactoring.  
L'objectif est de comparer les opérations de refactoring offertes nativement par l'IDE **Eclipse** avec celles réalisées par un **LLM** sur deux programmes Java présentant chacun un défaut de conception (*code smell* / *anti-pattern*).

Conformément aux consignes du sujet (sections 2.1 et 2.2) :
1. Chaque étudiant crée un programme Java fonctionnel contenant volontairement le défaut qui lui a été attribué, accompagné d'une méthode `main` testant les différents cas.
2. Les deux étudiants échangent ensuite leurs programmes : chacun applique les opérations de refactoring sur le programme de son binôme :
   - **(a) Avec Eclipse** (en utilisant au maximum le menu `Refactor` d'Eclipse et en complétant manuellement ce que l'IDE n'automatise pas).
   - **(b) Avec un LLM** (qui réalise intégralement la correction du défaut).

---

## 2. Attribution des défauts et répartition du travail

| Étudiant ayant créé le code initial | Défaut attribué (*AffectationSmells.xlsx*) | Scénario Java illustrant le défaut | Étudiant réalisant le refactoring (Eclipse & LLM) |
| :--- | :--- | :--- | :--- |
| **TEDJNI MOHAMED** | *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."* | Calcul de primes et de bilans d'employés (`Employe`, `Developpeur`, `Commercial`, `Manager`, `CalculateurPrime`) | **SACKO DANGUE** |
| **SACKO DANGUE** | *"Mixing return codes, nulls, and exceptions creates confusion and bugs."* | Gestion de réservations de salles (`Salle`, `Reservation`, `ServiceReservation`) | **TEDJNI MOHAMED** |

---

## 3. Structure du projet

Le projet utilise une structure Maven standard compatible Java 17+ et directement importable dans Eclipse :

```text
HAI913I-TP-Refactoring/
├── pom.xml                                  # Configuration Maven (Java 17, aucune dépendance externe)
├── README.md                                # Présentation générale et instructions d'utilisation
├── .gitignore                               # Exclusion de target/, docs/ et fichiers locaux IDE
├── scripts/
│   └── verifier_tout.sh                     # Script compilant et exécutant les 6 classes Main
├── rapport/
│   ├── RAPPORT_REFACTORING.md               # Rapport complet d'analyse et de comparaison Eclipse vs LLM
│   ├── GUIDE_ECLIPSE.md                     # Guide pas à pas pour réaliser les refactorings dans Eclipse
│   └── UTILISATION_IA.md                    # Documentation sur l'usage du LLM, prompts et vérifications
├── preuves/
│   ├── resultats_execution.txt              # Trace réelle de compilation et d'exécution des 6 programmes
│   └── eclipse/
│       ├── CONSIGNES_CAPTURES.md            # Liste des captures d'écran à prendre sous Eclipse
│       ├── mohamed/                         # Dossier pour les captures du refactoring du code de Mohamed
│       └── sacko/                           # Dossier pour les captures du refactoring du code de Sacko
└── src/main/java/fr/umontpellier/hai913i/refactoring/
    ├── mohamed/
    │   ├── initial/                         # Version initiale avec défaut (créée par Mohamed)
    │   ├── llm/                             # Version corrigée par le LLM (refactoring par Sacko)
    │   └── eclipse/                         # Version préparée pour refactoring dans Eclipse (par Sacko)
    └── sacko/
        ├── initial/                         # Version initiale avec défaut (créée par Sacko)
        ├── llm/                             # Version corrigée par le LLM (refactoring par Mohamed)
        └── eclipse/                         # Version préparée pour refactoring dans Eclipse (par Mohamed)
```

---

## 4. Statut réel de chaque version

| Package Java | Statut actuel | Description |
| :--- | :--- | :--- |
| `...refactoring.mohamed.initial` | **Terminé et testé** | Contient volontairement les tests `instanceof` et les conversions explicites (`(Developpeur)`, `(Commercial)`, `(Manager)`, `(Employe)`). |
| `...refactoring.mohamed.llm` | **Terminé et testé** | Refactoré intégralement par le LLM (*Replace Conditional with Polymorphism*, *Move Method*, *Pull Up Method*, *Encapsulate Downcast*). |
| `...refactoring.mohamed.eclipse` | **Prêt pour manipulation Eclipse** | Copie fonctionnelle du code initial prête à être refactorée manuellement dans Eclipse par SACKO DANGUE en suivant `rapport/GUIDE_ECLIPSE.md`. |
| `...refactoring.sacko.initial` | **Terminé et testé** | Contient volontairement le mélange de codes d'erreur (`0`, `-1`, `-2`, `-3`), de retours `null` et d'exceptions (`IllegalArgumentException`). |
| `...refactoring.sacko.llm` | **Terminé et testé** | Refactoré intégralement par le LLM (*Replace Error Code with Exception*, harmonisation par exceptions standards). |
| `...refactoring.sacko.eclipse` | **Prêt pour manipulation Eclipse** | Copie fonctionnelle du code initial prête à être refactorée manuellement dans Eclipse par TEDJNI MOHAMED en suivant `rapport/GUIDE_ECLIPSE.md`. |

---

## 5. Prérequis, compilation et exécution en ligne de commande

### Prérequis
- **JDK 17** ou supérieur (testé avec OpenJDK).
- **Apache Maven 3.8+**.

### Compilation avec Maven
À la racine du projet :
```bash
mvn clean test-compile
```

### Exécution individuelle de chaque programme `Main`
```bash
# Sujet 1 (Mohamed) : conversions explicites vers des types concrets
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.initial.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.llm.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.eclipse.Main

# Sujet 2 (Sacko) : mélange codes de retour, null et exceptions
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.initial.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.llm.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.eclipse.Main
```

### Exécution automatique de toutes les vérifications
Un script shell compile l'ensemble du projet, exécute les 6 méthodes `main` et enregistre la sortie console dans `preuves/resultats_execution.txt` :
```bash
./scripts/verifier_tout.sh
```

---

## 6. Procédure d'ouverture et d'exécution sous Eclipse

1. Ouvrir **Eclipse IDE for Java Developers**.
2. Dans le menu principal, cliquer sur **File → Import...**.
3. Dérouler la catégorie **Maven**, sélectionner **Existing Maven Projects**, puis cliquer sur **Next >**.
4. Dans **Root Directory**, cliquer sur **Browse...** et sélectionner le dossier racine de ce projet (`HAI913I-TP-Refactoring`).
5. Vérifier que le fichier `pom.xml` est coché dans la liste, puis cliquer sur **Finish**.
6. Pour exécuter l'une des 6 versions :
   - Dans l'explorateur **Package Explorer**, dérouler `src/main/java` puis le package souhaité (par exemple `fr.umontpellier.hai913i.refactoring.mohamed.initial`).
   - Faire un **clic droit sur `Main.java` → Run As → Java Application**.
   - Vérifier les résultats affichés dans la vue **Console** d'Eclipse.

---

## 7. Étapes restant à effectuer manuellement par le binôme

1. Importer le projet Maven dans Eclipse.
2. Suivre le guide détaillé [`rapport/GUIDE_ECLIPSE.md`](rapport/GUIDE_ECLIPSE.md) pour appliquer les opérations de refactoring dans les packages `.eclipse` (`mohamed.eclipse` et `sacko.eclipse`).
3. Prendre les captures d'écran demandées à chaque étape et les enregistrer dans `preuves/eclipse/mohamed/` et `preuves/eclipse/sacko/` en suivant [`preuves/eclipse/CONSIGNES_CAPTURES.md`](preuves/eclipse/CONSIGNES_CAPTURES.md).
4. Compléter les sections marquées *"À compléter après manipulation Eclipse"* dans [`rapport/RAPPORT_REFACTORING.md`](rapport/RAPPORT_REFACTORING.md).
