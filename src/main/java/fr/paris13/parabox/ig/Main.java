package fr.paris13.parabox.ig;

import fr.paris13.parabox.ig.menu.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class Main extends Application {

    public static final int HEIGHT = 700;
    public static final int WIDTH = 1100;
    
    private BorderPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("HerTech Game");
        root = new BorderPane();
        StackPane game = new StackPane();
        
        showMainMenu(game);
        
        root.setCenter(game);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();

    }
    
    private void showMainMenu(StackPane game){
        game.getChildren().setAll(new MainMenu(game));
    }
}
