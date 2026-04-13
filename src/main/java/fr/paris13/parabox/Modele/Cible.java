package fr.paris13.parabox.Modele;

/**
 * Classe Cible
 * 
 * Cette classe représente une cible (destination) dans le jeu Sokoban.
 * Une cible est un emplacement où une boîte doit être placée.
 * 
 * Caractéristiques :
 * - Franchissable : le joueur et les boîtes peuvent passer dessus
 * - Ne peut pas être poussée
 * - Symbole ASCII : '.'
 * - Le niveau est gagné quand toutes les cibles ont une boîte dessus
 * 
 */
public class Cible extends Objet {
    
    // ========== ATTRIBUT SUPPLÉMENTAIRE ==========
    
    /**
     * Indique si une boîte est actuellement placée sur cette cible
     */
    private boolean estOccupee;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur d'une cible
     * @param x Position horizontale de la cible
     * @param y Position verticale de la cible
     * @param parent La grille contenant cette cible
     */
    public Cible(int x, int y, Grille parent) {
        super(x, y, parent);
        this.estOccupee = false;
        // Couleur jaune/orange pour les cibles
        this.couleur = "255,165,0";
    }
    
    /**
     * Constructeur avec un objet Position
     * @param pos La position de la cible
     * @param parent La grille contenant cette cible
     */
    public Cible(Position pos, Grille parent) {
        super(pos, parent);
        this.estOccupee = false;
        this.couleur = "255,165,0";
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    /**
     * Vérifier si la cible est occupée par une boîte
     * @return true si une boîte est sur cette cible, false sinon
     */
    public boolean estOccupee() {
        return this.estOccupee;
    }
    
    /**
     * Marquer la cible comme occupée ou libre
     * @param occupee true si une boîte est placée, false si elle est retirée
     */
    public void setOccupee(boolean occupee) {
        this.estOccupee = occupee;
        // Changer la couleur selon l'état
        if (occupee) {
            this.couleur = "0,255,0"; // Vert si occupée
        } else {
            this.couleur = "255,165,0"; // Orange si libre
        }
    }
    
    // ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ==========
    
    /**
     * Une cible EST franchissable
     * Le joueur et les boîtes peuvent passer dessus
     * @return true toujours
     */
    @Override
    public boolean estFranchissable() {
        return true;
    }
    
    /**
     * Une cible ne peut PAS être poussée
     * C'est un élément fixe du terrain
     * @param direction La direction (non utilisée ici)
     * @return false toujours
     */
    @Override
    public boolean peutEtrePousse(Direction direction) {
        return false;
    }
    
    /**
     * Symbole ASCII d'une cible selon la convention Sokoban
     * @return Le caractère '.'
     */
    @Override
    public char getSymbole() {
        return '.';
    }
    
    /**
     * Créer une copie de cette cible
     * @return Une nouvelle instance de Cible avec les mêmes propriétés
     */
    @Override
    public Objet copier() {
        Cible copie = new Cible(this.getX(), this.getY(), this.grilleParente);
        copie.setOccupee(this.estOccupee);
        copie.setCouleur(this.couleur);
        return copie;
    }
    
    /**
     * Représentation textuelle de la cible
     * @return Une description de la cible
     */
    @Override
    public String toString() {
        String etat = estOccupee ? " (occupée)" : " (libre)";
        return "Cible à " + position.toString() + etat;
    }
}
