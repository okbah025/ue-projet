package fr.paris13.parabox.ig.menu;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ig.Level;
import fr.paris13.parabox.ig.Parabox;
import fr.paris13.parabox.ig.Sokoban;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * Classe VictoryMenu qui affiche le menu de victoire.
 * 
 */
public class VictoryMenu extends StackPane {
    
    private final MenuButton home;
    private final MenuButton next;
    private final MenuButton list;
    private final Text text;
    
    /**
     * Initialise le menu de victoire
     * 
     * @param root pour changement de vue
     * @param ver version du jeu
     * @param lvl num du niveau
     * @param mvt nombre de mouvement à la fin du niveau
     * @param pousse nombre de boites poussées à la fin du niveau
     * 
     */
    public VictoryMenu(StackPane root, Version ver, Level lvl, int mvt, int pousse){
        Grille[] levelList;
        HBox button = new HBox();
        HBox score = new HBox();
        VBox box = new VBox();
        
        next = new MenuButton("next", 100, 75);
        list = new MenuButton("list", 100, 75);
        home = new MenuButton("home", 100, 75);
        text = new Text("Niveau terminé !!");
        Text text2 = new Text("Score final :");
        Text text3 = new Text("Mouvement : " + mvt);
        Text text4 = new Text("Poussées : " + pousse);
        
        text.setStyle("-fx-font: 60 arial;");
        text2.setStyle("-fx-font: 24 arial;");
        text3.setStyle("-fx-font: 24 arial;");
        text4.setStyle("-fx-font: 24 arial;");
        
        button.getChildren().addAll(home, list, next);
        score.getChildren().addAll(text3, text4);
        box.getChildren().addAll(text, text2, score, button);
        
        button.setAlignment(Pos.CENTER);
        score.setSpacing(10);
        score.setAlignment(Pos.CENTER);
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
        
        if (ver == Version.SIMPLE){
            next.setOnAction(() -> root.getChildren().setAll(new Sokoban(levelList[lvl.getLvl()], root, lvl.getLvl()+1)));        
        } else {
            next.setOnAction(() -> root.getChildren().setAll(new Parabox(levelList[lvl.getLvl()], root, lvl.getLvl()+1)));        
        }
        
        list.setOnAction(() -> root.getChildren().setAll(new LevelMenu(root, levelList, ver)));
        home.setOnAction(() -> root.getChildren().setAll(new MainMenu(root)));
    }
}
