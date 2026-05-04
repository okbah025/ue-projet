package fr.paris13.parabox.ig;

import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.Jeu;
import fr.paris13.parabox.Modele.Position;
import fr.paris13.parabox.Modele.SauvegardeHistorique;
import fr.paris13.parabox.Modele.SauvegardePlateau;
import fr.paris13.parabox.Modele.SokobanLevel;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ResoAuto.PileDir;
import fr.paris13.parabox.ResoAuto.ResoAutoClassique;
import fr.paris13.parabox.ig.menu.HelpMenu;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Classe Parabox qui gère le jeu d'un niveau classique.
 * 
 *
 */
public class Sokoban extends StackPane {
    
    private final Jeu jeu;
    private final Level level;
    private final PauseMenu pause;
    private final HelpMenu help;
    private final StackPane root;
    private final int lvl;
    private boolean helpVisible;
    
    /**
     * Initialise le jeu classique avec une grille donnée
     * 
     * @param g la grille du niveau
     * @param root StackPane permettant de changer de vue
     * @param lvl num du niveau
     */
    public Sokoban(Grille g, StackPane root, int lvl){
        this.root = root;
        this.lvl = lvl;

        jeu = new Jeu(g);
        level = new Level(g, lvl);
        level.setBoard(Direction.BAS);
        help = new HelpMenu(Version.SIMPLE);
        helpVisible = true;
        pause = new PauseMenu(root, Version.SIMPLE);
        pause.setVisible(false);
       
        getChildren().addAll(level, showLevelNum(), help, pause);
        
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
                restart();
                break;
            case A: // Auto
                if (level.getLvl() >=1 && level.getLvl() <=4){
                    restart();
                    if(!jeu.estNiveauTermine()) {
                        PileDir d = ResoAutoClassique.classique(jeu.getGrille(), lvl);
                        int cmpt = d.size();
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.5), 
                            e -> {
                                Direction dir2 = d.depilerDir();
                                jeu.deplacerJoueur(dir2);
                                level.updateBoard(dir2);

                                if (jeu.estNiveauTermine()) {
                                    showVictory();
                                }
                            }
                        ));
                        t.setCycleCount(cmpt);
                        t.play();
                    }
                }
                break;
            case P: // Chemin
                level.setBoard(Direction.BAS);
                Level copie = new Level(jeu.getGrille(), lvl);
                this.getChildren().add(copie);
                copie.setBoard(Direction.BAS);
                copie.setOnMouseClicked(e -> {
                    Position pos = copie.getSelectedCell();
                    this.getChildren().remove(getChildren().size()-1);
                    if(jeu.getGrille().estCaseLibre(pos)){
                        level.chemin_court(pos.getX(), pos.getY(), jeu.getGrille());
                    } else {
                        this.getChildren().add(showMsg("Mouvement impossible"));
                        PauseTransition p = new PauseTransition(Duration.seconds(1));
                        p.setOnFinished(ev -> 
                            this.getChildren().remove(getChildren().size()-1));
                        p.play();
                    }
                });
                break;
            case ESCAPE: // Pause
               showPauseMenu();
            case H: // Aide
                if(helpVisible){
                    help.setVisible(false);
                    helpVisible = false;
                } else {
                    help.setVisible(true);
                    helpVisible = true;
                }
        }

        if (direction != null) {
            jeu.deplacerJoueur(direction);
            level.updateBoard(direction);

            if (jeu.estNiveauTermine()) {
                showVictory();
            }
        }
    }
    
    /**
     * Renvoie la grille du niveau donné
     * 
     * @param numero num du niveau choisi
     * @return la grille du niveau donné
     */
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
    
    private static StackPane showMsg(String s){
        StackPane st = new StackPane();
        Label msg = new Label(s);
        st.setAlignment(Pos.TOP_CENTER);
        msg.setPadding(new Insets(10));
        msg.setStyle("-fx-font: 20 system; ");
        st.getChildren().add(msg);
        
        return st;
    }
    
    private void showVictory(){
        String fichierSolution = "niveau" + lvl + "_solution.txt";
        String fichierHistoSolution = "niveau" + lvl + "_sol_deplacements.txt";
        // sauvegarde solution
        SauvegardePlateau save = new SauvegardePlateau(fichierSolution);
        save.ecrireGrille(jeu.getGrille());
        /* suppression sauvegarde en cours
        new File(fichierSave).delete();*/
        SauvegardeHistorique histoSolution = new SauvegardeHistorique(fichierHistoSolution);
        histoSolution.ecrireHistorique(jeu);

        PauseTransition p = new PauseTransition(Duration.seconds(0.5));
        p.setOnFinished(ev -> {
            root.getChildren().setAll(new VictoryMenu(root, Version.SIMPLE, level, jeu.getNombreMouvements(), jeu.getNombrePoussees()));});
        p.play();
    }
    
    private void restart(){
        Grille nouvelleGrille = creerNiveauSimple(lvl);
        jeu.setGrille(nouvelleGrille);
        level.reset(nouvelleGrille);
        level.setBoard(Direction.BAS);
    }
    
    private void showPauseMenu(){
        String fichierSave = "niveau" + lvl + "_save.txt";
        String fichierHisto = "niveau" + lvl + "_histo_deplacements.txt";
   
        SauvegardePlateau save = new SauvegardePlateau(fichierSave);
        save.ecrireGrille(jeu.getGrille());
        SauvegardeHistorique histo = new SauvegardeHistorique(fichierHisto);
        histo.ecrireHistorique(jeu);
        pause.setVisible(true);
    }
    
    private StackPane showLevelNum(){
        StackPane niveau = new StackPane();
        Label niv = new Label("Niveau "+ lvl);
        Rectangle contour = new Rectangle(75, 45);
        contour.setFill(Color.WHITE);
        niveau.getChildren().addAll(contour, niv);
        niveau.setAlignment(Pos.TOP_RIGHT);
        niv.setPadding(new Insets(10));
        niv.setStyle("-fx-font: 15 system; ");
        
        return niveau;
    }
}
