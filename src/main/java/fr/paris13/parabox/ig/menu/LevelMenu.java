package fr.paris13.parabox.ig.menu;

import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ig.Parabox;
import fr.paris13.parabox.ig.Sokoban;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


/**
 * Classe LevelMenu qui affiche une liste des niveaux disponible selon la version choisis.
 *
 */
public class LevelMenu extends VBox {
    
    /**
     * Initialise le Menu des niveaux 
     * 
     * @param root StackPane pour changement de vue
     * @param grid tableau contenant les différentes grille de niveau
     * @param ver version du jeu
     */
    public LevelMenu(StackPane root, Grille[] grid, Version ver){
        
        Label text = new Label("Choisissez un niveau");
        
        HBox box1 = new HBox(10);
        HBox box2 = new HBox(10);
        
        for (int i = 0; i<grid.length/2; i++){
            int level = i + 1;
            MenuButton btn = new MenuButton("Niv " + level, 150, 75);
            Grille g = grid[i];
            
            if (ver == Version.SIMPLE){
                btn.setOnAction(() -> root.getChildren().setAll(new Sokoban(g, root, level)));
            } else {
                btn.setOnAction(() -> root.getChildren().setAll(new Parabox(g, root, level)));
            }
            
            box1.getChildren().add(btn);
        }
        
        for (int i = grid.length/2; i<grid.length; i++){
            int level = i + 1;
            MenuButton btn = new MenuButton("Niv " + level, 150, 75);
            Grille g = grid[i];
            
            if (ver == Version.SIMPLE){
                btn.setOnAction(() -> root.getChildren().setAll(new Sokoban(g, root, level)));
            } else {
                btn.setOnAction(() -> root.getChildren().setAll(new Parabox(g, root, level)));
            }
            
            box2.getChildren().add(btn);
        }
        box1.setAlignment(Pos.CENTER);
        box2.setAlignment(Pos.CENTER);
        
        MenuButton back = new MenuButton("Back", 100, 100);
        back.setOnAction(() -> root.getChildren().setAll(new MainMenu(root)));
        
        this.setSpacing(10);
        this.setStyle("-fx-background-color: lightblue;");
        this.getChildren().addAll(text, box1, box2, back);
        this.setAlignment(Pos.CENTER);
    }
}
