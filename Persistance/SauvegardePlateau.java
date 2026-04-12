import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


/*pour l'instant on gere un fichier du type du fichier nommé "niveau 1", cad niveau non récursif*/ 
public class SauvegardePlateau {
	
	String fichierEcriture;
	
	public SauvegardePlateau(String fichierEcriture) {
		this.fichierEcriture=fichierEcriture;
		
	}

	public void ecrireGrille(Grille grille) {
	

		try {
		    // Création d'un fileWriter pour écrire dans un fichier
		    FileWriter fileWriter = new FileWriter("fichierEcriture", false);        /*contenu de fichierEcriture est effacé, fichier est prêt*/

		    // Création d'un bufferedWriter qui utilise le fileWriter
		    BufferedWriter writer = new BufferedWriter(fileWriter);

		    // ajout d'un texte à notre fichier
		    
		    
		    writer.write(grille.afficherGrille());

		    // Retour à la ligne
		    writer.newLine();

		   /* writer.write("preferenceColor=#425384");*/
		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
