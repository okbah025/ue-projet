
import java.util.Collection;

public class Piece extends Objet{
  /*une piece est constituée d'une grille 
  **c'est aussi un objet dans une grille*/
  private Grille grille;
  /*on construit une piece en lui donnant:
  **sa position dans sa grille mère;
  **un identifiant vers la grille mère;
  **une collection d'objet à mettre dans une grille;
  **quand on entre la pièce devient une grille;
  **quand on ressort elle redevient la simple instance dans sa grille mère*/
  /*
  @param : x et y , les positions dans la grille mère
  @param : Grille p, la référence à la grille mère ou la pièce mère
  @param : l et h , longeur et hauteur de la grille
  @param : la collection d'objets dans la piece*/
  /*la grille mère de la toute première pièce est null*/
  public Piece(int x, int y, int l, int h, Grille p, Collection<Objet> c ){
    super(x, y, p);
    grille = new Grille(c, l, h, this);
  }

  /*Méthodes*/
  /*avoir la grille */
  public Grille getGrid(){
    return this.grille;
  }
  /*on peut entrer dans une pièce et en ressortir*/
  public void entre(Objet obj, int posX, int posY, Piece p){
    /*lorsque l'on entre dans la pièce, les attributs de l'objet changent*/
    obj.setPos(posX, posY);
    obj.setGrilleParent(p.getGrid());
    obj.setGparent(grille);
    /*agrandir la grille*/
    /*d'ici la piece devient une grille entière*/
}
  /*on peut ressortir de la grille*/
  public void sortir(Objet obj, int posX, int posY, Piece p){
    obj.setPos(posX, posY);
    obj.setGrilleParent(grille);
    obj.setGparent(null);
    /*rendre petite la grille*/
  }
}