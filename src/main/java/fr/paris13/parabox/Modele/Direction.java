package fr.paris13.parabox.Modele;
/**
 * Énumération Direction
 * 
 * Cette énumération représente les 4 directions possibles de déplacement
 * dans le jeu Sokoban : HAUT, BAS, GAUCHE, DROITE.
 * 
 * Chaque direction contient son déplacement associé (dx, dy).
 * 
 */
public enum Direction {
    // Les 4 directions possibles avec leur déplacement associé
    HAUT(0, -1),      // Se déplacer vers le haut = diminuer y
    BAS(0, 1),        // Se déplacer vers le bas = augmenter y
    GAUCHE(-1, 0),    // Se déplacer vers la gauche = diminuer x
    DROITE(1, 0);     // Se déplacer vers la droite = augmenter x
    
    // Attributs : déplacement horizontal et vertical
    private final int dx;  // Déplacement sur l'axe X
    private final int dy;  // Déplacement sur l'axe Y
    
    /**
     * Constructeur privé (car c'est une énumération)
     * @param dx Le déplacement horizontal
     * @param dy Le déplacement vertical
     */
    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * Obtenir le déplacement horizontal de cette direction
     * @return Le déplacement en X
     */
    public int getDx() {
        return this.dx;
    }
    
    /**
     * Obtenir le déplacement vertical de cette direction
     * @return Le déplacement en Y
     */
    public int getDy() {
        return this.dy;
    }
    
    /**
     * Calculer la nouvelle position après un déplacement dans cette direction
     * @param pos La position de départ
     * @return La nouvelle position après déplacement
     */
    public Position appliquerSur(Position pos) {
        return new Position(pos.getX() + this.dx, pos.getY() + this.dy);
    }
    
    /**
     * Obtenir la direction opposée
     * Utile par exemple pour les annulations de mouvement
     * @return La direction opposée
     */
    public Direction getOppose() {
        switch(this) {
            case HAUT: return BAS;
            case BAS: return HAUT;
            case GAUCHE: return DROITE;
            case DROITE: return GAUCHE;
            default: return this; // Ne devrait jamais arriver
        }
    }
    
    /**
     * Convertir une chaîne de caractères en Direction
     * Utile pour le chargement de fichiers de niveaux
     * @param dir La chaîne représentant la direction ("u", "d", "l", "r")
     * @return La Direction correspondante, ou null si invalide
     */
    public static Direction depuisChar(char dir) {
        switch(dir) {
            case 'u':  // up
            case 'U':
                return HAUT;
            case 'd':  // down
            case 'D':
                return BAS;
            case 'l':  // left
            case 'L':
                return GAUCHE;
            case 'r':  // right
            case 'R':
                return DROITE;
            default:
                return null;
        }
    }
    
    /**
     * Convertir la direction en caractère pour la sauvegarde
     * Format Sokoban standard : u/d/l/r
     * @return Le caractère représentant la direction
     */
    public char versChar() {
        switch(this) {
            case HAUT: return 'u';
            case BAS: return 'd';
            case GAUCHE: return 'l';
            case DROITE: return 'r';
            default: return '?';
        }
    }
}
