package fr.paris13.parabox.Modele;
/**
 * Classe TestModele
 * 
 * Cette classe contient des exemples d'utilisation du modèle.
 * Elle montre comment créer un niveau simple et effectuer des déplacements.
 * 
 * Cette classe est principalement pour :
 * - Tester le modèle
 * - Montrer aux autres membres de l'équipe comment utiliser les classes
 * - Servir d'exemple de documentation
 * 
 */
public class TestModele {
    
    /**
     * Méthode principale pour tester le modèle
     */
    public static void main(String[] args) {
        System.out.println("=== TEST DU MODELE SOKOBAN ===\n");
        
        // Test 1 : Créer un niveau simple
        testCreationNiveauSimple();
        
        // Test 2 : Tester les déplacements
        testDeplacements();
        
        // Test 3 : Tester l'annulation
        testAnnulation();
        
        System.out.println("\n=== FIN DES TESTS ===");
    }
    
    /**
     * Test 1 : Créer un niveau Sokoban simple
     * 
     * Crée un petit niveau 5x5 avec :
     * - Des murs sur les bords
     * - Un joueur
     * - Une boîte
     * - Une cible
     */
    private static void testCreationNiveauSimple() {
        System.out.println("--- Test 1 : Création d'un niveau simple ---");
        
        // Créer une grille 5x5
        Grille grille = new Grille(5, 5, "Test1");
        
        // Ajouter des murs sur les bords
        // Ligne du haut (y=0)
        for (int x = 0; x < 5; x++) {
            grille.setObjet(new Mur(x, 0, grille), x, 0);
        }
        // Ligne du bas (y=4)
        for (int x = 0; x < 5; x++) {
            grille.setObjet(new Mur(x, 4, grille), x, 4);
        }
        // Colonne de gauche (x=0)
        for (int y = 1; y < 4; y++) {
            grille.setObjet(new Mur(0, y, grille), 0, y);
        }
        // Colonne de droite (x=4)
        for (int y = 1; y < 4; y++) {
            grille.setObjet(new Mur(4, y, grille), 4, y);
        }
        
        // Ajouter une cible au centre
        grille.setObjet(new Cible(2, 2, grille), 2, 2);
        
        // Ajouter une boîte à côté de la cible
        grille.setObjet(new Boite(2, 1, grille), 2, 1);
        
        // Ajouter le joueur
        grille.setObjet(new Joueur(2, 3, grille), 2, 3);
        
        // Afficher la grille
        System.out.println("Grille créée :");
        System.out.println(grille.afficherGrille());
        
        // Afficher les informations
        System.out.println("Informations : " + grille);
        System.out.println();
    }
    
    /**
     * Test 2 : Tester les déplacements du joueur
     * 
     * Crée un niveau et effectue plusieurs déplacements :
     * - Déplacement simple
     * - Déplacement avec poussée de boîte
     * - Déplacement impossible (dans un mur)
     */
    private static void testDeplacements() {
        System.out.println("--- Test 2 : Déplacements ---");
        
        // Créer un niveau simple
        Grille grille = creerNiveauTest();
        Jeu jeu = new Jeu(grille);
        
        System.out.println("Niveau initial :");
        System.out.println(jeu.afficherGrille());
        System.out.println(jeu.getStatistiques());
        
        // Test déplacement vers le haut (pousser la boîte)
        System.out.println("Déplacement HAUT (pousser la boîte) :");
        boolean succes = jeu.deplacerJoueur(Direction.HAUT);
        System.out.println("Résultat : " + (succes ? "RÉUSSI" : "ÉCHOUÉ"));
        System.out.println(jeu.afficherGrille());
        System.out.println(jeu.getStatistiques());
        
        // Test déplacement impossible (dans un mur)
        System.out.println("Déplacement GAUCHE (dans un mur) :");
        succes = jeu.deplacerJoueur(Direction.GAUCHE);
        System.out.println("Résultat : " + (succes ? "RÉUSSI" : "ÉCHOUÉ"));
        
        // Test déplacement simple
        System.out.println("Déplacement DROITE (déplacement simple) :");
        succes = jeu.deplacerJoueur(Direction.DROITE);
        System.out.println("Résultat : " + (succes ? "RÉUSSI" : "ÉCHOUÉ"));
        System.out.println(jeu.afficherGrille());
        System.out.println(jeu.getStatistiques());
        
        // Vérifier la victoire
        if (jeu.estNiveauTermine()) {
            System.out.println("*** NIVEAU GAGNÉ ! ***");
        }
        
        System.out.println();
    }
    
    /**
     * Test 3 : Tester l'annulation de mouvements
     */
    private static void testAnnulation() {
        System.out.println("--- Test 3 : Annulation (Ctrl+Z) ---");
        
        // Créer un niveau et faire quelques mouvements
        Grille grille = creerNiveauTest();
        Jeu jeu = new Jeu(grille);
        
        System.out.println("Niveau initial :");
        System.out.println(jeu.afficherGrille());
        
        // Faire deux mouvements
        System.out.println("Faire 2 mouvements...");
        jeu.deplacerJoueur(Direction.HAUT);
        jeu.deplacerJoueur(Direction.DROITE);
        System.out.println("Après 2 mouvements :");
        System.out.println(jeu.afficherGrille());
        System.out.println("Mouvements : " + jeu.getNombreMouvements());
        
        // Annuler un mouvement
        System.out.println("Annuler 1 mouvement...");
        boolean annule = jeu.annulerMouvement();
        System.out.println("Annulation : " + (annule ? "RÉUSSIE" : "ÉCHOUÉE"));
        System.out.println(jeu.afficherGrille());
        System.out.println("Mouvements : " + jeu.getNombreMouvements());
        
        // Annuler encore
        System.out.println("Annuler encore 1 mouvement...");
        annule = jeu.annulerMouvement();
        System.out.println("Annulation : " + (annule ? "RÉUSSIE" : "ÉCHOUÉE"));
        System.out.println(jeu.afficherGrille());
        System.out.println("Mouvements : " + jeu.getNombreMouvements());
        
        System.out.println();
    }
    
    /**
     * Méthode utilitaire pour créer un niveau de test
     * 
     * Niveau 5x5 :
     * #####
     * #  .#
     * # $.#
     * #  @#
     * #####
     * 
     * @ = joueur, $ = boîte, . = cible, # = mur
     * 
     * @return La grille créée
     */
    private static Grille creerNiveauTest() {
        Grille grille = new Grille(5, 5, "TestNiveau");
        
        // Créer les murs (bords)
        for (int x = 0; x < 5; x++) {
            grille.setObjet(new Mur(x, 0, grille), x, 0);
            grille.setObjet(new Mur(x, 4, grille), x, 4);
        }
        for (int y = 0; y < 5; y++) {
            grille.setObjet(new Mur(0, y, grille), 0, y);
            grille.setObjet(new Mur(4, y, grille), 4, y);
        }
        
        // Ajouter la cible
        grille.setObjet(new Cible(3, 1, grille), 3, 1);
        
        // Ajouter la boîte
        grille.setObjet(new Boite(2, 2, grille), 2, 2);
        
        // Ajouter le joueur
        grille.setObjet(new Joueur(3, 3, grille), 3, 3);
        
        return grille;
    }
    
    /**
     * Test bonus : Montrer comment utiliser les Direction
     */
    public static void testDirections() {
        System.out.println("--- Test Bonus : Directions ---");
        
        // Créer une position
        Position pos = new Position(5, 5);
        System.out.println("Position initiale : " + pos);
        
        // Appliquer différentes directions
        Position nouvPos = Direction.HAUT.appliquerSur(pos);
        System.out.println("Après HAUT : " + nouvPos);
        
        nouvPos = Direction.BAS.appliquerSur(pos);
        System.out.println("Après BAS : " + nouvPos);
        
        nouvPos = Direction.GAUCHE.appliquerSur(pos);
        System.out.println("Après GAUCHE : " + nouvPos);
        
        nouvPos = Direction.DROITE.appliquerSur(pos);
        System.out.println("Après DROITE : " + nouvPos);
        
        // Tester la direction opposée
        Direction dir = Direction.HAUT;
        System.out.println("Direction : " + dir + ", opposée : " + dir.getOppose());
        
        System.out.println();
    }
}
