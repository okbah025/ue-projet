package fr.paris13.parabox.Modele;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SauvegardePlateau {

    private String fichierEcriture;

    public SauvegardePlateau(String fichierEcriture) {
        this.fichierEcriture = fichierEcriture;
    }

    public void ecrireGrille(Grille grille) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichierEcriture, false))) {

            int largeur = grille.getLargeur();
            int hauteur = grille.getHauteur();
            String nom = grille.getNom();

            // Header
            writer.write(nom.replace(" ", "").toLowerCase() + " " + largeur + " " + hauteur);
           /* writer.write(nom + " " + largeur + " " + hauteur);*/
            writer.newLine();

            // Grille
            for (int y = 0; y < hauteur; y++) {
                for (int x = 0; x < largeur; x++) {

                    Objet o = grille.getObjet(x, y);
                    writer.write(convertir(o));
                }
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char convertir(Objet o) {

        if (o == null) return ' ';
        if (o instanceof Mur) return '#';

        if (o instanceof Joueur) {
            Joueur j = (Joueur) o;
            return j.estSurCible() ? '+' : '@';
        }

        if (o instanceof Boite) {
            Boite b = (Boite) o;
            return b.estSurCible() ? '*' : '$';
        }

        if (o instanceof Cible) return '.';
        
        return ' ';
    }
}
