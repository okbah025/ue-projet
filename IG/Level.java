import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;

/** Classe Level permettant d'afficher un niveau **/

public class Level {
    public static final int cellSize = 90;
    public static final Image floorImg = new Image("/images/floor.gif", cellSize, cellSize, false, false);
    private GridPane root;
    private int level;
    private Joueur player;
    private Grille grid;
    private StackPane[][] cells;
    private int col;
    private int row;

    public Level(GridPane root, Grille grid, int level){
        this.root = root;
        this.grid = grid;
        this.level = level;
        player = grid.getJoueur();
        col = grid.getLargeur();
        row = grid.getHauteur();
        cells = new StackPane[col][row];
    }

    public void setFloor(int i, int j){
        ImageView floor = new ImageView();
        floor.setImage(floorImg);
        cells[i][j].getChildren().add(floor);
    }

    public void setWall(int i, int j){
        ImageView wall = new ImageView();
        wall.setImage(new Image("/images/wall.png", cellSize, cellSize, false, false));
        cells[i][j].getChildren().add(wall);
    }

     public void setBox(int i, int j){
        ImageView box = new ImageView();
        box.setImage(new Image("/images/box.png", cellSize, cellSize, false, false));
        cells[i][j].getChildren().add(box);
    }

    public void setTarget(int i, int j){
        ImageView target = new ImageView();
        target.setImage(new Image("/images/target.png", cellSize, cellSize, false, false));
        cells[i][j].getChildren().add(target);
    }

    public void setPlayer(int i, int j){
        ImageView p = new ImageView();
        p.setImage(new Image("/images/p.png"));
        p.setFitHeight(cellSize);
        p.setFitWidth(cellSize);
        cells[i][j].getChildren().add(p);
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
                root.add(cells[i][j], i, j);
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
            setBox(i, j);
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
}
