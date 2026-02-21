 //j'ecris ici tous les import 
 import java.util.*;
 public class c_chemin{
    public static pile c_chemin(boolean[][] M , int x1,int y1,int x2,int y2){
        int n=M.length ; 
        //la file qui contient l'ensemble des points possibles vers le cible
        file  F=new file();
        //l'ensemble des points visites 
        HashSet<Position> V =new HashSet<>();
        //initialisation
        pile debut =new pile();
        //j'empile la position de depart du chemin 
        debut.empiler(new Position(x1,y1));
        F.emfiler(debut);//represente mon premier chemin 
    
        //directions :droite , gauche, bas , haut 
        int[][] dir={ {1,0},{-1,0},{0,1},{0,-1}};
         
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
             
           //verifications
           if(x<0 ||x>=n ||  y<0 || y>=n){ continue;}
           if(!M[x][y]){ continue;}
           //est ce que j'avais passer a ce point
           if(V.contains(tete)){ continue;}    
           V.add(tete);//comme c'est non je l'ajoute 
           
           //comme j'ai pas atteint le cible j'ajoute quatre directions possible de passer pour atteindre le cible 
           for( int[] d:dir){
		      int nx=x + d[0];
		      int ny=y + d[1];
		      
			      pile copi =c.copie(); 
			      copi.empiler(new Position(nx,ny));
			      
			      F.emfiler(copi);
			      }
			      }
			      
			      return new pile();
			      }
			      }
			   
			   
			   
			   
		   
		   
		   
		   
		   
		   
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
