# Parabox - Jeu Sokoban Récursif

Projet universitaire L2 Informatique - Université Paris 13  
Un jeu de type Sokoban avec des boîtes-mondes récursives.

---

## Prérequis

| Outil | Version minimale | Lien |
|-------|-----------------|------|
| Java  | 17              | [adoptium.net](https://adoptium.net) |
| Maven | 3.x             | Installé automatiquement par le script |

> Maven sera installé automatiquement si absent.

---

## Installation

### 1. Cloner le projet
```bash
git clone https://github.com/okbah025/ue-projet.git
cd ue-projet
```

### 2. Lancer le script d'installation

**Linux / macOS :**
```bash
chmod +x install.sh run.sh
./install.sh
```

**Windows :**
```bat
install.bat
```

Le script vérifie automatiquement :
- ✅ La présence de Java 17
- ✅ La présence de Maven (et l'installe si absent)
- ✅ La compilation du projet

---

## Lancer le jeu

### Interface graphique (JavaFX)

**Linux / macOS :**
```bash
./run.sh
```

**Windows :**
```bat
run.bat
```

### Mode terminal

**Linux / macOS :**
```bash
./run.sh terminal
```

**Windows :**
```bat
run.bat terminal
```

---

## Commandes Maven utiles

```bash
mvn clean compile    # Compiler le projet
mvn test             # Lancer les tests
mvn javafx:run       # Lancer l'interface graphique
mvn exec:java        # Lancer le mode terminal
mvn clean            # Nettoyer les fichiers compilés
```

---

## Structure du projet

```
parabox/
├── pom.xml                          # Configuration Maven
├── install.sh / install.bat         # Scripts d'installation
├── run.sh / run.bat                 # Scripts de lancement
├── README.md
└── src/
    ├── main/
    │   ├── java/fr/paris13/parabox/
    │   │   ├── Modele/              # Logique du jeu
    │   │   ├── ig/                  # Interface graphique
    │   │   └── chemin/              # Algorithme de chemin
    │   └── resources/
    │       └── images/              # Images du jeu
    └── test/
        └── java/fr/paris13/parabox/ # Tests JUnit
```

---

## Problèmes fréquents

### Java non reconnu après installation
Redémarrez votre terminal ou votre machine.

### Erreur JavaFX au lancement
Vérifiez que vous lancez via `mvn javafx:run` et non directement `java -jar`.

### Erreur de compilation
```bash
mvn clean compile
```
Lisez les messages d'erreur et vérifiez que Java 17 est bien utilisé :
```bash
java -version
mvn -version
```

---

## Équipe

| Rôle | Responsabilité |
|------|---------------|
| Coordinateur | Coordination générale |
| Modèle | Logique du jeu |
| Interface graphique | Affichage JavaFX |
| Persistance | Sauvegarde/chargement |
| Chemin | Algorithme de résolution |
| Auto-résolution | Résolution automatique |
| **Installation/Déploiement** | **Scripts, Maven, Git** |
