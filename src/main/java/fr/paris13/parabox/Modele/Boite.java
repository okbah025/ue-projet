package fr.paris13.parabox.Modele;

/**
 * Classe Boite
 * 
 * Cette classe représente une boîte dans le jeu Sokoban.
 * Une boîte peut être poussée par le joueur et doit être placée sur une cible.
 * 
 * Caractéristiques :
 * - Peut être poussée par le joueur
 * - Ne peut pas pousser d'autres boîtes
 * - Symbole ASCII : '$' (ou '*' si sur une cible)
 * - Le but du jeu est de placer toutes les boîtes sur les cibles
 * 
 */
public class Boite extends Objet {
    
    // ========== ATTRIBUT SUPPLÉMENTAIRE ==========
    
    /**
     * Indique si la boîte est actuellement sur une cible
     * Important pour vérifier la condition de victoire
     */
    protected boolean surCible;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur d'une boîte
     * @param x Position horizontale initiale de la boîte
     * @param y Position verticale initiale de la boîte
     * @param parent La grille contenant cette boîte
     */
    public Boite(int x, int y, Grille parent) {
        super(x, y, parent);
        this.surCible = false;
        // Couleur marron/beige pour les boîtes
        this.couleur = "210,180,140";
    }
    
    /**
     * Constructeur avec un objet Position
     * @param pos La position initiale de la boîte
     * @param parent La grille contenant cette boîte
     */
    public Boite(Position pos, Grille parent) {
        super(pos, parent);
        this.surCible = false;
        this.couleur = "210,180,140";
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    /**
     * Vérifier si la boîte est sur une cible
     * @return true si la boîte est sur une cible, false sinon
     */
    public boolean estSurCible() {
        return this.surCible;
    }
    
    /**
     * Indiquer si la boîte est sur une cible
     * Cette méthode change aussi la couleur de la boîte
     * @param surCible true si la boîte est sur une cible, false sinon
     */
    public void setSurCible(boolean surCible) {
        this.surCible = surCible;
        // Changer la couleur selon l'état
        if (surCible) {
            this.couleur = "0,200,0"; // Vert si sur cible
        } else {
            this.couleur = "210,180,140"; // Marron si pas sur cible
        }
    }
    
    // ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ==========
    
    /**
     * Une boîte n'est PAS franchissable normalement
     * Elle bloque le passage sauf si elle peut être poussée
     * @return false
     */
    @Override
    public boolean estFranchissable() {
        return false;
    }
    
    /**
     * Vérifier si la boîte peut être poussée dans une direction
     * Une boîte peut être poussée si :
     * - Elle a une grille parente
     * - La case de destination est libre ou franchissable
     * - La case de destination n'est pas hors de la grille
     * 
     * @param direction La direction dans laquelle pousser la boîte
     * @return true si la boîte peut être poussée, false sinon
     */
    @Override
    public boolean peutEtrePousse(Direction direction) {
        // Vérifier qu'on a une grille parente
        if (this.grilleParente == null) {
            return false;
        }
        
        // Calculer la position après le déplacement
        Position nouvellePosition = direction.appliquerSur(this.position);
        
        // Vérifier que la nouvelle position est dans la grille
        if (!grilleParente.estDansGrille(nouvellePosition)) {
            return false;
        }
        
        // Vérifier ce qu'il y a à la position de destination
        Objet objetDestination = grilleParente.getObjet(nouvellePosition);
        
        // Si la case est vide (null) ou c'est une cible, on peut pousser
        if (objetDestination == null) {
            return true;
        }
        
        // Si c'est une cible, on peut pousser
        if (objetDestination instanceof Cible) {
            return true;
        }
        
        // Si c'est une Piece, on peut "pousser" la boîte vers elle
        // (la boîte va entrer dans la Piece, c'est JeuRecursif qui gère ensuite)
        if (objetDestination instanceof Piece) {
            return true;
        }

        // Sinon (mur, autre boîte, joueur), on ne peut pas pousser
        return false;
    }
    
    /**
     * Symbole ASCII de la boîte selon la convention Sokoban
     * '$' si sur une case normale
     * '*' si sur une cible
     * @return Le caractère représentant la boîte
     */
    @Override
    public char getSymbole() {
        if (this.surCible) {
            return '*'; // Boîte sur cible
        } else {
            return '$'; // Boîte normale
        }
    }
    
    /**
     * Créer une copie de cette boîte
     * @return Une nouvelle instance de Boite avec les mêmes propriétés
     */
    @Override
    public Objet copier() {
        Boite copie = new Boite(this.getX(), this.getY(), this.grilleParente);
        copie.setSurCible(this.surCible);
        copie.setCouleur(this.couleur);
        return copie;
    }
    
    /**
     * Représentation textuelle de la boîte
     * @return Une description de la boîte
     */
    @Override
    public String toString() {
        String etat = surCible ? " (sur cible)" : "";
        return "Boîte à " + position.toString() + etat;
    }
}
