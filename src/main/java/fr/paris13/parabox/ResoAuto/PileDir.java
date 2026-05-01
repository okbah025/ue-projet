package fr.paris13.parabox.ResoAuto;
import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.chemin.c_chemin;
import java.util.*;

public class PileDir extends c_chemin {

    ArrayDeque<Direction> pile;

    public PileDir() {
        this.pile=new ArrayDeque<>();
    }

    public void empilerDir(Direction dir) {
        pile.push(dir);
    }

    public Direction depilerDir() {
        Direction dir=pile.pop();
        return dir;
    }

    public boolean estVidePile() {
        return pile.isEmpty();
    }

    public int size() {
        return pile.size();
    }
}
