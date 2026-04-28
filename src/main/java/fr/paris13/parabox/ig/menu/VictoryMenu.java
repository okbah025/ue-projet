package fr.paris13.parabox.ig.menu;

import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class VictoryMenu extends StackPane {
    
    private final MenuButton next;
    private final MenuButton home;
    private final Label text;
    
    public VictoryMenu(StackPane root, Version ver){
        Grille[] levelList;

        HBox button = new HBox();
        VBox box = new VBox();
        next = new MenuButton("next", 100, 75);
        home = new MenuButton("home", 100, 75);
        text = new Label("Victory");
        
        button.getChildren().addAll(home, next);
        box.getChildren().addAll(text, button);
        
        box.setAlignment(Pos.CENTER);
        box.setSpacing(25);
        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        
        this.getChildren().add(box);
        this.setStyle("-fx-background-color: lightblue;");
        
        if (ver == Version.SIMPLE){
            levelList = SokobanLevel.getList();
        } else {
            levelList = ParaboxLevel.getList(); //dzdz
        }

        next.setOnAction(() -> root.getChildren().setAll(new LevelMenu(root, levelList, ver)));
        home.setOnAction(() -> root.getChildren().setAll(new MainMenu(root)));
    }
}
