package model;

import controller.MainMenuController;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import view.MainMenuView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import static model.player.userdata.loadUser;

public class Intro {


    MainMenuModel model;
    //TicTacToeView view;
    MainMenuView view;
    MainMenuController controller;
    public MediaPlayer mediaPlayer;
    public Thread one;



    public Intro(Stage primaryStage){



        //LOAD THE DATA
        loadUser();


        // OST: https://www.youtube.com/watch?v=Zuw_O5MU5CE
        one = new Thread() {
            public void run() {
                int i = 0;
                while(i <18){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                Platform.runLater(() -> {




                    model = new MainMenuModel();
                    view = new MainMenuView(primaryStage);
                    controller = new MainMenuController(model, view,mediaPlayer);

                });
            }
        };


        Thread music = new Thread() {
            public void run() {

                Platform.runLater(() -> {

                    Media sound = null;


                    try {
                        sound = new Media(getClass().getResource("/music/ost.mp3").toURI().toString());
                    } catch (NullPointerException | MediaException e){
                        sound = new Media(Paths.get("/music/ost.mp3").toUri().toString());


                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.play();


                });
            }
        };

        music.start();
        one.start();



    }




    }




