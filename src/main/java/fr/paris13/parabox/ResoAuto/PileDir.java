import java.util.*;

public class PileDir extends c_chemin{

    ArrayDeque<Direction> pile;

    public PileDir(){
        this.pile=new ArrayDeque<>();
    }

    public void empiler2(Direction dir){
        pile.push(dir);
    }

    public Direction depiler2(){
        Direction dir=pile.pop();
        return dir;
    }

    public boolean isEmpty(){
        return pile.isEmpty();
    }
}
