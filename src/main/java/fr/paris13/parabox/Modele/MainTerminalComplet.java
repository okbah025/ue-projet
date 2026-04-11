package fr.paris13.parabox.Modele;
import java.util.Scanner;

/**
 * Main Terminal - VERSION COMPLÈTE AMÉLIORÉE
 * 
 * Permet de choisir entre :
 * - Version Simple (Sokoban classique)
 * - Version Récursive (Parabox)
 * 
 * Avec 10 niveaux pour chaque version
 */
public class MainTerminalComplet {
    
    // Codes ANSI pour l'affichage
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final String ROUGE = "\033[31m";
    private static final String VERT = "\033[32m";
    private static final String BLEU = "\033[34m";
    private static final String JAUNE = "\033[33m";
    private static final String CYAN = "\033[36m";
    private static final String MAGENTA = "\033[35m";
    private static final String RESET = "\033[0m";
    
    // Type de version
    private enum Version {
        SIMPLE,
        RECURSIVE
    }
    
    /**
     * Point d'entrée du jeu
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Afficher le menu principal
        effacerEcran();
        Version versionChoisie = afficherMenuPrincipal(scanner);
        
        // Afficher les niveaux disponibles
        effacerEcran();
        int choixNiveau = afficherMenuNiveaux(scanner, versionChoisie);
        
        // Créer le niveau choisi
        Grille grille = creerNiveau(versionChoisie, choixNiveau);
        if (grille == null) {
            System.out.println(ROUGE + "Niveau invalide !" + RESET);
            scanner.close();
            return;
        }
        
        // Créer le jeu
        Jeu jeu = new Jeu(grille);
        
        // Afficher les informations sur le niveau
        effacerEcran();
        afficherIntroNiveau(versionChoisie, choixNiveau);
        System.out.println("\n" + JAUNE + "Appuyez sur ENTRÉE pour commencer..." + RESET);
        scanner.nextLine();
        
        // Lancer la boucle de jeu
        jouer(jeu, scanner, versionChoisie);
        
        scanner.close();
    }
    
    /**
     * Afficher le menu principal et obtenir le choix de version
     */
    private static Version afficherMenuPrincipal(Scanner scanner) {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║                                                        ║" + RESET);
        System.out.println(CYAN + "║" + RESET + JAUNE + "           🎮  SOKOBAN / PARABOX  🎮                " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "                  Projet L2 Informatique                " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║                                                        ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(VERT + "┌────────────────────────────────────────────────────────┐" + RESET);
        System.out.println(VERT + "│" + RESET + "             CHOISISSEZ VOTRE VERSION                   " + VERT + "│" + RESET);
        System.out.println(VERT + "└────────────────────────────────────────────────────────┘" + RESET);
        System.out.println();
        System.out.println("  " + BLEU + "1." + RESET + " VERSION SIMPLE (Sokoban Classique)");
        System.out.println("     • Poussez des boîtes sur des cibles");
        System.out.println("     • 10 niveaux de difficulté croissante");
        System.out.println("     • Parfait pour débuter");
        System.out.println();
        System.out.println("  " + MAGENTA + "2." + RESET + " VERSION RÉCURSIVE (Parabox)");
        System.out.println("     • Entrez dans des boîtes-mondes");
        System.out.println("     • 10 niveaux avec récursivité croissante");
        System.out.println("     • Pour les joueurs avancés");
        System.out.println();
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
        
        int choix = 0;
        while (choix != 1 && choix != 2) {
            System.out.print(JAUNE + "\n➤ Votre choix (1 ou 2) : " + RESET);
            try {
                choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne
                
                if (choix != 1 && choix != 2) {
                    System.out.println(ROUGE + "✗ Veuillez choisir 1 ou 2 !" + RESET);
                }
            } catch (Exception e) {
                System.out.println(ROUGE + "✗ Entrée invalide !" + RESET);
                scanner.nextLine(); // Nettoyer le buffer
            }
        }
        
        return (choix == 1) ? Version.SIMPLE : Version.RECURSIVE;
    }
    
    /**
     * Afficher le menu des niveaux selon la version
     */
    private static int afficherMenuNiveaux(Scanner scanner, Version version) {
        String titre = (version == Version.SIMPLE) ? "SOKOBAN CLASSIQUE" : "PARABOX RÉCURSIF";
        String couleurTitre = (version == Version.SIMPLE) ? BLEU : MAGENTA;
        
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + RESET + couleurTitre + "              " + titre + "                    " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(VERT + "┌────────────────────────────────────────────────────────┐" + RESET);
        System.out.println(VERT + "│" + RESET + "              SÉLECTION DU NIVEAU                      " + VERT + "│" + RESET);
        System.out.println(VERT + "└────────────────────────────────────────────────────────┘" + RESET);
        System.out.println();
        
        if (version == Version.SIMPLE) {
            afficherNiveauxSimples();
        } else {
            afficherNiveauxRecursifs();
        }
        
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
        
        int choix = 0;
        while (choix < 1 || choix > 10) {
            System.out.print(JAUNE + "\n➤ Choisir un niveau (1-10) : " + RESET);
            try {
                choix = scanner.nextInt();
                scanner.nextLine();
                
                if (choix < 1 || choix > 10) {
                    System.out.println(ROUGE + "✗ Veuillez choisir entre 1 et 10 !" + RESET);
                }
            } catch (Exception e) {
                System.out.println(ROUGE + "✗ Entrée invalide !" + RESET);
                scanner.nextLine();
            }
        }
        
        return choix;
    }
    
    /**
     * Afficher la liste des niveaux simples
     */
    private static void afficherNiveauxSimples() {
        System.out.println("  " + VERT + "1." + RESET + "  Initiation        ★☆☆☆☆ │ 1 boîte,  grille 7x5");
        System.out.println("  " + VERT + "2." + RESET + "  Découverte        ★☆☆☆☆ │ 2 boîtes, grille 8x6");
        System.out.println("  " + VERT + "3." + RESET + "  Apprentissage     ★★☆☆☆ │ 2 boîtes, obstacles");
        System.out.println("  " + VERT + "4." + RESET + "  Progression       ★★☆☆☆ │ 3 boîtes, grille 9x7");
        System.out.println("  " + VERT + "5." + RESET + "  Intermédiaire     ★★★☆☆ │ 3 boîtes, labyrinthe");
        System.out.println("  " + VERT + "6." + RESET + "  Avancé            ★★★☆☆ │ 4 boîtes, grille 10x8");
        System.out.println("  " + VERT + "7." + RESET + "  Confirmé          ★★★★☆ │ 4 boîtes, complexe");
        System.out.println("  " + VERT + "8." + RESET + "  Expert            ★★★★☆ │ 5 boîtes, grille 11x9");
        System.out.println("  " + VERT + "9." + RESET + "  Maître            ★★★★★ │ 5 boîtes, très dur");
        System.out.println("  " + VERT + "10." + RESET + " Grand Maître      ★★★★★ │ 6 boîtes, ultime");
    }
    
    /**
     * Afficher la liste des niveaux récursifs
     */
    private static void afficherNiveauxRecursifs() {
        System.out.println("  " + MAGENTA + "1." + RESET + "  Initiation        ★☆☆☆☆ │ 0 récursion");
        System.out.println("  " + MAGENTA + "2." + RESET + "  Première Boîte    ★☆☆☆☆ │ 1 niveau, 1 pièce");
        System.out.println("  " + MAGENTA + "3." + RESET + "  Deux Mondes       ★★☆☆☆ │ 1 niveau, 2 pièces");
        System.out.println("  " + MAGENTA + "4." + RESET + "  Récursion Simple  ★★☆☆☆ │ 2 niveaux");
        System.out.println("  " + MAGENTA + "5." + RESET + "  Récursion Double  ★★★☆☆ │ 2 niveaux, portes");
        System.out.println("  " + MAGENTA + "6." + RESET + "  Triple Récursion  ★★★☆☆ │ 3 niveaux");
        System.out.println("  " + MAGENTA + "7." + RESET + "  Monde Complexe    ★★★★☆ │ 3 niveaux, portes");
        System.out.println("  " + MAGENTA + "8." + RESET + "  Quatre Niveaux    ★★★★☆ │ 4 niveaux");
        System.out.println("  " + MAGENTA + "9." + RESET + "  Récursion Extrême ★★★★★ │ 4+ niveaux");
        System.out.println("  " + MAGENTA + "10." + RESET + " Défi Ultime       ★★★★★ │ 5+ niveaux, expert");
    }
    
    /**
     * Afficher l'introduction du niveau
     */
    private static void afficherIntroNiveau(Version version, int niveau) {
        String[] nomsSimples = {
            "Initiation", "Découverte", "Apprentissage", "Progression",
            "Intermédiaire", "Avancé", "Confirmé", "Expert", "Maître", "Grand Maître"
        };
        
        String[] nomsRecursifs = {
            "Initiation", "Première Boîte", "Deux Mondes", "Récursion Simple",
            "Récursion Double", "Triple Récursion", "Monde Complexe", "Quatre Niveaux",
            "Récursion Extrême", "Défi Ultime"
        };
        
        String[] noms = (version == Version.SIMPLE) ? nomsSimples : nomsRecursifs;
        String couleur = (version == Version.SIMPLE) ? BLEU : MAGENTA;
        
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + RESET + couleur + "               NIVEAU " + niveau + " / 10                         " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "              " + noms[niveau - 1] + "                          " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        if (version == Version.SIMPLE) {
            System.out.println(VERT + "Objectif :" + RESET + " Poussez toutes les boîtes sur les cibles");
        } else {
            System.out.println(MAGENTA + "Objectif :" + RESET + " Naviguez entre les mondes et résolvez le puzzle");
        }
    }
    
    /**
     * Boucle principale du jeu
     */
    private static void jouer(Jeu jeu, Scanner scanner, Version version) {
        boolean continuer = true;
        
        effacerEcran();
        afficherJeu(jeu, version);
        afficherControles();
        
        while (continuer && !jeu.estNiveauTermine()) {
            System.out.print("\n" + JAUNE + "➤ " + RESET);
            String commande = scanner.nextLine().trim().toUpperCase();
            
            if (commande.isEmpty()) {
                continue;
            }
            
            char cmd = commande.charAt(0);
            Direction direction = null;
            boolean deplacementTente = false;
            
            switch (cmd) {
                case 'Z':
                case '8':
                    direction = Direction.HAUT;
                    deplacementTente = true;
                    break;
                case 'S':
                case '2':
                    direction = Direction.BAS;
                    deplacementTente = true;
                    break;
                case 'Q':
                case '4':
                    direction = Direction.GAUCHE;
                    deplacementTente = true;
                    break;
                case 'D':
                case '6':
                    direction = Direction.DROITE;
                    deplacementTente = true;
                    break;
                case 'U':
                    if (jeu.annulerMouvement()) {
                        effacerEcran();
                        afficherJeu(jeu, version);
                        System.out.println("\n" + VERT + "✓ Mouvement annulé !" + RESET);
                    } else {
                        System.out.println("\n" + ROUGE + "✗ Rien à annuler !" + RESET);
                    }
                    break;
                case 'R':
                    jeu.reinitialiser();
                    effacerEcran();
                    afficherJeu(jeu, version);
                    System.out.println("\n" + VERT + "✓ Niveau réinitialisé !" + RESET);
                    break;
                case 'H':
                    afficherControles();
                    break;
                case 'X':
                    continuer = false;
                    System.out.println("\n" + CYAN + "Merci d'avoir joué ! 👋" + RESET);
                    break;
                default:
                    System.out.println(ROUGE + "Commande invalide ! (H pour aide)" + RESET);
            }
            
            if (deplacementTente && direction != null) {
                boolean succes = jeu.deplacerJoueur(direction);
                
                if (succes) {
                    effacerEcran();
                    afficherJeu(jeu, version);
                    
                    if (jeu.estNiveauTermine()) {
                        afficherVictoire(jeu);
                    }
                } else {
                    System.out.println(ROUGE + "✗ Déplacement impossible !" + RESET);
                }
            }
        }
    }
    
    /**
     * Effacer l'écran
     */
    private static void effacerEcran() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }
    
    /**
     * Afficher l'état du jeu
     */
    private static void afficherJeu(Jeu jeu, Version version) {
        String couleur = (version == Version.SIMPLE) ? BLEU : MAGENTA;
        String titre = (version == Version.SIMPLE) ? "SOKOBAN" : "PARABOX";
        
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + RESET + couleur + "                  " + titre + " - EN JEU                   " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        // Afficher la grille
        String[] lignes = jeu.afficherGrille().split("\n");
        for (int i = 1; i < lignes.length; i++) {
            System.out.println("  " + lignes[i]);
        }
        
        System.out.println();
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
        
        // Statistiques
        Grille grille = jeu.getGrille();
        int boitesSurCibles = 0;
        for (Boite boite : grille.getBoites()) {
            if (boite.estSurCible()) {
                boitesSurCibles++;
            }
        }
        int totalCibles = grille.getCibles().size();
        
        System.out.println(CYAN + "📊 Statistiques :" + RESET);
        System.out.println("   Mouvements : " + JAUNE + jeu.getNombreMouvements() + RESET);
        System.out.println("   Poussées   : " + JAUNE + jeu.getNombrePoussees() + RESET);
        System.out.println("   Progression: " + VERT + boitesSurCibles + RESET + "/" + totalCibles + " boîtes");
        System.out.println(VERT + "─────────────────────────────────────────────────────────" + RESET);
    }
    
    /**
     * Afficher les contrôles
     */
    private static void afficherControles() {
        System.out.println();
        System.out.println(CYAN + "🎮 CONTRÔLES :" + RESET);
        System.out.println("   " + VERT + "Z" + RESET + " ou " + VERT + "8" + RESET + " = Haut    ↑");
        System.out.println("   " + VERT + "S" + RESET + " ou " + VERT + "2" + RESET + " = Bas     ↓");
        System.out.println("   " + VERT + "Q" + RESET + " ou " + VERT + "4" + RESET + " = Gauche  ←");
        System.out.println("   " + VERT + "D" + RESET + " ou " + VERT + "6" + RESET + " = Droite  →");
        System.out.println();
        System.out.println("   " + JAUNE + "U" + RESET + " = Annuler  │ " + JAUNE + "R" + RESET + " = Recommencer");
        System.out.println("   " + JAUNE + "H" + RESET + " = Aide     │ " + JAUNE + "X" + RESET + " = Quitter");
    }
    
    /**
     * Afficher l'écran de victoire
     */
    private static void afficherVictoire(Jeu jeu) {
        System.out.println();
        System.out.println(VERT + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(VERT + "║                                                        ║" + RESET);
        System.out.println(VERT + "║" + RESET + JAUNE + "            🎉 FÉLICITATIONS ! 🎉                   " + RESET + VERT + "║" + RESET);
        System.out.println(VERT + "║" + RESET + "               NIVEAU TERMINÉ !                         " + VERT + "║" + RESET);
        System.out.println(VERT + "║                                                        ║" + RESET);
        System.out.println(VERT + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println("   " + CYAN + "Score final :" + RESET);
        System.out.println("   • Mouvements : " + JAUNE + jeu.getNombreMouvements() + RESET);
        System.out.println("   • Poussées   : " + JAUNE + jeu.getNombrePoussees() + RESET);
        System.out.println();
    }
    
    /**
     * Créer un niveau selon la version et le numéro
     */
    private static Grille creerNiveau(Version version, int numero) {
        if (version == Version.SIMPLE) {
            return creerNiveauSimple(numero);
        } else {
            return creerNiveauRecursif(numero);
        }
    }
    
    // ========== NIVEAUX VERSION SIMPLE (10 niveaux) ==========
    
    private static Grille creerNiveauSimple(int numero) {
        switch (numero) {
            case 1: return niveauSimple1_Initiation();
            case 2: return niveauSimple2_Decouverte();
            case 3: return niveauSimple3_Apprentissage();
            case 4: return niveauSimple4_Progression();
            case 5: return niveauSimple5_Intermediaire();
            case 6: return niveauSimple6_Avance();
            case 7: return niveauSimple7_Confirme();
            case 8: return niveauSimple8_Expert();
            case 9: return niveauSimple9_Maitre();
            case 10: return niveauSimple10_GrandMaitre();
            default: return null;
        }
    }
    
    private static Grille niveauSimple1_Initiation() {
        Grille g = new Grille(7, 5, "Niveau 1");
        creerCadre(g, 7, 5);
        g.setObjet(new Cible(5, 2, g), 5, 2);
        g.setObjet(new Boite(3, 2, g), 3, 2);
        g.setObjet(new Joueur(1, 2, g), 1, 2);
        return g;
    }
    
    private static Grille niveauSimple2_Decouverte() {
        Grille g = new Grille(8, 6, "Niveau 2");
        creerCadre(g, 8, 6);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }
    
    private static Grille niveauSimple3_Apprentissage() {
        Grille g = new Grille(8, 6, "Niveau 3");
        creerCadre(g, 8, 6);
        g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(4, 3, g), 4, 3);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(3, 1, g), 3, 1);
        g.setObjet(new Boite(2, 4, g), 2, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }
    
    private static Grille niveauSimple4_Progression() {
        Grille g = new Grille(9, 7, "Niveau 4");
        creerCadre(g, 9, 7);
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
    
    private static Grille niveauSimple5_Intermediaire() {
        Grille g = new Grille(10, 7, "Niveau 5");
        creerCadre(g, 10, 7);
        g.setObjet(new Mur(3, 1, g), 3, 1);
        g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(6, 2, g), 6, 2);
        g.setObjet(new Mur(6, 3, g), 6, 3);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Mur(3, 5, g), 3, 5);
        g.setObjet(new Cible(8, 1, g), 8, 1);
        g.setObjet(new Cible(8, 3, g), 8, 3);
        g.setObjet(new Cible(8, 5, g), 8, 5);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(4, 5, g), 4, 5);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }
    
    private static Grille niveauSimple6_Avance() {
        Grille g = new Grille(10, 8, "Niveau 6");
        creerCadre(g, 10, 8);
        for (int y = 2; y < 6; y++) {
            g.setObjet(new Mur(5, y, g), 5, y);
        }
        g.setObjet(new Mur(2, 3, g), 2, 3);
        g.setObjet(new Mur(8, 4, g), 8, 4);
        g.setObjet(new Cible(8, 1, g), 8, 1);
        g.setObjet(new Cible(8, 2, g), 8, 2);
        g.setObjet(new Cible(8, 5, g), 8, 5);
        g.setObjet(new Cible(8, 6, g), 8, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1);
        g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4);
        g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }
    
    private static Grille niveauSimple7_Confirme() {
        Grille g = new Grille(11, 8, "Niveau 7");
        creerCadre(g, 11, 8);
        for (int y = 2; y < 6; y++) {
            g.setObjet(new Mur(5, y, g), 5, y);
        }
        g.setObjet(new Mur(2, 3, g), 2, 3);
        g.setObjet(new Mur(8, 2, g), 8, 2);
        g.setObjet(new Mur(8, 5, g), 8, 5);
        g.setObjet(new Cible(9, 1, g), 9, 1);
        g.setObjet(new Cible(9, 2, g), 9, 2);
        g.setObjet(new Cible(9, 5, g), 9, 5);
        g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1);
        g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4);
        g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }
    
    private static Grille niveauSimple8_Expert() {
        Grille g = new Grille(11, 9, "Niveau 8");
        creerCadre(g, 11, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i);
            g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i);
            g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 4, g), 5, 4);
        g.setObjet(new Cible(9, 2, g), 9, 2);
        g.setObjet(new Cible(9, 3, g), 9, 3);
        g.setObjet(new Cible(9, 5, g), 9, 5);
        g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Cible(9, 7, g), 9, 7);
        g.setObjet(new Boite(2, 2, g), 2, 2);
        g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2);
        g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }
    
    private static Grille niveauSimple9_Maitre() {
        Grille g = new Grille(12, 9, "Niveau 9");
        creerCadre(g, 12, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i);
            g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i);
            g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 2, g), 5, 2);
        g.setObjet(new Mur(5, 3, g), 5, 3);
        g.setObjet(new Mur(5, 5, g), 5, 5);
        g.setObjet(new Mur(5, 6, g), 5, 6);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Cible(9, 3, g), 9, 3);
        g.setObjet(new Cible(10, 3, g), 10, 3);
        g.setObjet(new Cible(9, 4, g), 9, 4);
        g.setObjet(new Cible(10, 4, g), 10, 4);
        g.setObjet(new Cible(9, 5, g), 9, 5);
        g.setObjet(new Boite(2, 2, g), 2, 2);
        g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2);
        g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }
    
    private static Grille niveauSimple10_GrandMaitre() {
        Grille g = new Grille(13, 10, "Niveau 10");
        creerCadre(g, 13, 10);
        // Structure complexe
        for (int i = 2; i < 5; i++) {
            g.setObjet(new Mur(4, i, g), 4, i);
            g.setObjet(new Mur(9, i, g), 9, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(4, i, g), 4, i);
            g.setObjet(new Mur(9, i, g), 9, i);
        }
        g.setObjet(new Mur(6, 3, g), 6, 3);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Mur(6, 5, g), 6, 5);
        g.setObjet(new Mur(6, 6, g), 6, 6);
        g.setObjet(new Mur(2, 5, g), 2, 5);
        g.setObjet(new Mur(11, 5, g), 11, 5);
        // Cibles groupées
        g.setObjet(new Cible(10, 2, g), 10, 2);
        g.setObjet(new Cible(11, 2, g), 11, 2);
        g.setObjet(new Cible(10, 3, g), 10, 3);
        g.setObjet(new Cible(11, 3, g), 11, 3);
        g.setObjet(new Cible(10, 6, g), 10, 6);
        g.setObjet(new Cible(11, 6, g), 11, 6);
        // Boîtes dispersées
        g.setObjet(new Boite(2, 2, g), 2, 2);
        g.setObjet(new Boite(3, 4, g), 3, 4);
        g.setObjet(new Boite(5, 5, g), 5, 5);
        g.setObjet(new Boite(7, 3, g), 7, 3);
        g.setObjet(new Boite(8, 6, g), 8, 6);
        g.setObjet(new Boite(2, 7, g), 2, 7);
        g.setObjet(new Joueur(1, 5, g), 1, 5);
        return g;
    }
    
    // ========== NIVEAUX VERSION RÉCURSIVE (10 niveaux) ==========
    
    private static Grille creerNiveauRecursif(int numero) {
        // Pour l'instant, retourner un niveau simple
        // TODO: Implémenter avec la classe Piece
        System.out.println(JAUNE + "\n⚠ Version récursive en développement !" + RESET);
        System.out.println("  Pour l'instant, voici un niveau simple.\n");
        return creerNiveauSimple(numero);
    }
    
    // ========== UTILITAIRES ==========
    
    /**
     * Créer le cadre de murs autour de la grille
     */
    private static void creerCadre(Grille g, int largeur, int hauteur) {
        for (int x = 0; x < largeur; x++) {
            g.setObjet(new Mur(x, 0, g), x, 0);
            g.setObjet(new Mur(x, hauteur - 1, g), x, hauteur - 1);
        }
        for (int y = 0; y < hauteur; y++) {
            g.setObjet(new Mur(0, y, g), 0, y);
            g.setObjet(new Mur(largeur - 1, y, g), largeur - 1, y);
        }
    }
}
