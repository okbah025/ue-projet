/* on part du principe qu'il est question du sokoban classique*/
/*on ecrit historique d'un seul niveau*/

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


public class SauvegardeHistorique {

	String fichierEcrireHisto;


	public SauvegardeHistorique(String fichierEcrireHisto) {
		this.fichierEcrireHisto = fichierEcrireHisto;
	
	}
	
	

/*pb : pour une partie que je reprends, je veux garder ce que j'avais ecrit dans mon fichier historique de ce niveau. -> il faut ecrire en mode append ? ou recopier qqchose ? */



	public void ecrireHistorique(Jeu jeu) {
		/*parcours de la pile de mvts de l'historique*/
		/*on doit convertir les directions en r, l, u, d (right, left, up down), en majuscule si le mvt a provoqué une poussée de boite*/
		char c;
		String chaine = "";
		String chaine2 ="";
		int i;
		Direction d;
		Boite b;
		
		Mouvement mvt;
		
		
		try {
		    // Création d'un fileWriter pour écrire dans un fichier
		    FileWriter fileWriter = new FileWriter(fichierEcrireHisto, false);        /*contenu de fichierEcriture est effacé, fichier est prêt*/

		    // Création d'un bufferedWriter qui utilise le fileWriter
		    BufferedWriter writer = new BufferedWriter(fileWriter);

		
		while (jeu.getHistorique().getNombreMouvements() > 0) {
			mvt = (Mouvement) jeu.getHistorique().popMouvement();
			
			d = (Direction) mvt.getDirection();
			/*il faut faire un new avec constructeur?*/
			b = (Boite) mvt.getboitePoussee();
			
			c= convertir_direction_en_caractere(d, b);
			
			chaine= chaine + c;

			
		}
			/*mettre dans le bon sens la chaine*/
			for (i=chaine.length() -1; i>=0; i--) {
				chaine2= chaine2 + chaine.charAt(i);
			}
			
			
			
			writer.write(chaine2);	
		    // Retour à la ligne
		    writer.newLine();

		   /* writer.write("preferenceColor=#425384");*/
		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
			
		
	}
	
	
	

	public char  convertir_direction_en_caractere(Direction d, Boite b) {
		char c;
		
		if (b == null) {
			/*if (d==Direction.HAUT) c ='u';
			if (d==Direction.BAS) c ='d';
			if (d==Direction.GAUCHE) c ='l';
			if (d==Direction.DROITE) c ='r';*/
			
			switch (d) {
			    case HAUT:
				c = 'u';
				break;
			    case BAS:
				c = 'd';
				break;
			    case GAUCHE:
				c = 'l';
				break;
			    case DROITE:
				c = 'r';
				break;
			    default:
				throw new IllegalArgumentException("Direction inconnue");
			}
		}
		
		else {
			switch (d) {
			    case HAUT:
				c = 'U';
				break;
			    case BAS:
				c = 'D';
				break;
			    case GAUCHE:
				c = 'L';
				break;
			    case DROITE:
				c = 'R';
				break;
			    default:
				throw new IllegalArgumentException("Direction inconnue");
			}
			
		}
		
		return c;
	}	


}



/* dans main : 
svgHisto = new SauvegardeHistorique(fichierEcrireHisto);

svgHisto.ecrireHistorique(jeu)

*/
