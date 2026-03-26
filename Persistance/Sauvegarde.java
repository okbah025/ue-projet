public class Sauvegarde {   
/*classe qui a des methodes pour ecrire la pile (historique ) dans un fichier à chaque fois qu'on s apprete a  quitter le jeu* 
on devra ensuite récupérer ce fichier et lire dedans pour retrouver les infos (historique)*/
/* dans notre main : on devra ecrire à la fin (moment de quitter le jeu) un appel à la fonction qui ecrit;
et au debut : appel à la fonction qui lit*/

/* pour la sauvegarde des solutions : dans le main on ecrit "if niveau gagnee" et dans ce cas on appelle la fonction qui va sauvegarder la solution (copier notre pile, cad historique) dans le fichier contenant toutes les soulutions (cad trouvées par le joueur)*/

    public static void main(String[] args) {

        try {
            // Création d'un fileWriter pour écrire dans un fichier
            FileWriter fileWriter = new FileWriter("/path/to/the/file", false);

            // Création d'un bufferedWriter qui utilise le fileWriter
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // ajout d'un texte à notre fichier
            writer.write("preferenceNewsletter=false");

            // Retour à la ligne
            writer.newLine();

            writer.write("preferenceColor=#425384");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
}



