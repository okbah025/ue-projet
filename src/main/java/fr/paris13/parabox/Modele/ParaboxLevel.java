package fr.paris13.parabox.Modele;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParaboxLevel {
    
    private static final String DOSSIER = "src/main/resources";
    private static Grille[] levelList;
    
    public static Grille[] getList(){
        List<String> levels = listerFichiersNiveaux();   
        levelList = new Grille [levels.size()];
        System.out.println(levels.size());
        for(int i = 0; i<levels.size(); i++){
            levelList[i] = ChargeurNiveau.charger(DOSSIER + File.separator + levels.get(i));
        }
        return levelList;
    }
    
    private static List<String> listerFichiersNiveaux() {
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
    }
}
