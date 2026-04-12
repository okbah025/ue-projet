

public class MainTestFich {
	
	public static void main(String[] arg) {
	String fichierLecture = arg[0];
	String fichierEcriture=arg[1];
	String fichierEcritureHistoDeplacement= arg[2];
	
	/*========ON CHARGE LE NIVEAU============*/
        
        LireFichier lireFich= new LireFichier(arg[0]);	
        int i = lireFich.getLargeurGrilleDuFichier();
        int j = lireFich.getHauteurGrilleDuFichier();
        
        System.out.println("Largeur = " + i);
	System.out.println("Hauteur = " + j);
        
        
        Grille grille = new Grille(i, j, "Test1");
        
        lireFich.remplirGrille(grille);
        
        Jeu jeu = new Jeu(grille);
        
        System.out.println(jeu.afficherGrille());
        
        
        /*========DEPLACEMENTS==========*/
        
        jeu.deplacerJoueur(Direction.BAS);
        jeu.deplacerJoueur(Direction.BAS);
        jeu.deplacerJoueur(Direction.BAS);
        jeu.deplacerJoueur(Direction.DROITE);
        jeu.deplacerJoueur(Direction.HAUT);
        jeu.deplacerJoueur(Direction.HAUT);
        jeu.deplacerJoueur(Direction.HAUT);
        jeu.deplacerJoueur(Direction.GAUCHE);
        
        System.out.println("DDDruuul \n");
        
        
        /*========SAUVEGARDE===========*/        
        
        
        /************test de la sauvegarde du plateau dans le fichier fichierEcriture : normalement on sauvegarde apres avoir joué*/
        
        SauvegardePlateau svgPlateau= new SauvegardePlateau(arg[1]);
        
        svgPlateau.ecrireGrille(grille);
        
        
        /**********test de la svg de l'historique des déplacement dans le fichier fichierEcritureHistoDeplacement : 
        ** au moment où on veut quitter la partie, utile pour garder la trace des déplacements effectués dans le niveau courant, on aura juste a reprendre ce fichier et continuer de le remplir (jusqu'à ce que la partie soit gagnée)
        ** ou quand on a gagné un niveau et qu'on veut sauvegarder la solution = historique des déplacements
        *************/
        
        SauvegardeHistorique svgHisto = new SauvegardeHistorique(fichierEcritureHistoDeplacement); /*(arg[2])*/

	svgHisto.ecrireHistorique(jeu);
        
        
        }
}



/* exécuter : java MainTestFich niveau1 fichierEcriture fichierEcritureHistoDeplacement
** voir Readme.txt pour plus d'explications*/
