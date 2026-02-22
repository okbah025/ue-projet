import java.util.ArrayDeque ;
 import java.util.*; 
  public class file {
           private ArrayDeque<pile> F ; 
         
         
         
         //initialiser 
         public  file (){
               this.F = new ArrayDeque<>();
               }
               // additionne un maillpn = une case passer 
           public void emfiler( pile p){
                       this.F.offer(p);
                    }
                    
            
            public pile defiler(){
                  
                   return F.poll();
                   }
                 //on regarde le sommet du pile 
               /* public Position sommet(){
                          return F.peek();
                        }*/
                public boolean isEmpty(){
                       return F.isEmpty();
                       }
                       //la taille de la pile   
               /* public int  size(){
                   return F.size();
                   }
                 public void clear()
                  {
                      F.clear();
                      }
              }*/
              }
