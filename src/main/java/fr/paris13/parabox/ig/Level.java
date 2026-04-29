package fr.paris13.parabox.ig;
import fr.paris13.parabox.Modele.*;
import fr.paris13.parabox.Modele.Piece;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/** Classe Level permettant d'afficher un niveau **/

public class Level extends GridPane {
    public static final int SIZE = 50;
    public static final Image floorImg = new Image("/images/floor.gif");
    private final Joueur player;
    private Grille grid;
    private StackPane[][] cells;
    private int col;
    private int row;

    public Level(Grille grid){
        this.grid = grid;
        player = grid.getJoueur();
        col = grid.getLargeur();
        row = grid.getHauteur();
        cells = new StackPane[col][row];
        setAlignment(Pos.CENTER);
        
        this.getColumnConstraints().add(new ColumnConstraints());
        this.getRowConstraints().add(new RowConstraints());
    }

    public void setFloor(int i, int j){
        ImageView floor = new ImageView();
        floor.setImage(floorImg);
        floor.setFitHeight(SIZE);
        floor.setFitWidth(SIZE);
        cells[i][j].getChildren().add(floor);
    }

    public void setWall(int i, int j){
        ImageView wall = new ImageView();
        wall.setImage(new Image("/images/wall.png"));
        wall.setFitHeight(SIZE);
        wall.setFitWidth(SIZE);
        cells[i][j].getChildren().add(wall);
    }

     public void setBox(int i, int j){
        ImageView box = new ImageView();
        box.setImage(new Image("/images/box.png"));
        box.setFitHeight(SIZE);
        box.setFitWidth(SIZE);
        cells[i][j].getChildren().add(box);
    }

    public void setTarget(int i, int j){
        ImageView target = new ImageView();
        target.setImage(new Image("/images/target.png"));
        target.setFitHeight(SIZE);
        target.setFitWidth(SIZE);
        cells[i][j].getChildren().add(target);
    }

    public void setPlayer(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/p.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        cells[i][j].getChildren().add(p);
    }
    
    public void setRoom(int i, int j, Grille grid){
        ImageView level = new ImageView();
        level.setImage(new Image("/images/room.png"));
        level.setFitHeight(SIZE);
        level.setFitWidth(SIZE);
        cells[i][j].getChildren().add(level);
    }

    public void setBoard(){
        for (int i = 0; i<col; i++){
            for (int j = 0; j<row; j++){
                cells[i][j] = new StackPane();
                if(grid.getObjet(i, j) instanceof Mur){
                    setWall(i ,j);
                } else {
                    setFloor(i, j);
                }
                this.add(cells[i][j], i, j);
            }
        }

        for (Cible target : grid.getCibles()){
            int i = target.getX();
            int j = target.getY();
            setTarget(i, j);
        }
        for (Boite box : grid.getBoites()){
            int i = box.getX();
            int j = box.getY();
            
            if (box instanceof Piece room){
                Grille g = room.getGrilleInterne();
                setRoom(i, j, g);
            }
            else {
                setBox(i, j);
            }
        }
        
        int i = player.getX();
        int j = player.getY();
        setPlayer(i, j);
    }

    public void updateBoard(){
        int oldX = player.getOldX();
        int oldY = player.getOldY();
        int x = player.getX();
        int y = player.getY();

        cells[x][y].getChildren().clear();
        setFloor(x, y);
        cells[oldX][oldY].getChildren().clear();
        setFloor(oldX, oldY);

        for (Cible target : grid.getCibles()){
            int i = target.getX();
            int j = target.getY();
            if (i == x && j == y) setTarget(i, j);
            if (i == oldX && j == oldY) setTarget(i, j);
        }

        setPlayer(x ,y);

        for (Boite box : grid.getBoites()){
            int i = box.getX();
            int j = box.getY();
            cells[i][j].getChildren().clear();
            setFloor(i, j);
            
            if (box instanceof Piece room){
                Grille g = room.getGrilleInterne();
                setRoom(i, j, g);
            }
            else {
                setBox(i, j);
            }
        }
    }
    public void updateBoardReverse(){
        for (int i = 0; i<col; i++){
            for (int j = 0; j<row; j++){
                cells[i][j].getChildren().clear();
                if(grid.getObjet(i, j) instanceof Mur){
                    setWall(i ,j);
                }
                else {
                    setFloor(i, j);
                }
            }
        }

        for (Cible target : grid.getCibles()){
            int i = target.getX();
            int j = target.getY();
            setTarget(i, j);
        }
        for (Boite box : grid.getBoites()){
            int i = box.getX();
            int j = box.getY();
            setBox(i, j);
        }

        int i = player.getX();
        int j = player.getY();
        setPlayer(i, j);

    }
    
    public void updateBoardRec(Grille g){
        grid = g;
        col = grid.getLargeur();
        row = grid.getHauteur();
        clearBoard();
        cells = new StackPane[col][row];

        setBoard();
    }
    
    public void clearBoard(){
        this.getChildren().clear();
    }
    
    public void updateBoardReverseRec(Grille g){
        grid = g;
        setBoard();
    }
    
//    private void setInsideBoard(){
//        for (int i = 0; i<col; i++){
//            for (int j = 0; j<row; j++){
//                cells[i][j] = new StackPane();
//                if(grid.getObjet(i, j) instanceof Mur){
//                    setWall(i ,j);
//                } else {
//                    setFloor(i, j);
//                }
//                this.add(cells[i][j], i, j);
//            }
//        }
//
//        for (Cible target : grid.getCibles()){
//            int i = target.getX();
//            int j = target.getY();
//            setTarget(i, j);
//        }
//        for (Boite box : grid.getBoites()){
//            int i = box.getX();
//            int j = box.getY();
//            
//            if (box instanceof Piece room){
//                Grille g = room.getGrilleInterne();
//                setRoom(i, j, g);
//            }
//            else {
//                setBox(i, j);
//            }
//        }
//    }
}
