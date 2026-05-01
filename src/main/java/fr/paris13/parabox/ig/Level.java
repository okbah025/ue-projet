package fr.paris13.parabox.ig;
import fr.paris13.parabox.Modele.*;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;

 /**
  * Classe Level permettant d'afficher un niveau.
  *
  */

public class Level extends GridPane {
    public static final int SIZE = 50;
    public static final Image floorImg = new Image("/images/floor.png");
    private Joueur player;
    private Grille grid;
    private StackPane[][] cells;
    private int col;
    private int row;
    private final int lvl;

    
    /**
     * Inintialise un Level avec une grille.
     * 
     * @param grid la Grille à afficher
     * @param lvl  le numéro du niveau
     */
    public Level(Grille grid, int lvl){
        this.lvl = lvl;
        this.grid = grid;
        player = grid.getJoueur();
        col = grid.getLargeur();
        row = grid.getHauteur();
        cells = new StackPane[col][row];
        setAlignment(Pos.CENTER);
    }

    /**
     * Retourne le numéro du niveau.
     * 
     * @return le numéro du niveau
     */
    public int getLvl(){
        return this.lvl;
    }
    
    /**
     * Réinitialise le niveau avec la grille donné en paramètre.
     * 
     * @param g la nouvelle grille
     */
    public void reset(Grille g){
        clearBoard();
        resetGrid(g);
        col = grid.getLargeur();
        row = grid.getHauteur();
        resetPlayer(g.getJoueur());
        cells = new StackPane[col][row];
        setBoard(Direction.BAS);
    }
    
    /**
     * Mets la grille du niveau à la grille donné en paramètre.
     * 
     * @param grid la nouvelle grille
     */
    private void resetGrid(Grille grid){
        this.grid = grid;
    }
    
    /**
     * Mets le joueur du niveau en tant que joueur donné en paramètre.
     * 
     * @param player le nouveau joueur
     */
    private void resetPlayer(Joueur player){
        this.player = player;
    }
    
   /**
    * Ajoute une image (sol) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setFloor(int i, int j){
        ImageView floor = new ImageView();
        floor.setImage(floorImg);
        floor.setFitHeight(SIZE);
        floor.setFitWidth(SIZE);
        floor.setPreserveRatio(true);
        cells[i][j].getChildren().add(floor);
    }

    /**
    * Ajoute une image (mur) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setWall(int i, int j){
        ImageView wall = new ImageView();
        wall.setImage(new Image("/images/wall.png"));
        wall.setFitHeight(SIZE);
        wall.setFitWidth(SIZE);
        wall.setPreserveRatio(true);
        cells[i][j].getChildren().add(wall);
    }

    /**
    * Ajoute une image (boite) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setBox(int i, int j){
        ImageView box = new ImageView();
        box.setImage(new Image("/images/box.png"));
        box.setFitHeight(SIZE);
        box.setFitWidth(SIZE);
        box.setPreserveRatio(true);

        cells[i][j].getChildren().add(box);
    }

    /**
    * Ajoute une image (cible) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setTarget(int i, int j){
        ImageView target = new ImageView();
        target.setImage(new Image("/images/target.png"));
        target.setFitHeight(SIZE);
        target.setFitWidth(SIZE);
        target.setPreserveRatio(true);

        cells[i][j].getChildren().add(target);
    }

    /**
    * Ajoute une image (joueur) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setPlayerDefault(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);

        cells[i][j].getChildren().add(p);
    }
    
    /**
    * Ajoute une image (joueur se dirigeant vers la droite) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setPlayerRight(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_right.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    /**
    * Ajoute une image (joueur se dirigeant vers la gauche) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setPlayerLeft(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_left.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    /**
    * Ajoute une image (joueur se dirigeant vers le haut) à la cellule position (i,j).
    * 
    * @param i colonne i
    * @param j ligne j
    */
    private void setPlayerUp(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/player_back.png"));
        p.setFitHeight(SIZE);
        p.setFitWidth(SIZE);
        p.setPreserveRatio(true);
        cells[i][j].getChildren().add(p);
    }
    
    /**
     * Ajoute le joueur se dirigeant vers la direction donné à la cellule position (i,j).
     * 
     * @param i colonne i
     * @param j ligne j
     * @param dir direction du joueur
     */
    private void setPlayer(int i, int j, Direction dir){
        switch (dir) {
            case DROITE -> setPlayerRight(i, j);
            case GAUCHE -> setPlayerLeft(i, j);
            case HAUT -> setPlayerUp(i, j);
            default -> setPlayerDefault(i ,j);
        }
    }
    
    /**
     * Ajoute une image (pièce) à la cellule position (i,j).
     * 
     * @param i colonne i
     * @param j ligne j
     */
    private void setRoom(int i, int j){
        ImageView level = new ImageView();
        level.setImage(new Image("/images/room.png"));
        level.setFitHeight(SIZE);
        level.setFitWidth(SIZE);
        level.setPreserveRatio(true);
        
        cells[i][j].getChildren().add(level);
    }

    /**
     * Permet d'afficher le niveau en ajoutant les images dans les cellules qui
     * seront ensuite ajouté dans notre layout GridPane.
     * 
     * @param dir direction dans laquel le joueur se dirige (utile pour récursif)
     */
    public void setBoard(Direction dir){
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
        setPlayer(i, j, dir);
    }

    /**
     * Mets à jour l'affichage du jeu classique
     * 
     * @param dir direction du joueur
     */
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
        

        setPlayer(x ,y, dir);
        

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
    
    /**
     * Mets à jour l'affichage lors de l'annulation d'un mouvement.
     * 
     */
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
        setPlayer(i, j, Direction.BAS);
    }
    
    /**
     * Mets à jour l'affichage du jeu récursif
     * 
     * @param g la grille dans lequel le joueur est actuellement
     * @param dir direction du joueur
     */
    public void updateBoardRec(Grille g, Direction dir){
        reset(g);
    }
    
    /**
     * Enlève tous les éléments de notre layout GridPane.
     * 
     */
    private void clearBoard(){
        this.getChildren().clear();
    }
}
