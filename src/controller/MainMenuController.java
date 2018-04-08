package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MainMenuModel;
import model.offline_game;
import model.player.figures;
import view.MainMenuView;
import view.TicTacToeView;

import java.nio.file.Paths;
import java.util.Random;

public class MainMenuController {

    //0 = PLAY
    //1 = OFFLINE/ONLINE
    int menumode =0;

    MediaPlayer mediaPlayer;

    public MainMenuController(MainMenuModel model, MainMenuView view, MediaPlayer Player) {



        changeMenuMode(0,view);



        this.mediaPlayer = Player;
        if ((Player==null) || (!Player.getStatus().equals(MediaPlayer.Status.PLAYING) || Player.getCurrentTime().equals(Duration.ZERO))){
            Media sound = new Media(Paths.get("src/res/music/ost.mp3").toUri().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }


        startAnimation((Pane)view.background.getChildren().get(0));
        startAnimation((Pane)view.background.getChildren().get(1));


    }

    public void changeMenuMode(int MenuMode, MainMenuView view){

        menumode=MenuMode;


        view.row3.getChildren().clear();

        ImageView exit_btn2 = new ImageView("/res//img/back/back.png");
        HBox imageHolder6 =  new HBox();
        imageHolder6.setMinSize(600,100);
        imageHolder6.setMaxSize(600,100);
        imageHolder6.getChildren().add(exit_btn2);

        imageHolder6.setOnMouseEntered(t -> exit_btn2.setImage(new Image("/res/img/back/back_hover.png")));
        imageHolder6.setOnMouseExited(t -> exit_btn2.setImage(new Image("/res/img/back/back.png")));
        imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(menumode-1,view));
        view.row3.getChildren().add(imageHolder6);

        if(MenuMode ==0){
            if(!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();
            if(!view.row3.getChildren().isEmpty())
                view.row3.getChildren().clear();





            ImageView settings_stats = new ImageView("/res//img/settings_stats/settings_stats.png");
            HBox imageHolder2=  new HBox();
            imageHolder2.setMinSize(600,100);
            imageHolder2.setMaxSize(600,100);
            imageHolder2.getChildren().add(settings_stats);

            imageHolder2.setOnMouseEntered(t -> settings_stats.setImage(new Image("/res//img/settings_stats/settings_stats_hover.png")));
            imageHolder2.setOnMouseExited(t -> settings_stats.setImage(new Image("/res//img/settings_stats/settings_stats.png")));
            imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(-1,view));


            ImageView exit_btn = new ImageView("/res//img/exit/exit.png");
            HBox imageHolder3 =  new HBox();
            imageHolder3.setMinSize(600,100);
            imageHolder3.setMaxSize(600,100);
            imageHolder3.getChildren().add(exit_btn);

            imageHolder3.setOnMouseEntered(t -> exit_btn.setImage(new Image("/res//img/exit/exit_hover.png")));
            imageHolder3.setOnMouseExited(t -> exit_btn.setImage(new Image("/res//img/exit/exit.png")));
            imageHolder3.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->System.exit(0));


            ImageView play_btn = new ImageView("/res//img/offline/offline.png");
            play_btn.setImage(new Image("/res/img/play/play.png"));


            Pane imageHolder=  new Pane();
            imageHolder.setMinSize(600,200);
            imageHolder.setMaxSize(600,200);
            imageHolder.getChildren().add(play_btn);

            imageHolder.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(1,view));
            imageHolder.setOnMouseEntered(t -> play_btn.setImage(new Image("/res/img/play/play_hover.png")));
            imageHolder.setOnMouseExited(t -> play_btn.setImage(new Image("/res/img/play/play.png")));



            view.row2.getChildren().addAll(imageHolder);
            view.row3.getChildren().addAll(imageHolder2,imageHolder3);

        }else{




        }


        if(MenuMode ==1){
            view.row2.getChildren().clear();

            ImageView offline_btn = new ImageView("/res//img/offline/offline.png");
            HBox imageHolder=  new HBox();
            imageHolder.setMinSize(600,100);
            imageHolder.setMaxSize(600,100);
            imageHolder.getChildren().add(offline_btn);

            imageHolder.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::
                mediaPlayer.stop();

                offline_game gamemodel = new offline_game();
                TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView);


            });

            imageHolder.setOnMouseEntered(t -> offline_btn.setImage(new Image("/res//img/offline/offline_hover.png")));
            imageHolder.setOnMouseExited(t -> offline_btn.setImage(new Image("/res//img/offline/offline.png")));

            view.row2.getChildren().addAll(imageHolder);


            ImageView online_btn = new ImageView("/res//img/online/online.png");

            HBox imageHolder2=  new HBox();
            imageHolder2.setMinSize(600,100);
            imageHolder2.setMaxSize(600,100);
            imageHolder2.getChildren().add(online_btn);



            imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::

              /*  offline_game gamemodel = new offline_game();
                TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView);
*/
            });

            imageHolder2.setOnMouseEntered(t -> online_btn.setImage(new Image("/res//img/online/online_hover.png")));
            imageHolder2.setOnMouseExited(t -> online_btn.setImage(new Image("/res//img/online/online.png")));

            view.row2.getChildren().addAll(imageHolder2);


        }

        if(MenuMode ==-1) {
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

            if (!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();

            view.row3.getChildren().add(0,figures.getAllFigures(50));




            imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

                ImageView IV3 = new ImageView("/res/img/title.gif");
                IV3.setFitHeight(200);
                IV3.setFitWidth(600);
                IV3.setPreserveRatio(true);
                Pane imageHolder3= new Pane();
                imageHolder3.setMinSize(600,200);
                imageHolder3.setMaxSize(600,200);
                imageHolder3.getChildren().add(IV3);
                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder3);

                changeMenuMode(0,view);

            });

        }

        }

    private void startAnimation(Pane pane) {
        //https://www.mkyong.com/javafx/javafx-animated-ball-example/

        Random r = new Random();

        pane.setLayoutX(r.nextInt(350)+100);
        pane.setLayoutY(r.nextInt(350)+100);

        final int[] glitch_counter = {0};
        final int[] nextGlitch = {r.nextInt(350) + 30};
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

                    double dx = r.nextInt(5)+1; //Step on x or velocity
                    double dy = r.nextInt(4)+1; //Step on y


                    @Override
                    public void handle(ActionEvent t) {

                        glitch_counter[0]++;

                        ColorAdjust blackout = new ColorAdjust();


                        if (glitch_counter[0] > nextGlitch[0]){

                            pane.setLayoutX(pane.getLayoutX()+10 + dx);

                            blackout.setBrightness(2.0);

                            pane.setEffect(blackout);
                            pane.setCache(true);
                            pane.setCacheHint(CacheHint.SPEED);

                            if (glitch_counter[0] > nextGlitch[0] +5){
                                nextGlitch[0] = r.nextInt(350) + 30;
                                glitch_counter[0]=0;
                                pane.setLayoutX(pane.getLayoutX()-(r.nextInt(50)+50) + dx);
                                if(pane.getLayoutX()<-300 || pane.getLayoutX()>900 || pane.getLayoutY()<00 || pane.getLayoutY()>500) {
                                    pane.setLayoutX(r.nextInt(350) + 100);
                                    pane.setLayoutY(r.nextInt(350) + 100);

                                }
                                blackout.setBrightness(1.0);
                                pane.setEffect(null);


                            }

                        }else {

                            //move the ball
                            pane.setLayoutX(pane.getLayoutX() + dx);

                        }
                        pane.setLayoutY(pane.getLayoutY() + dy);


                        //If the ball reaches the left or right border make the step negative
                        if(pane.getLayoutX() >= (900- pane.getHeight()) ||
                                pane.getLayoutX() < (-300 ) ){
                            dx = -dx;
                        }

                        //If the ball reaches the bottom or top border make the step negative
                        if((pane.getLayoutY() >= (600- pane.getHeight()))||
                                (pane.getLayoutY() < (0) )){

                            dy = -dy;



                        }
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
