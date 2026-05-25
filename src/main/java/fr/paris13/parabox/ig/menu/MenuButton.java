package fr.paris13.parabox.ig.menu;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Classe MenuButton qui affiche un boutton.
 * 
 */
public class MenuButton extends StackPane {
    
    private final Text text;
    private final Rectangle button;
    
    /**
     * Initialise un bouton pour menu
     * 
     * @param name nom à afficher
     * @param width largeur du boutton
     * @param height hauteur du boutton
     */
    public MenuButton(String name, int width, int height){
        
        this.setAlignment(Pos.CENTER);
        
        text = new Text(name);
        text.setFill(Color.BLACK);
        
        Rectangle contour = new Rectangle(width, height);
        contour.setStroke(Color.BLACK);
        contour.setFill(Color.ALICEBLUE);
        button = new Rectangle(width, height);
        button.setStroke(Color.BLACK);
        button.setFill(Color.PINK);
        button.setVisible(false);
        
        this.getChildren().addAll(contour, button, text);
        
        this.setOnMouseEntered(e -> {
            onSelect();
        });

        this.setOnMouseExited(e -> {
            onDeselect();
        });
        
    }
    
    /**
     * Action lorsque le bouton est selectionné.
     */
    private void onSelect(){
        button.setVisible(true);
    }
    
    /**
     * Action lorsque le bouton n'est plus selectionné.
     */
    private void onDeselect(){
        button.setVisible(false);
    }
    
    /**
     * Action lorsque le bouton est cliqué
     * 
     * @param action action à faire
     */
    public void setOnAction(Runnable action){
        this.setOnMouseClicked(e -> {
            action.run();
        });
    }
}
