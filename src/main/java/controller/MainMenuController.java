package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import model.*;
import model.net.Client;
import model.net.Server;
import model.player.figures;
import model.player.userdata;
import view.MainMenuView;
import view.TicTacToeView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class MainMenuController {

    //0 = PLAY
    //1 = OFFLINE/ONLINE
    int menumode =0;

    MediaPlayer mediaPlayer;
    String IP_address ="";
    int PORT =14909;
    MainMenuView mView;
    Boolean stopAnimation = !userdata.isPerformanceOn();

    public MainMenuController(MainMenuModel model, MainMenuView view, MediaPlayer Player) {


        mView=view;

        changeMenuMode(userdata.getUsername().equals("")?-2:0,view);


        if(userdata.getSoundOn()) {
            this.mediaPlayer = Player;
            if ((Player == null) || (!Player.getStatus().equals(MediaPlayer.Status.PLAYING) || Player.getCurrentTime().equals(Duration.ZERO))) {
                Media sound = null;
                try {
                    sound = new Media(getClass().getResource("/music/ost.mp3").toURI().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            }

        }
        startAnimation((Pane)view.background.getChildren().get(0));
        startAnimation((Pane)view.background.getChildren().get(1));



    }

    public void changeMenuMode(int MenuMode, MainMenuView view){


        menumode=MenuMode;


        view.row3.getChildren().clear();

        ImageView exit_btn2 = new ImageView("/img/back/back.png");
        HBox imageHolder6 =  new HBox();
        imageHolder6.setMinSize(600,100);
        imageHolder6.setMaxSize(600,100);
        imageHolder6.setAlignment(Pos.CENTER);
        imageHolder6.getChildren().add(exit_btn2);

        imageHolder6.setOnMouseEntered(t -> exit_btn2.setImage(new Image("/img/back/back_hover.png")));
        imageHolder6.setOnMouseExited(t -> exit_btn2.setImage(new Image("/img/back/back.png")));
        imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(menumode-1,view));
        view.row3.getChildren().add(imageHolder6);

        if(MenuMode ==0){
            if(!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();
            if(!view.row3.getChildren().isEmpty())
                view.row3.getChildren().clear();

            ImageView settings_stats = new ImageView("/img/settings_stats/settings_stats.png");
            HBox imageHolder2=  new HBox();
            imageHolder2.setMinSize(600,100);
            imageHolder2.setMaxSize(600,100);
            imageHolder2.getChildren().add(settings_stats);

            imageHolder2.setOnMouseEntered(t -> settings_stats.setImage(new Image("/img/settings_stats/settings_stats_hover.png")));
            imageHolder2.setOnMouseExited(t -> settings_stats.setImage(new Image("/img/settings_stats/settings_stats.png")));
            imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(-1,view));


            ImageView exit_btn = new ImageView("/img/exit/exit.png");
            HBox imageHolder3 =  new HBox();
            imageHolder3.setMinSize(600,100);
            imageHolder3.setMaxSize(600,100);
            imageHolder3.getChildren().add(exit_btn);

            imageHolder3.setOnMouseEntered(t -> exit_btn.setImage(new Image("/img/exit/exit_hover.png")));
            imageHolder3.setOnMouseExited(t -> exit_btn.setImage(new Image("/img/exit/exit.png")));
            imageHolder3.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->System.exit(0));


            ImageView play_btn = new ImageView("/img/offline/offline.png");
            play_btn.setImage(new Image("/img/play/play.png"));


            VBox imageHolder=  new VBox();
            imageHolder.setMinSize(600,200);
            imageHolder.setMaxSize(600,200);
            imageHolder.setAlignment(Pos.CENTER);
            imageHolder.getChildren().add(play_btn);

            imageHolder.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> changeMenuMode(1,view));
            imageHolder.setOnMouseEntered(t -> play_btn.setImage(new Image("/img/play/play_hover.png")));
            imageHolder.setOnMouseExited(t -> play_btn.setImage(new Image("/img/play/play.png")));



            view.row2.getChildren().addAll(imageHolder);
            view.row3.getChildren().addAll(imageHolder2,imageHolder3);

        }else{




        }


        /////////
        //OFFLINE / ONLINE
        ////////



        if(MenuMode ==1){
            view.row2.getChildren().clear();

            ImageView offline_btn = new ImageView("/img/offline/offline.png");
            HBox imageHolder=  new HBox();
            imageHolder.setMinSize(600,100);
            imageHolder.setMaxSize(600,100);
            imageHolder.getChildren().add(offline_btn);

            imageHolder.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {

                changeMenuMode(3,view);

            });

            imageHolder.setOnMouseEntered(t -> offline_btn.setImage(new Image("/img/offline/offline_hover.png")));
            imageHolder.setOnMouseExited(t -> offline_btn.setImage(new Image("/img/offline/offline.png")));

            view.row2.getChildren().addAll(imageHolder);


            ImageView online_btn = new ImageView("/img/online/online.png");

            HBox imageHolder2=  new HBox();
            imageHolder2.setMinSize(600,100);
            imageHolder2.setMaxSize(600,100);
            imageHolder2.getChildren().add(online_btn);



            imageHolder2.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                changeMenuMode(4,view);


            });

            imageHolder2.setOnMouseEntered(t -> online_btn.setImage(new Image("/img/online/online_hover.png")));
            imageHolder2.setOnMouseExited(t -> online_btn.setImage(new Image("/img/online/online.png")));

            view.row2.getChildren().addAll(imageHolder2);


        }


        ////////
        // Offline
        ////////


        if(MenuMode ==3){
            view.row2.getChildren().clear();
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();


            ImageView easy_image = new ImageView("/img/easy/easy.png");

            HBox easy_imageHolder1=  new HBox();
            easy_imageHolder1.setMinSize(200,100);
            easy_imageHolder1.setMaxSize(200,100);
            easy_imageHolder1.getChildren().add(easy_image);
            easy_imageHolder1.setOnMouseEntered(t -> easy_image.setImage(new Image("/img/easy/easy_hover.png")));
            easy_imageHolder1.setOnMouseExited(t -> easy_image.setImage(new Image("/img/easy/easy.png")));

            ImageView medium_image = new ImageView("/img/medium/medium.png");

            HBox medium_imageHolder1=  new HBox();
            medium_imageHolder1.setMinSize(200,100);
            medium_imageHolder1.setMaxSize(200,100);
            medium_imageHolder1.getChildren().add(medium_image);
            medium_imageHolder1.setOnMouseEntered(t -> medium_image.setImage(new Image("/img/medium/medium_hover.png")));
            medium_imageHolder1.setOnMouseExited(t -> medium_image.setImage(new Image("/img/medium/medium.png")));


            ImageView hard_image = new ImageView("/img/HARD/hard.png");

            HBox hard_imageHolder1=  new HBox();
            hard_imageHolder1.setMinSize(200,100);
            hard_imageHolder1.setMaxSize(200,100);
            hard_imageHolder1.getChildren().add(hard_image);
            hard_imageHolder1.setOnMouseEntered(t -> hard_image.setImage(new Image("/img/HARD/hard_hover.png")));
            hard_imageHolder1.setOnMouseExited(t -> hard_image.setImage(new Image("/img/HARD/hard.png")));



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


            ImageView computer_image = new ImageView("/img/computer/computer.png");

            HBox computer_imageHolder1=  new HBox();
            computer_imageHolder1.setMinSize(300,100);
            computer_imageHolder1.setMaxSize(300,100);
            computer_imageHolder1.getChildren().add(computer_image);
            computer_imageHolder1.setOnMouseEntered(t -> computer_image.setImage(new Image("/img/computer/computer_hover.png")));
            computer_imageHolder1.setOnMouseExited(t -> computer_image.setImage(new Image("/img/computer/computer.png")));



            ImageView human_image = new ImageView("/img/human/human.png");

            HBox human_imageHolder1=  new HBox();
            human_imageHolder1.setMinSize(300,100);
            human_imageHolder1.setMaxSize(300,100);
            human_imageHolder1.getChildren().add(human_image);
            human_imageHolder1.setOnMouseEntered(t -> human_image.setImage(new Image("/img/human/human_hover.png")));
            human_imageHolder1.setOnMouseExited(t -> human_image.setImage(new Image("/img/human/human.png")));

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


            ImageView computer_image2 = new ImageView("/img/computer/computer.png");

            HBox computer_imageHolder2=  new HBox();
            computer_imageHolder2.setMinSize(300,100);
            computer_imageHolder2.setMaxSize(300,100);
            computer_imageHolder2.getChildren().add(computer_image2);
            computer_imageHolder2.setOnMouseEntered(t -> computer_image2.setImage(new Image("/img/computer/computer_hover.png")));
            computer_imageHolder2.setOnMouseExited(t -> computer_image2.setImage(new Image("/img/computer/computer.png")));



            Label lbl2 = new Label("Who starts?");
            lbl2.setTextFill(Color.WHITE);
            lbl2.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));

            ImageView human_image2 = new ImageView("/img/human/human.png");

            HBox human_imageHolder2=  new HBox();
            human_imageHolder2.setMinSize(300,100);
            human_imageHolder2.setMaxSize(300,100);
            human_imageHolder2.getChildren().add(human_image2);
            human_imageHolder2.setOnMouseEntered(t -> human_image2.setImage(new Image("/img/human/human_hover.png")));
            human_imageHolder2.setOnMouseExited(t -> human_image2.setImage(new Image("/img/human/human.png")));

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



            ImageView play = new ImageView("/img/play/play.png");
            HBox imageHolder_Play=  new HBox();
            imageHolder_Play.setMinSize(600,100);
            imageHolder_Play.setMaxSize(600,100);
            imageHolder_Play.getChildren().add(play);
            imageHolder_Play.setOnMouseEntered(t -> play.setImage(new Image("/img/play/play_hover.png")));
            imageHolder_Play.setOnMouseExited(t -> play.setImage(new Image("/img/play/play.png")));





            imageHolder_Play.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->  {
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // these two of them return the same stage
                // Swap screen::
                if(userdata.getSoundOn())
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


            backToMainMenu(view, imageHolder6,null, null);


        }



        ////////
        // online
        ///////
        if(MenuMode ==4) {

            view.row1.getChildren().clear();
            view.row2.getChildren().clear();
            view.row3.getChildren().clear();


            AtomicReference<Boolean> host = new AtomicReference<>(true);

            TextField ip_input = new TextField();
            ip_input.setFont(Font.font("ARIAL", FontWeight.BOLD, 60));
            try {
                ip_input.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ip_input.selectAll();
            ip_input.setAlignment(Pos.CENTER_RIGHT);
            ip_input.setEditable(false);
            ip_input.setMinSize(600,100);
            ip_input.setMaxSize(600,100);
            ip_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");



            TextField port_input = new TextField();
            port_input.setAlignment(Pos.CENTER_LEFT);
            port_input.setFont(Font.font("ARIAL", FontWeight.BOLD, 60));
            port_input.setPromptText("(Port)");
            port_input.setMinSize(300,100);
            port_input.setMaxSize(300,100);
            port_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");


            HBox ip_box = new HBox(ip_input,port_input);
            ip_box.setSpacing(-35);
            ip_box.setAlignment(Pos.CENTER);

            Label lbl = new Label(":");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 60));

            port_input.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!port_input.getText().equals("")) {
                        if(ip_box.getChildren().size()==2)
                        ip_box.getChildren().add(1,lbl);

                    }else{
                        ip_box.getChildren().remove(lbl);
                    }
                }
            });


            view.row2.setAlignment(Pos.CENTER);
            view.row2.getChildren().add(ip_box);
            view.row2.setMaxSize(1000,200);
            view.row2.setMinSize(1000,200);
            view.row2.requestFocus();


            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.5);

            HBox host_holder = new HBox();
            host_holder.setMinSize(300,100);
            host_holder.setMaxSize(300,100);

            ImageView host_image = new ImageView("/img/host/host.png");
            host_holder.getChildren().add(host_image);

            host_holder.setOnMouseEntered(t -> host_image.setImage(new Image("/img/host/host_hover.png")));
            host_holder.setOnMouseExited(t -> host_image.setImage(new Image("/img/host/host.png")));



            HBox client_holder = new HBox();
            client_holder.setMinSize(300,100);
            client_holder.setMaxSize(300,100);

            ImageView client_image = new ImageView("/img/client/client.png");
            client_holder.getChildren().add(client_image);

            client_holder.setOnMouseEntered(t -> client_image.setImage(new Image("/img/client/client_hover.png")));
            client_holder.setOnMouseExited(t -> client_image.setImage(new Image("/img/client/client.png")));

            client_holder.setEffect(blackout);



            Label lbl2 = new Label("Host IP address:");
            lbl2.setTextFill(Color.WHITE);
            lbl2.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));
            view.row1.setMaxSize(1000,100);


            client_holder.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                host.set(false);

                ip_input.setText("");
                ip_input.setPromptText("Host IP-Address");
                ip_input.getParent().requestFocus();
                lbl2.setText("Type in the host IP address:");
                ip_input.setEditable(true);

                client_holder.setEffect(null);
                host_holder.setEffect(blackout);

            });
            host_holder.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                host.set(true);

                try {
                    ip_input.setText(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                lbl2.setText("Host IP address:");

                ip_input.setEditable(false);

                client_holder.setEffect(blackout);
                host_holder.setEffect(null);

                //todo not working if nothing typed in in client
                ip_input.deselect();
                ip_input.selectAll();


            });
            HBox ClHO_container = new HBox(client_holder,host_holder);
            ClHO_container.setAlignment(Pos.CENTER);
            view.row1.getChildren().addAll(ClHO_container,lbl2);






            ImageView play = new ImageView("/img/play/play.png");
            HBox imageHolder_Play=  new HBox();
            imageHolder_Play.setAlignment(Pos.CENTER);
            imageHolder_Play.setMinSize(600,100);
            imageHolder_Play.setMaxSize(600,100);
            imageHolder_Play.getChildren().add(play);
            imageHolder_Play.setOnMouseEntered(t -> play.setImage(new Image("/img/play/play_hover.png")));
            imageHolder_Play.setOnMouseExited(t -> play.setImage(new Image("/img/play/play.png")));


            imageHolder_Play.setOnMouseClicked(t ->{
                if(!host.get())
                IP_address = ip_input.getText();
                String s = port_input.getText();
                PORT = (s.equals("")?PORT:Integer.valueOf(s));
                changeMenuMode(host.get()?5:6,view);

            } );

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
            backToMainMenu(view, imageHolder6,null, null);
            view.row3.getChildren().add(bottomline);
        }

        /////////
        //HOST
        ////////
        if(MenuMode ==5) {

            view.row1.getChildren().clear();
            view.row2.getChildren().clear();


            Label lbl = new Label("Waiting for other Players...");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));
            view.row1.setMaxSize(1000,200);
            view.row1.getChildren().add(lbl);


            final Boolean[] ready = {false};


            try {
                Server server = new Server(PORT,2,String.valueOf(InetAddress.getLocalHost().getHostAddress()));

                Thread server_thread = new Thread() {
                    public void run() {
                        server.connect();



                    }
                };
                server_thread.start();

                Thread lobby_thread = new Thread() {
                    public void run() {
                        try {

                            while(!ready[0]){
                                if(server.players.size()>1){
                                    ready[0] = true;
                                }
                                sleep(1000);

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Platform.runLater(() -> {
                            lbl.setText("Ready!");



                            HBox gameRow = new HBox();
                            gameRow.setMaxSize(1000,100);
                            gameRow.setMinSize(1000,100);
                            gameRow.setAlignment(Pos.CENTER);

                            Label roomName = new Label((server.players).stream().map(e -> e.getName()+" ").reduce("", String::concat));
                            roomName.setMinWidth(1000);
                            roomName.setMaxWidth(1000);
                            roomName.setTextFill(Color.WHITE);
                            roomName.setAlignment(Pos.CENTER);
                            roomName.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));


                            ImageView play_btn = new ImageView("/img/play/play.png");
                            HBox imageHolder3 =  new HBox();
                            imageHolder3.setAlignment(Pos.CENTER);

                            imageHolder3.setMinSize(600,100);
                            imageHolder3.setMaxSize(600,100);
                            imageHolder3.getChildren().add(0,play_btn);

                            imageHolder3.setOnMouseEntered(t -> play_btn.setImage(new Image("/img/play/play_hover.png")));
                            imageHolder3.setOnMouseExited(t -> play_btn.setImage(new Image("/img/play/play.png")));
                            imageHolder3.setOnMouseClicked(e->{

                                TicTacToeView gameView = new TicTacToeView((Stage)view.row1.getScene().getWindow());
                                online_game gamemodel = new online_game(gameView,server,null);
                                OnlineController game_1_controller = new OnlineController(gamemodel,gameView);


                                this.interrupt();


                            });
                            gameRow.getChildren().addAll(roomName);
                            view.row2.getChildren().add(gameRow);
                            view.row3.getChildren().add(imageHolder3);

                        });
                    }

                };

                lobby_thread.start();

                    backToMainMenu(view, imageHolder6, server_thread,server);



            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        /////////
        ///CLIENT
        ////////
        if(MenuMode ==6) {

            view.row1.getChildren().clear();
            view.row2.getChildren().clear();



            Label lbl = new Label("Searching for hosts...");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));
            view.row1.setMaxSize(1000,200);
            view.row1.getChildren().add(lbl);

            final Client[] client = {null};


            Thread lobby_thread = new Thread() {
                public void run() {
                    try {
                        String available = "1";
                        String[] s = null;
                        while (available.equals("1")) {
                            Thread.sleep(1000);

                            available="0";
                            s = client[0].communication("check", "");

                            available = s==null?"0":s[0];
                            if(s!=null &&s[1]!=null &&s[1].equals("ready")){



                               // this.interrupt();

                                break;
                            }

                        }

                        String[] finalS = s;
                        Platform.runLater(() -> {

                            if(!(finalS==null?"0":finalS[0]).equals("1")) {
                                ((Pane) ((Pane) view.row2.getParent()).getChildren().get(0)).getChildren().clear();
                                ImageView IV3 = new ImageView("/img/title.gif");
                                IV3.setFitHeight(200);
                                IV3.setFitWidth(600);
                                IV3.setPreserveRatio(true);
                                Pane imageHolder3 = new Pane();
                                imageHolder3.setMinSize(600, 200);
                                imageHolder3.setMaxSize(600, 200);
                                imageHolder3.getChildren().add(IV3);
                                ((Pane) ((Pane) view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder3);

                                changeMenuMode(0, view);

                                HBox dialog = DialogCreator.vanillaDialog("Host closed connection", "",true);
                                view.mainPane.getChildren().add(dialog);

                                this.interrupt();
                            }else{
                                TicTacToeView gameView = new TicTacToeView((Stage)view.row1.getScene().getWindow());
                                online_game gamemodel = new online_game(gameView,client[0],Boolean.valueOf(finalS[2]));
                                OnlineController game_1_controller = new OnlineController(gamemodel, gameView);
                                this.interrupt();


                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            };




            Thread client_thread = new Thread() {
                public void run() {
                    client[0] = new Client(IP_address,PORT);

                    try {

                    while(client[0].socket==null) {
                           Thread.sleep(1000);

                       client[0] = new Client(IP_address,PORT);

                   }
                   String[] s=null;
                         s = client[0].communication("status","");
                         System.out.println(s);


                    String[] finalS = s;
                    Platform.runLater(() -> {
                        lbl.setText("Select a game:");



                        HBox gameRow = new HBox();
                        gameRow.setMaxSize(1000,100);
                        gameRow.setMinSize(1000,100);
                        gameRow.setAlignment(Pos.CENTER);

                        Label roomFillment = new Label(finalS[0]);
                        roomFillment.setTextFill(Color.WHITE);
                        roomFillment.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));
                        roomFillment.setMinWidth(100);
                        roomFillment.setMaxWidth(100);

                        Label roomName = new Label("Game of "+finalS[1]);
                        roomName.setMinWidth(600);
                        roomName.setMaxWidth(600);
                        roomName.setTextFill(Color.WHITE);
                        roomName.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));


                        ImageView join_btn = new ImageView("/img/join/join.png");
                        HBox imageHolder3 =  new HBox();
                        imageHolder3.setAlignment(Pos.CENTER);

                        imageHolder3.setMinSize(300,100);
                        imageHolder3.setMaxSize(300,100);
                        imageHolder3.getChildren().add(join_btn);

                        imageHolder3.setOnMouseEntered(t -> join_btn.setImage(new Image("/img/join/join_hover.png")));
                        imageHolder3.setOnMouseExited(t -> join_btn.setImage(new Image("/img/join/join.png")));
                        imageHolder3.setOnMouseClicked(e->{

                            String[] k =null;

                            try {
                                k = client[0].communication("join",userdata.getUsername() + "," + userdata.get_selected_figure());
                            } catch (IOException | ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            System.out.println(k);


                            view.row2.getChildren().clear();
                            lbl.setText("Waiting for game to start...");
                            lobby_thread.start();

                        });
                        gameRow.getChildren().addAll(roomFillment,roomName,imageHolder3);
                        view.row2.getChildren().add(gameRow);




                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.err.println("Sorry for that^^");
                    }


                }
            };
            client_thread.start();





            backToMainMenu(view, imageHolder6, client_thread, null);


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
            name_input.setMinSize(600,100);
            name_input.setMaxSize(600,100);
            name_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");

            view.row1.getChildren().add(name_input);
            name_input.requestFocus();


            Label lbl = new Label("Games played: " + userdata.getPlayedGames()+"   "+"K/D: "+((float)userdata.getWins()/(userdata.getLoses()==0?1:userdata.getLoses())));
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 50));
            view.row1.getChildren().add(lbl);
            view.row1.setMaxSize(1000,200);



            ImageView performance_btn = new ImageView("/img/performance_mode/performance_mode_"+userdata.isPerformanceOnString()+".png");
            HBox imageHolder5 =  new HBox();
            imageHolder5.setAlignment(Pos.CENTER);

            imageHolder5.setMinSize(600,100);
            imageHolder5.setMaxSize(600,100);
            imageHolder5.getChildren().add(performance_btn);

            imageHolder5.setOnMouseEntered(t -> performance_btn.setImage(new Image("/img/performance_mode/performance_mode_"+userdata.isPerformanceOnString()+"_hover.png")));
            imageHolder5.setOnMouseExited(t -> performance_btn.setImage(new Image("/img/performance_mode/performance_mode_"+userdata.isPerformanceOnString()+".png")));

            imageHolder5.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                setPerformance(userdata.isPerformanceOn());
                userdata.setPerformanceOn(!userdata.isPerformanceOn());
                performance_btn.setImage(new Image("/img/performance_mode/performance_mode_"+userdata.isPerformanceOnString()+".png"));


            });
            view.row2.getChildren().add(imageHolder5);


            ImageView sound_btn = new ImageView("/img/sound/sound_"+userdata.isSoundOn()+".png");
            HBox imageHolder4 =  new HBox();
            imageHolder4.setAlignment(Pos.CENTER);

            imageHolder4.setMinSize(600,100);
            imageHolder4.setMaxSize(600,100);
            imageHolder4.getChildren().add(sound_btn);

            imageHolder4.setOnMouseEntered(t -> sound_btn.setImage(new Image("/img/sound/sound_"+userdata.isSoundOn()+"_hover.png")));
            imageHolder4.setOnMouseExited(t -> sound_btn.setImage(new Image("/img/sound/sound_"+userdata.isSoundOn()+".png")));

            imageHolder4.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                setSound(userdata.getSoundOn());
                userdata.setSoundOn(!userdata.getSoundOn());
                sound_btn.setImage(new Image("/img/sound/sound_"+userdata.isSoundOn()+".png"));


            });
            view.row2.getChildren().add(0,imageHolder4);

            view.row3.getChildren().add(0,figures.getAllFigures(50,view.mainPane));






            imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

                userdata.setUsername(name_input.getText());

                ImageView IV3 = new ImageView("/img/title.gif");
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

        if(MenuMode ==-2) {
            ///What's your name?
            ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

            if (!view.row2.getChildren().isEmpty())
                view.row2.getChildren().clear();

            if(!view.row3.getChildren().isEmpty())
                view.row3.getChildren().clear();

            Label lbl = new Label("What's your name stranger?");
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));
            view.row1.getChildren().add(0,lbl);

            ImageView exit_btn = new ImageView("/img/play/play.png");
            HBox imageHolder3 =  new HBox();
            imageHolder3.setAlignment(Pos.CENTER);

            imageHolder3.setMinSize(600,100);
            imageHolder3.setMaxSize(600,100);
            imageHolder3.getChildren().add(exit_btn);

            imageHolder3.setOnMouseEntered(t -> exit_btn.setImage(new Image("/img/play/play_hover.png")));
            imageHolder3.setOnMouseExited(t -> exit_btn.setImage(new Image("/img/play/play.png")));


            view.row3.getChildren().addAll(imageHolder3);





            TextField name_input = new TextField();
            name_input.setAlignment(Pos.CENTER);
            name_input.setFont(Font.font("ARIAL", FontWeight.BOLD, 70));
            name_input.setMinSize(600,200);
            name_input.setMaxSize(600,200);
            name_input.setStyle("-fx-background-color: rgba(0,0,0,0.0);" +
                    "    -fx-text-fill: #FFFFFF;");

            view.row2.getChildren().add(0,name_input);
            name_input.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    setUserName(name_input, view);
                }
            });

            imageHolder3.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {

                setUserName(name_input, view);

            });

        }


        }

    private void setPerformance(boolean performanceOn) {
        if(performanceOn){
            stopAnimation=true;
        }else{
            stopAnimation=false;
        }
    }

    private void setSound(boolean soundOn) {
        if(soundOn) {
            if(mediaPlayer!=null) {
                mediaPlayer.setMute(true);
                mediaPlayer.stop();
                mediaPlayer = null;
            }
        }
        else {
            Media sound = null;
            try {
                sound = new Media(getClass().getResource("/music/ost.mp3").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }
    }

    private void setUserName(TextField name_input, MainMenuView view) {
        userdata.setUsername(name_input.getText());

        ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();

        ImageView IV3 = new ImageView("/img/title.gif");
        IV3.setFitHeight(200);
        IV3.setFitWidth(600);
        IV3.setPreserveRatio(true);
        Pane imageHolder = new Pane();
        imageHolder.setMinSize(600,200);
        imageHolder.setMaxSize(600,200);
        imageHolder.getChildren().add(IV3);
        ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder);

        changeMenuMode(0,view);
    }

    private void backToMainMenu(MainMenuView view, HBox imageHolder5, Object o, Server server) {

        if(o !=null) {



            view.row3.getChildren().remove(imageHolder5);

            ImageView exit_btn2 = new ImageView("/img/back/back.png");
            HBox imageHolder6 = new HBox();
            imageHolder6.setMinSize(600, 100);
            imageHolder6.setMaxSize(600, 100);
            imageHolder6.setAlignment(Pos.CENTER);
            imageHolder6.getChildren().add(exit_btn2);

            imageHolder6.setOnMouseEntered(t -> exit_btn2.setImage(new Image("/img/back/back_hover.png")));
            imageHolder6.setOnMouseExited(t -> exit_btn2.setImage(new Image("/img/back/back.png")));

            imageHolder6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                if (server != null) {
                    server.stop();
                }


                ((Thread) o).interrupt();


                ((Pane) ((Pane) view.row2.getParent()).getChildren().get(0)).getChildren().clear();
                ImageView IV3 = new ImageView("/img/title.gif");
                IV3.setFitHeight(200);
                IV3.setFitWidth(600);
                IV3.setPreserveRatio(true);
                Pane imageHolder3 = new Pane();
                imageHolder3.setMinSize(600, 200);
                imageHolder3.setMaxSize(600, 200);
                imageHolder3.getChildren().add(IV3);
                ((Pane) ((Pane) view.row2.getParent()).getChildren().get(0)).getChildren().add(imageHolder3);

                changeMenuMode(0, view);

            });
            view.row3.getChildren().add(imageHolder6);
        }
        else{

            imageHolder5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ((Pane)((Pane)view.row2.getParent()).getChildren().get(0)).getChildren().clear();
                ImageView IV3 = new ImageView("/img/title.gif");
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

        pane.setCache(true);
        pane.setCacheShape(true);
        pane.setCacheHint(CacheHint.SPEED);
        //https:/www.mkyong.com/javafx/javafx-animated-ball-example/

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
                    double dy = r.nextInt(5)+1; //Step on y


                    @Override
                    public void handle(ActionEvent t) {
                        if(stopAnimation) {

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
                                if(pane.getLayoutX()<-330 || pane.getLayoutX()>900 || pane.getLayoutY()<-20 || pane.getLayoutY()>400) {
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
                        if(pane.getLayoutX() >= (920- pane.getHeight()) ||
                                pane.getLayoutX() < (-320 ) ){
                            dx = -dx;
                        }

                        //If the ball reaches the bottom or top border make the step negative
                        if((pane.getLayoutY() >= (620- pane.getHeight()))||
                                (pane.getLayoutY() < (-20) )){

                            dy = -dy;



                        }
                        }
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
