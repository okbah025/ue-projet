package fr.paris13.parabox.ResoAuto;
import fr.paris13.parabox.Modele.Position;
import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Objet;
import fr.paris13.parabox.Modele.Mur;
import fr.paris13.parabox.Modele.Piece;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.chemin.pile;

public class ResoAutoFonctions extends PileDir {

    public static boolean[][] matrice(int n, int m) {
        boolean[][] M=new boolean[n][m];
        for (int i=0; i<n; i++) {
            for (int j=0; j<m; j++) {
                if (i==0 || i==n-1 || j==0 || j==m-1)
                    M[i][j]=false;
                else
                    M[i][j]=true;
            }
        }
        return M;
    }

    public static void afficher(boolean[][] M, int n, int m) {
        int i, j;
        for (i=0; i<n; i++) {
            for (j=0; j<m; j++) {
                if (M[i][j]==false)
                    System.out.print("# ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    public static boolean[][] remplir_vide(Grille g) {
        int n=g.getHauteur();
        int m=g.getLargeur();
        boolean M[][]=new boolean[n][m];
        int i, j;
        for (i=0; i<n; i++)
            for (j=0; j<m; j++)
                M[i][j]=true;
        return M;
    }

    public static boolean[][] remplir(Grille g) {
        int n=g.getHauteur();
        int m=g.getLargeur();
        boolean M[][]=new boolean[n][m];
        int i, j;
        for (i=0; i<n; i++) {
            for (j=0; j<m; j++) {
                if (g.getObjet(j, i) instanceof Mur || g.getObjet(j,i) instanceof Piece)
                    M[i][j]=false;
                else
                    M[i][j]=true;
            }
        }
        return M;
    }

    public static PileDir directions(pile p) {
        PileDir p2=new PileDir();
        Position pos1=p.depiler();
        Position pos2=pos1;
        while (!p.isEmpty()) {
            pos2=p.depiler();
            if (pos2.getX()<pos1.getX())
                p2.empiler2(Direction.DROITE);
            if (pos2.getX()>pos1.getX())
                p2.empiler2(Direction.GAUCHE);
            if (pos2.getY()<pos1.getY())
                p2.empiler2(Direction.BAS);
            if (pos2.getY()>pos1.getY())
                p2.empiler2(Direction.HAUT);
            pos1=pos2;
        }
        return p2;
    }

    public static pile inverser(pile p) {
        pile p2=new pile();
        while (!p.isEmpty())
            p2.empiler(p.depiler());
        return p2;
    }
}
