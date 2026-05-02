package fr.paris13.parabox.ResoAuto;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.chemin.pile;
import fr.paris13.parabox.chemin.c_chemin;

public class ResoAutoRecursif extends ResoAutoClassique  {

    public static PileDir recursif1(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverserPile(c_chemin(M, 2, 2, 2, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 3, 3, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 1, 3, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 6, 2, 6, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 5, 3, 5, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 4, 4, 4, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 4, 3, 4, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M2=matrice(6, 6);
        p=inverserPile(c_chemin(M2, 2, 5, 2, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M2, 1, 2, 1, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M2, 2, 1, 2, 1);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir recursif2(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverserPile(c_chemin(M, 5, 12, 6, 12));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 6, 11, 6, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 5, 3, 5, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 6, 2, 10, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 10, 1, 11, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 11, 2, 11, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M2=matrice(6, 6);
        p=inverserPile(c_chemin(M2, 2, 0, 2, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M2, 1, 2, 1, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M2, 2, 3, 2, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir recursif3(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverserPile(c_chemin(M, 3, 1, 5, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 5, 2, 2, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 2, 1, 1, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M2=matrice(6, 6);
        p=inverserPile(c_chemin(M2, 2, 3, 2, 6));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M3=matrice(6, 6);
        p=inverserPile(c_chemin(M3, 2, 0, 2, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir recursif4(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverserPile(c_chemin(M, 1, 3, 2, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 2, 4, 2, 5));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 1, 5, 1, 6));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M2=matrice(8, 8);
        p=c_chemin(M2, 7, 3, 4, 0);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 2, 7, 2, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 1, 3, 1, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 2, 2, 5, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M3=matrice(7, 7);
        p=inverserPile(c_chemin(M3, 1, 4, 4, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M3, 4, 3, 5, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M3, 5, 4, 5, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M4=matrice(7, 7);
        p=inverserPile(c_chemin(M4, 2, 0, 2, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M4, 3, 2, 3, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M4, 2, 3, 2, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M4, 2, 2, 1, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M4, 1, 3, 1, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir recursif5(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 5, 2, 7, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 5, 3, 1, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 1, 4, 1, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 1, 5, 1, 5);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        c_chemin(M, 2, 5, 2, 5);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 2, 6, 2, 6);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 6, 3, 6);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 5, 3, 5);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M2=matrice(8, 8);
        p=inverserPile(c_chemin(M2, 1, 7, 1, 5));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M2, 0, 5, 0, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M2, 1, 3, 1, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M2, 0, 4, 0, 5));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M2, 1, 5, 1, 5);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        boolean[][] M3=matrice(5, 5);
        p=inverserPile(c_chemin(M3, 1, 2, 4, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir recursif(Grille g, int niveau) {
        PileDir d=new PileDir();
        if (niveau==1)
            d=recursif1(g);
        if (niveau==2)
            d=recursif2(g);
        if (niveau==3)
            d=recursif3(g);
        if (niveau==4)
            d=recursif4(g);
        if (niveau==5)
            d=recursif5(g);
        return d;
    }
}
