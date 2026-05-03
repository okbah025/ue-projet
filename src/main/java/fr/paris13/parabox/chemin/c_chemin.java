package fr.paris13.parabox.chemin;
import fr.paris13.parabox.Modele.Position;

 //j'ecris ici tous les import 
 import java.util.*;


 public class c_chemin {
    public static pile c_chemin(boolean[][] M , int x1,int y1,int x2,int y2){
        //  int n=M.length ;
        int largeur =M.length;
        int hauteur=M[0].length; 
        pile copi =new pile();
        //la file qui contient l'ensemble des points possibles vers le cible
        file  F=new file();
        Set<Position> V = new HashSet<>(); //les points visiter 
        //l'ensemble des points visites 
        //initialisation le chemin initiale 
        pile debut =new pile();
        //j'empile la position de depart du chemin 
        debut.empiler(new Position(x1,y1));
        F.emfiler(debut);//represente mon premier chemin 
        V.add(new Position(x1,y1));
        //directions :droite , gauche, bas , haut 
        
         
        // boucle principale
        while(!F.isEmpty()){
            pile  c = F.defiler();//je depile le premier chemin 
            Position tete = c.sommet(); //je retire le sommet du chemin 
            int x=tete.getX();
            int y=tete.getY();


            //je verifie si avec ce chemin on a arriver au cible 
            if(x==x2 && y==y2){
                return c;
            }
            Position[] direc = { 
                new Position(x+1 ,y),
                new Position(x-1, y),
                new Position(x , y-1),
                new Position(x,y+1),
            };
        
        
            for(Position p : direc){
                int nx =p.getX();
                int ny=p.getY();
                // condition avant l'ajout 
                if(nx>=0 && nx <largeur && ny>=0 && ny <hauteur && M[nx][ny]==true && !V.contains(p)){

                    V.add(p); //on marque qu'on l'a ajouter 
                    pile copie=c.copie();
                    copie.empiler(p);
                    F.emfiler(copie);
                }
            }

        }

        return new pile();
    }
}

           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
