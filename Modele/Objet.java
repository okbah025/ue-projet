public class Objet{
  private int posx;
  private int posy;
  private Grille parent;
  private Grille gparent;

  public Objet(int x, int y, Grille p){
    this.posx = x;
    this.posy = y;
    this.parent = p;
  }

  /*méthodes pour avoir la position x de l'objet*/
  public int getPosx(){
    return this.posx;
  }
  /*la position y e l'objet*/
  public int getPosy(){
    return this.posy;
  }
  /*mettre une nouvelle position de l'objet*/
  public void setPos(int new_x, int new_y){
    this.posx = new_x;
    this.posy = new_y;
  }
  /*mettre une nouvelle grille parente à l'objet*/
  public void setGrilleParent(Grille p){
    this.parent = p;
  }
  /*avoir le parent de l'objet*/
  public Grille getParent(){
    return this.parent;
  }
  /*référence vers le grand parent d'un objet*/
  public void setGparent(Grille p){
    this.gparent = p;
  }
}