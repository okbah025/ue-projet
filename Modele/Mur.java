/**
 * Classe Mur
 * 
 * Cette classe représente un mur dans le jeu Sokoban.
 * Un mur est un obstacle infranchissable et qui ne peut pas être poussé.
 * 
 * Caractéristiques :
 * - Bloque le passage du joueur
 * - Ne peut pas être déplacé
 * - Symbole ASCII : '#'
 * 
 */
public class Mur extends Objet {
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur d'un mur
     * @param x Position horizontale du mur
     * @param y Position verticale du mur
     * @param parent La grille contenant ce mur
     */
    public Mur(int x, int y, Grille parent) {
        super(x, y, parent);
        // Couleur grise pour les murs
        this.couleur = "128,128,128";
    }
    
    /**
     * Constructeur avec un objet Position
     * @param pos La position du mur
     * @param parent La grille contenant ce mur
     */
    public Mur(Position pos, Grille parent) {
        super(pos, parent);
        this.couleur = "128,128,128";
    }
    
    // ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ==========
    
    /**
     * Un mur n'est PAS franchissable
     * Le joueur ne peut pas passer à travers un mur
     * @return false toujours
     */
    @Override
    public boolean estFranchissable() {
        return false;
    }
    
    /**
     * Un mur ne peut PAS être poussé
     * C'est un obstacle fixe
     * @param direction La direction (non utilisée ici)
     * @return false toujours
     */
    @Override
    public boolean peutEtrePousse(Direction direction) {
        return false;
    }
    
    /**
     * Symbole ASCII d'un mur selon la convention Sokoban
     * @return Le caractère '#'
     */
    @Override
    public char getSymbole() {
        return '#';
    }
    
    /**
     * Créer une copie de ce mur
     * Utile pour la sauvegarde d'état du jeu
     * @return Une nouvelle instance de Mur avec les mêmes propriétés
     */
    @Override
    public Objet copier() {
        Mur copie = new Mur(this.getX(), this.getY(), this.grilleParente);
        copie.setCouleur(this.couleur);
        return copie;
    }
    
    /**
     * Représentation textuelle du mur
     * @return Une description du mur
     */
    @Override
    public String toString() {
        return "Mur à " + position.toString();
    }
}