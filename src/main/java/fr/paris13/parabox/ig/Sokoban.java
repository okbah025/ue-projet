package fr.paris13.parabox.ig;


import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.Jeu;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Sokoban extends StackPane {
    
    private final Jeu jeu;
    private final Level level;
    private final PauseMenu pause;
    private final StackPane root;
    
    public Sokoban(Grille g, StackPane root){
        this.root = root;
        jeu = new Jeu(g);
        level = new Level(g);
        level.setBoard();
        pause = new PauseMenu(root, Version.SIMPLE);
        pause.setVisible(false);
        getChildren().addAll(level, pause);
        
        Scene scene = root.getScene();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

    }
    
    private void handleKeyInput(KeyCode code){
        Direction direction = null;
        boolean deplacementTente = false;

        switch(code){
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
            case ESCAPE:
                pause.setVisible(true);
        }

        if (deplacementTente && direction != null) {
            jeu.deplacerJoueur(direction);
            level.updateBoard();

            if (jeu.estNiveauTermine()) {
                root.getChildren().setAll(new VictoryMenu(root, Version.SIMPLE));
            }
        }
    }    
}

