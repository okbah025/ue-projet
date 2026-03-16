/**
 * Classe Piece (VERSION RÉCURSIVE - PARABOX)
 * 
 * Une Piece est une boîte spéciale qui contient un monde entier.
 * Le joueur peut entrer dedans et en ressortir.
 * 
 * Différence avec Boite simple :
 * - Boite : juste une boîte à pousser
 * - Piece : contient une grille interne (monde récursif)
 */
public class Piece extends Boite {
    
    // ========== ATTRIBUTS SUPPLÉMENTAIRES ==========
    
    /**
     * La grille interne (le monde à l'intérieur de cette pièce)
     */
    private Grille grilleInterne;
    
    /**
     * Identifiant de la pièce (lettre : 'a', 'b', 'c', etc.)
     */
    private char identifiant;
    
    /**
     * Nombre de portes ouvertes (1 à 4)
     * Par défaut, toutes les 4 portes sont ouvertes
     */
    private int nombrePortes;
    
    /**
     * État des portes pour chaque direction
     * [0]=HAUT, [1]=BAS, [2]=GAUCHE, [3]=DROITE
     */
    private boolean[] portesOuvertes;
    
    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur d'une pièce-monde
     * @param x Position horizontale
     * @param y Position verticale
     * @param parent Grille parente
     * @param largeur Largeur de la grille interne
     * @param hauteur Hauteur de la grille interne
     * @param id Identifiant de la pièce
     */
    public Piece(int x, int y, Grille parent, int largeur, int hauteur, char id) {
        super(x, y, parent);
        this.identifiant = id;
        this.grilleInterne = new Grille(largeur, hauteur, "Monde_" + id);
        
        // Toutes les portes ouvertes par défaut
        this.portesOuvertes = new boolean[]{true, true, true, true};
        this.nombrePortes = 4;
        
        // Couleur violette pour les pièces-mondes
        this.couleur = "200,50,200";
    }
    
    // ========== GETTERS ==========
    
    public Grille getGrilleInterne() {
        return this.grilleInterne;
    }
    
    public char getIdentifiant() {
        return this.identifiant;
    }
    
    public int getNombrePortes() {
        return this.nombrePortes;
    }
    
    /**
     * Vérifier si une porte est ouverte
     * @param direction La direction
     * @return true si la porte est ouverte
     */
    public boolean aPorte(Direction direction) {
        int index = directionVersIndex(direction);
        return portesOuvertes[index];
    }
    
    // ========== SETTERS ==========
    
    /**
     * Ouvrir ou fermer une porte
     * @param direction La direction
     * @param ouverte true pour ouvrir, false pour fermer
     */
    public void setPorte(Direction direction, boolean ouverte) {
        int index = directionVersIndex(direction);
        boolean ancienEtat = portesOuvertes[index];
        portesOuvertes[index] = ouverte;
        
        // Mettre à jour le compteur
        if (ancienEtat && !ouverte) {
            nombrePortes--;
        } else if (!ancienEtat && ouverte) {
            nombrePortes++;
        }
    }
    
    // ========== MÉTHODES POUR ENTRER/SORTIR ==========
    
    /**
     * Faire entrer le joueur dans la pièce
     * @param joueur Le joueur qui entre
     * @param direction La direction par laquelle il entre
     * @return true si l'entrée a réussi
     */
    public boolean entrer(Joueur joueur, Direction direction) {
        // Vérifier que la porte est ouverte
        if (!aPorte(direction)) {
            return false;
        }
        
        // Calculer la position d'entrée dans la grille interne
        Position posEntree = calculerPositionEntree(direction);
        
        // Vérifier que la position est libre
        if (!grilleInterne.estCaseLibre(posEntree)) {
            return false;
        }
        
        // Retirer le joueur de la grille externe
        Grille grilleExterne = joueur.getGrilleParente();
        if (grilleExterne != null) {
            grilleExterne.setObjet(null, joueur.getPosition());
        }
        
        // Placer le joueur dans la grille interne
        grilleInterne.setObjet(joueur, posEntree);
        
        return true;
    }
    
    /**
     * Faire sortir le joueur de la pièce
     * @param joueur Le joueur qui sort
     * @param direction La direction par laquelle il sort
     * @return true si la sortie a réussi
     */
    public boolean sortir(Joueur joueur, Direction direction) {
        // Vérifier que la porte est ouverte
        if (!aPorte(direction)) {
            return false;
        }
        
        // Calculer la position de sortie dans la grille parente
        Position posSortie = direction.appliquerSur(this.position);
        
        // Vérifier que la sortie est libre
        if (grilleParente != null && !grilleParente.estCaseLibre(posSortie)) {
            return false;
        }
        
        // Retirer le joueur de la grille interne
        grilleInterne.setObjet(null, joueur.getPosition());
        
        // Placer le joueur dans la grille parente
        if (grilleParente != null) {
            grilleParente.setObjet(joueur, posSortie);
        }
        
        return true;
    }
    
    // ========== MÉTHODES UTILITAIRES ==========
    
    /**
     * Calculer la position d'entrée selon la direction
     */
    private Position calculerPositionEntree(Direction direction) {
        int largeur = grilleInterne.getLargeur();
        int hauteur = grilleInterne.getHauteur();
        
        switch (direction) {
            case HAUT:
                return new Position(largeur / 2, hauteur - 1);
            case BAS:
                return new Position(largeur / 2, 0);
            case GAUCHE:
                return new Position(largeur - 1, hauteur / 2);
            case DROITE:
                return new Position(0, hauteur / 2);
            default:
                return new Position(0, 0);
        }
    }
    
    /**
     * Convertir une Direction en index
     */
    private int directionVersIndex(Direction direction) {
        switch (direction) {
            case HAUT: return 0;
            case BAS: return 1;
            case GAUCHE: return 2;
            case DROITE: return 3;
            default: return 0;
        }
    }
    
    // ========== REDÉFINITION DES MÉTHODES ==========
    
    /**
     * Symbole : lettre minuscule ou MAJUSCULE si sur cible
     */
    @Override
    public char getSymbole() {
        if (this.surCible) {
            return Character.toUpperCase(identifiant);
        } else {
            return Character.toLowerCase(identifiant);
        }
    }
    
    @Override
    public Objet copier() {
        Piece copie = new Piece(this.getX(), this.getY(), this.grilleParente,
                               this.grilleInterne.getLargeur(),
                               this.grilleInterne.getHauteur(),
                               this.identifiant);
        copie.setSurCible(this.surCible);
        return copie;
    }
    
    @Override
    public String toString() {
        return "Piece '" + identifiant + "' à " + position.toString() +
               " (monde " + grilleInterne.getLargeur() + "x" +
               grilleInterne.getHauteur() + ")";
    }
}