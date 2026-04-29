package fr.paris13.parabox;

import fr.paris13.parabox.Modele.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParaboxTest {

    private Jeu jeu;
    private Grille grille;

    @BeforeEach
    void setUp() {
        // Création d'une grille de 5x5 pour les tests
        grille = new Grille(5, 5, "Grille de Test");
        // Initialisation du contrôleur de jeu avec cette grille
        jeu = new Jeu(grille); 
    }

    @Test
    void testDeplacementJoueurSimple() {
        // Placement d'un joueur en (2,2)
        Joueur joueur = new Joueur(2, 2, grille);
        grille.setObjet(joueur, 2, 2);

        // Déplacement vers le BAS
        boolean succes = jeu.deplacerJoueur(Direction.BAS);

        assertTrue(succes, "Le joueur devrait pouvoir se déplacer sur une case vide");
        assertEquals(new Position(2, 3), joueur.getPosition(), "La position du joueur devrait être (2,3)");
    }

    @Test
    void testCollisionMur() {
        // On entoure le joueur de murs ou on le met au bord
        Joueur joueur = new Joueur(0, 0, grille);
        grille.setObjet(joueur, 0, 0);

        // Tentative de sortir de la grille vers le HAUT (0,-1)
        boolean succes = jeu.deplacerJoueur(Direction.HAUT);

        assertFalse(succes, "Le joueur ne doit pas pouvoir sortir de la grille");
        assertEquals(new Position(0, 0), joueur.getPosition(), "Le joueur doit rester en (0,0)");
    }

    @Test
    void testPousserBoite() {
        Joueur joueur = new Joueur(1, 1, grille);
        grille.setObjet(joueur, 1, 1);
        
        Boite boite = new Boite(2, 1, grille); // Boîte à droite du joueur
        grille.setObjet(boite, 2, 1);

        // Le joueur pousse la boîte vers la DROITE
        boolean succes = jeu.deplacerJoueur(Direction.DROITE);

        assertTrue(succes, "Le mouvement de poussée devrait réussir");
        assertEquals(new Position(2, 1), joueur.getPosition(), "Le joueur prend la place de la boîte");
        assertEquals(new Position(3, 1), boite.getPosition(), "La boîte a été décalée en (3,1)");
    }

    @Test
    void testConditionVictoire() {
        grille.setObjet(new Joueur(1, 1, grille), 1, 1);
        
        // Placement d'une cible et d'une boîte
        Cible cible = new Cible(3, 1, grille);
        grille.setObjet(cible, 3, 1);
        
        Boite boite = new Boite(2, 1, grille);
        grille.setObjet(boite, 2, 1);

        assertFalse(jeu.estNiveauTermine(), "Le niveau ne doit pas être fini au début");

        // Pousser la boîte sur la cible (mouvement vers la DROITE)
        jeu.deplacerJoueur(Direction.DROITE);

        assertTrue(boite.estSurCible(), "La boîte doit être marquée comme étant sur la cible");
        assertTrue(jeu.estNiveauTermine(), "Le jeu doit détecter la victoire");
    }
}
