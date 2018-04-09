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
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MainMenuModel;
import model.offline_game;
import model.player.figures;
import model.player.userdata;
import view.MainMenuView;
import view.TicTacToeView;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Random;

public class MainMenuController {

    //0 = PLAY
    //1 = OFFLINE/ONLINE
    int menumode =0;

    MediaPlayer mediaPlayer;

    public MainMenuController(MainMenuModel model, MainMenuView view, MediaPlayer Player) {



        changeMenuMode(userdata.getUsername().equals("")?-2:0,view);



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
        imageHolder6.setAlignment(Pos.CENTER);
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


            VBox imageHolder=  new VBox();
            imageHolder.setMinSize(600,200);
            imageHolder.setMaxSize(600,200);
            imageHolder.setAlignment(Pos.CENTER);
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

                changeMenuMode(3,view);
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
            //Settings/stats
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

            if (!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();


            TextField name_input = new TextField();
            name_input.setAlignment(Pos.CENTER);
            name_input.setFont(Font.font("ARIAL", FontWeight.BOLD, 70));
            name_input.setText(userdata.getUsername());
            name_input.setMinSize(600,200);
            name_input.setMaxSize(600,200);
            name_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");

            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(0,name_input);



            view.row3.getChildren().add(0,figures.getAllFigures(50));





            imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

                userdata.setUsername(name_input.getText());

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


        if(MenuMode ==3){
            view.row2.getChildren().clear();
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();


            ImageView easy_image = new ImageView("/res//img/easy/easy.png");

            HBox easy_imageHolder1=  new HBox();
            easy_imageHolder1.setMinSize(200,100);
            easy_imageHolder1.setMaxSize(200,100);
            easy_imageHolder1.getChildren().add(easy_image);
            easy_imageHolder1.setOnMouseEntered(t -> easy_image.setImage(new Image("/res//img/easy/easy_hover.png")));
            easy_imageHolder1.setOnMouseExited(t -> easy_image.setImage(new Image("/res//img/easy/easy.png")));

            ImageView medium_image = new ImageView("/res//img/medium/medium.png");

            HBox medium_imageHolder1=  new HBox();
            medium_imageHolder1.setMinSize(200,100);
            medium_imageHolder1.setMaxSize(200,100);
            medium_imageHolder1.getChildren().add(medium_image);
            medium_imageHolder1.setOnMouseEntered(t -> medium_image.setImage(new Image("/res//img/medium/medium_hover.png")));
            medium_imageHolder1.setOnMouseExited(t -> medium_image.setImage(new Image("/res//img/medium/medium.png")));


            ImageView hard_image = new ImageView("/res//img/hard/hard.png");

            HBox hard_imageHolder1=  new HBox();
            hard_imageHolder1.setMinSize(200,100);
            hard_imageHolder1.setMaxSize(200,100);
            hard_imageHolder1.getChildren().add(hard_image);
            hard_imageHolder1.setOnMouseEntered(t -> hard_image.setImage(new Image("/res//img/hard/hard_hover.png")));
            hard_imageHolder1.setOnMouseExited(t -> hard_image.setImage(new Image("/res//img/hard/hard.png")));



            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.5);


            final int[] hardness = {1};
            medium_imageHolder1.setEffect(null);
            easy_imageHolder1.setEffect(blackout);
            hard_imageHolder1.setEffect(blackout);
            hardness[0] =1;

            easy_imageHolder1.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                easy_imageHolder1.setEffect(null);
                medium_imageHolder1.setEffect(blackout);
                hard_imageHolder1.setEffect(blackout);
                hardness[0] =0;
            });
            medium_imageHolder1.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                medium_imageHolder1.setEffect(null);
                easy_imageHolder1.setEffect(blackout);
                hard_imageHolder1.setEffect(blackout);
                hardness[0] =1;

            });
            hard_imageHolder1.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                hard_imageHolder1.setEffect(null);
                easy_imageHolder1.setEffect(blackout);
                medium_imageHolder1.setEffect(blackout);
                hardness[0] =2;

            });


            HBox imageHolder1=  new HBox();
            imageHolder1.setMinSize(600,100);
            imageHolder1.setMaxSize(600,100);
            imageHolder1.getChildren().addAll(easy_imageHolder1,medium_imageHolder1,hard_imageHolder1);


            ImageView computer_image = new ImageView("/res//img/computer/computer.png");

            HBox computer_imageHolder1=  new HBox();
            computer_imageHolder1.setMinSize(300,100);
            computer_imageHolder1.setMaxSize(300,100);
            computer_imageHolder1.getChildren().add(computer_image);
            computer_imageHolder1.setOnMouseEntered(t -> computer_image.setImage(new Image("/res//img/computer/computer_hover.png")));
            computer_imageHolder1.setOnMouseExited(t -> computer_image.setImage(new Image("/res//img/computer/computer.png")));



            ImageView human_image = new ImageView("/res//img/human/human.png");

            HBox human_imageHolder1=  new HBox();
            human_imageHolder1.setMinSize(300,100);
            human_imageHolder1.setMaxSize(300,100);
            human_imageHolder1.getChildren().add(human_image);
            human_imageHolder1.setOnMouseEntered(t -> human_image.setImage(new Image("/res//img/human/human_hover.png")));
            human_imageHolder1.setOnMouseExited(t -> human_image.setImage(new Image("/res//img/human/human.png")));

            HBox imageHolder=  new HBox();
            imageHolder.setMinSize(600,100);
            imageHolder.setMaxSize(600,100);
            imageHolder.getChildren().addAll(computer_imageHolder1,human_imageHolder1);

            final int[] AI_HUMAN = {1};
            final int[] whostarts = {0};
            computer_imageHolder1.setEffect(blackout);
            human_imageHolder1.setEffect(null);

            Label lbl = new Label("Play against:");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));

            view.row1.getChildren().add(0,lbl);




            view.row1.getChildren().addAll(imageHolder);


            ImageView computer_image2 = new ImageView("/res//img/computer/computer.png");

            HBox computer_imageHolder2=  new HBox();
            computer_imageHolder2.setMinSize(300,100);
            computer_imageHolder2.setMaxSize(300,100);
            computer_imageHolder2.getChildren().add(computer_image2);
            computer_imageHolder2.setOnMouseEntered(t -> computer_image2.setImage(new Image("/res//img/computer/computer_hover.png")));
            computer_imageHolder2.setOnMouseExited(t -> computer_image2.setImage(new Image("/res//img/computer/computer.png")));



            Label lbl2 = new Label("Who starts?");
            lbl2.setTextFill(Color.WHITE);
            lbl2.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));

            ImageView human_image2 = new ImageView("/res//img/human/human.png");

            HBox human_imageHolder2=  new HBox();
            human_imageHolder2.setMinSize(300,100);
            human_imageHolder2.setMaxSize(300,100);
            human_imageHolder2.getChildren().add(human_image2);
            human_imageHolder2.setOnMouseEntered(t -> human_image2.setImage(new Image("/res//img/human/human_hover.png")));
            human_imageHolder2.setOnMouseExited(t -> human_image2.setImage(new Image("/res//img/human/human.png")));

            HBox imageHolder2=  new HBox();
            imageHolder2.setMinSize(600,100);
            imageHolder2.setMaxSize(600,100);
            imageHolder2.getChildren().addAll(computer_imageHolder2,human_imageHolder2);
            view.row3.getChildren().clear();


            computer_imageHolder2.setEffect(null);
            human_imageHolder2.setEffect(blackout);

            computer_imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                whostarts[0] = 0;
                computer_imageHolder2.setEffect(null);
                human_imageHolder2.setEffect(blackout);
                    });
            human_imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                whostarts[0] = 1;
                computer_imageHolder2.setEffect(blackout);
                human_imageHolder2.setEffect(null);

            });




                computer_imageHolder1.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                computer_imageHolder1.setEffect(null);
                human_imageHolder1.setEffect(blackout);
                AI_HUMAN[0] =0;
                if(view.row2.getChildren().size()==0) {
                    view.row2.getChildren().add(lbl2);
                    view.row3.getChildren().add(0,imageHolder2);
                    view.row2.getChildren().add(0, imageHolder1);

                }


            });

            human_imageHolder1.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                if((view.row2.getChildren().size()!=0 &&view.row2.getChildren().get(0)!=null)) {

                    view.row2.getChildren().clear();
                    //view.row2.getChildren().remove(1);
                    view.row3.getChildren().remove(0);

                }
                computer_imageHolder1.setEffect(blackout);
                human_imageHolder1.setEffect(null);
                AI_HUMAN[0] =1;
            });



            ImageView play = new ImageView("/res//img/play/play.png");
            HBox imageHolder_Play=  new HBox();
            imageHolder_Play.setMinSize(600,100);
            imageHolder_Play.setMaxSize(600,100);
            imageHolder_Play.getChildren().add(play);
            imageHolder_Play.setOnMouseEntered(t -> play.setImage(new Image("/res//img/play/play_hover.png")));
            imageHolder_Play.setOnMouseExited(t -> play.setImage(new Image("/res//img/play/play.png")));





            imageHolder_Play.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::
                mediaPlayer.stop();

                offline_game gamemodel = new offline_game();
                TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView,hardness[0],AI_HUMAN[0],whostarts[0]);

            });

            HBox bottomline = new HBox();
            bottomline.setMinSize(600,100);
            bottomline.setMaxSize(600,100);
            bottomline.setSpacing(-200);
            bottomline.setAlignment(Pos.CENTER);
            imageHolder6.setAlignment(Pos.CENTER);
            imageHolder_Play.setAlignment(Pos.CENTER);

            ((ImageView)imageHolder6.getChildren().get(0)).setFitWidth(300);
            ((ImageView)imageHolder_Play.getChildren().get(0)).setFitWidth(300);

            bottomline.getChildren().addAll(imageHolder6,imageHolder_Play);
            view.row3.getChildren().add(bottomline);


            imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

                ImageView IV3 = new ImageView("/res/img/title.gif");
                IV3.setFitHeight(200);
                IV3.setFitWidth(600);
                IV3.setPreserveRatio(true);
                Pane imageHolder3 = new Pane();
                imageHolder3.setMinSize(600,200);
                imageHolder3.setMaxSize(600,200);
                imageHolder3.getChildren().add(IV3);
                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder3);

                changeMenuMode(0,view);

            });


        }


        if(MenuMode ==-2) {
            //What's your name?
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

            if (!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();

            if(!view.row3.getChildren().isEmpty())
                view.row3.getChildren().clear();

            Label lbl = new Label("What's your name stranger?");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(0,lbl);

            ImageView exit_btn = new ImageView("/res//img/play/play.png");
            HBox imageHolder3 =  new HBox();
            imageHolder3.setMinSize(600,100);
            imageHolder3.setMaxSize(600,100);
            imageHolder3.getChildren().add(exit_btn);

            imageHolder3.setOnMouseEntered(t -> exit_btn.setImage(new Image("/res//img/play/play_hover.png")));
            imageHolder3.setOnMouseExited(t -> exit_btn.setImage(new Image("/res//img/play/play.png")));


            view.row3.getChildren().addAll(imageHolder3);





            TextField name_input = new TextField();
            name_input.setAlignment(Pos.CENTER);
            name_input.setFont(Font.font("ARIAL", FontWeight.BOLD, 70));
            name_input.setMinSize(600,200);
            name_input.setMaxSize(600,200);
            name_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");

            view.row2.getChildren().add(0,name_input);

            imageHolder3.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                userdata.setUsername(name_input.getText());

                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

                ImageView IV3 = new ImageView("/res/img/title.gif");
                IV3.setFitHeight(200);
                IV3.setFitWidth(600);
                IV3.setPreserveRatio(true);
                Pane imageHolder = new Pane();
                imageHolder.setMinSize(600,200);
                imageHolder.setMaxSize(600,200);
                imageHolder.getChildren().add(IV3);
                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder);

                changeMenuMode(0,view);

            });

        }


        }

    private void startAnimation(Pane pane) {
        //https://www.mkyong.com/javafx/javafx-animated-ball-example/

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Random r = new Random(System.currentTimeMillis());

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
