
# Makefile - Projet Parabox
# ===========================

# Variables
MVN = mvn
JAR = target/parabox-1.0-SNAPSHOT.jar


# Cible par défaut

all: compile


# Compiler le projet

compile:
	$(MVN) compile


# Lancer le jeu (mode graphique)

run:
	$(MVN) javafx:run


# Lancer le jeu (mode terminal)

run-terminal:
	$(MVN) exec:java


# Compiler et créer le jar

package:
	$(MVN) clean package -DskipTests


# Lancer le jar

jar: package
	java -jar $(JAR)


# Lancer les tests

test:
	$(MVN) test


# Nettoyer le projet

clean:
	$(MVN) clean


# Compiler + tester + créer le jar

build: clean
	$(MVN) clean package


# Afficher l'aide

help:
	@echo "Commandes disponibles :"
	@echo "  make compile      - Compiler le projet"
	@echo "  make run          - Lancer le jeu (mode graphique)"
	@echo "  make run-terminal - Lancer le jeu (mode terminal)"
	@echo "  make package      - Créer le jar sans les tests"
	@echo "  make jar          - Créer et lancer le jar"
	@echo "  make test         - Lancer les tests JUnit"
	@echo "  make clean        - Nettoyer le projet"
	@echo "  make build        - Compiler + tester + créer le jar"

.PHONY: all compile run run-terminal package jar test clean build help
