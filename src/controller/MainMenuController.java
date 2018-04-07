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
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MainMenuModel;
import model.offline_game;
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

        view.exit_btn.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            if(menumode==0)System.exit(0); else menumode--; changeMenuMode(menumode,view);

        });


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

        view.exit_btn.setImage(new Image("/res//img/back/back.png"));
        view.exit_btn.setOnMouseEntered(t -> view.exit_btn.setImage(new Image("/res/img/back/back_hover.png")));
        view.exit_btn.setOnMouseExited(t -> view.exit_btn.setImage(new Image("/res/img/back/back.png")));



        if(MenuMode ==0){
            if(!view.row2.getChildren().isEmpty())
            view.row2.getChildren().clear();

            view.exit_btn.setImage(new Image("/res//img/exit/exit.png"));
            view.exit_btn.setOnMouseEntered(t -> view.exit_btn.setImage(new Image("/res/img/exit/exit_hover.png")));
            view.exit_btn.setOnMouseExited(t -> view.exit_btn.setImage(new Image("/res/img/exit/exit.png")));

            ImageView play_btn = new ImageView("/res//img/offline/offline.png");
            play_btn.setImage(new Image("/res/img/play/play.png"));
            play_btn.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(1,view));
            play_btn.setOnMouseEntered(t -> play_btn.setImage(new Image("/res/img/play/play_hover.png")));
            play_btn.setOnMouseExited(t -> play_btn.setImage(new Image("/res/img/play/play.png")));
            view.row2.getChildren().addAll(play_btn);

        }


        if(MenuMode ==1){
            view.row2.getChildren().remove(0);

            ImageView offline_btn = new ImageView("/res//img/offline/offline.png");
            offline_btn.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::

                offline_game gamemodel = new offline_game();
                TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView);
                mediaPlayer.stop();


            });

            offline_btn.setOnMouseEntered(t -> offline_btn.setImage(new Image("/res//img/offline/offline_hover.png")));
            offline_btn.setOnMouseExited(t -> offline_btn.setImage(new Image("/res//img/offline/offline.png")));

            view.row2.getChildren().addAll(offline_btn);


            ImageView online_btn = new ImageView("/res//img/online/online.png");
            online_btn.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::

              /*  offline_game gamemodel = new offline_game();
                TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView);
*/
            });

            online_btn.setOnMouseEntered(t -> online_btn.setImage(new Image("/res//img/online/online_hover.png")));
            online_btn.setOnMouseExited(t -> online_btn.setImage(new Image("/res//img/online/online.png")));

            view.row2.getChildren().addAll(online_btn);


        }


        }

    private void startAnimation(Pane pane) {
        //https://www.mkyong.com/javafx/javafx-animated-ball-example/

        Random r = new Random();

        pane.setLayoutX(r.nextInt(350)+100);
        pane.setLayoutY(r.nextInt(350)+100);

        final int[] glitch_counter = {0};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

                    double dx = r.nextInt(5)+1; //Step on x or velocity
                    double dy = r.nextInt(4)+1; //Step on y


                    @Override
                    public void handle(ActionEvent t) {

                        glitch_counter[0]++;

                        ColorAdjust blackout = new ColorAdjust();

                        if (glitch_counter[0] >100){

                            pane.setLayoutX(pane.getLayoutX()+10 + dx);

                            blackout.setBrightness(2.0);

                            pane.setEffect(blackout);
                            pane.setCache(true);
                            pane.setCacheHint(CacheHint.SPEED);

                            if (glitch_counter[0] >105){
                                glitch_counter[0]=0;
                                pane.setLayoutX(pane.getLayoutX()-(r.nextInt(50)+50) + dx);
                                if(pane.getLayoutX()<600 || pane.getLayoutX()>1200 || pane.getLayoutY()<700 || pane.getLayoutY()>1100)
                                    pane.setLayoutX(r.nextInt(350)+100);
                                blackout.setBrightness(1.0);
                                pane.setEffect(null);


                            }

                        }else {

                            //move the ball
                            pane.setLayoutX(pane.getLayoutX() + dx);

                        }
                        pane.setLayoutY(pane.getLayoutY() + dy);


                        //If the ball reaches the left or right border make the step negative
                        if(pane.getLayoutX() >= (600- pane.getHeight()) ||
                                pane.getLayoutX() < (0 ) ){
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
