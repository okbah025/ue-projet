package fr.paris13.parabox.ig;

import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.JeuRecursif;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

/**
 *
 * @author yakuros
 */
public class Parabox extends StackPane {
    private final JeuRecursif jeu;
    private Level level;
    private final PauseMenu pause;
    private final StackPane root;
    
    public Parabox(Grille g, StackPane root){
        this.root = root;
        jeu = new JeuRecursif(g);
        level = new Level(g);
        level.setBoard();
        pause = new PauseMenu(root, Version.RECURSIVE);
        pause.setVisible(false);
        getChildren().addAll(level, pause);
        
        Scene scene = root.getScene();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

    }
   
    
    private void handleKeyInput(KeyCode code){
        Direction direction = null;

        switch(code){
            case UP:
                direction = Direction.HAUT;
                
                break;
            case DOWN:
                direction = Direction.BAS;
                
                break;
            case LEFT:
                direction = Direction.GAUCHE;
                
                break;
            case RIGHT:
                direction = Direction.DROITE;
                
                break;
            case Z:
                if (jeu.annulerMouvement()) {
                    level.updateBoardRec(jeu.getGrilleActive());
                }
                break;
            case ESCAPE:
                pause.setVisible(true);
        }

        if (direction != null) {
            boolean ok = jeu.deplacerJoueur(direction);
            if (ok){
                level.updateBoardRec(jeu.getGrilleActive());
                if (jeu.estNiveauTermine()) {
                    root.getChildren().setAll(new VictoryMenu(root, Version.RECURSIVE));
                }
            }
        }    
    }
}
