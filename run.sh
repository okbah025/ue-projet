#!/bin/bash

# ============================================================
#   Script de lancement - Parabox
#   Compatible : Linux / macOS
#   Usage : ./run.sh           -> interface graphique
#           ./run.sh terminal  -> mode terminal
# ============================================================

ROUGE='\033[0;31m'
VERT='\033[0;32m'
JAUNE='\033[1;33m'
CYAN='\033[0;36m'
RESET='\033[0m'

MODE=${1:-"javafx"}

echo -e "${CYAN}"
echo "   ------Lancement de Jeu -------    "
echo -e "${RESET}"

if [ "$MODE" = "terminal" ]; then
    echo -e "${JAUNE} Lancement en mode terminal...${RESET}"
    mvn exec:java -q
else
    echo -e "${JAUNE} Lancement en mode interface graphique...${RESET}"
    mvn javafx:run -q
fi

if [ $? -ne 0 ]; then
    echo -e "${ROUGE}✗ Erreur lors du lancement !${RESET}"
    echo "  Vérifiez que vous avez bien lancé ./install.sh d'abord"
    exit 1
fi
