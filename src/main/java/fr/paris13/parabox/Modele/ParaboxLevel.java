package fr.paris13.parabox.Modele;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gère les niveaux récursif du jeu
 * 
 */
public class ParaboxLevel {
    
    private static final String DOSSIER = "src/main/resources";
    private static Grille[] levelList;
    
    /**
     * Initialise et retourne un tableau des noveaux disponibles.
     * 
     * @return tableau des niveaux disponibles
     */
    public static Grille[] getList(){
        List<String> levels = listerFichiersNiveaux();   
        levelList = new Grille [levels.size()];
        for(int i = 0; i<levels.size(); i++){
            levelList[i] = ChargeurNiveau.charger(DOSSIER + File.separator + levels.get(i));
        }
        return levelList;
    }
    
    /**
     * Lister les fichiers niveau*.txt dans le dossier courant,
     * triés par ordre alphabétique.
     *
     * @return La liste des noms de fichiers trouvés
     */
    /*public static List<String> listerFichiersNiveaux() {
        List<String> liste = new ArrayList<>();
        File dir = new File(DOSSIER);
        if (dir.exists() && dir.isDirectory()) {
            File[] fichiers = dir.listFiles();
            if (fichiers != null) {
                Arrays.sort(fichiers); // tri alphabétique
                for (File f : fichiers) {
                    if (f.isFile() && f.getName().startsWith("niveau")
                            && f.getName().endsWith(".txt")) {
                        liste.add(f.getName());
                    }
                }
            }
        }
        return liste;
    }*/
    public static List<String> listerFichiersNiveaux() {
    List<String> liste = new ArrayList<>();
    
    // Essayer depuis le disque (mode développement)
    File dir = new File(DOSSIER);
    if (dir.exists() && dir.isDirectory()) {
        File[] fichiers = dir.listFiles();
        if (fichiers != null) {
            Arrays.sort(fichiers);
            for (File f : fichiers) {
                if (f.isFile() && f.getName().startsWith("niveau")
                        && f.getName().endsWith(".txt")) {
                    liste.add(f.getName());
                }
            }
        }
        if (!liste.isEmpty()) return liste;
    }
    
    // Fallback : liste codée en dur pour le jar installé
    for (int i = 1; i <= 5; i++) {
        liste.add("niveau" + i + ".txt");
    }
    return liste;
	}
}
