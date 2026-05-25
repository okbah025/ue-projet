package fr.paris13.parabox.ig.menu;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Classe MainMenu qui affiche le menu principal de l'application.
 * 
 */
public class MainMenu extends VBox {
    
    private final MenuButton sokoban;
    private final MenuButton parabox;
    private final MenuButton exit;
    private Grille[] lvlIte;
    private Grille[] lvlRec;
    
    /**
     * Initialise le menu principal
     * 
     * @param root StackPane pour changer la vue
     */
    public MainMenu(StackPane root){
        lvlIte = SokobanLevel.getList();
        lvlRec = ParaboxLevel.getList();
        
        sokoban = new MenuButton("Sokoban", 200, 75);
        parabox = new MenuButton("Parabox", 200, 75);
        exit = new MenuButton("Quitter", 200, 75);
        
        Label title = new Label("HerTech");
        title.setStyle("-fx-font-size: 100;");
        
        this.getChildren().addAll(title, sokoban, parabox, exit);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        
        sokoban.setOnAction(() -> {
            root.getChildren().setAll(new LevelMenu(root, lvlIte, Version.SIMPLE));
        });
        
        parabox.setOnAction(() -> {
            root.getChildren().setAll(new LevelMenu(root, lvlRec, Version.RECURSIVE));
        });
        
        exit.setOnAction(() -> System.exit(0));
    }    
}
