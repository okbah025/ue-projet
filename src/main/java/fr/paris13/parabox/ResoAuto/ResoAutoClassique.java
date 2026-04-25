public class ResoAutoClassique extends ResoAutoFonctions {

    public static PileDir classique1(boolean[][] M) {
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 4, 2, 1, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=directions(chemin);
        return d;
    }

    public static PileDir classique2(boolean[][] M) {
        pile chemin=new pile();
        pile p=new pile();
        p=inverser(c_chemin(M, 1, 3, 3, 4));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 4, 4, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 4, 4, 3, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 1, 4, 1);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=directions(chemin);
        return d;
    }

    public static PileDir classique3(boolean[][] M) {
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 1, 4, 1, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 4, 2, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 4, 4, 2, 1));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 5, 1, 3, 1);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        PileDir d=directions(chemin);
        return d;
    }

    public static PileDir classique4(boolean[][] M) {
        pile chemin=new pile();
        pile p=new pile();
        p=c_chemin(M, 6, 3, 1, 3);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 4, 6, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 6, 5, 3, 5));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 3, 4, 3, 4);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 2, 4, 2, 3));
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
        p=inverser(c_chemin(M, 5, 3, 5, 2));
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=c_chemin(M, 6, 2, 6, 2);
        while (!p.isEmpty())
            chemin.empiler(p.depiler());
        p=inverser(c_chemin(M, 6, 1, 3, 1));
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
        PileDir d=directions(chemin);
        return d;
    }
}
