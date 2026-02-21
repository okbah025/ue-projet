/**
 * Classe Position
 * 
 * Cette classe représente une position (coordonnées) dans le jeu.
 * Elle permet de simplifier la gestion des coordonnées x et y.
 * 
 */
public class Position {
    // Attributs : coordonnées de la position
    private int x;  // Position horizontale (colonne)
    private int y;  // Position verticale (ligne)
    
    /**
     * Constructeur de Position
     * @param x La coordonnée horizontale (colonne)
     * @param y La coordonnée verticale (ligne)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Obtenir la coordonnée X
     * @return La position horizontale
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * Obtenir la coordonnée Y
     * @return La position verticale
     */
    public int getY() {
        return this.y;
    }
    
    /**
     * Modifier la coordonnée X
     * @param x La nouvelle position horizontale
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Modifier la coordonnée Y
     * @param y La nouvelle position verticale
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Modifier les deux coordonnées en même temps
     * @param x La nouvelle position horizontale
     * @param y La nouvelle position verticale
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Créer une nouvelle position en ajoutant un déplacement
     * @param dx Déplacement horizontal
     * @param dy Déplacement vertical
     * @return Une nouvelle Position
     */
    public Position ajouterDeplacement(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }
    
    /**
     * Vérifier si deux positions sont identiques
     * @param autre L'autre position à comparer
     * @return true si les positions sont identiques, false sinon
     */
    public boolean equals(Position autre) {
        if (autre == null) {
            return false;
        }
        return (this.x == autre.x && this.y == autre.y);
    }
    
    /**
     * Représentation textuelle de la position
     * Utile pour le débogage et l'affichage
     * @return Une chaîne décrivant la position
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}