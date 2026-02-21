import java.util.Stack;

/**
 * Classe Historique
 * 
 * Cette classe gère l'historique des mouvements du jeu.
 * Elle permet de sauvegarder chaque mouvement et de les annuler (Ctrl+Z).
 * 
 * Utilise une pile (Stack) pour stocker les mouvements dans l'ordre.
 * 
 */
public class Historique {
    
    // ========== CLASSE INTERNE : MOUVEMENT ==========
    
    /**
     * Classe interne représentant un mouvement
     * Contient la direction et les positions avant/après le mouvement
     */
    private class Mouvement {
        Direction direction;
        Position positionJoueurAvant;
        Position positionJoueurApres;
        // Pour gérer les boîtes poussées
        Boite boitePoussee;
        Position positionBoiteAvant;
        Position positionBoiteApres;
        boolean boiteSurCibleAvant;
        boolean boiteSurCibleApres;
        boolean joueurSurCibleAvant;
        boolean joueurSurCibleApres;
        
        /**
         * Constructeur d'un mouvement simple (sans pousser de boîte)
         */
        public Mouvement(Direction dir, Position posJoueurAvant, Position posJoueurApres,
                        boolean joueurCibleAvant, boolean joueurCibleApres) {
            this.direction = dir;
            this.positionJoueurAvant = posJoueurAvant;
            this.positionJoueurApres = posJoueurApres;
            this.joueurSurCibleAvant = joueurCibleAvant;
            this.joueurSurCibleApres = joueurCibleApres;
            this.boitePoussee = null;
        }
        
        /**
         * Constructeur d'un mouvement avec poussée de boîte
         */
        public Mouvement(Direction dir, Position posJoueurAvant, Position posJoueurApres,
                        boolean joueurCibleAvant, boolean joueurCibleApres,
                        Boite boite, Position posBoiteAvant, Position posBoiteApres,
                        boolean boiteCibleAvant, boolean boiteCibleApres) {
            this(dir, posJoueurAvant, posJoueurApres, joueurCibleAvant, joueurCibleApres);
            this.boitePoussee = boite;
            this.positionBoiteAvant = posBoiteAvant;
            this.positionBoiteApres = posBoiteApres;
            this.boiteSurCibleAvant = boiteCibleAvant;
            this.boiteSurCibleApres = boiteCibleApres;
        }
    }
    
    // ========== ATTRIBUTS ==========
    
    /**
     * Pile contenant l'historique des mouvements
     * Le dernier mouvement est au sommet de la pile
     */
    private Stack<Mouvement> mouvements;
    
    /**
     * Nombre maximum de mouvements à conserver
     * Évite de consommer trop de mémoire
     */
    private static final int TAILLE_MAX = 1000;
    
    // ========== CONSTRUCTEUR ==========
    
    /**
     * Constructeur de l'historique
     * Initialise une pile vide
     */
    public Historique() {
        this.mouvements = new Stack<>();
    }
    
    // ========== MÉTHODES PRINCIPALES ==========
    
    /**
     * Ajouter un mouvement simple à l'historique
     * (déplacement du joueur sans pousser de boîte)
     * @param direction La direction du mouvement
     * @param posAvant Position du joueur avant le mouvement
     * @param posApres Position du joueur après le mouvement
     * @param surCibleAvant Le joueur était sur une cible avant
     * @param surCibleApres Le joueur est sur une cible après
     */
    public void ajouterMouvement(Direction direction, Position posAvant, Position posApres,
                                boolean surCibleAvant, boolean surCibleApres) {
        // Créer le mouvement
        Mouvement mvt = new Mouvement(direction, posAvant, posApres, surCibleAvant, surCibleApres);
        
        // Ajouter à la pile
        mouvements.push(mvt);
        
        // Vérifier la taille maximale
        if (mouvements.size() > TAILLE_MAX) {
            // Supprimer le plus ancien (au fond de la pile)
            mouvements.remove(0);
        }
    }
    
    /**
     * Ajouter un mouvement avec poussée de boîte à l'historique
     * @param direction La direction du mouvement
     * @param posJoueurAvant Position du joueur avant
     * @param posJoueurApres Position du joueur après
     * @param joueurCibleAvant Le joueur était sur une cible avant
     * @param joueurCibleApres Le joueur est sur une cible après
     * @param boite La boîte qui a été poussée
     * @param posBoiteAvant Position de la boîte avant
     * @param posBoiteApres Position de la boîte après
     * @param boiteCibleAvant La boîte était sur une cible avant
     * @param boiteCibleApres La boîte est sur une cible après
     */
    public void ajouterMouvementAvecBoite(Direction direction,
                                         Position posJoueurAvant, Position posJoueurApres,
                                         boolean joueurCibleAvant, boolean joueurCibleApres,
                                         Boite boite,
                                         Position posBoiteAvant, Position posBoiteApres,
                                         boolean boiteCibleAvant, boolean boiteCibleApres) {
        // Créer le mouvement
        Mouvement mvt = new Mouvement(direction, posJoueurAvant, posJoueurApres,
                                     joueurCibleAvant, joueurCibleApres,
                                     boite, posBoiteAvant, posBoiteApres,
                                     boiteCibleAvant, boiteCibleApres);
        
        // Ajouter à la pile
        mouvements.push(mvt);
        
        // Vérifier la taille maximale
        if (mouvements.size() > TAILLE_MAX) {
            mouvements.remove(0);
        }
    }
    
    /**
     * Annuler le dernier mouvement (Ctrl+Z)
     * Remet le jeu dans l'état précédent
     * @param grille La grille du jeu (nécessaire pour restaurer les positions)
     * @return true si un mouvement a été annulé, false si l'historique est vide
     */
    public boolean annulerDernierMouvement(Grille grille) {
        // Vérifier qu'il y a des mouvements à annuler
        if (mouvements.isEmpty()) {
            return false;
        }
        
        // Récupérer le dernier mouvement
        Mouvement dernierMvt = mouvements.pop();
        
        // Récupérer le joueur
        Joueur joueur = grille.getJoueur();
        if (joueur == null) {
            return false;
        }
        
        // Restaurer la position du joueur
        Position posActuelle = joueur.getPosition();
        
        // Retirer le joueur de sa position actuelle
        grille.setObjet(null, posActuelle);
        
        // Si une boîte avait été poussée, la remettre à sa position précédente
        if (dernierMvt.boitePoussee != null) {
            Boite boite = dernierMvt.boitePoussee;
            Position posBoiteActuelle = boite.getPosition();
            
            // Retirer la boîte de sa position actuelle
            grille.setObjet(null, posBoiteActuelle);
            
            // Remettre la boîte à sa position précédente
            grille.setObjet(boite, dernierMvt.positionBoiteAvant);
            boite.setSurCible(dernierMvt.boiteSurCibleAvant);
        }
        
        // Remettre le joueur à sa position précédente
        grille.setObjet(joueur, dernierMvt.positionJoueurAvant);
        joueur.setSurCible(dernierMvt.joueurSurCibleAvant);
        
        return true;
    }
    
    /**
     * Vider l'historique
     * Utilisé par exemple lors du chargement d'un nouveau niveau
     */
    public void vider() {
        mouvements.clear();
    }
    
    /**
     * Vérifier si l'historique est vide
     * @return true si aucun mouvement n'est enregistré, false sinon
     */
    public boolean estVide() {
        return mouvements.isEmpty();
    }
    
    /**
     * Obtenir le nombre de mouvements dans l'historique
     * @return Le nombre de mouvements
     */
    public int getNombreMouvements() {
        return mouvements.size();
    }
    
    /**
     * Représentation textuelle de l'historique
     * @return Une description de l'historique
     */
    @Override
    public String toString() {
        return "Historique : " + mouvements.size() + " mouvement(s)";
    }
}