package fr.paris13.parabox.Modele;
import fr.paris13.parabox.chemin.pile;
import fr.paris13.parabox.chemin.c_chemin;


import java.util.ArrayList;
import java.util.List;

/**
 * Classe Grille
 * 
 * Cette classe représente une grille de jeu Sokoban.
 * Une grille est une matrice 2D contenant des objets du jeu (joueur, boîtes, murs, cibles).
 * 
 * Caractéristiques :
 * - Contient tous les objets du niveau
 * - Gère les déplacements des objets
 * - Vérifie les conditions de victoire
 * - Peut être sauvegardée et chargée
 * 
 */
public class Grille {
    
    // ========== ATTRIBUTS ==========
    
    /**
     * Matrice contenant tous les objets de la grille
     * null représente une case vide
     */
    private Objet[][] matrice;
    
    /**
     * Largeur de la grille (nombre de colonnes)
     */
    private int largeur;
    
    /**
     * Hauteur de la grille (nombre de lignes)
     */
    private int hauteur;
    
    /**
     * Référence vers le joueur dans cette grille
     * Facilite l'accès au joueur pour les déplacements
     */
    private Joueur joueur;
    
    
    /** * Liste des cibles dans la grille
     * Utile pour vérifier la condition de victoire
     */
    private List<Cible> cibles;
    
    /**
     * Liste des boîtes dans la grille
     * Utile pour vérifier la condition de victoire
     */
    private List<Boite> boites;
    
    /**
     * Nom ou identifiant de la grille
     * Utile pour les niveaux à plusieurs mondes
     */
    private String nom;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur principal d'une grille vide
     * @param largeur La largeur de la grille
     * @param hauteur La hauteur de la grille
     * @param nom Le nom de la grille
     */
    public Grille(int largeur, int hauteur, String nom) {
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException("Les dimensions doivent être positives");
        }
        
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.nom = nom;
        this.matrice = new Objet[largeur][hauteur];
        this.cibles = new ArrayList<>();
        this.boites = new ArrayList<>();
        this.joueur = null;
    }
    
    /**
     * Constructeur simplifié sans nom
     * @param largeur La largeur de la grille
     * @param hauteur La hauteur de la grille
     */
    public Grille(int largeur, int hauteur) {
        this(largeur, hauteur, "Niveau");
    }
    
    // ========== GETTERS ==========
    
    /**
     * Obtenir la largeur de la grille
     * @return Le nombre de colonnes
     */
    public int getLargeur() {
        return this.largeur;
    }
    
    /**
     * Obtenir la hauteur de la grille
     * @return Le nombre de lignes
     */
    public int getHauteur() {
        return this.hauteur;
    }
    
    /**
     * Obtenir le nom de la grille
     * @return Le nom
     */
    public String getNom() {
        return this.nom;
    }
    
    /**
     * Obtenir le joueur de cette grille
     * @return Le joueur, ou null s'il n'y en a pas
     */
    public Joueur getJoueur() {
        return this.joueur;
    }
    
    /**
     * Obtenir la liste des cibles
     * @return La liste des cibles
     */
    public List<Cible> getCibles() {
        return this.cibles;
    }
    
    /**
     * Obtenir la liste des boîtes
     * @return La liste des boîtes
     */
    public List<Boite> getBoites() {
        return this.boites;
    }
    
    /**
     * Obtenir un objet à une position donnée
     * @param x La position horizontale
     * @param y La position verticale
     * @return L'objet à cette position, ou null si case vide ou hors grille
     */
    public Objet getObjet(int x, int y) {
        if (!estDansGrille(x, y)) {
            return null;
        }
        return matrice[x][y];
    }
    
    /**
     * Obtenir un objet à une position donnée (version avec Position)
     * @param pos La position
     * @return L'objet à cette position, ou null si case vide ou hors grille
     */
    public Objet getObjet(Position pos) {
        return getObjet(pos.getX(), pos.getY());
    }
    
    // ========== SETTERS ==========
    
    /**
     * Placer un objet dans la grille
     * Met automatiquement à jour les listes de cibles, boîtes et joueur
     * @param objet L'objet à placer
     * @param x La position horizontale
     * @param y La position verticale
     */
    public void setObjet(Objet objet, int x, int y) {
        if (!estDansGrille(x, y)) {
            throw new IllegalArgumentException("Position hors de la grille");
        }
        
        // Mettre à jour la matrice
        matrice[x][y] = objet;
        
        // Si l'objet n'est pas null, mettre à jour sa position et sa grille parente
        if (objet != null) {
            objet.setPosition(x, y);
            objet.setGrilleParente(this);
            
            // Mettre à jour les références selon le type d'objet
            if (objet instanceof Joueur) {
                this.joueur = (Joueur) objet;
            } else if (objet instanceof Cible) {
                if (!cibles.contains(objet)) {
                    cibles.add((Cible) objet);
                }
            } else if (objet instanceof Boite) {
                if (!boites.contains(objet)) {
                    boites.add((Boite) objet);
                }
            }
        }
    }
    
    /**
     * Placer un objet dans la grille (version avec Position)
     * @param objet L'objet à placer
     * @param pos La position
     */
    public void setObjet(Objet objet, Position pos) {
        setObjet(objet, pos.getX(), pos.getY());
    }

    /**
    * Retirer l'objet à une position donnée (mettre la case à null).
    * Utilisé par la version RÉCURSIVE pour sortir le joueur d'une grille
    * quand il entre dans une Piece ou en sort.
    *
    * Si une cible se trouvait sous l'objet, elle est automatiquement
    * remise à la surface (visible dans la matrice).
    *
    * REMARQUE : cette méthode ne supprime PAS l'objet des listes internes
    * (boites, cibles) car l'objet continue d'exister dans le jeu,
    * simplement dans une autre grille.
    *
    * @param pos La position à vider
    */
    public void retirerObjet(Position pos) {
    if (!estDansGrille(pos)) return;

    Objet objet = matrice[pos.getX()][pos.getY()];

    // Si c'était le joueur, on efface la référence interne
    if (objet instanceof Joueur) {
        this.joueur = null;
    }

    // Vider la case
    matrice[pos.getX()][pos.getY()] = null;

    // Si une cible était sous l'objet, la remettre visible dans la matrice
    Cible cible = trouverCible(pos);
    if (cible != null) {
        matrice[pos.getX()][pos.getY()] = cible;
        // Si c'était une boîte qui était dessus, libérer la cible
        if (objet instanceof Boite) {
            cible.setOccupee(false);
        }
    }
}
    
    // ========== MÉTHODES DE VÉRIFICATION ==========
    
    /**
     * Vérifier si une position est dans la grille
     * @param x Position horizontale
     * @param y Position verticale
     * @return true si dans la grille, false sinon
     */
    public boolean estDansGrille(int x, int y) {
        return (x >= 0 && x < largeur && y >= 0 && y < hauteur);
    }
    
    /**
     * Vérifier si une position est dans la grille (version avec Position)
     * @param pos La position à vérifier
     * @return true si dans la grille, false sinon
     */
    public boolean estDansGrille(Position pos) {
        return estDansGrille(pos.getX(), pos.getY());
    }
    
    /**
     * Vérifier si une case est vide (ou contient uniquement une cible)
     * @param x Position horizontale
     * @param y Position verticale
     * @return true si la case est libre, false sinon
     */
    public boolean estCaseLibre(int x, int y) {
        if (!estDansGrille(x, y)) {
            return false;
        }
        
        Objet objet = matrice[x][y];
        // Une case est libre si elle est null ou si c'est une cible
        return (objet == null || objet instanceof Cible);
    }
    
    /**
     * Vérifier si une case est vide (version avec Position)
     * @param pos La position à vérifier
     * @return true si la case est libre, false sinon
     */
    public boolean estCaseLibre(Position pos) {
        return estCaseLibre(pos.getX(), pos.getY());
    }
    
    /**
     * Vérifier si le niveau est gagné
     * Le niveau est gagné si toutes les boîtes sont sur des cibles
     * @return true si le niveau est gagné, false sinon
     */
    
    public boolean estNiveauGagne() {
        int nombreBoitesNormales = 0;
        for (Boite boite : boites) {
            if (boite instanceof Piece) continue; // Les Pieces ne comptent pas
            nombreBoitesNormales++;
            if (!boite.estSurCible()) return false;
        }
        return nombreBoitesNormales > 0;
    }

    
    /**
     * Vérifier si un déplacement est valide pour un objet
     * @param objet L'objet qui veut se déplacer
     * @param destination La position de destination
     * @param direction La direction du déplacement
     * @return true si le déplacement est possible, false sinon
     */
    public boolean estDeplacementValide(Objet objet, Position destination, Direction direction) {
        // Vérifier que la destination est dans la grille
        if (!estDansGrille(destination)) {
            return false;
        }
        
        // Récupérer l'objet à la position de destination
        Objet objetDestination = getObjet(destination);
        
        // Si la case est vide ou c'est une cible, le déplacement est valide
        if (objetDestination == null || objetDestination instanceof Cible) {
            return true;
        }
        
        // Si c'est une boîte, vérifier si on peut la pousser
        if (objetDestination instanceof Boite) {
            Boite boite = (Boite) objetDestination;
            return boite.peutEtrePousse(direction);
        }
        
        // Sinon (mur, autre joueur), le déplacement n'est pas valide
        return false;
    }
    
    // ========== MÉTHODES DE DÉPLACEMENT ==========
    
    /**
     * Déplacer le joueur dans une direction
     * Cette méthode gère aussi le déplacement des boîtes si nécessaire
     * @param direction La direction du déplacement
     * @return true si le déplacement a réussi, false sinon
     */
    public boolean deplacerJoueur(Direction direction) {
        if (joueur == null) {
            return false;
        }
        
        // Calculer la nouvelle position du joueur
        Position positionActuelle = joueur.getPosition();
        Position nouvellePosition = direction.appliquerSur(positionActuelle);
        
        // Vérifier que la nouvelle position est dans la grille
        if (!estDansGrille(nouvellePosition)) {
            return false;
        }
        
        // Récupérer l'objet à la nouvelle position
        Objet objetDestination = getObjet(nouvellePosition);
        
        // Cas 1 : Case vide ou cible -> déplacement simple
        if (objetDestination == null || objetDestination instanceof Cible) {
            return deplacerObjetSimple(joueur, nouvellePosition);
        }
        
        // Cas 2 : Boîte NORMALE (pas une Piece) -> essayer de pousser
        if (objetDestination instanceof Boite && !(objetDestination instanceof Piece)) {
            Boite boite = (Boite) objetDestination;
            if (deplacerBoite(boite, direction)) {
                return deplacerObjetSimple(joueur, nouvellePosition);
            }
            return false;
        }

        // Cas 2b : Piece (version récursive) -> JeuRecursif gère l'entrée
        // On retourne false ici pour signaler à JeuRecursif d'intercepter.
        if (objetDestination instanceof Piece) {
            return false;
        }
        
        // Cas 3 : Mur ou autre obstacle -> déplacement impossible
        return false;
    }
    
    /**
     * Déplacer une boîte dans une direction
     * @param boite La boîte à déplacer
     * @param direction La direction du déplacement
     * @return true si le déplacement a réussi, false sinon
     */
    private boolean deplacerBoite(Boite boite, Direction direction) {
        // Vérifier que la boîte peut être poussée
        if (!boite.peutEtrePousse(direction)) {
            return false;
        }
        
        // Calculer la nouvelle position de la boîte
        Position positionActuelle = boite.getPosition();
        Position nouvellePosition = direction.appliquerSur(positionActuelle);
        
        // Déplacer la boîte
        return deplacerObjetSimple(boite, nouvellePosition);
    }
    
    /**
     * Déplacer un objet simple (sans pousser d'autres objets)
     * Gère automatiquement les cibles
     * @param objet L'objet à déplacer
     * @param nouvellePosition La nouvelle position
     * @return true si le déplacement a réussi, false sinon
     */
    private boolean deplacerObjetSimple(Objet objet, Position nouvellePosition) {
        Position anciennePosition = objet.getPosition();
        
        // Récupérer ce qu'il y a aux deux positions
        Objet objetDestination = getObjet(nouvellePosition);
        Objet objetDepart = getObjet(anciennePosition);
        
        // Gérer le départ de l'ancienne position
        // S'il y a une cible sous l'objet, la remettre
        Cible cibleDepart = trouverCible(anciennePosition);
        if (cibleDepart != null) {
            matrice[anciennePosition.getX()][anciennePosition.getY()] = cibleDepart;
            
            // Si c'était une boîte, marquer la cible comme libre
            if (objet instanceof Boite) {
                cibleDepart.setOccupee(false);
            }
            // Si c'était le joueur, il n'est plus sur une cible
            if (objet instanceof Joueur) {
                ((Joueur) objet).setSurCible(false);
            }
        } else {
            // Sinon, laisser la case vide
            matrice[anciennePosition.getX()][anciennePosition.getY()] = null;
        }
        
        // Gérer l'arrivée à la nouvelle position
        // S'il y a une cible à la destination
        if (objetDestination instanceof Cible) {
            Cible cibleDestination = (Cible) objetDestination;
            
            // Si c'est une boîte, marquer la cible comme occupée
            if (objet instanceof Boite) {
                ((Boite) objet).setSurCible(true);
                cibleDestination.setOccupee(true);
            }
            // Si c'est le joueur, il est maintenant sur une cible
            if (objet instanceof Joueur) {
                ((Joueur) objet).setSurCible(true);
            }
        } else {
            // Si pas de cible à la destination
            if (objet instanceof Boite) {
                ((Boite) objet).setSurCible(false);
            }
            if (objet instanceof Joueur) {
                ((Joueur) objet).setSurCible(false);
            }
        }
        
        // Placer l'objet à sa nouvelle position
        matrice[nouvellePosition.getX()][nouvellePosition.getY()] = objet;
        objet.setPosition(nouvellePosition);
        
        return true;
    }
    
    /**
     * Trouver une cible à une position donnée
     * @param pos La position
     * @return La cible si elle existe, null sinon
     */
    private Cible trouverCible(Position pos) {
        for (Cible cible : cibles) {
            if (cible.getPosition().equals(pos)) {
                return cible;
            }
        }
        return null;
    }
    
    // ========== MÉTHODES D'AFFICHAGE ==========
    
    /**
     * Afficher la grille en mode texte
     * @return Une chaîne représentant la grille
     */
    public String afficherGrille() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" ").append(largeur).append("\n");
        
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                Objet objet = matrice[x][y];
                if (objet == null) {
                    sb.append(' '); // Case vide
                } else {
                    sb.append(objet.getSymbole());
                }
            }
            sb.append('\n');
        }
        
        return sb.toString();
    }
    
    /**
     * Représentation textuelle de la grille
     * @return Une description de la grille
     */
    @Override
    public String toString() {
        return "Grille '" + nom + "' (" + largeur + "x" + hauteur + ") - " +
               boites.size() + " boîtes, " + cibles.size() + " cibles";
    }
     
    
    public boolean[][] genererMatricebol() {
        boolean[][] M = new boolean[this.largeur][this.hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                M[x][y] = estCaseLibre(x, y);
            }
        }

        return M ;
    } 
    public void chemin_court(int x, int y) {

		  
        Joueur j = this.getJoueur();
        if (j == null) {
            System.out.println("Pas de joueur !");
            return;
        }

        int x1 = j.getPosition().getX();
        int y1 = j.getPosition().getY();

        boolean[][] M = this.genererMatricebol();

        // Appel de ton algo
        pile pi = c_chemin.c_chemin(M, x1, y1, x, y);

        if (pi == null) {
            System.out.println("Pas de chemin !");
            return;
        }

        // tableau d'affichage (ligne = y, colonne = x)
        char[][] affichage = new char[this.hauteur][this.largeur];

        //  copier la grille
        for (int y2 = 0; y2 < hauteur; y2++) {
            for (int x2 = 0; x2 < largeur; x2++) {
                Objet obj = this.getObjet(x2, y2);

                if (obj == null) {
                    affichage[y2][x2] = ' ';
                } else {
                    affichage[y2][x2] = obj.getSymbole();
                }
            }
        }

        // dessiner le chemin
        while (!pi.isEmpty()) {
            Position p = pi.depiler();

            // on évite d'écraser mur, joueur, boite...
            if (affichage[p.getY()][p.getX()] == ' ') {
            affichage[p.getY()][p.getX()] = '+';
            }
        }

        // remettre le joueur
        affichage[y1][x1] = '@';

        //  afficher
        System.out.println("Chemin le plus court :");

        for (int y2 = 0; y2 < hauteur; y2++) {
            for (int x2 = 0; x2 < largeur; x2++) {
                System.out.print(affichage[y2][x2]);
            }
        System.out.println();
        }
    }

}
         
         
         
          
