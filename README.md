# HAI913I — TP de Refactoring (Partie 2 : Mise en œuvre pratique)

**Master 2 Informatique — Parcours ICO — Université de Montpellier (2026)**  
**Module :** HAI913I — Évolution et Restructuration Logicielle (Enseignante : Marianne Huchard)  
**Étudiants :**
- **TEDJINI MOHAMED**
- **SACKO DANGUE**

---

## 1. Objectif de la partie 2

Ce projet correspond à la **partie 2 (Mise en œuvre pratique)** du TP de refactoring.  
L'objectif est de comparer les opérations de refactoring offertes nativement par l'IDE **Eclipse** (complétées manuellement lorsque l'IDE n'automatise pas une transformation) avec celles réalisées par un **LLM** sur deux programmes Java présentant chacun un défaut de conception (*code smell* / *anti-pattern*).

Conformément aux consignes du sujet (sections 2.1 et 2.2), nous disposons pour chacun des deux défauts attribués :
1. D'un **programme Java initial** fonctionnel contenant volontairement le défaut et d'une méthode `main` testant les différents cas (`.initial`).
2. D'une **version refactorée sous Eclipse** en combinant le menu `Refactor` d'Eclipse et les adaptations manuelles nécessaires (`.eclipse`), documentée par des captures d'écran étape par étape.
3. D'une **version refactorée intégralement par un LLM** (`.llm`).

---

## 2. Attribution officielle des sujets (*AffectationSmells.xlsx*)

| Étudiant associé au sujet | Défaut attribué (*AffectationSmells.xlsx*) | Scénario Java illustrant le défaut | Packages Java |
| :--- | :--- | :--- | :--- |
| **TEDJINI MOHAMED** | *"Code frequently casts to concrete types, breaking polymorphism and adding fragility."* | Calcul de primes et de bilans d'employés (`Employe`, `Developpeur`, `Commercial`, `Manager`, `CalculateurPrime`) | `...refactoring.mohamed.{initial,eclipse,llm}` |
| **SACKO DANGUE** | *"Mixing return codes, nulls, and exceptions creates confusion and bugs."* | Gestion de réservations de salles (`Salle`, `Reservation`, `ServiceReservation`) | `...refactoring.sacko.{initial,eclipse,llm}` |

---

## 3. Structure du projet

Le projet utilise une structure Maven standard compatible Java 17+ et directement utilisable dans Eclipse :

```text
HAI913I-TP-Refactoring/
├── pom.xml                                  # Configuration Maven (Java 17, aucune dépendance externe)
├── README.md                                # Présentation générale et instructions d'utilisation
├── .gitignore                               # Exclusion de target/, docs/ et fichiers locaux IDE
├── scripts/
│   └── verifier_tout.sh                     # Script compilant et exécutant les 6 classes Main
├── rapport/
│   ├── RAPPORT_REFACTORING.md               # Rapport complet d'analyse et de comparaison (Initial vs Eclipse vs LLM)
│   ├── GUIDE_ECLIPSE.md                     # Procédure détaillée des manipulations réalisées dans Eclipse
│   └── UTILISATION_IA.md                    # Documentation sur l'usage du LLM, prompts et vérifications
├── preuves/
│   ├── resultats_execution.txt              # Trace réelle de compilation et d'exécution des 6 programmes
│   └── eclipse/
│       ├── CONSIGNES_CAPTURES.md            # Inventaire commenté des 23 captures d'écran réalisées sous Eclipse
│       ├── mohamed/                         # 15 captures PNG du refactoring Eclipse du programme de Mohamed
│       └── sacko/                           # 8 captures PNG du refactoring Eclipse du programme de Sacko
└── src/main/java/fr/umontpellier/hai913i/refactoring/
    ├── mohamed/
    │   ├── initial/                         # Version initiale avec défaut (instanceof et casts explicites)
    │   ├── eclipse/                         # Version refactorée dans Eclipse (polymorphisme + Encapsulate Downcast)
    │   └── llm/                             # Version refactorée par le LLM (polymorphisme + Encapsulate Downcast)
    └── sacko/
        ├── initial/                         # Version initiale avec défaut (codes -1/-2/-3, null et exceptions)
        ├── eclipse/                         # Version refactorée dans Eclipse (Replace Error Code with Exception)
        └── llm/                             # Version refactorée par le LLM (Replace Error Code with Exception)
```

---

## 4. Statut des six versions Java

Les **6 versions** sont complètes, compilent sans erreur et valident l'intégralité de leurs tests :

| Package Java | Statut | Description |
| :--- | :--- | :--- |
| `...refactoring.mohamed.initial` | **Terminé et validé** | Contient volontairement les tests `instanceof` et les conversions explicites (`(Developpeur)`, `(Commercial)`, `(Manager)`, `(Employe)`). |
| `...refactoring.mohamed.eclipse` | **Terminé et validé** | Refactoré dans Eclipse (*Change Method Signature*, *Extract Method*, *Move Method*, *Pull Up* + finalisation manuelle de `calculerPrime()` et `genererDescriptionBilan()`). |
| `...refactoring.mohamed.llm` | **Terminé et validé** | Refactoré intégralement par le LLM (*Replace Conditional with Polymorphism*, *Move Method*, *Pull Up Method*, *Encapsulate Downcast*). |
| `...refactoring.sacko.initial` | **Terminé et validé** | Contient volontairement le mélange de codes d'erreur (`0`, `-1`, `-2`, `-3`), de retours `null` et d'exceptions (`IllegalArgumentException`). |
| `...refactoring.sacko.eclipse` | **Terminé et validé** | Refactoré dans Eclipse (*Change Method Signature* `int` -> `void` sur `verifierEtReserver` et `annulerReservation` + remplacement des codes et `null` par des exceptions). |
| `...refactoring.sacko.llm` | **Terminé et validé** | Refactoré intégralement par le LLM (*Replace Error Code with Exception*, harmonisation par exceptions standards). |

---

## 5. Prérequis, compilation et exécution

### Prérequis
- **JDK 17** ou supérieur.
- **Apache Maven 3.8+**.

### Compilation avec Maven
À la racine du projet :
```bash
mvn clean test-compile
```

### Exécution individuelle de chaque programme `Main`
```bash
# Programme 1 (Mohamed) : conversions explicites vers des types concrets
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.initial.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.eclipse.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.mohamed.llm.Main

# Programme 2 (Sacko) : mélange codes de retour, null et exceptions
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.initial.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.eclipse.Main
java -cp target/classes fr.umontpellier.hai913i.refactoring.sacko.llm.Main
```

### Exécution automatique de toutes les vérifications
Le script shell compile l'ensemble du projet, exécute les 6 méthodes `main` et enregistre la sortie console complète dans `preuves/resultats_execution.txt` :
```bash
./scripts/verifier_tout.sh
```

---

## 6. Ouverture et exécution sous Eclipse

1. Ouvrir **Eclipse IDE for Java Developers**.
2. Cliquer sur **File → Import... → Maven → Existing Maven Projects → Next >**.
3. Dans **Root Directory**, sélectionner le dossier racine du projet (`HAI913I-TP-Refactoring`), vérifier que `pom.xml` est coché, puis cliquer sur **Finish**.
4. Dans le **Package Explorer**, dérouler `src/main/java` puis le package souhaité, et faire un **clic droit sur `Main.java` → Run As → Java Application**.
