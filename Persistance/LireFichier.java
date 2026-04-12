import java.io.FileReader;
import java.io.IOException;

public class LireFichier {
	
	String fichierLecture;
	
	public  LireFichier(String fichierLecture) {
		this.fichierLecture=fichierLecture;
		
		/*try {
		 entree = new FileReader(fichierLecture);
		 
		 }  catch (IOException e) {
            e.printStackTrace(); }*/
		 
	}
	
	
	
	public void remplirGrille(Grille grille) {
    int i = 0, j = 0, c;

    try {
        FileReader entree = new FileReader(fichierLecture);
        
        while ((c = entree.read()) != '\n'); // la grille commence apres la ligne avec le nom du monde et la dimension
       
        while ((c = entree.read()) != -1) {

            if (c == '\n') {
                j++;
                i = 0;
                continue;
            }

            Objet o = convertir_car_en_obj(c, i, j, grille);
            grille.setObjet(o, i, j);

            i++; // colonne
        }

        entree.close(); // ✅ on ferme ici

    } catch (IOException e) {
        e.printStackTrace();
    }
}
	
	public Objet  convertir_car_en_obj(int c, int x, int y, Grille grille) {
		Objet o= null;
		
	
	
		if (c=='#') o = new Mur(x, y, grille);
		if (c=='@')  o = new Joueur(x, y, grille);
		
		/*Joueur sur cible : ???*/
		if (c=='+' ) {
			/**Joueur o1= (Joueur) o;
			o1= new Joueur(x, y, grille);
			o1.setSurCible(true);*/
			Joueur j = new Joueur(x, y, grille);
  			j.setSurCible(true);
   			o = j;

   			grille.getCibles().add(new Cible(x, y, grille)); // IMPORTANT
			
		}
		
		if (c== '$' ) o = new Boite(x, y, grille);
		
		/*boite sur cible*/
		if (c=='*' ) { 
			Boite b = new Boite(x, y, grille);
   			 b.setSurCible(true);
  			 o = b;

   		 	grille.getCibles().add(new Cible(x, y, grille)); // IMPORTANT	

		}
		
		if (c=='.') o= new Cible(x, y, grille);
		if (c== ' ') o= null;
		
		return o;
	
	}
	
	
	public int getDimGrilleFichier() {
	 try (FileReader entree = new FileReader(fichierLecture)) {
	
	 char c = entree.read();
	 String chaine= "";
	 int nb;
	
        // while (!(c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) c= entree.read(); //avancer jusqu'à la premiere lettre, peut etre qu'elle nest pas tout au debut du fichier ?
          
	 c= entree.read(); //on arrive à " "
	 
	 while ( (c!=' ') || (c!= '\n')) {
	 	chaine= chaine + c;
	 	
	 	c=entree.read();
	}
	
	nb= Integer.parseInt(chaine);
	
	return nb;
	
	
	} catch (IOException e) {
        e.printStackTrace();
	}
   	 return -1;
	
      }
	
	
	
	public char getNomGrilleFichier() {
		char c = null;
		 try (FileReader entree = new FileReader(fichierLecture)) {
			 c = entree.read();
			 return c;
		}
		
		catch (IOException e) {
        e.printStackTrace();
 	        }

    		return c;	
    	}	

	
	
	
	//supp tout ce qui vient en dessous ?
	
	public int getHauteurGrilleDuFichier() {
    int j = 0, c;
    boolean lastWasNewLine = true;

    try (FileReader entree = new FileReader(fichierLecture)) {

        while ((c = entree.read()) != -1) {
            if (c == '\n') {
                j++;
                lastWasNewLine = true;
            } else {
                lastWasNewLine = false;
            }
        }

        if (!lastWasNewLine) j++; // dernière ligne

        return j;

    } catch (IOException e) {
        e.printStackTrace();
    }

    return -1;
}

public int getLargeurGrilleDuFichier() {
    int i = 0, i_max = 0, c;

    try (FileReader entree = new FileReader(fichierLecture)) {

     while ((c = entree.read()) != -1) {

    if (c == '\n') {
        if (i > i_max) i_max = i;
        i = 0;
        continue; // 🔥 IMPORTANT
    }

    i++; // uniquement pour les vrais caractères
}
     
     
     
     
       /* while ((c = entree.read()) != -1) {
            i++;

            if (c == '\n') {
                if (i > i_max) i_max = i;
                i = 0;
            }
        }*/

        // ⚠️ gérer la dernière ligne si elle ne finit pas par \n
        if (i > i_max) i_max = i;

        return i_max;

    } catch (IOException e) {
        e.printStackTrace();
    }

    return -1;
}

}


/*dans le main du jeu (exemple: une classe main a part) : 
	*on utilise i et j de getHauteur et getLargeur de cette classe*
        Grille grille = new Grille(i, j, "Test1");
        
        LireFichier lireFich= new LireFichier('nomDuFich');
        lireFich.remplirGrille(grille);
        
        jeu.afficherGrille(grille);
     
*/
        
