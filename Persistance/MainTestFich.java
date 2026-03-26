

public class MainTestFich {
	
	public static void main(String[] arg) {
	String fichierLecture = arg[0];
	

        
        LireFichier lireFich= new LireFichier(arg[0]);	
        int i = lireFich.getLargeurGrilleDuFichier();
        int j = lireFich.getHauteurGrilleDuFichier();
        
        System.out.println("Largeur = " + i);
System.out.println("Hauteur = " + j);
        
        
        Grille grille = new Grille(i, j, "Test1");
        
        lireFich.remplirGrille(grille);
        
        Jeu jeu = new Jeu(grille);
        
        System.out.println(jeu.afficherGrille());
        }
}
