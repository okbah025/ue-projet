package fr.paris13.parabox.ig.menu;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


/**
 * Classe PauseMenu qui affiche le menu de pause.
 * 
 */
public class PauseMenu extends StackPane {
    
    /** Bouton pour reprendre le jeu. **/
    private final MenuButton resume;
    /** Bouton pour afficher les niveaux. **/
    private final MenuButton levels;
    /** Bouton pour afficher le menu principal. **/
    private final MenuButton mainMenu;
    
    /**
     * Initialise le menu de pause.
     * 
     * @param root pour changement de vue
     * @param ver version du jeu
     */
    public PauseMenu(StackPane root, Version ver){
        Grille[] levelList;
        VBox box = new VBox();
        resume = new MenuButton("Reprendre", 200, 75);
        levels = new MenuButton("Niveaux", 200, 75);
        mainMenu = new MenuButton("Menu Principal", 200, 75);
        box.getChildren().addAll(resume, levels, mainMenu);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(25);
        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.getChildren().add(box);
        this.setStyle("-fx-background-color: lightblue;");
        if (ver == Version.SIMPLE){
            levelList = SokobanLevel.getList();
        } else {
            levelList = ParaboxLevel.getList();
        }
        
        resume.setOnAction(() -> this.setVisible(false));
        levels.setOnAction(() -> root.getChildren().setAll(new LevelMenu(root, levelList, ver)));
        mainMenu.setOnAction(() -> root.getChildren().setAll(new MainMenu(root)));
        
    }
}
