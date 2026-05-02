package fr.paris13.parabox.Modele;
import java.util.Stack;

/**
 * Classe JeuRecursif
 *
 * Contrôleur principal de la VERSION RÉCURSIVE du jeu (Parabox).
 * Cette classe gère toute la logique spécifique à la version récursive :
 *  - Le joueur peut ENTRER dans une Piece (grille-monde contenue dans une case)
 *  - Le joueur peut SORTIR d'une Piece (revenir à la grille parente)
 *  - Le jeu maintient une PILE des grilles visitées
 *
 */
public class JeuRecursif {

    // ========== ATTRIBUTS ==========

    /** La grille racine : la grille principale du niveau chargé depuis le fichier.  */
    private Grille grilleRacine;

    /** Pile des grilles visitées.  */
    private Stack<Grille> pileGrilles;

    /** Pile des Pieces correspondantes (parallèle à pileGrilles).  */
    private Stack<Piece> pilePieces;

    /** Historique des mouvements pour annuler  */
    private Historique historique;

    /** Nombre total de mouvements effectués sans les annulations de mouv */
    private int nombreMouvements;

    /** Nombre de poussées de boîtes normales */
    private int nombrePoussees;

    /** true quand toutes les boîtes normales sont sur des cibles */
    private boolean niveauTermine;


    // ========== LE CONSTRUCTEUR ==========

    /** Créer un jeu récursif à partir d'une grille racine chargée depuis un fichier.
     * @param grilleRacine La grille principale qui est chargée par ChargeurNiveau    */

    public JeuRecursif(Grille grilleRacine) {
        if (grilleRacine == null)
            throw new IllegalArgumentException("La grille racine ne peut pas être null !");

        this.grilleRacine = grilleRacine;
        this.pileGrilles = new Stack<>();
        this.pilePieces = new Stack<>();
        this.historique = new Historique();
        this.nombreMouvements = 0;
        this.nombrePoussees = 0;
        this.niveauTermine = false;

        // Au début on est dans la grille racine
        pileGrilles.push(grilleRacine);
        pilePieces.push(null); // on n'est rentré dans aucune Piece
    }

    // ========== METHODES ==========

    /** nous donne la grille dans laquelle le joueur se trouve actuellement.
     * @return La grille active */
    public Grille getGrilleActive(){ 
        return pileGrilles.peek(); 
    }

    /** Obtenir la grille racine (la grille principale du niveau).
     * @return La grille racine   */
    public Grille getGrilleRacine() { return this.grilleRacine; }
    
    /**
     * Obtenir la pile des grilles visitées.
     * @return La pile des grilles visitées
     */
    public Stack<Grille> getPileGrilles() { return this.pileGrilles; } 

    /** @return Le nombre de mouvements effectués */
    public int getNombreMouvements(){ 
        return this.nombreMouvements; 
    }

    /** @return Le nombre de poussées de boîtes */
    public int getNombrePoussees(){ 
        return this.nombrePoussees; 
    }

    /** @return true si le niveau est terminé càd toutes boîtes sont sur des cibles */
    public boolean estNiveauTermine(){ 
        return this.niveauTermine; 
    }

    /** @return true si on peut annuler un mouvement */
    public boolean peutAnnuler(){ 
        return !historique.estVide(); 
    }

    /** @return true si le joueur est à l'intérieur d'une Piece  */
    public boolean estDansUnePiece(){ 
        return pileGrilles.size() > 1; 
    }

    /** @return Le niveau de profondeur actuel :
     *         0 = grille racine,
     *         1 = dans une Piece,
     *         2 = dans une Piece dans une Piece, etc.    */
    public int getProfondeur(){ 
        return pileGrilles.size() - 1; 
    }

    /** Cette méthode est la base du jeu récursif. Elle gère 3 cas :
     *  1) Sortie de grille : le joueur veut dépasser le bord -> sortirDePiece()
     *  2) Entrée dans une Piece : la case visée est une Piece -> entrerDansPiece()
     *  3) Déplacement normal : case vide, cible, boîte -> grille.deplacerJoueur()
     *
     * @param direction La direction du déplacement (HAUT, BAS, GAUCHE, DROITE)
     * @return true si le mouvement a réussi, false si bloqué    */
    public boolean deplacerJoueur(Direction direction) {
        if (niveauTermine) return false;

        Grille grilleActive = getGrilleActive();
        Joueur joueur = grilleActive.getJoueur();
        if (joueur == null) return false;

        Position posActuelle   = joueur.getPosition();
        Position posVoulue     = direction.appliquerSur(posActuelle);

        // CAS 1 : Le joueur veut sortir de la grille donc dépasse le bord 
        if (!grilleActive.estDansGrille(posVoulue)) {
            if (estDansUnePiece()) {
                return sortirDePiece(joueur, direction, grilleActive);
            } else {
                return false;
            }
        }

        Objet objetVise = grilleActive.getObjet(posVoulue);

        // CAS 2 : La case visée est une Piece donc on veut entrer dans le monde récursif 
        if (objetVise instanceof Piece) {
            Piece piece = (Piece) objetVise;
            return entrerDansPiece(joueur, piece, direction, grilleActive);
        }

        // CAS 2b : La case visée est un MUR DE BORD et on est dans une Piece
        // Le joueur atteint le bord alors il sort dans la grille parente
        if (estDansUnePiece() && objetVise instanceof Mur && estMurDeBord(posVoulue, grilleActive)) {
            return sortirDePiece(joueur, direction, grilleActive);
        }

        // CAS 2c : Le joueur pousse une boîte vers le bord d'une grille interne 
        // Quand le joueur est dans une Piece et pousse une boîte vers le bord de sortie,
        // la boîte sort dans la grille parente. Le joueur reste sur la case de la boîte
        // (dans la grille interne). Au prochain appui, le joueur sort à son tour
        // et peut pousser encore la boîte (qui est maintenant dans la grille parente).
        if (estDansUnePiece() && objetVise instanceof Boite && !(objetVise instanceof Piece)) {
            Boite boite = (Boite) objetVise;
            Position posDerrierBoite = direction.appliquerSur(boite.getPosition());

            // La case derrière la boîte est hors de la grille interne et la boîte est sur le bord 
            // la boîte doit donc sortir dans la grille parente
            if (!grilleActive.estDansGrille(posDerrierBoite)) {
                boolean succes = sortirBoiteDePiece(joueur, boite, direction, grilleActive);
                if (succes) {
                    nombreMouvements++;
                    nombrePoussees++;
                }
                return succes;
            }

            // La case derrière la boîte est un mur de bord
            if (grilleActive.estDansGrille(posDerrierBoite)) {
                Objet objetDerrierBoite = grilleActive.getObjet(posDerrierBoite);
                if (objetDerrierBoite instanceof Mur && estMurDeBord(posDerrierBoite, grilleActive)) {
                    boolean succes = sortirBoiteDePiece(joueur, boite, direction, grilleActive);
                    if (succes) {
                        nombreMouvements++;
                        nombrePoussees++;
                    }
                    return succes;
                }
            }
        }

        // CAS 3 : Déplacement classique

        // CAS 3a : La case visée est une BOÎTE dont la destination est une PIECE 
        // Quand joueur -> boîte -> Piece
        if (objetVise instanceof Boite && !(objetVise instanceof Piece)) {
            Boite boite = (Boite) objetVise;
            Position posDerrierBoite = direction.appliquerSur(boite.getPosition());

            if (grilleActive.estDansGrille(posDerrierBoite)) {
                Objet objetDerrierBoite = grilleActive.getObjet(posDerrierBoite);

                // La boîte va vers une Piece : on fait entrer la boîte dans la Piece
                if (objetDerrierBoite instanceof Piece) {
                    Piece piece = (Piece) objetDerrierBoite;
                    boolean succes = entrerBoiteDansPiece(joueur, boite, piece, direction, grilleActive);
                    if (succes) {
                        nombreMouvements++;
                        nombrePoussees++;
                    }
                    return succes;
                }
            }
        }

        // CAS 3b : Déplacement standard 
        Position posAvant = new Position(joueur.getX(), joueur.getY());
        boolean  joueurSurCibleAvant = joueur.estSurCible();

        Boite boitePoussee = null;
        Position posBoiteAvant  = null;
        boolean  boiteSurCibleAvant = false;

        if (objetVise instanceof Boite && !(objetVise instanceof Piece)){
            boitePoussee = (Boite) objetVise;
            posBoiteAvant = new Position(boitePoussee.getX(), boitePoussee.getY());
            boiteSurCibleAvant = boitePoussee.estSurCible();
        }

        boolean succes = grilleActive.deplacerJoueur(direction);

        if (succes) {
            nombreMouvements++;

            Position posApres   = new Position(joueur.getX(), joueur.getY());
            boolean  joueurSurCibleApres = joueur.estSurCible();

            if (boitePoussee != null) {
                nombrePoussees++;
                Position posBoiteApres  = new Position(boitePoussee.getX(), boitePoussee.getY());
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

            if (estNiveauGagneRecursif()){
                niveauTermine =true;
            }
        }

        return succes;
    }

    
    /** but: faire ENTRER le joueur dans une Piece.
     * @param joueur         Le joueur qui entre
     * @param piece          La Piece dans laquelle il veut entrer
     * @param direction      La direction du déplacement
     * @param grilleActuelle La grille parente contenant la Piece
     * @return true si l'entrée a réussi
     */
    private boolean entrerDansPiece(Joueur joueur, Piece piece, Direction direction, Grille grilleActuelle) {

        Grille grilleInterne = piece.getGrilleInterne();
        Position posJoueurAvant = new Position(joueur.getX(), joueur.getY());
        Position posEntreeStandard = calculerPositionEntreeAvecBoite(direction, grilleInterne);

        if (posEntreeStandard == null) {
            // Grille infranchissable (ex : 1x1 avec '#')
            return false;
        }

        // Vérifier ce qu'il y a sur la case d'entrée standard
        Objet objetSurEntree = grilleInterne.getObjet(posEntreeStandard);
        if (objetSurEntree instanceof Boite) {
            Boite boiteSurEntree = (Boite) objetSurEntree;
            Position posDerrierBoite = direction.appliquerSur(posEntreeStandard);

            // Vérifier que la case derrière la boîte est libre dans la grille interne
            if (!grilleInterne.estDansGrille(posDerrierBoite)  || !grilleInterne.estCaseLibre(posDerrierBoite)) {
                // La boîte est coincée alors l'entrée est impossible
                return false;
            }

            // Pousser la boîte d'un cran vers l'intérieur
            grilleInterne.retirerObjet(posEntreeStandard);
            grilleInterne.setObjet(boiteSurEntree, posDerrierBoite);

            // Mettre à jour l'état surCible de la boîte via la liste des cibles
            boolean boiteAterritSurCible = false;
            for (Cible c : grilleInterne.getCibles()) {
                if (c.getPosition().equals(posDerrierBoite)) {
                    boiteAterritSurCible = true;
                    c.setOccupee(true);
                    break;
                }
                }
            boiteSurEntree.setSurCible(boiteAterritSurCible);

        } else if (objetSurEntree != null && !(objetSurEntree instanceof Cible)) {
            // La case d'entrée est bloquée par autre chose (mur intérieur)
            return false;
        }

        grilleActuelle.retirerObjet(joueur.getPosition());
        grilleInterne.setObjet(joueur, posEntreeStandard);

        // Empiler la grille interne et la Piece
        pileGrilles.push(grilleInterne);
        pilePieces.push(piece);

        nombreMouvements++;

        // Enregistrer dans l'historique
        historique.ajouterTransition(
            direction,
            piece,
            true,
            grilleActuelle,
            grilleInterne,
            posJoueurAvant,
            new Position(joueur.getX(), joueur.getY())
        );

        return true;
    }

    /** Calculer la position d'entrée STANDARD dans une grille interne.
     * @param direction     Direction du déplacement du joueur
     * @param grilleInterne La grille dans laquelle on entre
     * @return La position d'entrée standard, ou null si la grille est infranchissable   */
    private Position calculerPositionEntreeAvecBoite(Direction direction, Grille grilleInterne) {
        int larg = grilleInterne.getLargeur();
        int haut = grilleInterne.getHauteur();

        int xDepart, yDepart, dx, dy, maxPas;

        switch (direction) {
            case DROITE:
                xDepart = 0;    yDepart = haut / 2;     dx = 1;     dy = 0;     maxPas = larg; break;
            case GAUCHE:
                xDepart = larg - 1; yDepart = haut / 2; dx = -1;    dy = 0;    maxPas = larg; break;
            case BAS:
                xDepart = larg / 2; yDepart = 0;        dx = 0;     dy = 1;    maxPas = haut; break;
            case HAUT:
                xDepart = larg / 2; yDepart = haut - 1; dx = 0;     dy = -1;   maxPas = haut; break;
            default:
                return new Position(0, 0);
        }

        // Parcourir depuis le bord jusqu'à trouver une case qui n'est PAS un mur
        for (int pas = 0; pas < maxPas; pas++){
            int x = xDepart + dx * pas;
            int y = yDepart + dy * pas;
            if (!grilleInterne.estDansGrille(x, y)) continue;

            Objet obj = grilleInterne.getObjet(x, y);

            // case entrée valide si c'est pas un mur
            if (!(obj instanceof Mur)) {
                return new Position(x, y);
            }
        }

        // si tt la ligne/colonne est bloquée par des murs
        return null;
    }

    
    /** Faire entrer une BOÎTE dans une Piece (poussée par le joueur).
     * @param joueur         Le joueur qui pousse
     * @param boite          La boîte à faire entrer
     * @param piece          La Piece destination
     * @param direction      La direction du déplacement
     * @param grilleActuelle La grille parente
     * @return true si l'opération a réussi   */
    private boolean entrerBoiteDansPiece(Joueur joueur, Boite boite, Piece piece, Direction direction, Grille grilleActuelle){

        Grille grilleInterne = piece.getGrilleInterne();

        // Calculer la position d'entrée de la boîte dans la grille interne
        Position posEntreeBoite = calculerPositionEntree(direction, grilleInterne);

        if (posEntreeBoite == null) {
            return false; // la grille est infranchissable
        }

        // Vérifier que la case d'entrée est bien libre pour la boîte
        if (!grilleInterne.estCaseLibre(posEntreeBoite)) {
            return false;
        }

        // Mémoriser la position de la boîte dans la grille parente
        Position posBoiteDansParente = new Position(boite.getX(), boite.getY());

        // Retirer la boîte de la grille parente 
        grilleActuelle.retirerObjet(posBoiteDansParente);
        grilleActuelle.getBoites().remove(boite);  // Supprimer de la liste pour éviter le double enregistrement

        // Placer la boîte dans la grille interne
        grilleInterne.setObjet(boite, posEntreeBoite);

        // Mettre à jour l'état surCible de la boîte
        boolean boiteSurCible = false;
        for (Cible c : grilleInterne.getCibles()) {
            if (c.getPosition().equals(posEntreeBoite)){
                boiteSurCible = true;
                c.setOccupee(true);
                break;
            }
        }
        boite.setSurCible(boiteSurCible);

        // Le joueur avance sur la case où était la boîte 
        grilleActuelle.retirerObjet(joueur.getPosition());
        grilleActuelle.setObjet(joueur, posBoiteDansParente);

        // Vérifier la victoire après le déplacement
        if (estNiveauGagneRecursif()){
            niveauTermine = true;
        }

        return true;
    }

    /** Faire SORTIR le joueur d'une Piece
     * @param joueur         Le joueur qui sort
     * @param direction      La direction de sortie
     * @param grilleInterne  La grille interne dans laquelle est le joueur
     * @return true si la sortie a réussi
     */
    private boolean sortirDePiece(Joueur joueur, Direction direction, Grille grilleInterne) {
        // Récupérer la Piece et dépiler temporairement pour accéder à la grille parente
        Piece piece = pilePieces.peek();
        if (piece == null) return false;

        pileGrilles.pop();
        pilePieces.pop();
        Grille grilleParente = pileGrilles.peek();

        // Position de sortie dans la grille parente : case juste après la Piece
        Position posSortie = direction.appliquerSur(piece.getPosition());

        // Vérifier que posSortie est dans la grille parente
        if (!grilleParente.estDansGrille(posSortie)) {
            pileGrilles.push(grilleInterne);
            pilePieces.push(piece);
            return false;
        }

        Objet objetEnSortie = grilleParente.getObjet(posSortie);

        // une boîte se trouve sur la case de sortie
        if (objetEnSortie instanceof Boite && !(objetEnSortie instanceof Piece)) {
            Boite boiteEnSortie = (Boite) objetEnSortie;
            Position posApresBoite = direction.appliquerSur(posSortie);

            if (!grilleParente.estDansGrille(posApresBoite) || !grilleParente.estCaseLibre(posApresBoite)){
                // La boîte est bloquée, sortie impossible
                pileGrilles.push(grilleInterne);
                pilePieces.push(piece);
                return false;
            }

            // Pousser la boîte d'un cran dans la grille parente
            grilleParente.retirerObjet(posSortie);
            grilleParente.setObjet(boiteEnSortie, posApresBoite);

            // Mettre à jour surCible de la boîte
            boolean boiteSurCible = false;
            for (Cible c : grilleParente.getCibles()){
                if (c.getPosition().equals(posApresBoite)) {
                    boiteSurCible = true;
                    c.setOccupee(true);
                    break;
                }
            }
            boiteEnSortie.setSurCible(boiteSurCible);

        } else if (objetEnSortie != null && !(objetEnSortie instanceof Cible)) {
            // Sortie bloquée par autre chose (ex: mur, etc )
            pileGrilles.push(grilleInterne);
            pilePieces.push(piece);
            return false;
        }

        // Déplacer le joueur dans la grille parente 
        Position posJoueurDansGrilleInterne = new Position(joueur.getX(), joueur.getY());
        grilleInterne.retirerObjet(joueur.getPosition());
        grilleParente.setObjet(joueur, posSortie);

        nombreMouvements++;

        // Enregistrer dans l'historique
        historique.ajouterTransition(
            direction,
            piece,
            false,                                         // c'est une SORTIE
            grilleInterne,                                 // grille d'avant (interne)
            grilleParente,                                 // grille d'après (parente)
            posJoueurDansGrilleInterne,                    // pos joueur AVANT (dans grilleInterne)
            new Position(joueur.getX(), joueur.getY())     // pos joueur APRÈS (dans grilleParente)
        );

        return true;
    }

    /** Faire SORTIR une BOÎTE d'une Piece qui est poussée par le joueur depuis l'intérieur
     * @param joueur         Le joueur qui pousse (dans la grille interne)
     * @param boite          La boîte à faire sortir
     * @param direction      La direction du déplacement
     * @param grilleInterne  La grille interne depuis laquelle on sort
     * @return true si la sortie de la boîte a réussi   */
    private boolean sortirBoiteDePiece(Joueur joueur, Boite boite, Direction direction, Grille grilleInterne) {
        // Récupérer la Piece et la grille parente depuis la pile
        Piece piece = pilePieces.peek();
        if (piece == null) return false;

        Grille grilleParente = pileGrilles.get(pileGrilles.size() - 2);

        // La boîte sortira sur la case juste après la Piece dans la direction de sortie
        Position posSortieBoite = direction.appliquerSur(piece.getPosition());

        // Vérifier que cette case existe et est libre dans la grille parente
        if (!grilleParente.estDansGrille(posSortieBoite) || !grilleParente.estCaseLibre(posSortieBoite)) {
            return false;
        }

        // Mémoriser la position actuelle de la boîte dans la grille interne
        Position posBoiteDansGrilleInterne = new Position(boite.getX(), boite.getY());

        // Retirer la boîte de la grille interne
        grilleInterne.retirerObjet(posBoiteDansGrilleInterne);

        // Placer la boîte dans la grille parente
        grilleParente.setObjet(boite, posSortieBoite);
        boite.setGrilleParente(grilleParente);

        // Gérer l'état surCible de la boîte dans la grille parente
        Objet objetSousBoite = grilleParente.getObjet(posSortieBoite);
        if (objetSousBoite instanceof Cible) {
            boite.setSurCible(true);
            ((Cible) objetSousBoite).setOccupee(true);
        } else {
            boite.setSurCible(false);
        }

        grilleInterne.retirerObjet(joueur.getPosition());
        grilleInterne.setObjet(joueur, posBoiteDansGrilleInterne);

        // Vérifier la victoire après le déplacement
        if (estNiveauGagneRecursif()){
            niveauTermine = true;
        }

        return true;
    }

    /** Vérifier si le niveau est gagné en parcourant TOUTES les grilles récursivement.
     * Cette méthode explore toutes les grilles (racine + grilles internes des Pieces)
     * pour trouver toutes les boîtes normales et vérifier qu'elles sont sur des cibles.
     * @return true si toutes les boîtes normales sont sur des cibles  */
    private boolean estNiveauGagneRecursif() {
        // Collecter toutes les boîtes normales dans toutes les grilles
        // et vérifier qu'elles sont toutes sur des cibles
        ResultatRecherche resultat = new ResultatRecherche();
        chercherBoitesDansGrille(grilleRacine, resultat);

        // Le niveau est gagné si on a trouvé au moins une boîte normale
        // ET qu'elles sont toutes sur des cibles
        return resultat.totalBoites > 0 && resultat.boitesHorsCible == 0;
    }

    /** Classe interne pour stocker le résultat de la recherche récursive de boîtes. */
    private static class ResultatRecherche {
        int totalBoites = 0;       // Nombre total de boîtes normales trouvées
        int boitesHorsCible = 0;   // Nombre de boîtes normales PAS sur une cible
    }

    /** Parcourir une grille et toutes ses grilles internes (via les Pieces)
     * pour compter les boîtes normales et celles qui ne sont pas sur des cibles.
     * Méthode récursive -> quand on rencontre une Piece, on explore sa grilleInterne.
     * @param grille   La grille à explorer
     * @param resultat Le compteur à mettre à jour  */
    private void chercherBoitesDansGrille(Grille grille, ResultatRecherche resultat) {
        for (Boite boite : grille.getBoites()) {
            if (boite instanceof Piece) {
                // C'est une Piece (grille-monde) : explorer sa grille interne récursivement
                Piece piece = (Piece) boite;
                chercherBoitesDansGrille(piece.getGrilleInterne(), resultat);

            } else {
                // C'est une boîte normale : la compter
                resultat.totalBoites++;
                if (!boite.estSurCible()) {
                    resultat.boitesHorsCible++;
                }
            }
        }
    }
    
    /** Vérifier si une position est sur le bord de la grille.
     * Un "bord" est  x=0, x=largeur-1, y=0, ou y=hauteur-1.
     * cette méthodes est utiliser pou détecter si le joueur veut sortir via un mur de bord
     * @param pos    La position du mur
     * @param grille La grille dans laquelle on vérifie
     * @return true si la position est sur le bord de la grille  */
    private boolean estMurDeBord(Position pos, Grille grille) {
        int x = pos.getX();
        int y = pos.getY();
        return (x == 0 || x == grille.getLargeur() - 1  ||  y == 0 || y == grille.getHauteur() - 1);
    }

    /** Calculer la position d'entrée dans une grille interne selon la direction.
     * On entre par le bord OPPOSÉ à la direction de déplacement.
     * Comme les grilles ont souvent un mur '#' sur leur pourtour, on avance depuis
     * le bord vers l'intérieur jusqu'à trouver la PREMIÈRE CASE LIBRE.
     * @param direction     Direction du déplacement du joueur
     * @param grilleInterne La grille dans laquelle on entre
     * @return La position d'entrée libre, ou null si la grille est infranchissable   */
    private Position calculerPositionEntree(Direction direction, Grille grilleInterne) {
        int larg = grilleInterne.getLargeur();
        int haut = grilleInterne.getHauteur();

        // Position de départ sur le bord + incrémente pour avancer vers l'intérieur
        int xDepart, yDepart, dx, dy, maxPas;

        switch (direction){
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

        // Parcourir depuis le bord jusqu'à trouver une case libre
        for (int pas = 0; pas < maxPas; pas++) {
            int x = xDepart + dx * pas;
            int y = yDepart + dy * pas;
            if (grilleInterne.estDansGrille(x, y) && grilleInterne.estCaseLibre(x, y)) {
                return new Position(x, y);
            }
        }

        // si aucun case libre
        return null;
    }

    // ========== ANNULATION ET RÉINITIALISATION ==========

    /** Annuler le dernier mouvement (touche U).
     * Gère trois cas :
     *  1. Déplacement simple ou avec boîte -> délègue à historique.annulerDernierMouvement()
     *  2. Transition ENTRÉE dans une Piece -> en plus de repositionner le joueur,
     *     dépile pileGrilles et pilePieces (on "désempile" l'entrée)
     *  3. Transition SORTIE d'une Piece -> en plus de repositionner le joueur,
     *     réempile pileGrilles et pilePieces (on "réempile" pour revenir dans la Piece)
     * @return true si un mouvement a été annulé
     */
    public boolean annulerMouvement() {
        if (historique.estVide()) return false;

        // Avant d'annuler on note l'état des piles
        Grille grilleActive = getGrilleActive();
        boolean annule = historique.annulerDernierMouvement(grilleActive);

        if (!annule) return false;

        if (nombreMouvements > 0) nombreMouvements--;
        niveauTermine = false;

        // Si la dernière annulation était une transition de grille on  ajuster les piles 
        if (historique.estDerniereAnnulationTransition) {
            if (historique.derniereTransitionEstEntree) {
                // On annule une ENTRÉE : dépiler pour revenir à la grille parente
                if (pileGrilles.size() > 1) {
                    pileGrilles.pop();
                    pilePieces.pop();
                }
            } else {
                // On annule une SORTIE : réempiler pour revenir dans la grille interne
                Piece piece = historique.derniereTransitionPiece;
                pileGrilles.push(piece.getGrilleInterne());
                pilePieces.push(piece);
            }
            // Réinitialiser le flag
            historique.estDerniereAnnulationTransition = false;
        }

        return true;
    }

    /** Réinitialiser entièrement le jeu avec une nouvelle grille racine.
     * Utilisé par "R" (recommencer) dans le Main : on recharge le fichier.
     * @param nouvelleGrilleRacine La grille rechargée depuis le fichier   */
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

    /** Obtenir les statistiques du jeu sous forme de texte.
     * Affiche les mouvements, poussées, progression, et la profondeur.
     * @return Une chaîne avec les statistiques    */
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
