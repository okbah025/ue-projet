package fr.paris13.parabox.ig;


import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.Jeu;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ResoAuto.PileDir;
import fr.paris13.parabox.ResoAuto.ResoAutoClassique;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;


import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Sokoban extends StackPane {
    
    private final Jeu jeu;
    private final Level level;
    private final PauseMenu pause;
    private final StackPane root;
    private final int lvl;
    private final PileDir d;
    
    public Sokoban(Grille g, StackPane root, int lvl){
        this.root = root;
        this.lvl = lvl;
        d = ResoAutoClassique.classique(g, lvl);
        jeu = new Jeu(g);
        level = new Level(g, lvl);
        level.setBoard();
        pause = new PauseMenu(root, Version.SIMPLE);
        pause.setVisible(false);
        getChildren().addAll(level, pause);
        
        
        Scene scene = root.getScene();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }
    
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
                    level.updateBoardReverse();
                }
                break;
            case R: // Recommencer
                Grille nouvelleGrille = creerNiveauSimple(lvl);
                jeu.setGrille(nouvelleGrille);
                level.reset(nouvelleGrille);
                break;
            case A: // Auto
                if (level.getLvl() >=1 && level.getLvl() <=4){
                    Grille nouvelleGrille2 = creerNiveauSimple(lvl);                 
                    jeu.setGrille(nouvelleGrille2);
                    level.reset(nouvelleGrille2);
                    auto = true;
                }
                if(auto && !jeu.estNiveauTermine()) {
                    int cmpt = d.size();
                    Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.5), 
                            e -> {
                                Direction dir2 = d.depiler2();
                                jeu.deplacerJoueur(dir2);
                                level.updateBoard(dir2);
                                
                                if (jeu.estNiveauTermine()) {
                                    PauseTransition p = new PauseTransition(Duration.seconds(0.5));
                                    p.setOnFinished(ev -> 
                                        root.getChildren().setAll(new VictoryMenu(root, Version.SIMPLE, level)));
                                    p.play();
                                }
                            }
                        ));
                    t.setCycleCount(cmpt);
                    t.play();
                }
                break;
            
            case ESCAPE:
                pause.setVisible(true);
        }

        if (!auto && direction != null) {
            jeu.deplacerJoueur(direction);
            level.updateBoard(direction);

            if (jeu.estNiveauTermine()) {
                root.getChildren().setAll(new VictoryMenu(root, Version.SIMPLE, level));
            }
        }
    }
    
    private static Grille creerNiveauSimple(int numero) {
        switch (numero) {
            case 1:  return SokobanLevel.niveauSimple1();
            case 2:  return SokobanLevel.niveauSimple2();
            case 3:  return SokobanLevel.niveauSimple3();
            case 4:  return SokobanLevel.niveauSimple4();
            case 5:  return SokobanLevel.niveauSimple5();
            case 6:  return SokobanLevel.niveauSimple6();
            case 7:  return SokobanLevel.niveauSimple7();
            case 8:  return SokobanLevel.niveauSimple8();
            case 9:  return SokobanLevel.niveauSimple9();
            case 10: return SokobanLevel.niveauSimple10();
            default: return null;
        }
    }

    
}

