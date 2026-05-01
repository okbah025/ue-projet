package fr.paris13.parabox.ig;
import fr.paris13.parabox.Modele.ChargeurNiveau;
import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.JeuRecursif;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ResoAuto.PileDir;
import fr.paris13.parabox.ResoAuto.ResoAutoRecursif;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;
import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Classe Parabox qui gère le jeu d'un niveau récursif.
 * 
 * 
 */

public class Parabox extends StackPane {
    private static final String DOSSIER = "src/main/resources";
    private final JeuRecursif jeu;
    private final Level level;
    private final PauseMenu pause;
    private final StackPane root;
    private final int lvl;
    private final PileDir d;
    private final String nomFich;
    
    /**
     * Initialise le jeu récursif avec une grille donnée
     * 
     * @param g la grille du niveau
     * @param root StackPane permettant de changer de vue
     * @param lvl num du niveau
     */
    public Parabox(Grille g, StackPane root, int lvl){
        this.root = root;
        this.lvl = lvl;
        nomFich = ParaboxLevel.listerFichiersNiveaux().get(lvl-1);
        d = ResoAutoRecursif.recursif(g, lvl);
        jeu = new JeuRecursif(g);
        level = new Level(g, lvl);
        level.setBoard(Direction.BAS);
        pause = new PauseMenu(root, Version.RECURSIVE);
        pause.setVisible(false);
        getChildren().addAll(level, pause);
        
        Scene scene = root.getScene();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

    }
   
    /**
     * Gère les évènements selon l'entrée du clavier donné.
     * 
     * @param code entrée du clavier
     */
    private void handleKeyInput(KeyCode code){
        Direction direction = null;
        boolean auto = false;
        
        switch(code){
            case UP:
                direction = Direction.HAUT;
                break;
            case DOWN:
                direction = Direction.BAS;
                break;
            case LEFT:
                direction = Direction.GAUCHE;
                break;
            case RIGHT:
                direction = Direction.DROITE;
                break;
            case Z: // Annuler
                if (jeu.annulerMouvement()) {
                    level.updateBoardRec(jeu.getGrilleActive(), Direction.BAS);
                }
                break;
                
            case R: // Recommencer
                Grille ng = ChargeurNiveau.charger(DOSSIER + File.separator + nomFich);
                if(ng !=null) {
                    jeu.reinitialiser(ng);
                    level.reset(ng);
                }
                break;
                
            case A: // Auto
                if (level.getLvl() >=2 && level.getLvl() <=3){
                    Grille ng2 = ChargeurNiveau.charger(DOSSIER + File.separator + nomFich);                    if(ng2 !=null) {
                        jeu.reinitialiser(ng2);
                        level.reset(ng2);
                    }
                    auto = true;
                }
                if(auto && !jeu.estNiveauTermine()) {
                    int cmpt = d.size();
                    Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.5), 
                            e -> {
                                Direction dir2 = d.depilerDir();
                                jeu.deplacerJoueur(dir2);
                                level.updateBoard(dir2);
                                
                                if (jeu.estNiveauTermine()) {
                                    PauseTransition p = new PauseTransition(Duration.seconds(0.5));
                                    p.setOnFinished(ev -> 
                                        root.getChildren().setAll(new VictoryMenu(root, Version.RECURSIVE, level, jeu.getNombreMouvements(), jeu.getNombrePoussees())));
                                    p.play();
                                }
                            }
                        ));
                    t.setCycleCount(cmpt);
                    t.play();
                }
                break;
                
            case ESCAPE: // Pause
                pause.setVisible(true);
        }

        if (direction != null) {
            boolean ok = jeu.deplacerJoueur(direction);
            if (ok){
                level.updateBoardRec(jeu.getGrilleActive(), direction);
                if (jeu.estNiveauTermine()) {
                    root.getChildren().setAll(new VictoryMenu(root, Version.RECURSIVE, level, jeu.getNombreMouvements(), jeu.getNombrePoussees()));
                }
            }
        }    
    }
}
