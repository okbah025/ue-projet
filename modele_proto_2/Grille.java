import java.util.Collection;
import java.util.Iterator;

public class Grille {
  /*explication: une grille est une matrice constituée d'objets de la classe Objet
  **toute Grill est une piece
  **chaque grille a une référence par rapport à la pièce à la quelle elle appartient
  **elle a une longeur et une hauteur*/
  Objet[][] matrice;
  int longeur;
  int hauteur;
  Piece maitre;

  /*constructeur*/
  /*la grille reçoit en entrée une collection d'objets tel que les cases vides sont représentées par Null,
  **lorsque le nombre d'objects dans la collection et inférieur au nombres de cases dans grille on lève une exception*/
  public Grille(Collection<Objet> c, int l, int h, Piece m) {
    if ((c.size() < (l * h)) || c.isEmpty()) {
      throw new IllegalArgumentException("la collection d'objet doit avoir plus d'éléments");
    }
    this.longeur = l;
    this.hauteur = h;
    this.maitre = m;
    this.matrice = new Objet[l][h];
    Iterator<Objet> itr = c.iterator();
    int i = 0;
    int j = 0;
    if (itr.hasNext()) {
      while (i < l) {
        while (j < h) {
          matrice[i][j] = itr.next();
          if (!itr.hasNext()) {
            break;
          }
          j++;
        }
        if (!itr.hasNext()) {
          break;
        }
        i++;
        j = 0;
      }
    }
  }

  /*Méthodes*/
  /*avoir la piece maitresse de la grille*/
  public Piece getMaitre() {
    return this.maitre;
  }

  /*avoir la hauteur et largeur de la grille*/
  public int getLongueur() {
    return this.longeur;
  }

  public int getHauteur() {
    return this.hauteur;
  }

  /*avoir un objet à une coordonnées donnée*/
  public Objet getObjet(int x, int y) {
    if (horsGrille(x, y)) {
      return null;
    }
    return matrice[x][y];
  }

  /*vérifie si les coordonnées d'un objet sont hors de la matrice*/
  public boolean horsGrille(int x, int y) {
    // Correction : Utilisation de || au lieu de && car si une seule condition est vraie, on est hors grille
    return (x < 0 || x >= this.longeur || y < 0 || y >= this.hauteur);
  }

  /*mettre un objet à une place donnée*/
  public void setObjet(Objet obj, int x, int y) {
    matrice[x][y] = obj;
  }

  /*vérifie si un objet est au bord interne d'une grille*/
  public boolean bordInterne(Objet obj, int x, int y) {
    if (!obj.equals(matrice[x][y])) {
      return false;
    }
    return ((x == 0) || (y == 0) || (x == this.longeur - 1) || (y == this.hauteur - 1));
  }

  /*si l'objet est aux bords externes de la grille,
  0 si n'est pas au bord,
  1 si en haut,
  2 si à gauche,
  3 si en bas,
  4 si à droite*/
  public int emplacementExterne(Objet obj, Piece p, int x, int y, int posXp, int posYp) {
    if (x + 1 == posXp && y == posYp) {
      return 1;
    }
    if (x - 1 == posXp && y == posYp) {
      return 3;
    }
    if (x == posXp && y == posYp + 1) {
      return 2;
    }
    if (x == posXp && y == posYp - 1) {
      return 4;
    }
    return 0;
  }

  /*déplacer un objet dans la grille*/
  public boolean deplaceObejt(Objet obj, int dirX, int dirY) {
    if (horsGrille(dirX, dirY)) {
      return false;
    }

    int posX = obj.getPosx(); /*actuelle position l'objet*/
    int posY = obj.getPosy();

    /*si la direction donnée est près de l'emplacement de l'objet*/
    if (posX == dirX - 1 || posY == dirY - 1 || posX == dirX + 1 || posY == dirY - 1) {

      if (matrice[dirX][dirY] == null) {
        obj.setPos(dirX, dirY);
        matrice[dirX][dirY] = obj;
        matrice[posX][posY] = null;
        return true;
      }

      /*si l'objet se trouvant à la position donnée est une pièce,
      on entre dans la pièce*/
      if (matrice[dirX][dirY] instanceof Piece) {
        /*entre dans la pièce*/
        Piece p = (Piece) matrice[dirX][dirY];
        int i = emplacementExterne(obj, p, posX, posY, dirX, dirY);
        if (i == 1) {
          p.entre(obj, 0, posY, p);
          return true;
        }
        if (i == 2) {
          p.entre(obj, posX, 0, p);
          return true;
        }
        if (i == 3) {
          p.entre(obj, p.getGrid().getHauteur() - 1, posY, p);
          return true;
        }
        if (i == 4) {
          p.entre(obj, posX, p.getGrid().getLongueur() - 1, p);
          return true;
        }
      }

      /*si c'est une cible*/
      if (matrice[dirX][dirY] instanceof Cible) {
        obj.setPos(dirX, dirY);
        matrice[dirX][dirY] = obj;
        matrice[posX][posY] = null;
        /*une méthodes qui indique que la cible est atteinte sera implémentée*/
        return true;
      }
    }
    return false;
  }
  /*des méthodes d'agrandissement et rapetissement de la grille*/
}