package fr.paris13.parabox.ig;

import fr.paris13.parabox.Modele.ChargeurNiveau;
import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.JeuRecursif;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.Position;
import fr.paris13.parabox.Modele.SauvegardeHistorique;
import fr.paris13.parabox.Modele.SauvegardePlateauRecursif;
import fr.paris13.parabox.Modele.Version;
import fr.paris13.parabox.ResoAuto.PileDir;
import fr.paris13.parabox.ResoAuto.ResoAutoRecursif;
import fr.paris13.parabox.ig.menu.HelpMenu;
import fr.paris13.parabox.ig.menu.PauseMenu;
import fr.paris13.parabox.ig.menu.VictoryMenu;

import java.io.File;

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
 * Classe Parabox qui gère le jeu d'un niveau récursif.
 * 
 * 
 */

public class Parabox extends StackPane {
    private static final String DOSSIER = "src/main/resources";
    private final JeuRecursif jeu;
    private final Level level;
    private final PauseMenu pause;
    private final HelpMenu help;
    private final StackPane root;
    private final int lvl;
    private final String nomFich;
    private boolean helpVisible;
    
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
        
        jeu = new JeuRecursif(g);
        level = new Level(g, lvl);
        level.setBoard(Direction.BAS);
        help = new HelpMenu(Version.RECURSIVE);
        helpVisible = true;
        pause = new PauseMenu(root, Version.RECURSIVE);
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
                    level.updateBoardRecReverse(jeu.getPileGrilles());
                }
                break;
            case R: // Recommencer
                restart();
                break;
            case A: // Auto
                if (lvl == 1 || lvl == 2 || lvl == 4){
                    restart();
                    if(!jeu.estNiveauTermine()) {
                        PileDir d = ResoAutoRecursif.recursif(jeu.getGrilleActive(), lvl);
                        int cmpt = d.size();
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.5), 
                            e -> {
                                Direction dir2 = d.depilerDir();
                                jeu.deplacerJoueur(dir2);
                                level.updateBoardRec(jeu.getGrilleActive(), dir2);

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
                Level copie = new Level(jeu.getGrilleActive(), lvl);
                this.getChildren().add(copie);
                copie.setBoard(Direction.BAS);
                copie.setOnMouseClicked(e -> {
                    Position pos = copie.getSelectedCell();
                    this.getChildren().remove(getChildren().size()-1);
                    if(jeu.getGrilleActive().estCaseLibre(pos)){
                        level.chemin_court(pos.getX(), pos.getY(), jeu.getGrilleActive());
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
            default:
        }

        if (direction != null) {
            boolean ok = jeu.deplacerJoueur(direction);
            if (ok){
                level.updateBoardRec(jeu.getGrilleActive(), direction);
            }
        }
        if (jeu.estNiveauTermine()) {
            showVictory();
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
        // sauvegarde plateau solution
        SauvegardePlateauRecursif saveSol = new SauvegardePlateauRecursif(fichierSolution);
        saveSol.sauvegarder(jeu.getGrilleRacine());
        // sauvegarde historique solution
        SauvegardeHistorique histoSol = new SauvegardeHistorique(fichierHistoSolution);
        histoSol.ecrireHistoriqueRecursif(jeu);
        
        PauseTransition p = new PauseTransition(Duration.seconds(0.5));
        p.setOnFinished(ev -> 
            root.getChildren().setAll(new VictoryMenu(root, Version.RECURSIVE, level, jeu.getNombreMouvements(), jeu.getNombrePoussees())));
        p.play();
    }
    
    private void restart(){
        Grille ng = ChargeurNiveau.charger(DOSSIER + File.separator + nomFich);
        if(ng !=null) {
            jeu.reinitialiser(ng);
            level.reset(ng);
            level.setBoard(Direction.BAS);
        }
    }
    
    private void showPauseMenu(){
        String fichierSave = "niveau" + lvl + "_save.txt";
        String fichierHisto = "niveau" + lvl + "_histo_deplacements.txt";
        /*new : persistance*/
        SauvegardePlateauRecursif save = new SauvegardePlateauRecursif(fichierSave);
        save.sauvegarder(jeu.getGrilleRacine());
        SauvegardeHistorique histo = new SauvegardeHistorique(fichierHisto);
        histo.ecrireHistoriqueRecursif(jeu);
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
