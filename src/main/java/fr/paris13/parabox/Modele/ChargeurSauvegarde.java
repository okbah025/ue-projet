package fr.paris13.parabox.Modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChargeurSauvegarde {


public static Grille charger(String fichierLecture) {

    try {

        BufferedReader br = new BufferedReader(new FileReader(fichierLecture));

        // header
        String header = br.readLine();
        String[] infos = header.split(" ");

        String nom = infos[0];
        int largeur = Integer.parseInt(infos[1]);
        int hauteur = Integer.parseInt(infos[2]);

        Grille grille = new Grille(largeur, hauteur, nom);

        String ligne;
        int y = 0;

        while ((ligne = br.readLine()) != null && y < hauteur) {

            for (int x = 0; x < largeur; x++) {

                char c = (x < ligne.length()) ? ligne.charAt(x) : ' ';

                Objet o = convertir(c, x, y, grille);

                if (o != null) {
                    grille.setObjet(o, x, y);
                }
            }

            y++;
        }

        br.close();
        return grille;

    } catch (Exception e) {
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
