#!/bin/bash

# ============================================================
#   Script d'installation - Parabox
#   Compatible : Linux / macOS
# ============================================================

ROUGE='\033[0;31m'
VERT='\033[0;32m'
JAUNE='\033[1;33m'
CYAN='\033[0;36m'
RESET='\033[0m'

echo -e "${CYAN}"
echo " ----------Installation de Jeu -------------   "
echo -e "${RESET}"


echo -e "${JAUNE}[1/3] Vérification de Java...${RESET}"

if ! command -v java &> /dev/null; then
    echo -e "${ROUGE}✗ Java n'est pas installé !${RESET}"
    echo ""
    echo "Installez Java 17 :"
    echo "  Linux (Debian/Ubuntu) : sudo apt install openjdk-17-jdk"
    echo "  Linux (Fedora)        : sudo dnf install java-17-openjdk"
    echo "  macOS                 : brew install openjdk@17"
    echo "  Ou téléchargez sur   : https://adoptium.net"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1 | awk -F '"' '{print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${ROUGE}✗ Java $JAVA_VERSION détecté. Java 17 minimum requis !${RESET}"
    echo "  Téléchargez Java 17 sur : https://adoptium.net"
    exit 1
fi

echo -e "${VERT} Java $JAVA_VERSION détecté${RESET}"


echo -e "${JAUNE}[2/3] Vérification de Maven...${RESET}"

if ! command -v mvn &> /dev/null; then
    echo -e "${JAUNE} Maven non détecté. Installation en cours...${RESET}"

    OS="$(uname -s)"
    case "$OS" in
        Linux*)
            if command -v apt &> /dev/null; then
                sudo apt update && sudo apt install -y maven
            elif command -v dnf &> /dev/null; then
                sudo dnf install -y maven
            elif command -v pacman &> /dev/null; then
                sudo pacman -S maven
            else
                echo -e "${ROUGE} Gestionnaire de paquets non reconnu.${RESET}"
                echo "  Installez Maven manuellement : https://maven.apache.org/download.cgi"
                exit 1
            fi
            ;;
        Darwin*)
            if command -v brew &> /dev/null; then
                brew install maven
            else
                echo -e "${ROUGE} Homebrew non installé.${RESET}"
                echo "  Installez Homebrew : https://brew.sh"
                echo "  Puis relancez ce script."
                exit 1
            fi
            ;;
        *)
            echo -e "${ROUGE}✗ OS non reconnu : $OS${RESET}"
            exit 1
            ;;
    esac
fi

MVN_VERSION=$(mvn -version 2>&1 | head -1 | awk '{print $3}')
echo -e "${VERT} Maven $MVN_VERSION détecté${RESET}"


echo -e "${JAUNE}[3/3] Compilation du projet...${RESET}"

mvn clean compile -q
if [ $? -ne 0 ]; then
    echo -e "${ROUGE} Erreur lors de la compilation !${RESET}"
    echo "  Relancez avec : mvn clean compile pour voir les détails"
    exit 1
fi

echo -e "${VERT} Compilation réussie${RESET}"

# ─── Fin ──────────────────────────────────────────────────────
echo ""
echo -e "${VERT}"
echo "     Installation terminée avec succès! "
echo "========================================"
echo "  Lancer le jeu (terminal) :            "
echo "    ./run.sh terminal                   "
echo "  Lancer le jeu (interface) :           "
echo "    ./run.sh                            "
echo -e "${RESET}"
