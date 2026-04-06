/**
 * Classe abstraite Objet
 * 
 * Cette classe représente un objet générique dans le jeu.
 * Tous les éléments du jeu (Joueur, Boite, Mur, Cible, etc.) héritent de cette classe.
 * 
 * Un objet possède :
 * - Une position (x, y) dans sa grille parente
 * - Une référence vers la grille qui le contient
 * - Une couleur pour l'affichage (utilisée par l'interface graphique)
 * 
 */
public abstract class Objet {
    
    // ========== ATTRIBUTS ==========
    
    /**
     * Position de l'objet dans sa grille parente
     */
    protected Position position;

    protected Position oldPos;
    
    /**
     * Référence vers la grille qui contient cet objet
     * Peut être null si l'objet n'est pas encore placé
     */
    protected Grille grilleParente;
    
    /**
     * Couleur de l'objet pour l'affichage graphique
     * Utilisé par le responsable Interface Graphique
     * Format RGB : exemple "255,0,0" pour rouge
     */
    protected String couleur;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur principal d'un objet
     * @param x Position horizontale initiale
     * @param y Position verticale initiale
     * @param parent La grille qui contient cet objet
     */
    public Objet(int x, int y, Grille parent) {
        this.position = new Position(x, y);
        this.oldPos = null;
        this.grilleParente = parent;
        this.couleur = "255,255,255"; // Blanc par défaut
    }
    
    /**
     * Constructeur avec une Position
     * @param pos La position initiale
     * @param parent La grille qui contient cet objet
     */
    public Objet(Position pos, Grille parent) {
        this.position = pos;
        this.oldPos = null;
        this.grilleParente = parent;
        this.couleur = "255,255,255"; // Blanc par défaut
    }
    
    // ========== GETTERS (Accesseurs) ==========
    
    /**
     * Obtenir la position X de l'objet
     * @return La coordonnée horizontale
     */
    public int getX() {
        return this.position.getX();
    }
    
    /**
     * Obtenir la position Y de l'objet
     * @return La coordonnée verticale
     */
    public int getY() {
        return this.position.getY();
    }

    public int getOldX() {
        return this.oldPos.getX();
    }
    
    public int getOldY() {
        return this.oldPos.getY();
    }

    /**
     * Obtenir la position complète de l'objet
     * @return L'objet Position contenant x et y
     */
    public Position getPosition() {
        return this.position;
    }

    public Position getOldPos(){
        return this.oldPos;
    }
    
    /**
     * Obtenir la grille parente de l'objet
     * @return La grille qui contient cet objet
     */
    public Grille getGrilleParente() {
        return this.grilleParente;
    }
    
    /**
     * Obtenir la couleur de l'objet
     * @return La couleur au format RGB
     */
    public String getCouleur() {
        return this.couleur;
    }
    
    // ========== SETTERS (Mutateurs) ==========
    
    /**
     * Modifier la position de l'objet
     * @param nouvelleX Nouvelle position horizontale
     * @param nouvelleY Nouvelle position verticale
     */
    public void setPosition(int nouvelleX, int nouvelleY) {
        this.oldPos = getPosition();
        this.position.setPosition(nouvelleX, nouvelleY);
    }
    
    /**
     * Modifier la position de l'objet avec un objet Position
     * @param nouvellePosition La nouvelle position
     */
    public void setPosition(Position nouvellePosition) {
        this.oldPos = getPosition();
        this.position = nouvellePosition;
    }
    
    /**
     * Modifier la grille parente de l'objet
     * Utile quand un objet change de grille (entre/sort d'une boîte-monde)
     * @param nouvelleGrille La nouvelle grille parente
     */
    public void setGrilleParente(Grille nouvelleGrille) {
        this.grilleParente = nouvelleGrille;
    }
    
    /**
     * Modifier la couleur de l'objet
     * @param nouvelleCouleur La nouvelle couleur au format RGB
     */
    public void setCouleur(String nouvelleCouleur) {
        this.couleur = nouvelleCouleur;
    }
    
    // ========== MÉTHODES ABSTRAITES ==========
    
    /**
     * Méthode abstraite pour vérifier si l'objet est franchissable
     * Un objet franchissable permet au joueur de se déplacer dessus
     * 
     * Exemples :
     * - Case vide (null) : franchissable
     * - Cible : franchissable
     * - Mur : NON franchissable
     * - Boîte : dépend (franchissable si on peut la pousser)
     * 
     * @return true si le joueur peut passer sur cet objet, false sinon
     */
    public abstract boolean estFranchissable();
    
    /**
     * Méthode abstraite pour vérifier si l'objet peut être poussé
     * 
     * Exemples :
     * - Boîte : peut être poussée
     * - Mur : ne peut pas être poussé
     * - Joueur : ne peut pas être poussé
     * 
     * @param direction La direction dans laquelle on veut pousser l'objet
     * @return true si l'objet peut être poussé dans cette direction, false sinon
     */
    public abstract boolean peutEtrePousse(Direction direction);
    
    /**
     * Méthode abstraite pour obtenir le symbole ASCII de l'objet
     * Utilisé pour l'affichage en mode texte et la sauvegarde de niveaux
     * 
     * Convention Sokoban :
     * - '#' pour mur
     * - '@' pour joueur
     * - '$' pour boîte
     * - '.' pour cible
     * - ' ' pour case vide
     * - '*' pour boîte sur cible
     * - '+' pour joueur sur cible
     * 
     * @return Le caractère représentant cet objet
     */
    public abstract char getSymbole();
    
    /**
     * Méthode abstraite pour obtenir une copie de l'objet
     * Utile pour la sauvegarde d'état (annulation de mouvement)
     * @return Une copie de cet objet
     */
    public abstract Objet copier();
    
    // ========== MÉTHODES UTILES ==========
    
    /**
     * Vérifier si cet objet est à la même position qu'un autre
     * @param autre L'autre objet à comparer
     * @return true si les positions sont identiques, false sinon
     */
    public boolean memePosition(Objet autre) {
        if (autre == null) {
            return false;
        }
        return this.position.equals(autre.position);
    }
    
    /**
     * Représentation textuelle de l'objet
     * Utile pour le débogage
     * @return Une description de l'objet
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " à " + position.toString();
    }
}
