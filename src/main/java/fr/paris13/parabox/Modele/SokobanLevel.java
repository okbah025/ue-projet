package fr.paris13.parabox.Modele;

public class SokobanLevel {
    
    public static final Grille[] levelList = {niveauSimple1(), niveauSimple2(), niveauSimple3(), niveauSimple4(), niveauSimple5(), niveauSimple6(), niveauSimple7(), niveauSimple8(), niveauSimple9(), niveauSimple10()};
    
    public static Grille[] getList(){
        return SokobanLevel.levelList;
    }
    
    /** Créer un cadre de murs tout autour de la grille. */
    private static void cadre(Grille g, int l, int h) {
        for (int x = 0; x < l; x++) {
            g.setObjet(new Mur(x, 0, g), x, 0);
            g.setObjet(new Mur(x, h - 1, g), x, h - 1);
        }
        for (int y = 0; y < h; y++) {
            g.setObjet(new Mur(0, y, g), 0, y);
            g.setObjet(new Mur(l - 1, y, g), l - 1, y);
        }
    }

    public static Grille niveauSimple1() {
        Grille g = new Grille(7, 5, "Niveau 1");
        cadre(g, 7, 5);
        g.setObjet(new Cible(5, 2, g),  5, 2);
        g.setObjet(new Boite(3, 2, g),  3, 2);
        g.setObjet(new Joueur(1, 2, g), 1, 2);
        return g;
    }

    public static Grille niveauSimple2() {
        Grille g = new Grille(8, 6, "Niveau 2");
        cadre(g, 8, 6);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    public static Grille niveauSimple3() {
        Grille g = new Grille(8, 6, "Niveau 3");
        cadre(g, 8, 6);
        g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(4, 3, g), 4, 3);
        g.setObjet(new Cible(6, 1, g), 6, 1);
        g.setObjet(new Cible(6, 4, g), 6, 4);
        g.setObjet(new Boite(3, 1, g), 3, 1);
        g.setObjet(new Boite(2, 4, g), 2, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    public static Grille niveauSimple4() {
        Grille g = new Grille(9, 7, "Niveau 4");
        cadre(g, 9, 7);
        g.setObjet(new Mur(4, 2, g), 4, 2);
        g.setObjet(new Mur(4, 4, g), 4, 4);
        g.setObjet(new Cible(7, 2, g), 7, 2);
        g.setObjet(new Cible(7, 3, g), 7, 3);
        g.setObjet(new Cible(7, 4, g), 7, 4);
        g.setObjet(new Boite(3, 2, g), 3, 2);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(3, 4, g), 3, 4);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    public static Grille niveauSimple5() {
        Grille g = new Grille(10, 7, "Niveau 5");
        cadre(g, 10, 7);
        g.setObjet(new Mur(3, 1, g), 3, 1); g.setObjet(new Mur(3, 2, g), 3, 2);
        g.setObjet(new Mur(6, 2, g), 6, 2); g.setObjet(new Mur(6, 3, g), 6, 3);
        g.setObjet(new Mur(6, 4, g), 6, 4); g.setObjet(new Mur(3, 5, g), 3, 5);
        g.setObjet(new Cible(8, 1, g), 8, 1);
        g.setObjet(new Cible(8, 3, g), 8, 3);
        g.setObjet(new Cible(8, 5, g), 8, 5);
        g.setObjet(new Boite(4, 1, g), 4, 1);
        g.setObjet(new Boite(5, 3, g), 5, 3);
        g.setObjet(new Boite(4, 5, g), 4, 5);
        g.setObjet(new Joueur(1, 3, g), 1, 3);
        return g;
    }

    public static Grille niveauSimple6() {
        Grille g = new Grille(10, 8, "Niveau 6");
        cadre(g, 10, 8);
        for (int y = 2; y < 6; y++) g.setObjet(new Mur(5, y, g), 5, y);
        g.setObjet(new Mur(2, 3, g), 2, 3); g.setObjet(new Mur(8, 4, g), 8, 4);
        g.setObjet(new Cible(8, 1, g), 8, 1); g.setObjet(new Cible(8, 2, g), 8, 2);
        g.setObjet(new Cible(8, 5, g), 8, 5); g.setObjet(new Cible(8, 6, g), 8, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1); g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4); g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }

    public static Grille niveauSimple7() {
        Grille g = new Grille(11, 8, "Niveau 7");
        cadre(g, 11, 8);
        for (int y = 2; y < 6; y++) g.setObjet(new Mur(5, y, g), 5, y);
        g.setObjet(new Mur(2, 3, g), 2, 3); g.setObjet(new Mur(8, 2, g), 8, 2);
        g.setObjet(new Mur(8, 5, g), 8, 5);
        g.setObjet(new Cible(9, 1, g), 9, 1); g.setObjet(new Cible(9, 2, g), 9, 2);
        g.setObjet(new Cible(9, 5, g), 9, 5); g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Boite(3, 1, g), 3, 1); g.setObjet(new Boite(4, 3, g), 4, 3);
        g.setObjet(new Boite(6, 4, g), 6, 4); g.setObjet(new Boite(3, 6, g), 3, 6);
        g.setObjet(new Joueur(1, 1, g), 1, 1);
        return g;
    }

    public static Grille niveauSimple8() {
        Grille g = new Grille(11, 9, "Niveau 8");
        cadre(g, 11, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 4, g), 5, 4);
        g.setObjet(new Cible(9, 2, g), 9, 2); g.setObjet(new Cible(9, 3, g), 9, 3);
        g.setObjet(new Cible(9, 5, g), 9, 5); g.setObjet(new Cible(9, 6, g), 9, 6);
        g.setObjet(new Cible(9, 7, g), 9, 7);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2); g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }

    public static Grille niveauSimple9() {
        Grille g = new Grille(12, 9, "Niveau 9");
        cadre(g, 12, 9);
        for (int i = 1; i < 4; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        for (int i = 5; i < 8; i++) {
            g.setObjet(new Mur(3, i, g), 3, i); g.setObjet(new Mur(8, i, g), 8, i);
        }
        g.setObjet(new Mur(5, 2, g), 5, 2); g.setObjet(new Mur(5, 3, g), 5, 3);
        g.setObjet(new Mur(5, 5, g), 5, 5); g.setObjet(new Mur(5, 6, g), 5, 6);
        g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Cible(9, 3, g), 9, 3); g.setObjet(new Cible(10, 3, g), 10, 3);
        g.setObjet(new Cible(9, 4, g), 9, 4); g.setObjet(new Cible(10, 4, g), 10, 4);
        g.setObjet(new Cible(9, 5, g), 9, 5);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(4, 4, g), 4, 4);
        g.setObjet(new Boite(7, 2, g), 7, 2); g.setObjet(new Boite(7, 6, g), 7, 6);
        g.setObjet(new Boite(2, 6, g), 2, 6);
        g.setObjet(new Joueur(1, 4, g), 1, 4);
        return g;
    }

    public static Grille niveauSimple10() {
        Grille g = new Grille(13, 10, "Niveau 10");
        cadre(g, 13, 10);
        for (int i = 2; i < 8; i++) {
            if (i != 4 && i != 5) {
                g.setObjet(new Mur(4, i, g), 4, i);
                g.setObjet(new Mur(9, i, g), 9, i);
            }
        }
        g.setObjet(new Mur(6, 3, g), 6, 3); g.setObjet(new Mur(6, 4, g), 6, 4);
        g.setObjet(new Mur(6, 5, g), 6, 5); g.setObjet(new Mur(6, 6, g), 6, 6);
        g.setObjet(new Mur(2, 5, g), 2, 5); g.setObjet(new Mur(11, 5, g), 11, 5);
        g.setObjet(new Cible(10, 2, g), 10, 2); g.setObjet(new Cible(11, 2, g), 11, 2);
        g.setObjet(new Cible(10, 3, g), 10, 3); g.setObjet(new Cible(11, 3, g), 11, 3);
        g.setObjet(new Cible(10, 6, g), 10, 6); g.setObjet(new Cible(11, 6, g), 11, 6);
        g.setObjet(new Boite(2, 2, g), 2, 2); g.setObjet(new Boite(3, 4, g), 3, 4);
        g.setObjet(new Boite(5, 5, g), 5, 5); g.setObjet(new Boite(7, 3, g), 7, 3);
        g.setObjet(new Boite(8, 6, g), 8, 6); g.setObjet(new Boite(2, 7, g), 2, 7);
        g.setObjet(new Joueur(1, 5, g), 1, 5);
        return g;
    }
}

