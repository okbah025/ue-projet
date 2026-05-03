# Jeu Sokoban-Parabox
Projet UE L2 Informatique — Université Paris 13  
Un jeu de type Sokoban développé en Java.

---

## Télécharger et installer le jeu (utilisateurs)

Utiliser l'installateur fourni à côté de l'archive du code :

- **Linux** : utiliser `.deb` et lancer :
```bash
  sudo dpkg -i Parabox_1.0.deb
  /opt/parabox/bin/Parabox
```

- **Windows** : Double-cliquer sur l'installateur `.exe`  pour installer.

- **macOS (Apple Silicon M1/M2/M3)** : télécharger le `.dmg`, glisser Parabox dans Applications.  
  Si macOS affiche "application endommagée", ouvrir le Terminal et taper :
```bash
  xattr -cr /Applications/Parabox.app
```
  Puis relancer l'application.

- **macOS (Intel)** : installer le `.dmg` Intel fourni parmi les autres installateurs.

---

## Ce qu'il faut avoir installé (développeurs)

- **Java 17** minimum (si t'as pas ça, ça marchera pas)
- **Maven 3.x** — pas besoin de l'installer à la main, le script s'en occupe si t'as pas

---

## Installation (développeurs)

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

## Lancer le jeu (développeurs)

### Via Makefile
```bash
make run          # lancer le jeu (mode graphique)
make run-terminal # lancer en mode terminal
make test         # lancer les tests
make build        # compiler et créer le jar
make help         # afficher toutes les commandes
```

### Via les scripts
Linux / macOS :
```bash
./run.sh
```
Windows :
```bat
run.bat
```

### Mode terminal (si vous voulez tester)
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
├── Makefile
├── install.bat
├── install.sh
├── pom.xml
├── README.md
├── run.bat
├── run.sh
└── src
├── main
│   ├── java/fr/paris13/parabox/
│   │   									├── chemin
│   │   									├── ig
│   │   									├── Modele
│   │   									└── ResoAuto
│   └── resources
│       ├── images
│       ├── icones
│       ├── niveau1.txt
│       ├── niveau2.txt
│       ├── niveau3.txt
│       ├── niveau4.txt
│       └── niveau5.txt
└── test
└── java/fr/paris13/parabox/  
											└── ParaboxTest.java  # Tests JUnit
											
---

## Problèmes courants

**Peu probable mais si:** 

**-Java pas reconnu après installation** → Redémarrez le terminal (ou carrément la machine).

**-Erreur JavaFX** → Lancez bien via `mvn javafx:run` ou make run

**-Erreur de compilation** → Faites `mvn clean compile` et lisez les messages. Vérifiez aussi que c'est bien Java 17 qui est utilisé :
```bash
java -version
mvn -version
```

**-macOS : "application endommagée"** → Ouvrir le Terminal et taper :
```bash
xattr -cr /Applications/Parabox.app
```

**-Linux : icône introuvable après installation** → Lancer directement avec :
```bash
/opt/parabox/bin/Parabox
```

---

## Équipe

| Rôle | Responsabilité |
|------|----------------|
| Coordinateur | Coordination générale, Git |
| Modèle | Logique du jeu |
| Interface graphique | Affichage JavaFX |
| Persistance | Sauvegarde / chargement |
| Chemin | Algorithme de résolution |
| Auto-résolution | Résolution automatique |
| **Installation Déploiement** | **Scripts, Maven, jpackage, CI/CD GitHub Actions** |
