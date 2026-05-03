# Jeu Sokoban-Parabox
UE Projet L2 Informatique — Université Paris 13  

---

## Équipe

| Nom Prénom | Rôle | Responsabilité |
|-------------|---------|--------------|
| **Bah Oumou koultoumy** | Coordinatrice | Coordination générale, Git |
| **Mesbahi Khadija** | Modèle | Logique du jeu |
| **Luu Léa** | Interface graphique | Affichage JavaFX |
| **Ramdane Massylia** |Persistance | Sauvegarde / chargement |
| **Ndiaye Maguette** | Chemin | Algorithme de résolution |
| **Harzallah Nour** | Auto-résolution | Résolution automatique |
| **Bahadori Somayya** | **Installation Déploiement** | Scripts, Maven, jpackage, CI/CD GitHub Actions |

---

## Télécharger et installer le jeu (utilisateurs)

Utiliser l'installateur fourni à côté de l'archive du code :

- **Linux** : utiliser `.deb` et lancer :
```bash
  sudo dpkg -i Parabox_1.0.deb
  /opt/parabox/bin/Parabox
```

- **Windows** : Double-cliquer sur l'installateur `.exe`  pour installer.

- **macOS (Apple Silicon M1/M2/M3)** : télécharger le `.dmg` pour macOs arm, glisser Parabox dans Applications.  
  Si macOS affiche "application endommagée", ouvrir le Terminal et taper :
```bash
  xattr -cr /Applications/Parabox.app
```
  Puis relancer l'application.

- **macOS (Intel)** : télécharger le `.dmg` pour macOs intel, glisser Parabox dans Applications.  
  Si macOS affiche "application endommagée", ouvrir le Terminal et taper :
```bash
  xattr -cr /Applications/Parabox.app
```
  Puis relancer l'application.

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


