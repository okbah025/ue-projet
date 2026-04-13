package fr.paris13.parabox.Modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe ChargeurNiveau
 *
 * Cette classe est responsable de la lecture et du parsage des fichiers de niveaux
 * pour la VERSION RÉCURSIVE du jeu (Parabox).
 *
 * FORMAT D'UN FICHIER DE NIVEAU :
 * ─────────────────────────────────
 * Ligne 1 : <NOM_GRILLE> <LARGEUR>
 *    - NOM_GRILLE : une lettre MAJUSCULE (ex: C, A, B...)
 *    - LARGEUR    : un entier représentant le nombre de colonnes
 *
 * Lignes suivantes : la grille elle-même (caractère par caractère)
 *    - '#' = mur
 *    - '@' = joueur
 *    - '$' = boîte normale
 *    - '.' = cible
 *    - ' ' = case vide
 *    - Lettre MAJUSCULE (A-Z sauf @) = sous-grille (enfant)
 *      → ex: 'E' signifie qu'il y a une grille nommée 'E' ici
 *
 * Après la grille parente, on retrouve les grilles enfants dans le même ordre
 * qu'elles apparaissent dans le fichier.
 *
 * EXEMPLE :
 *   C 9         ← grille "C" de largeur 9
 *   #########
 *   # @ E  .#   ← le joueur est en (2,1), grille E en (4,1), cible en (7,1)
 *   #########
 *
 *   E 5         ← sous-grille "E" de largeur 5
 *   #####
 *   #   #
 *   #####
 *
 * ATTENTION : Cette classe ne modifie PAS la version simple du jeu.
 */
public class ChargeurNiveau {

    // ========== MÉTHODE PRINCIPALE ==========

    /**
     * Charger un fichier de niveau et construire la grille principale (parent).
     *
     * @param cheminFichier Le chemin vers le fichier .txt du niveau
     * @return La grille principale du niveau, ou null en cas d'erreur
     */
    public static Grille charger(String cheminFichier) {
        try {
            // Lire toutes les lignes du fichier
            List<String> lignes = lireFichier(cheminFichier);
            if (lignes == null || lignes.isEmpty()) {
                System.out.println("Erreur : fichier vide ou introuvable -> " + cheminFichier);
                return null;
            }

            // Découper le fichier en blocs (un bloc = une grille)
            // Un bloc commence par "NOM LARGEUR" (ex: "C 9")
            List<List<String>> blocs = decouperEnBlocs(lignes);
            if (blocs.isEmpty()) {
                System.out.println("Erreur : aucun bloc trouvé dans le fichier.");
                return null;
            }

            // Parser chaque bloc pour créer les données brutes
            // On stocke les données dans une map : NOM -> données brutes
            Map<Character, DonneesGrille> donneesMap = new HashMap<>();
            for (List<String> bloc : blocs) {
                DonneesGrille donnees = parserBloc(bloc);
                if (donnees != null) {
                    donneesMap.put(donnees.nom, donnees);
                }
            }

            if (donneesMap.isEmpty()) {
                System.out.println("Erreur : impossible de parser les blocs du fichier.");
                return null;
            }

            // La grille principale est celle du premier bloc
            char nomPrincipal = blocs.get(0).get(0).charAt(0);
            DonneesGrille donneesPrincipales = donneesMap.get(nomPrincipal);
            if (donneesPrincipales == null) {
                System.out.println("Erreur : grille principale introuvable.");
                return null;
            }

            // Construire toutes les grilles (en commençant par les enfants)
            // puis assembler la grille principale avec ses Piece
            Map<Character, Grille> grillesConstituees = new HashMap<>();
            Grille grillePrincipale = construireGrille(donneesPrincipales, donneesMap, grillesConstituees);

            return grillePrincipale;

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du niveau : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ========== MÉTHODES PRIVÉES - LECTURE ==========

    /**
     * Lire toutes les lignes d'un fichier texte.
     *
     * @param cheminFichier Le chemin du fichier
     * @return La liste des lignes (peut être vide si le fichier est vide)
     */
    private static List<String> lireFichier(String cheminFichier) {
        List<String> lignes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                lignes.add(ligne); // On garde toutes les lignes, même vides
            }
        } catch (IOException e) {
            System.out.println("Impossible de lire le fichier : " + cheminFichier);
            return null;
        }
        return lignes;
    }

    /**
     * Découper la liste de toutes les lignes en blocs.
     * Un bloc correspond à une grille (nom+largeur puis les lignes de la grille).
     *
     * On détecte le début d'un nouveau bloc quand une ligne a le format :
     *   <LETTRE_MAJUSCULE> <ENTIER>
     * ex: "C 9" ou "E 5" ou "I 1"
     *
     * @param lignes Toutes les lignes du fichier
     * @return Une liste de blocs, chaque bloc étant une liste de lignes
     */
    private static List<List<String>> decouperEnBlocs(List<String> lignes) {
        List<List<String>> blocs = new ArrayList<>();
        List<String> blocCourant = null;

        for (String ligne : lignes) {
            // Gestion des lignes vides :
            // - Une ligne VIDE (length 0) sert de SÉPARATEUR entre deux blocs
            // - Une ligne avec des espaces (" ") est une ligne de grille valide !
            //   Ex: "A 1" / " " = grille 1x1 avec une case vide
            if (ligne.isEmpty()) {
                // Séparateur de blocs : terminer le bloc courant
                if (blocCourant != null && !blocCourant.isEmpty()) {
                    blocs.add(blocCourant);
                    blocCourant = null;
                }
                continue;
            }

            // Vérifier si cette ligne est un en-tête de grille ("X N" avec X majuscule, N entier)
            if (estEnTeteGrille(ligne)) {
                // Nouveau bloc : on sauvegarde l'ancien et on commence un nouveau
                if (blocCourant != null && !blocCourant.isEmpty()) {
                    blocs.add(blocCourant);
                }
                blocCourant = new ArrayList<>();
                blocCourant.add(ligne);
            } else {
                // Ligne appartenant au bloc courant (ligne de la grille)
                if (blocCourant != null) {
                    blocCourant.add(ligne);
                }
                // (si blocCourant est null, on ignore la ligne : ça ne devrait pas arriver)
            }
        }

        // Ajouter le dernier bloc
        if (blocCourant != null && !blocCourant.isEmpty()) {
            blocs.add(blocCourant);
        }

        return blocs;
    }

    /**
     * Vérifie si une ligne est un en-tête de grille.
     * Format attendu : une lettre majuscule, un espace, un entier
     * Exemples valides : "C 9", "E 5", "I 1"
     *
     * @param ligne La ligne à tester
     * @return true si c'est un en-tête de grille
     */
    private static boolean estEnTeteGrille(String ligne) {
        ligne = ligne.trim();
        // On cherche le pattern : LETTRE_MAJ ESPACE NOMBRE
        if (ligne.length() < 3) return false;
        char premierChar = ligne.charAt(0);
        if (!Character.isUpperCase(premierChar)) return false;
        if (ligne.charAt(1) != ' ') return false;
        // Le reste doit être un entier
        try {
            Integer.parseInt(ligne.substring(2).trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ========== MÉTHODES PRIVÉES - PARSING ==========

    /**
     * Classe interne pour stocker les données brutes d'une grille
     * avant de la construire en objets Java.
     */
    private static class DonneesGrille {
        char nom;           // Lettre identifiant la grille (ex: 'C', 'E')
        int largeur;        // Nombre de colonnes
        List<String> lignes; // Lignes de la grille (texte brut)

        DonneesGrille(char nom, int largeur, List<String> lignes) {
            this.nom = nom;
            this.largeur = largeur;
            this.lignes = lignes;
        }
    }

    /**
     * Parser un bloc (liste de lignes) pour en extraire les données brutes.
     *
     * @param bloc La liste de lignes d'un bloc
     * @return Les données brutes de la grille, ou null si erreur
     */
    private static DonneesGrille parserBloc(List<String> bloc) {
        if (bloc.isEmpty()) return null;

        // Première ligne : "NOM LARGEUR"
        String premiereLigne = bloc.get(0).trim();
        char nom = premiereLigne.charAt(0);
        int largeur;
        try {
            largeur = Integer.parseInt(premiereLigne.substring(2).trim());
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format pour le bloc : " + premiereLigne);
            return null;
        }

        // Les lignes suivantes = lignes de la grille
        List<String> lignesGrille = new ArrayList<>();
        for (int i = 1; i < bloc.size(); i++) {
            lignesGrille.add(bloc.get(i));
        }

        return new DonneesGrille(nom, largeur, lignesGrille);
    }

    // ========== MÉTHODES PRIVÉES - CONSTRUCTION ==========

    /**
     * Construire une Grille Java à partir des données brutes.
     * Cette méthode est récursive : si la grille contient des lettres majuscules
     * (sous-grilles), elle construit d'abord ces sous-grilles, puis les place
     * comme des objets Piece dans la grille courante.
     *
     * @param donnees         Les données brutes de la grille à construire
     * @param donneesMap      La map contenant toutes les données brutes disponibles
     * @param grillesDejaFaites Map des grilles déjà construites (pour éviter les doublons)
     * @return La Grille construite
     */
    private static Grille construireGrille(DonneesGrille donnees,
                                           Map<Character, DonneesGrille> donneesMap,
                                           Map<Character, Grille> grillesDejaFaites) {

        // Si déjà construite, on retourne la version existante
        if (grillesDejaFaites.containsKey(donnees.nom)) {
            return grillesDejaFaites.get(donnees.nom);
        }

        int largeur = donnees.largeur;
        int hauteur = donnees.lignes.size(); // La hauteur = nombre de lignes de la grille

        // Créer la grille vide
        Grille grille = new Grille(largeur, hauteur, String.valueOf(donnees.nom));

        // On l'enregistre AVANT de la remplir pour éviter les récursions infinies
        grillesDejaFaites.put(donnees.nom, grille);

        // Remplir la grille ligne par ligne
        for (int y = 0; y < hauteur; y++) {
            String ligne = donnees.lignes.get(y);

            for (int x = 0; x < largeur; x++) {
                // Si la ligne est plus courte que la largeur, on complète avec des espaces
                char c = (x < ligne.length()) ? ligne.charAt(x) : ' ';

                // Placer l'objet correspondant selon le caractère
                placerObjet(grille, c, x, y, donneesMap, grillesDejaFaites);
            }
        }

        return grille;
    }

    /**
     * Placer un objet dans la grille en fonction du caractère lu dans le fichier.
     *
     * Correspondances :
     *  '#' -> Mur
     *  '@' -> Joueur
     *  '$' -> Boite
     *  '.' -> Cible
     *  ' ' -> case vide (rien à faire)
     *  '*' -> Boite sur Cible (on place les deux)
     *  '+' -> Joueur sur Cible (on place les deux)
     *  Lettre majuscule A-Z -> Piece (sous-grille récursive)
     *
     * @param grille          La grille où placer l'objet
     * @param c               Le caractère lu
     * @param x               Position colonne
     * @param y               Position ligne
     * @param donneesMap      Toutes les données brutes disponibles
     * @param grillesDejaFaites Grilles déjà construites
     */
    private static void placerObjet(Grille grille, char c, int x, int y,
                                    Map<Character, DonneesGrille> donneesMap,
                                    Map<Character, Grille> grillesDejaFaites) {
        switch (c) {
            case '#':
                // Mur classique
                grille.setObjet(new Mur(x, y, grille), x, y);
                break;

            case '@':
                // Joueur (sur case vide)
                grille.setObjet(new Joueur(x, y, grille), x, y);
                break;

            case '+':
                // Joueur sur cible
                // On place d'abord la cible dans la liste, puis le joueur par-dessus
                Cible cibleSousJoueur = new Cible(x, y, grille);
                grille.getCibles().add(cibleSousJoueur); // On l'enregistre mais on met le joueur
                Joueur joueurSurCible = new Joueur(x, y, grille);
                joueurSurCible.setSurCible(true);
                grille.setObjet(joueurSurCible, x, y);
                break;

            case '$':
                // Boite normale
                grille.setObjet(new Boite(x, y, grille), x, y);
                break;

            case '*':
                // Boite sur cible
                Cible cibleSousBoite = new Cible(x, y, grille);
                grille.getCibles().add(cibleSousBoite); // On l'enregistre
                Boite boiteSurCible = new Boite(x, y, grille);
                boiteSurCible.setSurCible(true);
                grille.setObjet(boiteSurCible, x, y);
                break;

            case '.':
                // Cible (emplacement vide où poser une boîte)
                grille.setObjet(new Cible(x, y, grille), x, y);
                break;

            case ' ':
                // Case vide : on ne place rien (la matrice est déjà null)
                break;

            default:
                // Si c'est une lettre majuscule A-Z : c'est une sous-grille (Piece)
                if (Character.isUpperCase(c)) {
                    // Construire (ou récupérer) la sous-grille correspondante
                    DonneesGrille donneesEnfant = donneesMap.get(c);

                    if (donneesEnfant != null) {
                        // Construire la grille enfant récursivement
                        Grille grilleEnfant = construireGrille(donneesEnfant, donneesMap, grillesDejaFaites);

                        // Créer une Piece avec cette grille interne
                        Piece piece = new Piece(x, y, grille, grilleEnfant, c);
                        grille.setObjet(piece, x, y);
                    } else {
                        // La grille enfant n'est pas dans le fichier -> grille vide 1x1
                        // (cas d'une grille "I 1" avec juste "#" par exemple)
                        System.out.println("Avertissement : sous-grille '" + c + "' introuvable, ignorée.");
                        // On place un mur à la place pour ne pas laisser de case étrange
                        grille.setObjet(new Mur(x, y, grille), x, y);
                    }
                }
                // Sinon on ignore le caractère inconnu (ne devrait pas arriver)
                break;
        }
    }

    // ========== MÉTHODE UTILITAIRE ==========

    /**
     * Lister les fichiers de niveaux disponibles dans un dossier.
     * Utile pour afficher la liste des niveaux au joueur.
     *
     * @param dossier Le chemin du dossier contenant les fichiers
     * @return La liste des noms de fichiers .txt trouvés
     */
    public static List<String> listerFichiers(String dossier) {
        List<String> fichiers = new ArrayList<>();
        java.io.File dir = new java.io.File(dossier);
        if (dir.exists() && dir.isDirectory()) {
            for (java.io.File f : dir.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    fichiers.add(f.getName());
                }
            }
        }
        return fichiers;
    }
}
