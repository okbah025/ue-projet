package fr.paris13.parabox.Modele;
/**
 * Classe Jeu
 * 
 * Cette classe est le contrôleur principal du jeu Sokoban.
 * Elle coordonne la grille, le joueur, l'historique et les règles du jeu.
 * 
 * Responsabilités :
 * - Gérer la grille actuelle
 * - Traiter les commandes du joueur
 * - Vérifier les conditions de victoire
 * - Gérer l'historique des mouvements
 * - Compter les déplacements
 * 
 */
public class Jeu {
    
    // ========== ATTRIBUTS ==========
    
    /**
     * La grille de jeu actuelle
     */
    private Grille grilleActuelle;
    
    /**
     * L'historique des mouvements pour le Ctrl+Z
     */
    private Historique historique;
    
    /**
     * Compteur de mouvements effectués
     * (ne compte pas les mouvements annulés)
     */
    private int nombreMouvements;
    
    /**
     * Compteur de poussées de boîtes
     */
    private int nombrePoussees;
    
    /**
     * Indique si le niveau est terminé (gagné)
     */
    private boolean niveauTermine;
    
    // ========== CONSTRUCTEUR ==========
    
    /**
     * Constructeur du jeu
     * Initialise un jeu avec une grille donnée
     * @param grille La grille de départ
     */
    public Jeu(Grille grille) {
        if (grille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être null");
        }
        
        this.grilleActuelle = grille;
        this.historique = new Historique();
        this.nombreMouvements = 0;
        this.nombrePoussees = 0;
        this.niveauTermine = false;
    }
    
    /**
     * Constructeur sans grille (pour initialisation ultérieure)
     */
    public Jeu() {
        this.historique = new Historique();
        this.nombreMouvements = 0;
        this.nombrePoussees = 0;
        this.niveauTermine = false;
    }
    
    // ========== GETTERS ==========
    
    /**
     * Obtenir la grille actuelle
     * @return La grille de jeu
     */
    public Grille getGrille() {
        return this.grilleActuelle;
    }
    
    /**
     * Obtenir le nombre de mouvements effectués
     * @return Le nombre de mouvements
     */
    public int getNombreMouvements() {
        return this.nombreMouvements;
    }
    
    /**
     * Obtenir le nombre de poussées de boîtes
     * @return Le nombre de poussées
     */
    public int getNombrePoussees() {
        return this.nombrePoussees;
    }
    
    /**
     * Vérifier si le niveau est terminé
     * @return true si le niveau est gagné, false sinon
     */
    public boolean estNiveauTermine() {
        return this.niveauTermine;
    }
    
    /**
     * Obtenir l'historique des mouvements
     * @return L'historique
     */
    public Historique getHistorique() {
        return this.historique;
    }
    
    // ========== SETTERS ==========
    
    /**
     * Définir une nouvelle grille
     * Réinitialise les compteurs et l'historique
     * @param nouvelleGrille La nouvelle grille
     */
    public void setGrille(Grille nouvelleGrille) {
        if (nouvelleGrille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être null");
        }
        
        this.grilleActuelle = nouvelleGrille;
        this.reinitialiser();
    }
    
    // ========== MÉTHODES PRINCIPALES ==========
    
    /**
     * Déplacer le joueur dans une direction
     * Gère automatiquement :
     * - Le déplacement du joueur
     * - La poussée des boîtes
     * - L'historique
     * - Les compteurs
     * - La vérification de victoire
     * 
     * @param direction La direction du mouvement
     * @return true si le mouvement a réussi, false sinon
     */
    public boolean deplacerJoueur(Direction direction) {
        // Vérifier que le niveau n'est pas déjà terminé
        if (niveauTermine) {
            return false;
        }
        
        // Vérifier qu'on a une grille et un joueur
        if (grilleActuelle == null || grilleActuelle.getJoueur() == null) {
            return false;
        }
        
        Joueur joueur = grilleActuelle.getJoueur();
        
        // Sauvegarder l'état avant le mouvement
        Position positionAvant = new Position(joueur.getX(), joueur.getY());
        boolean joueurSurCibleAvant = joueur.estSurCible();
        
        // Calculer la position cible
        Position positionCible = direction.appliquerSur(positionAvant);
        
        // Vérifier s'il y a une boîte à pousser
        Objet objetCible = grilleActuelle.getObjet(positionCible);
        Boite boiteAPoussee = null;
        Position posBoiteAvant = null;
        boolean boiteSurCibleAvant = false;
        
        if (objetCible instanceof Boite) {
            boiteAPoussee = (Boite) objetCible;
            posBoiteAvant = new Position(boiteAPoussee.getX(), boiteAPoussee.getY());
            boiteSurCibleAvant = boiteAPoussee.estSurCible();
        }
        
        // Essayer d'effectuer le déplacement
        boolean mouvementReussi = grilleActuelle.deplacerJoueur(direction);
        
        if (mouvementReussi) {
            // Le mouvement a réussi, mettre à jour les compteurs
            nombreMouvements++;
            
            // Sauvegarder les positions après le mouvement
            Position positionApres = new Position(joueur.getX(), joueur.getY());
            boolean joueurSurCibleApres = joueur.estSurCible();
            
            // Enregistrer dans l'historique
            if (boiteAPoussee != null) {
                // Mouvement avec poussée de boîte
                nombrePoussees++;
                Position posBoiteApres = new Position(boiteAPoussee.getX(), boiteAPoussee.getY());
                boolean boiteSurCibleApres = boiteAPoussee.estSurCible();
                
                historique.ajouterMouvementAvecBoite(
                    direction,
                    positionAvant, positionApres,
                    joueurSurCibleAvant, joueurSurCibleApres,
                    boiteAPoussee,
                    posBoiteAvant, posBoiteApres,
                    boiteSurCibleAvant, boiteSurCibleApres
                );
            } else {
                // Mouvement simple
                historique.ajouterMouvement(
                    direction,
                    positionAvant, positionApres,
                    joueurSurCibleAvant, joueurSurCibleApres
                );
            }
            
            // Vérifier si le niveau est gagné
            if (grilleActuelle.estNiveauGagne()) {
                niveauTermine = true;
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Annuler le dernier mouvement (Ctrl+Z)
     * @return true si un mouvement a été annulé, false sinon
     */
    public boolean annulerMouvement() {
        boolean annule = historique.annulerDernierMouvement(grilleActuelle);
        
        if (annule) {
            // Décrémenter les compteurs
            if (nombreMouvements > 0) {
                nombreMouvements--;
            }
            
            // Si le niveau était marqué comme terminé, le remettre en cours
            niveauTermine = false;
        }
        
        return annule;
    }
    
    /**
     * Réinitialiser le jeu
     * Remet les compteurs à zéro et vide l'historique
     */
    public void reinitialiser() {
        this.nombreMouvements = 0;
        this.nombrePoussees = 0;
        this.niveauTermine = false;
        this.historique.vider();
    }
    
    /**
     * Vérifier si on peut annuler un mouvement
     * @return true si l'historique n'est pas vide, false sinon
     */
    public boolean peutAnnuler() {
        return !historique.estVide();
    }
    
    // ========== MÉTHODES D'AFFICHAGE ==========
    
    /**
     * Obtenir les statistiques du jeu
     * @return Une chaîne avec les statistiques
     */
    public String getStatistiques() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mouvements : ").append(nombreMouvements).append("\n");
        sb.append("Poussées : ").append(nombrePoussees).append("\n");
        
        if (grilleActuelle != null) {
            int boitesSurCibles = 0;
            for (Boite boite : grilleActuelle.getBoites()) {
                if (boite.estSurCible()) {
                    boitesSurCibles++;
                }
            }
            int totalCibles = grilleActuelle.getCibles().size();
            sb.append("Progression : ").append(boitesSurCibles).append("/").append(totalCibles);
            sb.append(" boîtes sur cibles\n");
        }
        
        if (niveauTermine) {
            sb.append("NIVEAU TERMINÉ !\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Afficher la grille en mode texte
     * @return La représentation textuelle de la grille
     */
    public String afficherGrille() {
        if (grilleActuelle == null) {
            return "Aucune grille chargée";
        }
        return grilleActuelle.afficherGrille();
    }
    
    /**
     * Représentation textuelle du jeu
     * @return Une description du jeu
     */
    @Override
    public String toString() {
        if (grilleActuelle == null) {
            return "Jeu non initialisé";
        }
        return "Jeu - " + grilleActuelle.toString() + " - " + nombreMouvements + " mouvements";
    }
}
