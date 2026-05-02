package fr.paris13.parabox.ig;

import fr.paris13.parabox.Modele.ChargeurNiveau;
import fr.paris13.parabox.Modele.Direction;
import fr.paris13.parabox.Modele.Grille;
import fr.paris13.parabox.Modele.JeuRecursif;
import fr.paris13.parabox.Modele.ParaboxLevel;
import fr.paris13.parabox.Modele.Position;
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
    private final PileDir d;
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
        d = ResoAutoRecursif.recursif(g, lvl);
        jeu = new JeuRecursif(g);
        level = new Level(g, lvl);
        level.setBoard(Direction.BAS);
        help = new HelpMenu(Version.RECURSIVE);
        helpVisible = true;
        pause = new PauseMenu(root, Version.RECURSIVE);
        pause.setVisible(false);
        StackPane niveau = new StackPane();
        Label niv = new Label("Niveau "+ lvl);
        Rectangle contour = new Rectangle(75, 45);
        contour.setFill(Color.WHITE);
        niveau.getChildren().addAll(contour, niv);
        niveau.setAlignment(Pos.TOP_RIGHT);
        niv.setPadding(new Insets(10));
        niv.setStyle("-fx-font: 15 system; ");
        
        getChildren().addAll(level, niveau, help, pause);
        
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
                    level.updateBoardRecReverse(jeu.getPileGrilles());
                }
                break;
                
            case R: // Recommencer
                Grille ng = ChargeurNiveau.charger(DOSSIER + File.separator + nomFich);
                if(ng !=null) {
                    jeu.reinitialiser(ng);
                    level.reset(ng);
                    level.setBoard(Direction.BAS);
                }
                break;
                
            case A: // Auto
                if (level.getLvl() == 1 && level.getLvl() == 2 && level.getLvl() == 4){
                    Grille ng2 = ChargeurNiveau.charger(DOSSIER + File.separator + nomFich);                    
                    if(ng2 !=null) {
                        jeu.reinitialiser(ng2);
                        level.reset(ng2);
                        level.setBoard(Direction.BAS);
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
                pause.setVisible(true);
                
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
            boolean ok = jeu.deplacerJoueur(direction);
            if (ok){
                level.updateBoardRec(jeu.getGrilleActive(), direction);
                if (jeu.estNiveauTermine()) {
                    root.getChildren().setAll(new VictoryMenu(root, Version.RECURSIVE, level, jeu.getNombreMouvements(), jeu.getNombrePoussees()));
                }
            }
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
}
