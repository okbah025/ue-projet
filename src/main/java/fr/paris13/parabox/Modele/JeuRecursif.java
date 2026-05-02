package fr.paris13.parabox.Modele;
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
    
    /**
     * Obtenir la pile des grilles visitées.
     * @return La pile des grilles visitées
     */
    public Stack<Grille> getPileGrilles() { return this.pileGrilles; } 

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
        // Le joueur atteint le bord → il sort dans la grille parente.
        if (estDansUnePiece() && objetVise instanceof Mur && estMurDeBord(posVoulue, grilleActive)) {
            return sortirDePiece(joueur, direction, grilleActive);
        }

        // ── CAS 2c : Le joueur pousse une boîte vers le bord d'une grille interne ──
        // Quand le joueur est dans une Piece et pousse une boîte vers le bord de sortie,
        // la boîte sort dans la grille parente. Le joueur reste sur la case de la boîte
        // (dans la grille interne). Au prochain appui, le joueur sort à son tour
        // et peut pousser encore la boîte (qui est maintenant dans la grille parente).
        if (estDansUnePiece() && objetVise instanceof Boite && !(objetVise instanceof Piece)) {
            Boite boite = (Boite) objetVise;
            Position posDerrierBoite = direction.appliquerSur(boite.getPosition());

            // La case derrière la boîte est hors de la grille interne ET la boîte
            // est sur le bord → la boîte doit sortir dans la grille parente
            if (!grilleActive.estDansGrille(posDerrierBoite)) {
                boolean succes = sortirBoiteDePiece(joueur, boite, direction, grilleActive);
                if (succes) {
                    nombreMouvements++;
                    nombrePoussees++;
                }
                return succes;
            }

            // La case derrière la boîte est un mur de bord → même effet
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

        // ── CAS 3 : Déplacement classique (vide, cible, boîte normale, mur intérieur) ──

        // ── CAS 3a : La case visée est une BOÎTE dont la destination est une PIECE ──
        // Quand joueur -> boîte -> Piece :
        // On ne peut pas déléguer à grilleActive.deplacerJoueur() car il ne sait pas
        // faire entrer une boîte dans une Piece. On gère ce cas ici directement.
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

        // ── CAS 3b : Déplacement standard (vide, cible, boîte vers case libre) ──
        Position posAvant            = new Position(joueur.getX(), joueur.getY());
        boolean  joueurSurCibleAvant = joueur.estSurCible();

        Boite boitePoussee      = null;
        Position posBoiteAvant  = null;
        boolean  boiteSurCibleAvant = false;

        if (objetVise instanceof Boite && !(objetVise instanceof Piece)) {
            boitePoussee       = (Boite) objetVise;
            posBoiteAvant      = new Position(boitePoussee.getX(), boitePoussee.getY());
            boiteSurCibleAvant = boitePoussee.estSurCible();
        }

        boolean succes = grilleActive.deplacerJoueur(direction);

        if (succes) {
            nombreMouvements++;

            Position posApres            = new Position(joueur.getX(), joueur.getY());
            boolean  joueurSurCibleApres = joueur.estSurCible();

            if (boitePoussee != null) {
                nombrePoussees++;
                Position posBoiteApres      = new Position(boitePoussee.getX(), boitePoussee.getY());
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

            if (estNiveauGagneRecursif()) {
                niveauTermine = true;
            }
        }

        return succes;
    }
    // ========== MÉTHODES PRIVÉES - GESTION RÉCURSIVE ==========

    
    /**
     * Faire ENTRER le joueur dans une Piece.
     *
     * Cas normal : la case d'entrée est libre → le joueur y entre.
     *
     * Cas avec boîte déjà dans la grille interne (vient d'être poussée par entrerBoiteDansPiece) :
     *   La boîte se trouve sur la case d'entrée normale.
     *   calculerPositionEntree() retournerait la case SUIVANTE (car la case d'entrée est occupée),
     *   ce qui placerait le joueur un cran trop loin devant la boîte.
     *   Correction : on cherche si la boîte est sur la case d'entrée standard et on place
     *   le joueur JUSTE DERRIÈRE elle (la case d'entrée normale), en la poussant si possible.
     *
     * @param joueur         Le joueur qui entre
     * @param piece          La Piece dans laquelle il veut entrer
     * @param direction      La direction du déplacement
     * @param grilleActuelle La grille parente contenant la Piece
     * @return true si l'entrée a réussi
     */
    private boolean entrerDansPiece(Joueur joueur, Piece piece,
                                    Direction direction, Grille grilleActuelle) {

        Grille grilleInterne = piece.getGrilleInterne();

        // Capturer la position du joueur AVANT de le déplacer (pour l'historique)
        Position posJoueurAvant = new Position(joueur.getX(), joueur.getY());

        // Calculer la position d'entrée STANDARD (première case depuis le bord,
        // en ignorant les murs). On la calcule en cherchant la première case
        // depuis le bord qui n'est pas un mur — qu'elle soit libre ou occupée.
        Position posEntreeStandard = calculerPositionEntreeAvecBoite(direction, grilleInterne);

        if (posEntreeStandard == null) {
            // Grille infranchissable (ex : 1x1 avec '#')
            return false;
        }

        // Vérifier ce qu'il y a sur la case d'entrée standard
        Objet objetSurEntree = grilleInterne.getObjet(posEntreeStandard);

        if (objetSurEntree instanceof Boite) {
            // ── La case d'entrée contient une boîte → essayer de la pousser ──
            Boite boiteSurEntree = (Boite) objetSurEntree;

            // La boîte irait une case plus loin dans la même direction
            Position posDerrierBoite = direction.appliquerSur(posEntreeStandard);

            // Vérifier que la case derrière la boîte est libre dans la grille interne
            if (!grilleInterne.estDansGrille(posDerrierBoite)
                    || !grilleInterne.estCaseLibre(posDerrierBoite)) {
                // La boîte est coincée, entrée impossible
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

        // ── Placer le joueur sur la case d'entrée standard ──
        // (que la boîte y était ou non, le joueur prend cette case)
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

    /**
     * Calculer la position d'entrée STANDARD dans une grille interne.
     *
     * Différence avec calculerPositionEntree() :
     *   calculerPositionEntree() cherche la première case LIBRE (pas un mur, pas une boîte).
     *   calculerPositionEntreeAvecBoite() cherche la première case qui n'est PAS un mur,
     *   qu'elle soit libre ou occupée par une boîte.
     *
     * Cela permet de toujours entrer sur la même case d'entrée, même si une boîte s'y trouve.
     *
     * @param direction     Direction du déplacement du joueur
     * @param grilleInterne La grille dans laquelle on entre
     * @return La position d'entrée standard, ou null si la grille est infranchissable
     */
    private Position calculerPositionEntreeAvecBoite(Direction direction, Grille grilleInterne) {
        int larg = grilleInterne.getLargeur();
        int haut = grilleInterne.getHauteur();

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

        // Parcourir depuis le bord jusqu'à trouver une case qui n'est PAS un mur
        // (peut être vide, une cible, ou une boîte — tout sauf un mur)
        for (int pas = 0; pas < maxPas; pas++) {
            int x = xDepart + dx * pas;
            int y = yDepart + dy * pas;
            if (!grilleInterne.estDansGrille(x, y)) continue;

            Objet obj = grilleInterne.getObjet(x, y);

            // Une case est une entrée valide si elle n'est pas un mur
            if (!(obj instanceof Mur)) {
                return new Position(x, y);
            }
        }

        // Toute la ligne/colonne est bloquée par des murs
        return null;
    }

    
    /**
     * Faire entrer une BOÎTE dans une Piece (poussée par le joueur).
     *
     * Configuration : Joueur → Boîte → Piece
     * - La boîte entre dans la grille interne de la Piece (première case libre)
     * - Le joueur avance sur la case où était la boîte (dans la grille parente)
     * - La pile de grilles n'est PAS modifiée (le joueur reste dans la grille parente)
     *
     * @param joueur         Le joueur qui pousse
     * @param boite          La boîte à faire entrer
     * @param piece          La Piece destination
     * @param direction      La direction du déplacement
     * @param grilleActuelle La grille parente
     * @return true si l'opération a réussi
     */
    private boolean entrerBoiteDansPiece(Joueur joueur, Boite boite, Piece piece,
                                         Direction direction, Grille grilleActuelle) {

        Grille grilleInterne = piece.getGrilleInterne();

        // Calculer la position d'entrée de la boîte dans la grille interne
        Position posEntreeBoite = calculerPositionEntree(direction, grilleInterne);

        if (posEntreeBoite == null) {
            return false; // Grille infranchissable
        }

        // Vérifier que la case d'entrée est bien libre pour la boîte
        if (!grilleInterne.estCaseLibre(posEntreeBoite)) {
            return false;
        }

        // Mémoriser la position de la boîte dans la grille parente
        // (c'est là que le joueur va avancer)
        Position posBoiteDansParente = new Position(boite.getX(), boite.getY());

        // Retirer la boîte de la grille parente (matrice ET liste boites)
        grilleActuelle.retirerObjet(posBoiteDansParente);
        grilleActuelle.getBoites().remove(boite);  // Supprimer de la liste pour éviter le double enregistrement

        // Placer la boîte dans la grille interne
        // setObjet l'ajoutera dans grilleInterne.boites et mettra à jour grilleParente de la boîte
        grilleInterne.setObjet(boite, posEntreeBoite);

        // Mettre à jour l'état surCible de la boîte
        boolean boiteSurCible = false;
        for (Cible c : grilleInterne.getCibles()) {
            if (c.getPosition().equals(posEntreeBoite)) {
                boiteSurCible = true;
                c.setOccupee(true);
                break;
            }
        }
        boite.setSurCible(boiteSurCible);

        // Le joueur avance sur la case où était la boîte (dans la grille parente)
        grilleActuelle.retirerObjet(joueur.getPosition());
        grilleActuelle.setObjet(joueur, posBoiteDansParente);

        // Vérifier la victoire après le déplacement
        if (estNiveauGagneRecursif()) {
            niveauTermine = true;
        }

        return true;
    }


    
    /**
     * Faire SORTIR le joueur d'une Piece (retour à la grille parente).
     *
     * Cas normal : la case de sortie est libre → le joueur sort.
     * Cas avec boîte : la case de sortie contient une boîte → le joueur sort
     *   EN POUSSANT la boîte si la case après la boîte est libre.
     *   Si la boîte est bloquée, la sortie est impossible.
     *
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

        // ── Cas : une boîte se trouve sur la case de sortie ──
        // C'est typiquement la boîte qu'on vient de sortir avec sortirBoiteDePiece().
        // Le joueur la pousse en sortant, si la case après est libre.
        if (objetEnSortie instanceof Boite && !(objetEnSortie instanceof Piece)) {
            Boite boiteEnSortie = (Boite) objetEnSortie;
            Position posApresBoite = direction.appliquerSur(posSortie);

            if (!grilleParente.estDansGrille(posApresBoite)
                    || !grilleParente.estCaseLibre(posApresBoite)) {
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
            for (Cible c : grilleParente.getCibles()) {
                if (c.getPosition().equals(posApresBoite)) {
                    boiteSurCible = true;
                    c.setOccupee(true);
                    break;
                }
            }
            boiteEnSortie.setSurCible(boiteSurCible);

        } else if (objetEnSortie != null && !(objetEnSortie instanceof Cible)) {
            // Sortie bloquée par autre chose (mur, etc.)
            pileGrilles.push(grilleInterne);
            pilePieces.push(piece);
            return false;
        }

        // ── Déplacer le joueur dans la grille parente ──
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


    /**
     * Faire SORTIR une BOÎTE d'une Piece (poussée par le joueur depuis l'intérieur).
     *
     * Quand le joueur est dans la grille interne et pousse une boîte vers le bord :
     *  1. La boîte sort et apparaît dans la grille parente, sur la case juste après la Piece.
     *  2. Le joueur avance sur la case que la boîte vient de quitter (dans la grille interne).
     *  3. Au prochain appui dans la même direction, le joueur lui-même peut sortir via
     *     sortirDePiece(), et s'il y a la boîte juste devant lui dans la grille parente,
     *     il la pousse encore (comportement standard de poussée de boîte).
     *
     * Si la case de destination de la boîte dans la grille parente est bloquée, l'opération échoue.
     *
     * @param joueur         Le joueur qui pousse (dans la grille interne)
     * @param boite          La boîte à faire sortir
     * @param direction      La direction du déplacement
     * @param grilleInterne  La grille interne depuis laquelle on sort
     * @return true si la sortie de la boîte a réussi
     */
    private boolean sortirBoiteDePiece(Joueur joueur, Boite boite,
                                       Direction direction, Grille grilleInterne) {
        // Récupérer la Piece et la grille parente depuis la pile
        Piece piece = pilePieces.peek();
        if (piece == null) return false;

        // La grille parente est juste en dessous du sommet de la pile
        // On y accède sans dépiler (le joueur reste dans la grille interne)
        Grille grilleParente = pileGrilles.get(pileGrilles.size() - 2);

        // La boîte sortira sur la case juste après la Piece dans la direction de sortie
        Position posSortieBoite = direction.appliquerSur(piece.getPosition());

        // Vérifier que cette case existe et est libre dans la grille parente
        if (!grilleParente.estDansGrille(posSortieBoite)
                || !grilleParente.estCaseLibre(posSortieBoite)) {
            // Case bloquée dans la grille parente : la boîte ne peut pas sortir
            return false;
        }

        // Mémoriser la position actuelle de la boîte dans la grille interne
        // (c'est là que le joueur va se placer ensuite)
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

        // Le joueur avance sur la case que la boîte vient de quitter (dans la grille interne)
        // Il reste dans la grille interne, juste au bord, prêt à sortir au prochain appui
        grilleInterne.retirerObjet(joueur.getPosition());
        grilleInterne.setObjet(joueur, posBoiteDansGrilleInterne);

        // Vérifier la victoire après le déplacement
        if (estNiveauGagneRecursif()) {
            niveauTermine = true;
        }

        return true;
    }

    /**
     * Vérifier si le niveau est gagné en parcourant TOUTES les grilles récursivement.
     *
     * Pourquoi cette méthode est nécessaire :
     *   La boîte du niveau peut se trouver dans la grille racine OU dans la grille interne
     *   d'une Piece (si elle a été poussée dedans). Dans ce cas, elle n'est plus dans
     *   grilleRacine.boites (car on l'a retirée lors de entrerBoiteDansPiece).
     *   grilleRacine.estNiveauGagne() ne la voit donc plus et retourne toujours false.
     *
     * Cette méthode explore toutes les grilles (racine + grilles internes des Pieces)
     * pour trouver toutes les boîtes normales et vérifier qu'elles sont sur des cibles.
     *
     * @return true si toutes les boîtes normales sont sur des cibles
     */
    private boolean estNiveauGagneRecursif() {
        // Collecter toutes les boîtes normales dans toutes les grilles
        // et vérifier qu'elles sont toutes sur des cibles
        ResultatRecherche resultat = new ResultatRecherche();
        chercherBoitesDansGrille(grilleRacine, resultat);

        // Le niveau est gagné si on a trouvé au moins une boîte normale
        // ET qu'elles sont toutes sur des cibles
        return resultat.totalBoites > 0 && resultat.boitesHorsCible == 0;
    }

    /**
     * Classe interne pour stocker le résultat de la recherche récursive de boîtes.
     * Simple conteneur de deux compteurs.
     */
    private static class ResultatRecherche {
        int totalBoites = 0;       // Nombre total de boîtes normales trouvées
        int boitesHorsCible = 0;   // Nombre de boîtes normales PAS sur une cible
    }

    /**
     * Parcourir une grille et toutes ses grilles internes (via les Pieces)
     * pour compter les boîtes normales et celles qui ne sont pas sur des cibles.
     *
     * Méthode récursive : quand on rencontre une Piece, on explore sa grilleInterne.
     *
     * @param grille   La grille à explorer
     * @param resultat Le compteur à mettre à jour
     */
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
     * Annuler le dernier mouvement (touche U).
     *
     * Gère trois cas :
     *  1. Déplacement simple ou avec boîte → délègue à historique.annulerDernierMouvement()
     *  2. Transition ENTRÉE dans une Piece → en plus de repositionner le joueur,
     *     dépile pileGrilles et pilePieces (on "désempile" l'entrée)
     *  3. Transition SORTIE d'une Piece → en plus de repositionner le joueur,
     *     réempile pileGrilles et pilePieces (on "réempile" pour revenir dans la Piece)
     *
     * @return true si un mouvement a été annulé
     */
    public boolean annulerMouvement() {
        if (historique.estVide()) return false;

        // Avant d'annuler, noter l'état des piles
        // L'historique va modifier les grilles directement.
        // On passe la grille ACTIVE au moment de l'annulation (le sommet de la pile).
        // Pour les transitions, historique.annulerDernierMouvement() travaille
        // directement sur les grilles stockées dans le Mouvement.
        Grille grilleActive = getGrilleActive();
        boolean annule = historique.annulerDernierMouvement(grilleActive);

        if (!annule) return false;

        if (nombreMouvements > 0) nombreMouvements--;
        niveauTermine = false;

        // Si la dernière annulation était une transition de grille,
        // ajuster les piles en conséquence
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
