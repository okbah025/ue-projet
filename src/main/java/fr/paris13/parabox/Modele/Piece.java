/**
 * Classe Piece (VERSION RÉCURSIVE - PARABOX)
 *
 * Une Piece est une case spéciale qui contient un monde entier (une grille interne).
 * Dans le fichier de niveau, elle est représentée par une lettre MAJUSCULE (ex: 'E').
 *
 * Le joueur peut :
 *  - Entrer dans une Piece en se déplaçant vers elle
 *  - Se déplacer dans la grille interne
 *  - Sortir de la Piece en atteignant le bord de la grille interne
 *
 * Différence avec Boite :
 *  - Boite : boîte à pousser sur une cible, symbole '$'
 *  - Piece  : monde récursif navigable, symbole = sa lettre (ex: 'E')
 *
 * Cette classe N'AFFECTE PAS la version simple du jeu.
 */
public class Piece extends Boite {

    /** La grille interne (le monde à l'intérieur de cette Piece) */
    private Grille grilleInterne;

    /** La lettre majuscule qui identifie cette Piece dans le fichier de niveau */
    private char identifiant;

    // ========== CONSTRUCTEURS ==========

    /**
     * Constructeur principal : grille interne DÉJÀ construite.
     * Utilisé par ChargeurNiveau après avoir parsé les fichiers.
     *
     * @param x             Colonne dans la grille parente
     * @param y             Ligne dans la grille parente
     * @param parent        La grille parente
     * @param grilleInterne La grille interne déjà construite
     * @param id            La lettre identifiant cette Piece (ex: 'E')
     */
    public Piece(int x, int y, Grille parent, Grille grilleInterne, char id) {
        super(x, y, parent);
        this.identifiant = id;
        this.grilleInterne = grilleInterne;
        // Couleur violette pour distinguer visuellement les Pieces
        this.couleur = "200,50,200";
    }

    /**
     * Constructeur secondaire : grille interne VIDE à créer.
     * Utile pour les tests ou la création manuelle.
     *
     * @param x       Colonne dans la grille parente
     * @param y       Ligne dans la grille parente
     * @param parent  La grille parente
     * @param largeur Largeur de la grille interne à créer
     * @param hauteur Hauteur de la grille interne à créer
     * @param id      La lettre identifiant cette Piece
     */
    public Piece(int x, int y, Grille parent, int largeur, int hauteur, char id) {
        super(x, y, parent);
        this.identifiant = id;
        this.grilleInterne = new Grille(largeur, hauteur, "Monde_" + id);
        this.couleur = "200,50,200";
    }

    // ========== GETTERS ==========

    /**
     * Obtenir la grille interne (le monde contenu dans cette Piece).
     * @return La grille interne
     */
    public Grille getGrilleInterne() { return this.grilleInterne; }

    /**
     * Obtenir l'identifiant (lettre majuscule) de cette Piece.
     * @return Ex: 'E', 'A', 'B'...
     */
    public char getIdentifiant() { return this.identifiant; }

    // ========== MÉTHODES HÉRITÉES REDÉFINIES ==========

    /**
     * Symbole affiché : la lettre majuscule de la Piece.
     * Contrairement à une Boite qui affiche '$' ou '*',
     * une Piece affiche toujours sa lettre.
     */
    @Override
    public char getSymbole() { return this.identifiant; }

    /**
     * Une Piece ne peut PAS être poussée.
     * On y entre en se déplaçant vers elle, on ne la pousse pas.
     */
    @Override
    public boolean peutEtrePousse(Direction direction) { return false; }

    /**
     * Copier cette Piece (pour l'historique des mouvements).
     * La grille interne est partagée (copie superficielle, simplification L2).
     */
    @Override
    public Objet copier() {
        Piece copie = new Piece(this.getX(), this.getY(),
                                this.grilleParente, this.grilleInterne, this.identifiant);
        copie.setSurCible(this.surCible);
        return copie;
    }

    @Override
    public String toString() {
        return "Piece '" + identifiant + "' à " + position.toString()
               + " (grille interne : " + grilleInterne.getLargeur()
               + "x" + grilleInterne.getHauteur() + ")";
    }
}