/**
 * Classe Joueur
 * 
 * Cette classe représente le joueur (personnage) dans le jeu Sokoban.
 * C'est l'élément que le joueur contrôle avec les touches directionnelles.
 * 
 * Caractéristiques :
 * - Peut se déplacer dans les 4 directions
 * - Peut pousser des boîtes
 * - Ne peut pas être poussé
 * - Symbole ASCII : '@' (ou '+' s'il est sur une cible)
 * 
 */
public class Joueur extends Objet {
    
    // ========== ATTRIBUT SUPPLÉMENTAIRE ==========
    
    /**
     * Indique si le joueur est actuellement sur une cible
     * Important pour l'affichage et la vérification de victoire
     */
    private boolean surCible;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur d'un joueur
     * @param x Position horizontale initiale du joueur
     * @param y Position verticale initiale du joueur
     * @param parent La grille contenant le joueur
     */
    public Joueur(int x, int y, Grille parent) {
        super(x, y, parent);
        this.surCible = false;
        // Couleur bleue pour le joueur
        this.couleur = "0,0,255";
    }
    
    /**
     * Constructeur avec un objet Position
     * @param pos La position initiale du joueur
     * @param parent La grille contenant le joueur
     */
    public Joueur(Position pos, Grille parent) {
        super(pos, parent);
        this.surCible = false;
        this.couleur = "0,0,255";
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    /**
     * Vérifier si le joueur est sur une cible
     * @return true si le joueur est sur une cible, false sinon
     */
    public boolean estSurCible() {
        return this.surCible;
    }
    
    /**
     * Indiquer si le joueur est sur une cible
     * @param surCible true si le joueur est sur une cible, false sinon
     */
    public void setSurCible(boolean surCible) {
        this.surCible = surCible;
    }
    
    // ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ==========
    
    /**
     * Le joueur n'est PAS franchissable par d'autres objets
     * (même si dans ce jeu, il est le seul à pouvoir se déplacer)
     * @return false
     */
    @Override
    public boolean estFranchissable() {
        return false;
    }
    
    /**
     * Le joueur ne peut PAS être poussé
     * C'est lui qui pousse les autres objets
     * @param direction La direction (non utilisée ici)
     * @return false toujours
     */
    @Override
    public boolean peutEtrePousse(Direction direction) {
        return false;
    }
    
    /**
     * Symbole ASCII du joueur selon la convention Sokoban
     * '@' si sur une case normale
     * '+' si sur une cible
     * @return Le caractère représentant le joueur
     */
    @Override
    public char getSymbole() {
        if (this.surCible) {
            return '+'; // Joueur sur cible
        } else {
            return '@'; // Joueur normal
        }
    }
    
    /**
     * Créer une copie de ce joueur
     * @return Une nouvelle instance de Joueur avec les mêmes propriétés
     */
    @Override
    public Objet copier() {
        Joueur copie = new Joueur(this.getX(), this.getY(), this.grilleParente);
        copie.setSurCible(this.surCible);
        copie.setCouleur(this.couleur);
        return copie;
    }
    
    // ========== MÉTHODES SPÉCIFIQUES AU JOUEUR ==========
    
    /**
     * Vérifier si le joueur peut se déplacer dans une direction donnée
     * Cette méthode délègue la vérification à la grille parente
     * @param direction La direction du déplacement
     * @return true si le déplacement est possible, false sinon
     */
    public boolean peutSeDeplacer(Direction direction) {
        if (this.grilleParente == null) {
            return false;
        }
        
        // Calculer la position cible
        Position positionCible = direction.appliquerSur(this.position);
        
        // Vérifier si la position cible est valide
        return grilleParente.estDeplacementValide(this, positionCible, direction);
    }
    
    /**
     * Représentation textuelle du joueur
     * @return Une description du joueur
     */
    @Override
    public String toString() {
        String etat = surCible ? " (sur cible)" : "";
        return "Joueur à " + position.toString() + etat;
    }
}