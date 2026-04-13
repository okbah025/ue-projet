import java.util.Stack;

/**
 * Classe JeuRecursif
 *
 * Contrôleur principal de la VERSION RÉCURSIVE du jeu (Parabox).
 *
 * Cette classe gère toute la logique spécifique à la version récursive :
 *  - Le joueur peut ENTRER dans une Piece (grille-monde contenue dans une case)
 *  - Le joueur peut SORTIR d'une Piece (revenir à la grille parente)
 *  - Le jeu maintient une PILE des grilles visitées
 *
 * FONCTIONNEMENT GÉNÉRAL :
 * ─────────────────────────────────────────────────────────────────────
 * Quand le joueur veut se déplacer dans la direction D :
 *
 *  1) Si la case visée EST une Piece :
 *       → On fait entrer le joueur dans la grille interne de la Piece
 *       → La grille active change (on empile la grille interne)
 *
 *  2) Si le joueur veut dépasser le bord de la grille active :
 *       → On sort le joueur dans la grille parente
 *       → La grille active redevient la grille parente (on dépile)
 *
 *  3) Sinon (case vide, cible, boîte normale) :
 *       → Déplacement classique dans la grille active
 *
 * Cette classe N'AFFECTE PAS la version simple du Sokoban.
 */
public class JeuRecursif {

    // ========== ATTRIBUTS ==========

    /**
     * La grille racine : la grille principale du niveau chargé depuis le fichier.
     * C'est toujours la grille de départ, on y revient quand on dépile tout.
     */
    private Grille grilleRacine;

    /**
     * Pile des grilles visitées.
     * - Au départ : [grilleRacine]
     * - Après être entré dans une Piece E : [grilleRacine, grilleInterne_E]
     * - Après en être sorti : [grilleRacine]
     * Le sommet de la pile = grille où se trouve le joueur actuellement.
     */
    private Stack<Grille> pileGrilles;

    /**
     * Pile des Pieces correspondantes (parallèle à pileGrilles).
     * pileGrilles[i+1] est la grille interne de pilePieces[i].
     * null au niveau 0 (grille racine, pas de Piece parente).
     */
    private Stack<Piece> pilePieces;

    /** Historique des mouvements pour annuler (Ctrl+Z / touche U) */
    private Historique historique;

    /** Nombre total de mouvements effectués (sans les annulations) */
    private int nombreMouvements;

    /** Nombre de poussées de boîtes normales */
    private int nombrePoussees;

    /** true quand toutes les boîtes normales sont sur des cibles */
    private boolean niveauTermine;

    // ========== CONSTRUCTEUR ==========

    /**
     * Créer un jeu récursif à partir d'une grille racine chargée depuis un fichier.
     *
     * @param grilleRacine La grille principale (chargée par ChargeurNiveau)
     */
    public JeuRecursif(Grille grilleRacine) {
        if (grilleRacine == null)
            throw new IllegalArgumentException("La grille racine ne peut pas être null !");

        this.grilleRacine   = grilleRacine;
        this.pileGrilles    = new Stack<>();
        this.pilePieces     = new Stack<>();
        this.historique     = new Historique();
        this.nombreMouvements = 0;
        this.nombrePoussees   = 0;
        this.niveauTermine    = false;

        // Au départ : on est dans la grille racine, pas dans de Piece
        pileGrilles.push(grilleRacine);
        pilePieces.push(null); // null = on n'est entré dans aucune Piece
    }

    // ========== GETTERS ==========

    /**
     * Obtenir la grille dans laquelle le joueur se trouve actuellement.
     * C'est toujours le sommet de la pile.
     * @return La grille active
     */
    public Grille getGrilleActive() { return pileGrilles.peek(); }

    /**
     * Obtenir la grille racine (la grille principale du niveau).
     * @return La grille racine
     */
    public Grille getGrilleRacine() { return this.grilleRacine; }

    /** @return Le nombre de mouvements effectués */
    public int getNombreMouvements() { return this.nombreMouvements; }

    /** @return Le nombre de poussées de boîtes */
    public int getNombrePoussees() { return this.nombrePoussees; }

    /** @return true si le niveau est terminé (toutes boîtes sur cibles) */
    public boolean estNiveauTermine() { return this.niveauTermine; }

    /** @return true si on peut annuler un mouvement */
    public boolean peutAnnuler() { return !historique.estVide(); }

    /**
     * @return true si le joueur est à l'intérieur d'une Piece
     *         (pas à la racine)
     */
    public boolean estDansUnePiece() { return pileGrilles.size() > 1; }

    /**
     * @return Le niveau de profondeur actuel :
     *         0 = grille racine,
     *         1 = dans une Piece,
     *         2 = dans une Piece dans une Piece, etc.
     */
    public int getProfondeur() { return pileGrilles.size() - 1; }

    // ========== MÉTHODE PRINCIPALE : DÉPLACER LE JOUEUR ==========

    /**
     * Tenter de déplacer le joueur dans une direction.
     *
     * Cette méthode est le cœur du jeu récursif. Elle gère 3 cas :
     *  1) Sortie de grille : le joueur veut dépasser le bord → sortirDePiece()
     *  2) Entrée dans une Piece : la case visée est une Piece → entrerDansPiece()
     *  3) Déplacement normal : case vide, cible, boîte → grille.deplacerJoueur()
     *
     * @param direction La direction du déplacement (HAUT, BAS, GAUCHE, DROITE)
     * @return true si le mouvement a réussi, false si bloqué
     */
    public boolean deplacerJoueur(Direction direction) {
        if (niveauTermine) return false;

        Grille grilleActive = getGrilleActive();
        Joueur joueur = grilleActive.getJoueur();
        if (joueur == null) return false;

        Position posActuelle   = joueur.getPosition();
        Position posVoulue     = direction.appliquerSur(posActuelle);

        // ── CAS 1 : Le joueur veut sortir de la grille active (dépasse le bord) ──
        if (!grilleActive.estDansGrille(posVoulue)) {
            if (estDansUnePiece()) {
                // On est dans une grille interne → essayer de sortir
                return sortirDePiece(joueur, direction, grilleActive);
            } else {
                // On est à la racine → on ne peut pas sortir
                return false;
            }
        }

        // La case visée est dans la grille : regarder ce qu'il y a dessus
        Objet objetVise = grilleActive.getObjet(posVoulue);

        // ── CAS 2 : La case visée est une Piece → entrée dans le monde récursif ──
        if (objetVise instanceof Piece) {
            Piece piece = (Piece) objetVise;
            return entrerDansPiece(joueur, piece, direction, grilleActive);
        }

        // ── CAS 2b : La case visée est un MUR DE BORD et on est dans une Piece ──
        // Dans la version récursive, quand le joueur est bloqué par un mur situé
        // sur le bord de la grille interne (x=0, x=max, y=0, y=max), il peut
        // "traverser" ce mur pour sortir dans la grille parente.
        if (estDansUnePiece() && objetVise instanceof Mur && estMurDeBord(posVoulue, grilleActive)) {
            return sortirDePiece(joueur, direction, grilleActive);
        }

        // ── CAS 3 : Déplacement classique (vide, cible, boîte normale, mur intérieur) ──
        // Sauvegarder l'état AVANT le déplacement pour l'historique
        Position posAvant           = new Position(joueur.getX(), joueur.getY());
        boolean  joueurSurCibleAvant = joueur.estSurCible();

        // Vérifier s'il y a une boîte à pousser pour mettre à jour les stats
        Boite boitePoussee     = null;
        Position posBoiteAvant = null;
        boolean  boiteSurCibleAvant = false;

        if (objetVise instanceof Boite && !(objetVise instanceof Piece)) {
            boitePoussee     = (Boite) objetVise;
            posBoiteAvant    = new Position(boitePoussee.getX(), boitePoussee.getY());
            boiteSurCibleAvant = boitePoussee.estSurCible();
        }

        // Effectuer le déplacement via la grille active
        boolean succes = grilleActive.deplacerJoueur(direction);

        if (succes) {
            nombreMouvements++;

            Position posApres           = new Position(joueur.getX(), joueur.getY());
            boolean  joueurSurCibleApres = joueur.estSurCible();

            // Enregistrer dans l'historique
            if (boitePoussee != null) {
                nombrePoussees++;
                Position posBoiteApres    = new Position(boitePoussee.getX(), boitePoussee.getY());
                boolean  boiteSurCibleApres = boitePoussee.estSurCible();
                historique.ajouterMouvementAvecBoite(
                    direction,
                    posAvant, posApres,
                    joueurSurCibleAvant, joueurSurCibleApres,
                    boitePoussee,
                    posBoiteAvant, posBoiteApres,
                    boiteSurCibleAvant, boiteSurCibleApres
                );
            } else {
                historique.ajouterMouvement(
                    direction, posAvant, posApres,
                    joueurSurCibleAvant, joueurSurCibleApres
                );
            }

            // Vérifier la victoire sur la grille racine
            if (grilleRacine.estNiveauGagne()) {
                niveauTermine = true;
            }
        }

        return succes;
    }

    // ========== MÉTHODES PRIVÉES - GESTION RÉCURSIVE ==========

    /**
     * Faire ENTRER le joueur dans une Piece.
     *
     * Le joueur se déplace vers la Piece → il entre dans sa grille interne.
     * Il apparaît sur le bord OPPOSÉ à sa direction de déplacement.
     *
     * Exemple :
     *   Joueur va vers DROITE, Piece E est à droite
     *   → Le joueur entre par le côté GAUCHE (x=0) de la grille interne de E
     *
     * Si le bord d'entrée est bloqué (mur), l'entrée échoue.
     *
     * @param joueur         Le joueur qui entre
     * @param piece          La Piece dans laquelle il veut entrer
     * @param direction      La direction du déplacement
     * @param grilleActuelle La grille qui contient la Piece (grille parente)
     * @return true si l'entrée a réussi
     */
    private boolean entrerDansPiece(Joueur joueur, Piece piece,
                                    Direction direction, Grille grilleActuelle) {

        Grille grilleInterne = piece.getGrilleInterne();

        // Calculer la position d'entrée dans la grille interne
        // (cherche la première case libre depuis le bord, en ignorant les murs de pourtour)
        Position posEntree = calculerPositionEntree(direction, grilleInterne);

        // Si null : la grille est entièrement bloquée (ex: grille "I 1" = un seul '#')
        if (posEntree == null) {
            return false;
        }

        // Retirer le joueur de sa position actuelle dans la grille parente
        grilleActuelle.retirerObjet(joueur.getPosition());

        // Placer le joueur dans la grille interne
        grilleInterne.setObjet(joueur, posEntree);

        // Empiler la grille interne et la Piece (pour pouvoir sortir plus tard)
        pileGrilles.push(grilleInterne);
        pilePieces.push(piece);

        nombreMouvements++;

        return true;
    }

    /**
     * Faire SORTIR le joueur d'une Piece (retour à la grille parente).
     *
     * Le joueur atteint le bord de la grille interne et continue dans la même
     * direction → il sort et réapparaît dans la grille parente, à côté de la Piece.
     *
     * Exemple :
     *   Joueur est dans la grille interne de E, va vers GAUCHE hors du bord
     *   → Il sort et réapparaît à gauche de la case où se trouve E dans la grille parente
     *
     * Si la case de sortie dans la grille parente est bloquée (mur), la sortie échoue.
     *
     * @param joueur         Le joueur qui sort
     * @param direction      La direction de sortie
     * @param grilleInterne  La grille interne dans laquelle est le joueur
     * @return true si la sortie a réussi
     */
    private boolean sortirDePiece(Joueur joueur, Direction direction, Grille grilleInterne) {
        // Récupérer la Piece par laquelle on est entré
        Piece piece = pilePieces.peek();
        if (piece == null) return false; // Ne devrait pas arriver

        // La grille parente est juste en dessous dans la pile
        // Pour y accéder temporairement, on dépile (on remettra si échec)
        pileGrilles.pop();
        pilePieces.pop();
        Grille grilleParente = pileGrilles.peek();

        // Position de sortie dans la grille parente :
        // la case juste APRÈS la Piece dans la direction de sortie
        Position posSortie = direction.appliquerSur(piece.getPosition());

        // Vérifier que la sortie est dans la grille parente ET libre
        if (!grilleParente.estDansGrille(posSortie) || !grilleParente.estCaseLibre(posSortie)) {
            // Sortie bloquée → remettre les piles comme avant
            pileGrilles.push(grilleInterne);
            pilePieces.push(piece);
            return false;
        }

        // Retirer le joueur de la grille interne
        grilleInterne.retirerObjet(joueur.getPosition());

        // Placer le joueur dans la grille parente, à côté de la Piece
        grilleParente.setObjet(joueur, posSortie);

        nombreMouvements++;

        return true;
    }

    /**
     * Vérifier si une position est sur le bord de la grille.
     * Un "bord" est : x=0, x=largeur-1, y=0, ou y=hauteur-1.
     *
     * Utilisé pour détecter si le joueur veut sortir via un mur de bord
     * (dans la version récursive, les murs de bord sont traversables pour sortir).
     *
     * @param pos    La position du mur
     * @param grille La grille dans laquelle on vérifie
     * @return true si la position est sur le bord de la grille
     */
    private boolean estMurDeBord(Position pos, Grille grille) {
        int x = pos.getX();
        int y = pos.getY();
        return (x == 0 || x == grille.getLargeur() - 1
             || y == 0 || y == grille.getHauteur() - 1);
    }

    /**
     * Calculer la position d'entrée dans une grille interne selon la direction.
     *
     * Convention : on entre par le bord OPPOSÉ à la direction de déplacement,
     * au CENTRE de ce bord.
     *
     *  Direction → DROITE  :  on entre par la gauche  (x=0,          y=hauteur/2)
     *  Direction → GAUCHE  :  on entre par la droite  (x=largeur-1,  y=hauteur/2)
     *  Direction → BAS     :  on entre par le haut    (x=largeur/2,  y=0)
     *  Direction → HAUT    :  on entre par le bas     (x=largeur/2,  y=hauteur-1)
     *
     * @param direction     Direction du déplacement du joueur
     * @param grilleInterne La grille dans laquelle on entre
     * @return La position d'entrée dans la grille interne
     */
    /**
     * Calculer la position d'entrée dans une grille interne selon la direction.
     *
     * On entre par le bord OPPOSÉ à la direction de déplacement.
     * Comme les grilles ont souvent un mur '#' sur leur pourtour, on avance depuis
     * le bord vers l'intérieur jusqu'à trouver la PREMIÈRE CASE LIBRE.
     *
     * Exemple avec une grille 5x5 (####/ # # /#####) :
     *  Direction → DROITE : bord gauche x=0 (mur) → on avance → x=1 (libre)
     *  Direction → GAUCHE : bord droit x=4  (mur) → on recule → x=3 (libre)
     *
     * @param direction     Direction du déplacement du joueur
     * @param grilleInterne La grille dans laquelle on entre
     * @return La position d'entrée libre, ou null si la grille est infranchissable
     */
    private Position calculerPositionEntree(Direction direction, Grille grilleInterne) {
        int larg = grilleInterne.getLargeur();
        int haut = grilleInterne.getHauteur();

        // Position de départ sur le bord et incrément pour avancer vers l'intérieur
        int xDepart, yDepart, dx, dy, maxPas;

        switch (direction) {
            case DROITE:
                xDepart = 0;        yDepart = haut / 2; dx = 1;  dy = 0; maxPas = larg; break;
            case GAUCHE:
                xDepart = larg - 1; yDepart = haut / 2; dx = -1; dy = 0; maxPas = larg; break;
            case BAS:
                xDepart = larg / 2; yDepart = 0;        dx = 0;  dy = 1; maxPas = haut; break;
            case HAUT:
                xDepart = larg / 2; yDepart = haut - 1; dx = 0;  dy = -1; maxPas = haut; break;
            default:
                return new Position(0, 0);
        }

        // Parcourir depuis le bord jusqu'à trouver une case libre (pas un mur)
        for (int pas = 0; pas < maxPas; pas++) {
            int x = xDepart + dx * pas;
            int y = yDepart + dy * pas;
            if (grilleInterne.estDansGrille(x, y) && grilleInterne.estCaseLibre(x, y)) {
                return new Position(x, y);
            }
        }

        // Aucune case libre : la grille est infranchissable (ex: grille 1x1 = "#")
        return null;
    }

    // ========== ANNULATION ET RÉINITIALISATION ==========

    /**
     * Annuler le dernier mouvement (fonctionnalité "Ctrl+Z" / touche U).
     *
     * Attention : l'annulation ne gère pas le retour entre grilles pour l'instant
     * (on annule seulement les déplacements dans la grille active).
     *
     * @return true si un mouvement a été annulé
     */
    public boolean annulerMouvement() {
        Grille grilleActive = getGrilleActive();
        boolean annule = historique.annulerDernierMouvement(grilleActive);
        if (annule) {
            if (nombreMouvements > 0) nombreMouvements--;
            niveauTermine = false;
        }
        return annule;
    }

    /**
     * Réinitialiser entièrement le jeu avec une nouvelle grille racine.
     * Utilisé par "R" (recommencer) dans le Main : on recharge le fichier.
     *
     * @param nouvelleGrilleRacine La grille rechargée depuis le fichier
     */
    public void reinitialiser(Grille nouvelleGrilleRacine) {
        this.grilleRacine = nouvelleGrilleRacine;
        this.pileGrilles.clear();
        this.pilePieces.clear();
        this.pileGrilles.push(grilleRacine);
        this.pilePieces.push(null);
        this.historique.vider();
        this.nombreMouvements = 0;
        this.nombrePoussees   = 0;
        this.niveauTermine    = false;
    }

    // ========== AFFICHAGE ==========

    /**
     * Obtenir les statistiques du jeu sous forme de texte.
     * Affiche les mouvements, poussées, progression, et la profondeur.
     *
     * @return Une chaîne avec les statistiques
     */
    public String getStatistiques() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mouvements : ").append(nombreMouvements).append("\n");
        sb.append("Poussées   : ").append(nombrePoussees).append("\n");

        // Compter uniquement les boîtes normales (pas les Pieces)
        int boitesSurCibles = 0;
        int totalBoites = 0;
        for (Boite b : grilleRacine.getBoites()) {
            if (b instanceof Piece) continue;
            totalBoites++;
            if (b.estSurCible()) boitesSurCibles++;
        }
        sb.append("Progression: ").append(boitesSurCibles)
          .append("/").append(totalBoites).append(" boîtes sur cibles\n");

        if (estDansUnePiece()) {
            sb.append("Profondeur : ").append(getProfondeur())
              .append(" (grille '").append(pilePieces.peek().getIdentifiant()).append("')\n");
        }

        return sb.toString();
    }

    /** Description textuelle pour le débogage. */
    @Override
    public String toString() {
        return "JeuRecursif [" + grilleRacine.getNom()
               + "] - " + nombreMouvements + " mouvements"
               + " - profondeur " + getProfondeur();
    }
}