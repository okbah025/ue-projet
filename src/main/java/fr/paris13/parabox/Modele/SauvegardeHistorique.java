/*gère les niveaux classiques et récursifs*/
/*on ecrit l'historique d'un seul niveau, un fichier par niveau*/
package fr.paris13.parabox.Modele;

import java.util.Stack;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.io.*;
import java.util.*;


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


	
	/*dans la même classe cette fois, méthode pour CHARGER l'historique des déplacements d'une partie précédente*/
    // CHARGEMENT

    public List<Character> chargerHistorique() {

        List<Character> coups =
            new ArrayList<>();

        try {

            BufferedReader reader =
                new BufferedReader(
                    new FileReader(fichierEcrireHisto)
                );

            String ligne;

            while ((ligne = reader.readLine()) != null) {

                for (char c : ligne.toCharArray()) {

                    if ("udlrUDLR".indexOf(c) != -1) {
                        coups.add(c);
                    }
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return coups;
    }

    // =========================
    // CONVERSION
    // =========================

    public static Direction charVersDirection(char c) {

        switch (Character.toLowerCase(c)) {

            case 'u':
                return Direction.HAUT;

            case 'd':
                return Direction.BAS;

            case 'l':
                return Direction.GAUCHE;

            case 'r':
                return Direction.DROITE;

            default:
                return null;
        }
    }
}





/* dans main , au moment de quitter la partie : 
svgHisto = new SauvegardeHistorique(fichierEcrireHisto);

svgHisto.ecrireHistorique(jeu)

remarque 1: on veut que notre fichier avec l'historique des deplacements du niveau n°X se nomme "niveauX_histo_deplacements.txt"

remarque 2: et si on a gagné, on enregistre l'historique des deplacements dans un fichier "niveauX_sol_deplacements.txt"
*/
