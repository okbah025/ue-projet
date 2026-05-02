/*gère les niveaux classiques et récursifs*/
/*on ecrit l'historique d'un seul niveau, un fichier par niveau*/
package fr.paris13.parabox.Modele;

import java.util.Stack;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


public class SauvegardeHistorique {

	String fichierEcrireHisto;


	public SauvegardeHistorique(String fichierEcrireHisto) {
		this.fichierEcrireHisto = fichierEcrireHisto;
	
	}
	
	
	 //  version simple
    public void ecrireHistorique(Jeu jeu) {
        ecrireDepuisHistorique(jeu.getHistorique());
    }

    //  version récursive
    public void ecrireHistoriqueRecursif(JeuRecursif jeu) {
        ecrireDepuisHistorique(jeu.getHistorique());
    }

    // méthode commune (le vrai travail)
    private void ecrireDepuisHistorique(Historique histo) {

        try {
            FileWriter fw = new FileWriter(fichierEcrireHisto, true);
            BufferedWriter writer = new BufferedWriter(fw);

            Stack<Character> stack = histo.viderEtConvertir();

            StringBuilder chaine = new StringBuilder();

            while (!stack.isEmpty()) {
                chaine.append(stack.pop());
            }

            writer.write(chaine.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








/*
	public void ecrireHistorique(Jeu jeu) {

    try {
        FileWriter fw = new FileWriter(fichierEcrireHisto, true); //mode append
        BufferedWriter writer = new BufferedWriter(fw);

        Stack<Character> stack = jeu.getHistorique().viderEtConvertir();

        String chaine = "";

        while (!stack.isEmpty()) {
            chaine += stack.pop();
        }

        writer.write(chaine);
        /*on veut tous les historiques des sessions de jeu de ce niveau sur une seule ligne*
        writer.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
} */



/*
	public char  convertir_direction_en_caractere(Direction d, Boite b) {
		char c;
		
		if (b == null) {
			
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


/* dans main , au moment de quitter la partie : 
svgHisto = new SauvegardeHistorique(fichierEcrireHisto);

svgHisto.ecrireHistorique(jeu)

remarque 1: on veut que notre fichier avec l'historique des deplacements du niveau n°X se nomme "niveauX_histo_deplacements.txt"

remarque 2: et si on a gagné, on enregistre l'historique des deplacements dans un fichier "niveauX_sol_deplacements.txt"
*/
