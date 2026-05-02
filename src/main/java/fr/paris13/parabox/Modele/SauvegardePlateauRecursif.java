package fr.paris13.parabox.Modele;

import java.io.*;
import java.util.*;


public class SauvegardePlateauRecursif {

    private String fichier;

    public SauvegardePlateauRecursif(String fichier) {
        this.fichier = fichier;
    }
    
    
    public void sauvegarder(Grille grille) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, false))) {

        Set<Grille> dejaSauvegardees = new HashSet<>();
        sauvegarderGrille(grille, writer, dejaSauvegardees);

    } catch (IOException e) {
        e.printStackTrace();
    }
}



   private void sauvegarderGrille(Grille grille,
                               BufferedWriter writer,
                               Set<Grille> dejaSauvegardees) throws IOException {

    if (grille == null || dejaSauvegardees.contains(grille)) return;

    dejaSauvegardees.add(grille);

    // 1. header
    writer.write(grille.getNom() + " " + grille.getLargeur());
    writer.newLine();

    // 2. grille
    for (int y = 0; y < grille.getHauteur(); y++) {

        StringBuilder ligne = new StringBuilder();

        for (int x = 0; x < grille.getLargeur(); x++) {

            Objet o = grille.getObjet(x, y);

            if (o == null) {
                ligne.append(' ');
            }

            else if (o instanceof Mur) {
                ligne.append('#');
            }

            else if (o instanceof Joueur) {
                ligne.append(((Joueur)o).estSurCible() ? '+' : '@');
            }

            else if (o instanceof Boite) {
                ligne.append(((Boite)o).estSurCible() ? '*' : '$');
            }

            else if (o instanceof Cible) {
                ligne.append('.');
            }

            else if (o instanceof Piece) {
                ligne.append(((Piece)o).getIdentifiant());
            }
        }

        writer.write(ligne.toString());
        writer.newLine();
    }

    writer.newLine(); // séparation propre entre blocs

    // 3. récursion : sous-grilles
    for (int y = 0; y < grille.getHauteur(); y++) {
        for (int x = 0; x < grille.getLargeur(); x++) {

            Objet o = grille.getObjet(x, y);

            if (o instanceof Piece) {
                Piece p = (Piece) o;
                sauvegarderGrille(p.getGrilleInterne(), writer, dejaSauvegardees);
            }
        }
    }
}
}
	
