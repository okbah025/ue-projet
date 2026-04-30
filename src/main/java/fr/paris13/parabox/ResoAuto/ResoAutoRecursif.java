package fr.paris13.parabox.ResoAuto;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.chemin.pile;
import fr.paris13.parabox.chemin.c_chemin;


public class ResoAutoRecursif extends ResoAutoClassique  {

    public static PileDir recursif2(Grille g) {
        boolean[][] M=remplir_vide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverser(c_chemin(M, 5, 12, 6, 12));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 6, 11, 6, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 5, 2, 5, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 6, 1, 9, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=directions(chemin);
        return d;
    }

    public static PileDir recursif3(Grille g) {
        boolean[][] M=remplir_vide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverser(c_chemin(M, 4, 1, 5, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 4, 2, 4, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 4, 1, 3, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 3, 2, 3, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=directions(chemin);
        return d;
    }

    public static PileDir recursif(Grille g, int niveau) {
        PileDir d=new PileDir();
        if (niveau==2)
            d=recursif2(g);
        if (niveau==3)
            d=recursif3(g);
        return d;
    }
}
