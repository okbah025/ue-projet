import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;


public class Main extends Application {

    public static final int HEIGHT = 700;
    public static final int WIDTH = 900;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Parabox Game");

        // Test
        VBox home = new VBox();
        Label etiquette = new Label("BLABLA");
        Button salut = new Button("Salut");
        Button bye = new Button("Bye");
        home.getChildren().addAll(etiquette, salut, bye);
        home.setStyle("-fx-background-color: pink;");
        home.setAlignment(Pos.CENTER);


        Scene scene = new Scene(home, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();

    }

}
