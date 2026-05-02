package fr.paris13.parabox.Modele;

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
        // Pour gérer les transitions de grille (entrée/sortie d'une Piece)
        // Si ce champ est non-null, c'est un mouvement de transition, pas un déplacement normal
        Piece pieceTransition;      // La Piece dans laquelle on entre (ou sort)
        boolean estEntree;          // true = entrée dans la Piece, false = sortie
        Grille grilleAvant;         // La grille où était le joueur avant la transition
        Grille grilleApres;         // La grille où se trouve le joueur après la transition
        Position posJoueurDansGrilleAvant;  // Position du joueur dans la grille d'avant
        Position posJoueurDansGrilleApres;  // Position du joueur dans la grille d'après
        
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

        /**
         * Constructeur d'un mouvement de transition (entrée ou sortie de Piece).
         *
         * @param dir       La direction du déplacement
         * @param piece     La Piece concernée
         * @param entree    true si c'est une entrée dans la Piece, false si c'est une sortie
         * @param grilleAv  La grille où était le joueur avant
         * @param grilleAp  La grille où est le joueur après
         * @param posAv     Position du joueur dans la grille d'avant
         * @param posAp     Position du joueur dans la grille d'après
         */
        public Mouvement(Direction dir, Piece piece, boolean entree,
                         Grille grilleAv, Grille grilleAp,
                         Position posAv, Position posAp) {
            this.direction = dir;
            this.pieceTransition = piece;
            this.estEntree = entree;
            this.grilleAvant = grilleAv;
            this.grilleApres = grilleAp;
            this.posJoueurDansGrilleAvant = posAv;
            this.posJoueurDansGrilleApres = posAp;
            this.boitePoussee = null;
            // Les positions joueurAvant/Apres "standards" ne sont pas utilisées ici
            this.positionJoueurAvant = posAv;
            this.positionJoueurApres = posAp;
        }
    }
    
    // ========== ATTRIBUTS ==========
    
    /**
     * Pile contenant l'historique des mouvements
     * Le dernier mouvement est au sommet de la pile
     */
    private Stack<Mouvement> mouvements;
    
    /**
     * Informations sur la dernière annulation, si c'était une transition de grille.
     * Utilisé par JeuRecursif.annulerMouvement() pour ajuster ses piles.
     */
    public boolean estDerniereAnnulationTransition = false;
    public boolean derniereTransitionEstEntree = false;
    public Piece derniereTransitionPiece = null;
    
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
     * Ajouter une transition de grille à l'historique.
     * Utilisé quand le joueur entre ou sort d'une Piece.
     *
     * @param direction La direction du déplacement
     * @param piece     La Piece concernée (le monde dans lequel on entre/sort)
     * @param estEntree true = le joueur vient d'entrer dans la Piece,
     *                  false = le joueur vient de sortir de la Piece
     * @param grilleAvant La grille parente (où était le joueur avant)
     * @param grilleApres La grille interne (où est le joueur après, si entrée)
     *                    ou la grille parente (si sortie)
     * @param posAvant  Position du joueur avant la transition
     * @param posApres  Position du joueur après la transition
     */
    public void ajouterTransition(Direction direction, Piece piece, boolean estEntree,
                                   Grille grilleAvant, Grille grilleApres,
                                   Position posAvant, Position posApres) {
        Mouvement mvt = new Mouvement(direction, piece, estEntree,
                                      grilleAvant, grilleApres, posAvant, posApres);
        mouvements.push(mvt);
        if (mouvements.size() > TAILLE_MAX) {
            mouvements.remove(0);
        }
    }

    /**
     * Annuler le dernier mouvement (Ctrl+Z / touche U).
     * Gère trois cas :
     *  - Mouvement simple : repositionne le joueur
     *  - Mouvement avec boîte : repositionne joueur ET boîte
     *  - Transition de grille : repositionne le joueur entre les deux grilles
     *    et met à jour la pile dans JeuRecursif (via le callback)
     *
     * @param grille La grille active au moment de l'annulation
     * @return true si un mouvement a été annulé, false si l'historique est vide
     */
    public boolean annulerDernierMouvement(Grille grille) {
        if (mouvements.isEmpty()) {
            return false;
        }

        Mouvement dernierMvt = mouvements.pop();

        // ── Cas : transition de grille (entrée ou sortie de Piece) ──
        // Ce cas est signalé par pieceTransition != null.
        // La restauration de la pile (pileGrilles/pilePieces) est faite
        // par JeuRecursif.annulerMouvement() qui détecte ce cas.
        if (dernierMvt.pieceTransition != null) {
            // Retirer le joueur de la grille où il se trouve APRÈS la transition
            Joueur joueur = dernierMvt.grilleApres.getJoueur();
            if (joueur == null) return false;

            dernierMvt.grilleApres.retirerObjet(joueur.getPosition());

            // Remettre le joueur dans la grille où il était AVANT la transition
            dernierMvt.grilleAvant.setObjet(joueur, dernierMvt.posJoueurDansGrilleAvant);
            joueur.setSurCible(dernierMvt.joueurSurCibleAvant);

            // Signaler à l'appelant que c'est une transition (pour ajuster les piles)
            // On stocke temporairement l'info dans un champ accessible
            this.derniereTransitionEstEntree = dernierMvt.estEntree;
            this.derniereTransitionPiece = dernierMvt.pieceTransition;
            this.estDerniereAnnulationTransition = true;

            return true;
        }

        // ── Cas : déplacement normal (avec ou sans boîte) ──
        this.estDerniereAnnulationTransition = false;

        Joueur joueur = grille.getJoueur();
        if (joueur == null) return false;

        Position posActuelle = joueur.getPosition();
        grille.setObjet(null, posActuelle);

        if (dernierMvt.boitePoussee != null) {
            Boite boite = dernierMvt.boitePoussee;
            Position posBoiteActuelle = boite.getPosition();
            grille.setObjet(null, posBoiteActuelle);
            grille.setObjet(boite, dernierMvt.positionBoiteAvant);
            boite.setSurCible(dernierMvt.boiteSurCibleAvant);
        }

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



/*nouvelle methode pour la sauvegarde*/
public Stack<Character> viderEtConvertir() {

    Stack<Character> res = new Stack<>();

    while (!mouvements.isEmpty()) {

        Mouvement m = mouvements.pop();
        char c;

        if (m.boitePoussee == null) {
            switch (m.direction) {
                case HAUT: c = 'u'; break;
                case BAS: c = 'd'; break;
                case GAUCHE: c = 'l'; break;
                case DROITE: c = 'r'; break;
                default: throw new IllegalArgumentException();
            }
        } else {
            switch (m.direction) {
                case HAUT: c = 'U'; break;
                case BAS: c = 'D'; break;
                case GAUCHE: c = 'L'; break;
                case DROITE: c = 'R'; break;
                default: throw new IllegalArgumentException();
            }
        }

        res.push(c);
    }

    return res;
}


public Stack<Character> convertirSansVider() { //pour la persistance

    Stack<Character> res = new Stack<>();

    for (int i = 0; i < mouvements.size(); i++) {

        Mouvement m = mouvements.get(i);
        char c;

        if (m.boitePoussee == null) {
            switch (m.direction) {
                case HAUT: c = 'u'; break;
                case BAS: c = 'd'; break;
                case GAUCHE: c = 'l'; break;
                case DROITE: c = 'r'; break;
                default: throw new IllegalArgumentException();
            }
        } else {
            switch (m.direction) {
                case HAUT: c = 'U'; break;
                case BAS: c = 'D'; break;
                case GAUCHE: c = 'L'; break;
                case DROITE: c = 'R'; break;
                default: throw new IllegalArgumentException();
            }
        }

        res.push(c);
    }

    return res;
}

}
