 public class Mouvement {	/*j ai modif !!!*/
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

    
    public Direction getDirection() {
    	return this.direction;
    }
    
    public Boite getboitePoussee() {
    	return this.boitePoussee;
    }
    
 }
