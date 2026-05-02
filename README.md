# Parabox - Jeu Sokoban

Projet UE L2 Informatique — Université Paris 13  
Un jeu de type Sokoban développé en Java.

---

## Ce qu'il faut avoir installé

- **Java 17** minimum (si t'as pas ça, ça marchera pas) → [adoptium.net](https://adoptium.net)
- **Maven 3.x** — pas besoin de l'installer à la main, le script s'en occupe si t'as pas

---

## Installation

### Cloner le projet

```bash
git clone https://github.com/okbah025/ue-projet.git
cd ue-projet
```

### Lancer le script d'installation

Sur **Linux / macOS** :
```bash
chmod +x install.sh run.sh
./install.sh
```

Sur **Windows** :
```bat
install.bat
```

Le script fait les vérifications de base (Java, Maven, compilation). Si Maven est absent il essaie de l'installer tout seul.

---

## Lancer le jeu

### Interface graphique (JavaFX)

Linux / macOS :
```bash
./run.sh
```

Windows :
```bat
run.bat
```

### Mode terminal (si la fenêtre marche pas)

Linux / macOS :
```bash
./run.sh terminal
```

Windows :
```bat
run.bat terminal
```

---

## Commandes Maven

Si vous préférez passer par Maven directement :

```bash
mvn clean compile    # compiler
mvn test             # lancer les tests
mvn javafx:run       # lancer l'interface graphique
mvn exec:java        # lancer en mode terminal
mvn clean            # nettoyer les fichiers compilés
```

---

## Structure du projet
.
├── install.bat
├── install.sh
├── pom.xml
├── README.md
├── run.bat
├── run.sh
└── src
    ├── main
    │   ├── java/fr/paris13/parabox/
    │   │     								├── chemin 
    │   │     								├── ig 
    │   │        							├── Modele  
    │   │               			└── ResoAuto
    │   └── resources
    │       ├── images
    │       ├── niveau1.txt
    │       ├── niveau2.txt
    │       ├── niveau3.txt
    │       ├── niveau4.txt
    │       └── niveau5.txt
    └── test
        └── java/fr/paris13/parabox/ # Tests JUnit
        
        
---

## Problèmes courants

**Java pas reconnu après installation** → Redémarrez le terminal (ou carrément la machine).

**Erreur JavaFX** → Lancez bien via `mvn javafx:run` et pas directement avec `java -jar`, ça marchera pas.

**Erreur de compilation** → Faites `mvn clean compile` et lisez les messages. Vérifiez aussi que c'est bien Java 17 qui est utilisé :
```bash
java -version
mvn -version
```

---

## Équipe

| Rôle |               Responsabilité |
|------|------------------------------|
| Coordinateur | Coordination générale, Git|
| Modèle |Logique du jeu| 
| Interface graphique | Affichage JavaFX |
| Persistance | Sauvegarde / chargement| 
| Chemin | Algorithme de résolution| 
| Auto-résolution | Résolution automatique |
| **Installation Déploiement** | **Scripts, Maven, Git** |

