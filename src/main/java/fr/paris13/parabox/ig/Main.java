package fr.paris13.parabox.ig;
import fr.paris13.parabox.Modele.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Main extends Application {

    public static final int HEIGHT = 700;
    public static final int WIDTH = 900;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("HerTech Game");



        // afficher page accueil
        // boutons pr choisir mode if ite : menu avec liste niveau if rec : niveau initial



        // Test
        Grille g = new Grille(10, 7, "Niveau 5");
        g.setObjet(new Mur(0, 1, g), 0, 1);
        g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(6, 2, g), 6, 2);
        g.setObjet(new Mur(6, 3, g), 6, 3);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Mur(3, 5, g), 3, 5);
        g.setObjet(new Cible(8, 1, g), 8, 1);
        g.setObjet(new Cible(8, 3, g), 8, 3);
        g.setObjet(new Cible(8, 5, g), 8, 5);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(4, 5, g), 4, 5);
        g.setObjet(new Joueur(1, 3, g), 1, 3);




        BorderPane home = new BorderPane();
        GridPane game = new GridPane();
        Jeu jeu = new Jeu(g);
        Level level = new Level(game, g, 5);

        level.setBoard();
        home.setCenter(game);

        Scene scene = new Scene(home, WIDTH, HEIGHT);

        scene.setOnKeyPressed(e -> {

            Direction direction = null;
            boolean deplacementTente = false;

            switch(e.getCode()){
                case UP:
                    direction = Direction.HAUT;
                    deplacementTente = true;
                    break;
                case DOWN:
                    direction = Direction.BAS;
                    deplacementTente = true;
                    break;
                case LEFT:
                    direction = Direction.GAUCHE;
                    deplacementTente = true;
                    break;
                case RIGHT:
                    direction = Direction.DROITE;
                    deplacementTente = true;
                    break;
                case Z:
                    if (jeu.annulerMouvement()) {
                        level.updateBoardReverse();
                    }
                    break;
            }

            if (deplacementTente && direction != null) {
                jeu.deplacerJoueur(direction);
                level.updateBoard();

                if (jeu.estNiveauTermine()) {
                    System.out.println("vic");
                }
            }
        });


        stage.setScene(scene);
        stage.show();

    }

}
