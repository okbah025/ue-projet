import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Classe MainTerminalComplet
 *
 * Point d'entrée principal du jeu Sokoban / Parabox en mode terminal.
 *
 * Ce programme propose DEUX modes de jeu :
 *
 *  1. VERSION SIMPLE  : Sokoban classique.
 *       - 10 niveaux codés directement dans le programme
 *       - Règles standard : pousser des boîtes ($) sur des cibles (.)
 *
 *  2. VERSION RÉCURSIVE : Parabox.
 *       - Niveaux chargés depuis des fichiers .txt (niveau1.txt, niveau2.txt...)
 *       - Les lettres MAJUSCULES sur la grille sont des "Pieces" (grilles-mondes)
 *       - Le joueur peut entrer dans une Piece et en sortir librement
 *
 * CONTRÔLES :
 *   ↑ ↓ ← →  : flèches directionnelles (séquences ANSI ESC [ A/B/C/D)
 *   Z S Q D   : alternative ZQSD (si terminal sans flèches)
 *   8 2 4 6   : pavé numérique
 *   U         : annuler le dernier mouvement
 *   R         : recommencer le niveau depuis le début
 *   H         : afficher l'aide / les contrôles
 *   X         : quitter le jeu
 *
 * COMPILATION ET LANCEMENT :
 *   javac *.java
 *   java MainTerminalComplet
 *   (les fichiers niveau*.txt doivent être dans le même dossier)
 */
public class MainTerminalComplet {

    // ── Codes couleur ANSI pour l'affichage terminal ──────────────────────────
    private static final String CLEAR  = "\033[H\033[2J";
    private static final String ROUGE  = "\033[31m";
    private static final String VERT   = "\033[32m";
    private static final String BLEU   = "\033[34m";
    private static final String JAUNE  = "\033[33m";
    private static final String CYAN   = "\033[36m";
    private static final String MAGENTA= "\033[35m";
    private static final String RESET  = "\033[0m";

    // ── Codes des touches flèches (séquences ANSI ESC [ X) ───────────────────
    // Quand on appuie sur une flèche, le terminal envoie 3 octets :
    //   ESC (27) + '[' (91) + lettre (A=haut, B=bas, C=droite, D=gauche)
    private static final int ESC = 27;

    // ── Dossier des fichiers de niveaux ───────────────────────────────────────
    // "." = dossier courant (là où on lance java)
    private static final String DOSSIER = ".";

    // ── Versions disponibles ──────────────────────────────────────────────────
    private enum Version { SIMPLE, RECURSIVE }

    // ── Commandes reconnues en jeu ────────────────────────────────────────────
    private enum Commande {
        HAUT, BAS, GAUCHE, DROITE,
        ANNULER, RECOMMENCER, AIDE, QUITTER, INCONNU
    }

    // =========================================================================
    //  POINT D'ENTRÉE
    // =========================================================================

    /**
     * Méthode principale : lance le jeu depuis le terminal.
     * @param args Arguments en ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ── Étape 1 : Choisir la version ──────────────────────────────────────
        effacer();
        Version version = menuPrincipal(scanner);

        // ── Étape 2 : Choisir le niveau et lancer le jeu ─────────────────────
        effacer();
        if (version == Version.SIMPLE) {
            lancerSimple(scanner);
        } else {
            lancerRecursif(scanner);
        }

        scanner.close();
    }

    // =========================================================================
    //  LANCEMENT VERSION SIMPLE
    // =========================================================================

    /**
     * Gérer le lancement et la boucle de jeu pour la VERSION SIMPLE.
     * Les niveaux sont codés en dur dans ce fichier.
     *
     * @param scanner Le scanner pour lire les entrées clavier
     */
    private static void lancerSimple(Scanner scanner) {
        // Afficher le menu des niveaux simples
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BLEU + "              SOKOBAN CLASSIQUE - NIVEAUX            " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        String[] noms = {
            "Initiation", "Découverte", "Apprentissage", "Progression",
            "Intermédiaire", "Avancé", "Confirmé", "Expert", "Maître", "Grand Maître"
        };
        String[] etoiles = {
            "★☆☆☆☆", "★☆☆☆☆", "★★☆☆☆", "★★☆☆☆", "★★★☆☆",
            "★★★☆☆", "★★★★☆", "★★★★☆", "★★★★★", "★★★★★"
        };
        for (int i = 0; i < 10; i++) {
            System.out.println("  " + VERT + (i + 1) + "." + RESET
                + "  " + etoiles[i] + "  " + noms[i]);
        }
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);

        int choix = demanderChoix(scanner, 1, 10);
        Grille grille = creerNiveauSimple(choix);
        if (grille == null) {
            System.out.println(ROUGE + "Niveau invalide !" + RESET);
            return;
        }

        // Intro
        effacer();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BLEU + "         SOKOBAN - Niveau " + choix + " : " + noms[choix-1] + "               " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(VERT + "Objectif : " + RESET + "Poussez toutes les boîtes ($) sur les cibles (.)");
        System.out.println(JAUNE + "\nAppuyez sur ENTRÉE pour commencer..." + RESET);
        scanner.nextLine();

        // Boucle de jeu
        Jeu jeu = new Jeu(grille);
        effacer();
        afficherEtatSimple(jeu);
        afficherControles();

        boolean continuer = true;
        while (continuer && !jeu.estNiveauTermine()) {
            Commande cmd = lireCommande();
            Direction dir = commandeVersDirection(cmd);

            if (dir != null) {
                boolean ok = jeu.deplacerJoueur(dir);
                if (ok) {
                    effacer();
                    afficherEtatSimple(jeu);
                    if (jeu.estNiveauTermine()) {
                        afficherVictoire(jeu.getNombreMouvements(), jeu.getNombrePoussees());
                    }
                }
                // Si ok=false : mur, on ne fait rien (pas de message parasite)

            } else {
                switch (cmd) {
                    case ANNULER:
                        if (jeu.annulerMouvement()) {
                            effacer(); afficherEtatSimple(jeu);
                            System.out.println(VERT + "✓ Mouvement annulé !" + RESET);
                        } else {
                            System.out.println(ROUGE + "✗ Rien à annuler !" + RESET);
                        }
                        break;
                    case RECOMMENCER:
                        // Recréer la grille depuis zéro (vrai recommencement)
                        Grille nouvelleGrille = creerNiveauSimple(choix);
                        jeu.setGrille(nouvelleGrille);
                        effacer(); afficherEtatSimple(jeu);
                        System.out.println(VERT + "✓ Niveau recommencé !" + RESET);
                        break;
                    case AIDE:
                        afficherControles();
                        break;
                    case QUITTER:
                        continuer = false;
                        System.out.println(CYAN + "\nMerci d'avoir joué ! 👋" + RESET);
                        break;
                    default:
                        break; // INCONNU : on ignore silencieusement
                }
            }
        }
    }

    // =========================================================================
    //  LANCEMENT VERSION RÉCURSIVE
    // =========================================================================

    /**
     * Gérer le lancement et la boucle de jeu pour la VERSION RÉCURSIVE.
     * Les niveaux sont chargés depuis les fichiers niveau*.txt du dossier courant.
     *
     * @param scanner Le scanner pour lire les entrées clavier
     */
    private static void lancerRecursif(Scanner scanner) {
        // Lister les fichiers niveau*.txt disponibles
        List<String> fichiers = listerFichiersNiveaux();

        if (fichiers.isEmpty()) {
            System.out.println(ROUGE + "Aucun fichier de niveau trouvé !" + RESET);
            System.out.println("Assurez-vous que les fichiers niveau1.txt, niveau2.txt, ...");
            System.out.println("sont dans le même dossier que le programme.");
            return;
        }

        // Afficher le menu des niveaux récursifs
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + MAGENTA + "              PARABOX RÉCURSIF - NIVEAUX             " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(MAGENTA + "  Niveaux disponibles (fichiers .txt trouvés) :" + RESET);
        System.out.println();
        for (int i = 0; i < fichiers.size(); i++) {
            System.out.println("  " + MAGENTA + (i + 1) + "." + RESET + "  " + fichiers.get(i));
        }
        System.out.println();
        System.out.println(CYAN + "  ℹ  Les lettres MAJUSCULES sont des mondes dans lesquels" + RESET);
        System.out.println(CYAN + "     vous pouvez entrer en vous déplaçant vers eux." + RESET);
        System.out.println(CYAN + "     Atteignez le bord d'une grille interne pour en sortir." + RESET);
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);

        int choix = demanderChoix(scanner, 1, fichiers.size());
        String fichierChoisi = fichiers.get(choix - 1);

        // Charger la grille depuis le fichier
        System.out.println(CYAN + "\nChargement : " + fichierChoisi + "..." + RESET);
        Grille grilleRacine = ChargeurNiveau.charger(DOSSIER + File.separator + fichierChoisi);
        if (grilleRacine == null) {
            System.out.println(ROUGE + "Impossible de charger le niveau !" + RESET);
            return;
        }

        // Intro
        effacer();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + MAGENTA + "        PARABOX - Niveau " + choix + " : " + fichierChoisi + "                " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(MAGENTA + "Objectif   : " + RESET + "Mettez toutes les boîtes ($) sur les cibles (.)");
        System.out.println(MAGENTA + "Récursivité: " + RESET + "Les LETTRES MAJUSCULES sont des mondes internes.");
        System.out.println("             → Entrez dedans en vous y déplaçant.");
        System.out.println("             → Sortez en atteignant le bord de la grille interne.");
        System.out.println(JAUNE + "\nAppuyez sur ENTRÉE pour commencer..." + RESET);
        scanner.nextLine();

        // Boucle de jeu récursif
        JeuRecursif jeu = new JeuRecursif(grilleRacine);
        effacer();
        afficherEtatRecursif(jeu);
        afficherControlesRecursif();

        boolean continuer = true;
        while (continuer && !jeu.estNiveauTermine()) {
            Commande cmd = lireCommande();
            Direction dir = commandeVersDirection(cmd);

            if (dir != null) {
                boolean ok = jeu.deplacerJoueur(dir);
                if (ok) {
                    effacer();
                    afficherEtatRecursif(jeu);
                    if (jeu.estNiveauTermine()) {
                        afficherVictoire(jeu.getNombreMouvements(), jeu.getNombrePoussees());
                    }
                }
                // ok=false = mouvement bloqué (mur) : on ignore silencieusement

            } else {
                switch (cmd) {
                    case ANNULER:
                        if (jeu.annulerMouvement()) {
                            effacer(); afficherEtatRecursif(jeu);
                            System.out.println(VERT + "✓ Mouvement annulé !" + RESET);
                        } else {
                            System.out.println(ROUGE + "✗ Rien à annuler !" + RESET);
                        }
                        break;
                    case RECOMMENCER:
                        // Recharger le fichier depuis le disque (vrai recommencement)
                        Grille ng = ChargeurNiveau.charger(DOSSIER + File.separator + fichierChoisi);
                        if (ng != null) {
                            jeu.reinitialiser(ng);
                            effacer(); afficherEtatRecursif(jeu);
                            System.out.println(VERT + "✓ Niveau rechargé depuis le fichier !" + RESET);
                        } else {
                            System.out.println(ROUGE + "✗ Impossible de recharger le niveau !" + RESET);
                        }
                        break;
                    case AIDE:
                        afficherControlesRecursif();
                        break;
                    case QUITTER:
                        continuer = false;
                        System.out.println(CYAN + "\nMerci d'avoir joué ! 👋" + RESET);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // =========================================================================
    //  LECTURE DES TOUCHES CLAVIER
    // =========================================================================

    /**
     * Lire une commande depuis le clavier.
     *
     * Gère les séquences ANSI des touches flèches :
     *   Flèche = ESC (27) + '[' (91) + lettre (A/B/C/D)
     *
     * Et les touches simples : Z S Q D (ZQSD), 8 2 4 6 (pavé), U R H X.
     *
     * Cette méthode BLOQUE jusqu'à ce qu'une touche soit pressée.
     *
     * @return La commande correspondant à la touche pressée
     */
    private static Commande lireCommande() {
        try {
            int premier = System.in.read();

            if (premier == ESC) {
                // Séquence de flèche : lire les 2 octets suivants
                int deuxieme = System.in.read(); // doit être '['
                if (deuxieme == '[') {
                    int troisieme = System.in.read(); // A, B, C ou D
                    switch ((char) troisieme) {
                        case 'A': return Commande.HAUT;
                        case 'B': return Commande.BAS;
                        case 'C': return Commande.DROITE;
                        case 'D': return Commande.GAUCHE;
                        default:  return Commande.INCONNU;
                    }
                }
                return Commande.INCONNU;

            } else {
                // Touche simple
                char c = Character.toUpperCase((char) premier);
                switch (c) {
                    // Déplacements ZQSD
                    case 'Z': return Commande.HAUT;
                    case 'S': return Commande.BAS;
                    case 'Q': return Commande.GAUCHE;
                    case 'D': return Commande.DROITE;
                    // Déplacements pavé numérique
                    case '8': return Commande.HAUT;
                    case '2': return Commande.BAS;
                    case '4': return Commande.GAUCHE;
                    case '6': return Commande.DROITE;
                    // Commandes
                    case 'U': return Commande.ANNULER;
                    case 'R': return Commande.RECOMMENCER;
                    case 'H': return Commande.AIDE;
                    case 'X': return Commande.QUITTER;
                    default:  return Commande.INCONNU;
                }
            }
        } catch (Exception e) {
            return Commande.INCONNU;
        }
    }

    /**
     * Convertir une Commande en Direction (ou null si ce n'est pas un déplacement).
     *
     * @param cmd La commande lue
     * @return La Direction correspondante, ou null si c'est une commande spéciale
     */
    private static Direction commandeVersDirection(Commande cmd) {
        switch (cmd) {
            case HAUT:   return Direction.HAUT;
            case BAS:    return Direction.BAS;
            case GAUCHE: return Direction.GAUCHE;
            case DROITE: return Direction.DROITE;
            default:     return null;
        }
    }

    // =========================================================================
    //  AFFICHAGE EN JEU
    // =========================================================================

    /**
     * Afficher l'état du jeu pour la VERSION SIMPLE.
     * Montre la grille + les statistiques.
     *
     * @param jeu Le jeu simple en cours
     */
    private static void afficherEtatSimple(Jeu jeu) {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BLEU + "                  SOKOBAN - EN JEU                  " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        // Afficher la grille (on saute la 1re ligne = "NomGrille largeur")
        String[] lignes = jeu.afficherGrille().split("\n");
        for (int i = 1; i < lignes.length; i++) {
            System.out.println("  " + colorier(lignes[i]));
        }

        System.out.println();
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);

        // Stats
        Grille g = jeu.getGrille();
        int surCibles = 0;
        for (Boite b : g.getBoites()) {
            if (!(b instanceof Piece) && b.estSurCible()) surCibles++;
        }
        int total = g.getCibles().size();
        System.out.println(CYAN + "📊 " + RESET
            + "Mouvements: " + JAUNE + jeu.getNombreMouvements() + RESET
            + "  |  Poussées: " + JAUNE + jeu.getNombrePoussees() + RESET
            + "  |  Boîtes: " + VERT + surCibles + "/" + total + RESET);
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
    }

    /**
     * Afficher l'état du jeu pour la VERSION RÉCURSIVE.
     * Montre la grille ACTIVE (celle où se trouve le joueur),
     * avec un indicateur de profondeur, + les stats de la grille racine.
     *
     * @param jeu Le jeu récursif en cours
     */
    private static void afficherEtatRecursif(JeuRecursif jeu) {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + MAGENTA + "               PARABOX - EN JEU                     " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        // Indicateur de profondeur : dans quelle grille on est
        if (jeu.estDansUnePiece()) {
            System.out.println(MAGENTA + "  ▶ Grille interne (profondeur " + jeu.getProfondeur() + ")" + RESET);
        } else {
            System.out.println(BLEU   + "  ▶ Grille principale" + RESET);
        }
        System.out.println();

        // Afficher la grille ACTIVE (là où est le joueur)
        Grille active = jeu.getGrilleActive();
        String[] lignes = active.afficherGrille().split("\n");
        for (int i = 1; i < lignes.length; i++) {
            System.out.println("  " + colorier(lignes[i]));
        }

        System.out.println();
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);

        // Stats depuis la grille RACINE (état global du niveau)
        Grille racine = jeu.getGrilleRacine();
        int surCibles = 0, total = 0;
        for (Boite b : racine.getBoites()) {
            if (b instanceof Piece) continue;
            total++;
            if (b.estSurCible()) surCibles++;
        }
        System.out.println(CYAN + "📊 " + RESET
            + "Mouvements: " + JAUNE + jeu.getNombreMouvements() + RESET
            + "  |  Boîtes: " + VERT + surCibles + "/" + total + RESET
            + "  |  Profondeur: " + MAGENTA + jeu.getProfondeur() + RESET);
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
    }

    /**
     * Colorier une ligne de la grille pour l'affichage.
     * Chaque type d'objet a une couleur différente.
     *
     *  '#' (mur)        → cyan
     *  '@' (joueur)     → bleu
     *  '$' (boîte)      → jaune
     *  '*' (boîte/cible)→ vert
     *  '.' (cible)      → rouge
     *  Lettre majuscule → magenta (Piece = monde récursif)
     *
     * @param ligne La ligne brute à colorier
     * @return La ligne avec les codes ANSI de couleur
     */
    private static String colorier(String ligne) {
        StringBuilder sb = new StringBuilder();
        for (char c : ligne.toCharArray()) {
            switch (c) {
                case '#': sb.append(CYAN)   .append(c).append(RESET); break;
                case '@': sb.append(BLEU)   .append(c).append(RESET); break;
                case '+': sb.append(BLEU)   .append(c).append(RESET); break;
                case '$': sb.append(JAUNE)  .append(c).append(RESET); break;
                case '*': sb.append(VERT)   .append(c).append(RESET); break;
                case '.': sb.append(ROUGE)  .append(c).append(RESET); break;
                default:
                    if (Character.isUpperCase(c))
                        sb.append(MAGENTA).append(c).append(RESET);
                    else
                        sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Afficher l'écran de victoire.
     *
     * @param mouvements Nombre total de mouvements effectués
     * @param poussees   Nombre total de poussées de boîtes
     */
    private static void afficherVictoire(int mouvements, int poussees) {
        System.out.println();
        System.out.println(VERT + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(VERT + "║" + JAUNE + "            🎉  FÉLICITATIONS !  🎉                  " + RESET + VERT + "║" + RESET);
        System.out.println(VERT + "║" + RESET + "                NIVEAU TERMINÉ !                        " + VERT + "║" + RESET);
        System.out.println(VERT + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println("   Score final :");
        System.out.println("   • Mouvements : " + JAUNE + mouvements + RESET);
        System.out.println("   • Poussées   : " + JAUNE + poussees   + RESET);
        System.out.println();
    }

    // =========================================================================
    //  AFFICHAGE DES CONTRÔLES
    // =========================================================================

    /** Afficher les contrôles communs aux deux versions. */
    private static void afficherControles() {
        System.out.println();
        System.out.println(CYAN + "🎮 CONTRÔLES :" + RESET);
        System.out.println("   " + VERT + "↑ ↓ ← →" + RESET + "  Flèches directionnelles");
        System.out.println("   " + VERT + "Z S Q D " + RESET + "  Alternative clavier AZERTY");
        System.out.println("   " + VERT + "8 2 4 6 " + RESET + "  Pavé numérique");
        System.out.println();
        System.out.println("   " + JAUNE + "U" + RESET + "  Annuler dernier mouvement");
        System.out.println("   " + JAUNE + "R" + RESET + "  Recommencer le niveau");
        System.out.println("   " + JAUNE + "H" + RESET + "  Afficher cette aide");
        System.out.println("   " + JAUNE + "X" + RESET + "  Quitter");
    }

    /** Afficher les contrôles avec les infos spécifiques à la version récursive. */
    private static void afficherControlesRecursif() {
        afficherControles();
        System.out.println();
        System.out.println(MAGENTA + "🌀 VERSION RÉCURSIVE :" + RESET);
        System.out.println("   Les " + MAGENTA + "LETTRES MAJUSCULES" + RESET + " sont des mondes internes.");
        System.out.println("   → Déplacez-vous vers une lettre pour y entrer.");
        System.out.println("   → Atteignez le bord de la grille interne pour sortir.");
    }

    // =========================================================================
    //  MENUS
    // =========================================================================

    /**
     * Afficher le menu principal et demander la version à jouer.
     *
     * @param scanner Le scanner
     * @return La version choisie (SIMPLE ou RECURSIVE)
     */
    private static Version menuPrincipal(Scanner scanner) {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + JAUNE + "           🎮  SOKOBAN / PARABOX  🎮                " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "                  Projet L2 Informatique                " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println("  " + BLEU + "1." + RESET + " VERSION SIMPLE (Sokoban classique)");
        System.out.println("     • 10 niveaux codés dans le programme");
        System.out.println("     • Poussez des boîtes sur des cibles");
        System.out.println();
        System.out.println("  " + MAGENTA + "2." + RESET + " VERSION RÉCURSIVE (Parabox)");
        System.out.println("     • Niveaux chargés depuis des fichiers .txt");
        System.out.println("     • Entrez dans des grilles-mondes (lettres majuscules)");
        System.out.println();
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);

        int choix = 0;
        while (choix != 1 && choix != 2) {
            System.out.print(JAUNE + "\n➤ Votre choix (1 ou 2) : " + RESET);
            try {
                choix = scanner.nextInt();
                scanner.nextLine();
                if (choix != 1 && choix != 2)
                    System.out.println(ROUGE + "✗ Saisissez 1 ou 2 !" + RESET);
            } catch (Exception e) {
                System.out.println(ROUGE + "✗ Entrée invalide !" + RESET);
                scanner.nextLine();
            }
        }
        return (choix == 1) ? Version.SIMPLE : Version.RECURSIVE;
    }

    /**
     * Demander un numéro de niveau entre min et max inclus.
     *
     * @param scanner Le scanner
     * @param min     Valeur minimale acceptée
     * @param max     Valeur maximale acceptée
     * @return Le choix validé
     */
    private static int demanderChoix(Scanner scanner, int min, int max) {
        int choix = -1;
        while (choix < min || choix > max) {
            System.out.print(JAUNE + "\n➤ Choisir un niveau (" + min + "-" + max + ") : " + RESET);
            try {
                choix = scanner.nextInt();
                scanner.nextLine();
                if (choix < min || choix > max)
                    System.out.println(ROUGE + "✗ Choisissez entre " + min + " et " + max + " !" + RESET);
            } catch (Exception e) {
                System.out.println(ROUGE + "✗ Entrée invalide !" + RESET);
                scanner.nextLine();
            }
        }
        return choix;
    }

    // =========================================================================
    //  UTILITAIRES
    // =========================================================================

    /** Effacer l'écran via le code ANSI. */
    private static void effacer() {
        System.out.print(CLEAR);
        System.out.flush();
    }

    /**
     * Lister les fichiers niveau*.txt dans le dossier courant,
     * triés par ordre alphabétique.
     *
     * @return La liste des noms de fichiers trouvés
     */
    private static List<String> listerFichiersNiveaux() {
        List<String> liste = new ArrayList<>();
        File dir = new File(DOSSIER);
        if (dir.exists() && dir.isDirectory()) {
            File[] fichiers = dir.listFiles();
            if (fichiers != null) {
                Arrays.sort(fichiers); // tri alphabétique
                for (File f : fichiers) {
                    if (f.isFile() && f.getName().startsWith("niveau")
                            && f.getName().endsWith(".txt")) {
                        liste.add(f.getName());
                    }
                }
            }
        }
        return liste;
    }

    // =========================================================================
    //  NIVEAUX VERSION SIMPLE (10 niveaux codés en dur)
    // =========================================================================

    /**
     * Créer le niveau simple correspondant au numéro donné.
     *
     * @param numero Le numéro du niveau (1 à 10)
     * @return La grille du niveau, ou null si numéro invalide
     */
    private static Grille creerNiveauSimple(int numero) {
        switch (numero) {
            case 1:  return niveauSimple1();
            case 2:  return niveauSimple2();
            case 3:  return niveauSimple3();
            case 4:  return niveauSimple4();
            case 5:  return niveauSimple5();
            case 6:  return niveauSimple6();
            case 7:  return niveauSimple7();
            case 8:  return niveauSimple8();
            case 9:  return niveauSimple9();
            case 10: return niveauSimple10();
            default: return null;
        }
    }

    /** Créer un cadre de murs tout autour de la grille. */
    private static void cadre(Grille g, int l, int h) {
        for (int x = 0; x < l; x++) {
            g.setObjet(new Mur(x, 0, g), x, 0);
            g.setObjet(new Mur(x, h - 1, g), x, h - 1);
        }
        for (int y = 0; y < h; y++) {
            g.setObjet(new Mur(0, y, g), 0, y);
            g.setObjet(new Mur(l - 1, y, g), l - 1, y);
        }
    }

    private static Grille niveauSimple1() {
        Grille g = new Grille(7, 5, "Niveau 1");
        cadre(g, 7, 5);
        g.setObjet(new Cible(5, 2, g),  5, 2);
        g.setObjet(new Boite(3, 2, g),  3, 2);
        g.setObjet(new Joueur(1, 2, g), 1, 2);
        return g;
    }

    private static Grille niveauSimple2() {
        Grille g = new Grille(8, 6, "Niveau 2");
        cadre(g, 8, 6);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    private static Grille niveauSimple3() {
        Grille g = new Grille(8, 6, "Niveau 3");
        cadre(g, 8, 6);
        g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(4, 3, g), 4, 3);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(3, 1, g), 3, 1);
        g.setObjet(new Boite(2, 4, g), 2, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    private static Grille niveauSimple4() {
        Grille g = new Grille(9, 7, "Niveau 4");
        cadre(g, 9, 7);
        g.setObjet(new Mur(4, 2, g), 4, 2);
        g.setObjet(new Mur(4, 4, g), 4, 4);
        g.setObjet(new Cible(7, 2, g), 7, 2);
        g.setObjet(new Cible(7, 3, g), 7, 3);
        g.setObjet(new Cible(7, 4, g), 7, 4);
        g.setObjet(new Boite(3, 2, g), 3, 2);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(3, 4, g), 3, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    private static Grille niveauSimple5() {
        Grille g = new Grille(10, 7, "Niveau 5");
        cadre(g, 10, 7);
        g.setObjet(new Mur(3, 1, g), 3, 1); g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(6, 2, g), 6, 2); g.setObjet(new Mur(6, 3, g), 6, 3);
        g.setObjet(new Mur(6, 4, g), 6, 4); g.setObjet(new Mur(3, 5, g), 3, 5);
        g.setObjet(new Cible(8, 1, g), 8, 1);
        g.setObjet(new Cible(8, 3, g), 8, 3);
        g.setObjet(new Cible(8, 5, g), 8, 5);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(4, 5, g), 4, 5);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    private static Grille niveauSimple6() {
        Grille g = new Grille(10, 8, "Niveau 6");
        cadre(g, 10, 8);
        for (int y = 2; y < 6; y++) g.setObjet(new Mur(5, y, g), 5, y);
        g.setObjet(new Mur(2, 3, g), 2, 3); g.setObjet(new Mur(8, 4, g), 8, 4);
        g.setObjet(new Cible(8, 1, g), 8, 1); g.setObjet(new Cible(8, 2, g), 8, 2);
        g.setObjet(new Cible(8, 5, g), 8, 5); g.setObjet(new Cible(8, 6, g), 8, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1); g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4); g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }

    private static Grille niveauSimple7() {
        Grille g = new Grille(11, 8, "Niveau 7");
        cadre(g, 11, 8);
        for (int y = 2; y < 6; y++) g.setObjet(new Mur(5, y, g), 5, y);
        g.setObjet(new Mur(2, 3, g), 2, 3); g.setObjet(new Mur(8, 2, g), 8, 2);
        g.setObjet(new Mur(8, 5, g), 8, 5);
        g.setObjet(new Cible(9, 1, g), 9, 1); g.setObjet(new Cible(9, 2, g), 9, 2);
        g.setObjet(new Cible(9, 5, g), 9, 5); g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1); g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4); g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }

    private static Grille niveauSimple8() {
        Grille g = new Grille(11, 9, "Niveau 8");
        cadre(g, 11, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 4, g), 5, 4);
        g.setObjet(new Cible(9, 2, g), 9, 2); g.setObjet(new Cible(9, 3, g), 9, 3);
        g.setObjet(new Cible(9, 5, g), 9, 5); g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Cible(9, 7, g), 9, 7);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2); g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }

    private static Grille niveauSimple9() {
        Grille g = new Grille(12, 9, "Niveau 9");
        cadre(g, 12, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 2, g), 5, 2); g.setObjet(new Mur(5, 3, g), 5, 3);
        g.setObjet(new Mur(5, 5, g), 5, 5); g.setObjet(new Mur(5, 6, g), 5, 6);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Cible(9, 3, g), 9, 3); g.setObjet(new Cible(10, 3, g), 10, 3);
        g.setObjet(new Cible(9, 4, g), 9, 4); g.setObjet(new Cible(10, 4, g), 10, 4);
        g.setObjet(new Cible(9, 5, g), 9, 5);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2); g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }

    private static Grille niveauSimple10() {
        Grille g = new Grille(13, 10, "Niveau 10");
        cadre(g, 13, 10);
        for (int i = 2; i < 8; i++) {
            if (i != 4 && i != 5) {
                g.setObjet(new Mur(4, i, g), 4, i);
                g.setObjet(new Mur(9, i, g), 9, i);
            }
        }
        g.setObjet(new Mur(6, 3, g), 6, 3); g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Mur(6, 5, g), 6, 5); g.setObjet(new Mur(6, 6, g), 6, 6);
        g.setObjet(new Mur(2, 5, g), 2, 5); g.setObjet(new Mur(11, 5, g), 11, 5);
        g.setObjet(new Cible(10, 2, g), 10, 2); g.setObjet(new Cible(11, 2, g), 11, 2);
        g.setObjet(new Cible(10, 3, g), 10, 3); g.setObjet(new Cible(11, 3, g), 11, 3);
        g.setObjet(new Cible(10, 6, g), 10, 6); g.setObjet(new Cible(11, 6, g), 11, 6);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(3, 4, g), 3, 4);
        g.setObjet(new Boite(5, 5, g), 5, 5); g.setObjet(new Boite(7, 3, g), 7, 3);
        g.setObjet(new Boite(8, 6, g), 8, 6); g.setObjet(new Boite(2, 7, g), 2, 7);
        g.setObjet(new Joueur(1, 5, g), 1, 5);
        return g;
    }
}