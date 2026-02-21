import java.util.Stack ;
 import java.util.*; 
public class pile {
       ArrayDeque<Position> pil ; 
         
         
         
         //initialiser 
         public pile(){
               this.pil=new ArrayDeque<>();
               }
           public void empiler( Position m ){
                    
                    pil.push(m);
                    }
                    
            // additionne un maillpn = une case passer 
            public Position depiler(){
                   Position m=pil.pop();
                   return m;
                   }
                 //on regarde le sommet du pile 
                public Position sommet(){
                        Position m ;
                        m=pil.peek();
                        return m;
                        }
                public boolean isEmpty(){
                       return pil.isEmpty();
                       }
                       //la taille de la pile   
                public int  size(){
                   return pil.size();
                   }
                /* public void clear()
                  {
                      pile.clear();}*/
               
               public pile copie(){
                 pile temp=new pile();
                   for(Position p1: this.pil){
                      temp.empiler(p1);
                      }
                      return temp;
                      }
                      }
