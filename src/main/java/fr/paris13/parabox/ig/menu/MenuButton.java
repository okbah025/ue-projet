
package fr.paris13.parabox.ig.menu;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class MenuButton extends StackPane {
    
    private final Text text;
    private final Rectangle button;
    
    public MenuButton(String name, int width, int height){
        
        this.setAlignment(Pos.CENTER);
        
        text = new Text(name);
        text.setFill(Color.BLACK);
        
        button = new Rectangle(width, height);
        button.setStroke(Color.BLACK);
        button.setFill(Color.PINK);
        button.setVisible(false);
        
        this.getChildren().addAll(button, text);
        
        this.setOnMouseEntered(e -> {
            onSelect();
        });

        this.setOnMouseExited(e -> {
            onDeselect();
        });
        
    }
    
    private void onSelect(){
        button.setVisible(true);
    }
    
    private void onDeselect(){
        button.setVisible(false);
    }
    
    public void setOnAction(Runnable action){
        this.setOnMouseClicked(e -> {
            action.run();
        });
    }
}
