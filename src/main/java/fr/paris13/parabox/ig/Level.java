package fr.paris13.parabox.ig;
import fr.paris13.parabox.Modele.*;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;

/** Classe Level permettant d'afficher un niveau **/

public class Level extends GridPane {
    public static final int SIZE = 50;
    public static final Image floorImg = new Image("/images/floor.gif");
    private Joueur player;
    private Grille grid;
    private StackPane[][] cells;
    private int col;
    private int row;
    private final int lvl;

    public Level(Grille grid, int lvl){
        this.lvl = lvl;
        this.grid = grid;
        player = grid.getJoueur();
        col = grid.getLargeur();
        row = grid.getHauteur();
        cells = new StackPane[col][row];
        setAlignment(Pos.CENTER);
    }

    public int getLvl(){
        return this.lvl;
    }
    
    public void reset(Grille g){
        clearBoard();
        resetGrid(g);
        resetPlayer(g.getJoueur());
        setBoard();
    }
    
    private void resetGrid(Grille grid){
        this.grid = grid;
    }
    
    private void resetPlayer(Joueur player){
        this.player = player;
    }
    
    private void setFloor(int i, int j){
        ImageView floor = new ImageView();
        floor.setImage(floorImg);
        floor.setFitHeight(SIZE);
        floor.setFitWidth(SIZE);
        floor.setPreserveRatio(true);
        cells[i][j].getChildren().add(floor);
    }

    private void setWall(int i, int j){
        ImageView wall = new ImageView();
        wall.setImage(new Image("/images/wall.png"));
        wall.setFitHeight(SIZE);
        wall.setFitWidth(SIZE);
        wall.setPreserveRatio(true);
        cells[i][j].getChildren().add(wall);
    }

    private void setBox(int i, int j){
        ImageView box = new ImageView();
        box.setImage(new Image("/images/box.png"));
        box.setFitHeight(SIZE);
        box.setFitWidth(SIZE);
        box.setPreserveRatio(true);

        cells[i][j].getChildren().add(box);
    }

    private void setTarget(int i, int j){
        ImageView target = new ImageView();
        target.setImage(new Image("/images/target.png"));
        target.setFitHeight(SIZE);
        target.setFitWidth(SIZE);
        target.setPreserveRatio(true);

        cells[i][j].getChildren().add(target);
    }

    private void setPlayer(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);

        cells[i][j].getChildren().add(p);
    }
    
    private void setPlayerRight(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_right.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    private void setPlayerLeft(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_left.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    private void setPlayerUp(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_back.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    private void setRoom(int i, int j){
        ImageView level = new ImageView();
        level.setImage(new Image("/images/room.png"));
        level.setFitHeight(SIZE);
        level.setFitWidth(SIZE);
        level.setPreserveRatio(true);
        
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
            
            if (box instanceof Piece){
                setRoom(i, j);
            }
            else {
                setBox(i, j);
            }
        }
        
        int i = player.getX();
        int j = player.getY();
        setPlayer(i, j);
    }

    public void updateBoard(Direction dir){
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
        

        switch (dir) {
            case DROITE -> setPlayerRight(x, y);
            case GAUCHE -> setPlayerLeft(x, y);
            case HAUT -> setPlayerUp(x, y);
            default -> setPlayer(x ,y);
        }

        for (Boite box : grid.getBoites()){
            int i = box.getX();
            int j = box.getY();
            cells[i][j].getChildren().clear();
            setFloor(i, j);
            
            if (box instanceof Piece){
                setRoom(i, j);
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
    
    private void clearBoard(){
        this.getChildren().clear();
    }
    
    
    /// à changer
//    public void updateBoardReverseRec(Grille g){
//        grid = g;
//        setBoard();
//    }
    

}
