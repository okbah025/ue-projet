package fr.paris13.parabox.ig.menu;

import fr.paris13.parabox.Modele.Version;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Menu pour afficher l'aide.
 * 
 */
public class HelpMenu extends StackPane {
    
    private static final String[] tabT = {"Annuler dernier mouvement","Chemin vers cellule cliqué", "Jeu automatique", "Recommencer le niveau", "Pause", "Afficher/Désafficher aide"};
    private static final String[] tabC = {"Z","P", "A", "R", "ESC", "H"};
    private static final String[] tabF = {"↑", "↓", "←", "→"};
    
    /**
     * Initialise un menu pour aide au jeu.
     * 
     * @param ver version du jeu
     */
    public HelpMenu(Version ver){
        VBox cont = new VBox(5);
        cont.setPadding(new Insets(10));
        
        Text commande = new Text("Commandes :");
        commande.setStyle("-fx-font: 15 system; ");
        cont.getChildren().add(commande);
        
        HBox box1 = new HBox(10);
        HBox boxArrow = new HBox();
        Label text1 = new Label("Se déplacer");
        
        for (String s : tabF) {
            StackPane symbole = symbole(s);
            boxArrow.getChildren().add(symbole);
        }
        box1.getChildren().addAll(boxArrow, text1); 
        
        cont.getChildren().add(box1);
        
        for (int i = 0; i<tabT.length; i++){
            HBox box = new HBox(10);
            Label text = new Label(tabT[i]);
            StackPane symbole = symbole(tabC[i]);
            box.getChildren().addAll(symbole, text);
           
            cont.getChildren().add(box);
        }
        
        // espace
        Rectangle r = new Rectangle(15, 15);
        r.setVisible(false);
        cont.getChildren().add(r);
        
        Text but = new Text("But du jeu :");
        but.setStyle("-fx-font: 15 system; ");
        Text aide = new Text("Poussez les fleurs sur une clible.");
        Text aide1 = new Text("Appuyez sur P, puis une case pour afficher un chemin.");
        
        cont.getChildren().addAll(but, aide, aide1);
        
        if (ver == Version.RECURSIVE){
            Text aideRec1 = new Text("Déplacez-vous sur une flaque pour y entrer.");
            Text aideRec2 = new Text("Atteignez le bord de la grille interne pour sortir.");
            
            cont.getChildren().addAll(aideRec1, aideRec2);
        }
        
        Text auto = new Text();
        if (ver == Version.RECURSIVE){
            auto.setText("L'auto est dispo pour les niveaux 1, 2 et 4.");
        } else {
            auto.setText("L'auto est dispo pour les niveaux 1 à 5.");
        }
        cont.getChildren().add(auto);
        
        this.getChildren().add(cont);

    }
    
    /**
     * Créer une icone avec le symbole donné
     * 
     * @param s symbole
     * @return une icone
     */
    private StackPane symbole(String s){
        StackPane c = new StackPane();
        c.setAlignment(Pos.CENTER);
        
        Text text = new Text(s);
        text.setFill(Color.BLACK);
        
        Rectangle r = new Rectangle(30, 30);
        r.setStroke(Color.BLACK);
        r.setFill(Color.WHITE);
        
        c.getChildren().addAll(r, text);
        
        return c;
    }
    
    
}
