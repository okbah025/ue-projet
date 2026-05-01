package fr.paris13.parabox.Modele;

import java.io.FileReader;
import java.io.IOException;

public class ChargeurSauvegarde {

    public static Grille charger(String fichierLecture) {

        try (FileReader entree = new FileReader(fichierLecture)) {

            // ─────────────────────────────
            // 1. LIRE LIGNE D'EN-TÊTE
            // format : Nom largeur hauteur
            // ─────────────────────────────

            StringBuilder header = new StringBuilder();
            int c;

            while ((c = entree.read()) != -1 && c != '\n') {
                header.append((char) c);
            }

            String[] infos = header.toString().trim().split(" ");

            String nom = infos[0];
            int largeur = Integer.parseInt(infos[1]);
            int hauteur = Integer.parseInt(infos[2]);

            Grille grille = new Grille(largeur, hauteur, nom);

            // ─────────────────────────────
            // 2. LIRE LA GRILLE
            // ─────────────────────────────

            int x = 0;
            int y = 0;

            while ((c = entree.read()) != -1) {
		    if (c == '\r') continue; /*windows*/
		    
                if (c == '\n') {
                    y++;
                    x = 0;
                    continue;
                }

                if (y >= hauteur) break;

                Objet o = convertir(c, x, y, grille);

                if (o != null) {
                    grille.setObjet(o, x, y);
                }

                x++;
            }

            return grille;

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ─────────────────────────────
    // CONVERSION CHAR → OBJET
    // ─────────────────────────────

    private static Objet convertir(int c, int x, int y, Grille grille) {

        switch (c) {

            case '#':
                return new Mur(x, y, grille);

            case '@':
                return new Joueur(x, y, grille);

            case '$':
                return new Boite(x, y, grille);

           case '.': {
   	 Cible cible = new Cible(x, y, grille);
   	 grille.getCibles().add(cible);
   	 return cible;
	}

            case '+': {
    Cible cible = new Cible(x, y, grille);
    grille.getCibles().add(cible);

    Joueur j = new Joueur(x, y, grille);
    j.setSurCible(true);
    return j;
}
            case '*': {
    Cible cible = new Cible(x, y, grille);
    grille.getCibles().add(cible);

    Boite b = new Boite(x, y, grille);
    b.setSurCible(true);
    return b;
}


            case ' ':
                return null;

            default:
                return null;
        }
    }
}
