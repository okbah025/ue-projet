package fr.paris13.parabox.ResoAuto;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.chemin.pile;
import fr.paris13.parabox.chemin.c_chemin;

public class ResoAutoClassique extends ResoAutoFonctions {

    public static PileDir classique1(Grille g) {
        boolean[][] M=matriceRemplie(g);
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 4, 2, 1, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir classique2(Grille g) {
        boolean[][] M=matriceRemplie(g);
        pile chemin=new pile();
        pile p=new pile();
        p=inverserPile(c_chemin(M, 1, 3, 3, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 4, 4, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 4, 4, 3, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 1, 4, 1);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir classique3(Grille g) {
        boolean[][] M=matriceVide(g);
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 1, 4, 1, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 4, 2, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 4, 4, 2, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 1, 3, 1);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir classique4(Grille g) {
        boolean[][] M=matriceRemplie(g);
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 6, 3, 1, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 4, 6, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 6, 5, 3, 5));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 4, 3, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 2, 4, 2, 3));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 3, 3, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 4, 5, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 3, 6, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 5, 3, 5, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 2, 6, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverserPile(c_chemin(M, 6, 1, 3, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 2, 3, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 2, 3, 2, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 3, 3, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 2, 5, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 3, 6, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 4, 5, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 4, 6, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=pileDirections(chemin);
        return d;
    }

    public static PileDir classique(Grille g, int niveau) {
        PileDir d=new PileDir();
        if (niveau==1)
            d=classique1(g);
        if (niveau==2)
            d=classique2(g);
        if (niveau==3)
            d=classique3(g);
        if (niveau==4)
            d=classique4(g);
        return d;
    }
}
