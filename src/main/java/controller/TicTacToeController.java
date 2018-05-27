package controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import model.player.figures_name;
import model.player.userdata;
import view.MainMenuView;
import view.TicTacToeView;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;

public class TicTacToeController {
    offline_game model;
    TicTacToeView view;
    private int AIPlayerNr = 1;
    Color AI_color;
    Color Player_color;

    Image AI_image;
    Image Player_Image;

    int hardness    ;
    int AI_HUMAN    ;
    int Whostarts   ;

    public TicTacToeController(offline_game model, TicTacToeView view, int hardness, int AI_Human, int Whostarts) {

        this.hardness =hardness ;
        this.AI_HUMAN =AI_Human ;
        this.Whostarts=Whostarts;

        this.model = model;
        this.view = view;

        userdata.setPlayedGames();


        switch (hardness){
            case 0:             model.AI.depthLimiter = 1;
                break;
            case 1:             model.AI.depthLimiter = 2;
                break;
            case 2:             model.AI.depthLimiter = 10;
                break;
        }
        Boolean againstComputer = false;



        //Set the AI Image
        Random r = new Random();
        figures_name fn = figures_name.values()[r.nextInt(figures_name.values().length)];
        while (userdata.get_selected_figure() == fn)
            fn = figures_name.values()[r.nextInt(figures_name.values().length)];
        AI_image       = new Image("/img/" + fn + "/" + fn + ".png");
        Player_Image   = new Image("/img/" + userdata.get_selected_figure() + "/" + userdata.get_selected_figure() + ".png");


        //setting the color for the icons
        String[] neon_colors = {
                "#000cff",
                "#2fa7e8",
                "#e1e832",
                "#41e831",
                "#30e8d2"};
        Player_color = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        AI_color = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        while(AI_color.equals(Player_color))
            AI_color = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);



        if (AI_Human == 0) {
            // ... user chose OK
            againstComputer = true;


            if (Whostarts == 0) {

                int p = model.AiTurn(AIPlayerNr);
                model.board.populateBoard(p,AIPlayerNr);
                gameMethods.setImage((ImageView) view.board[p].getChildren().get(0), AI_image, AI_color);
                gameMethods.animateMoves(view.board[p]);
                setChatMessage("The Computer has played in field ( "+ ((p+1)%3f==0?3:((p+1)%3f==2?2:1))+", "+(int)Math.ceil((p+1)/3f)+")");



            } else {
                // ... user chose CANCEL or closed the dialog
                AIPlayerNr = 2;

            }

        }




        final Boolean[] turn = {true};


        for (int i = 0; i < view.board.length; i++) {
            int a = i;
            int finalAIPlayerNr = AIPlayerNr;
            Boolean finalAgainstComputer = againstComputer;
            view.board[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                //Check if it's a possible move
                if (model.board.PlaceIsEmpty(a) && !model.board.isLocked()) {

                    //Play against computer
                    if (finalAgainstComputer) {


                        //populate the gameboard
                        model.board.populateBoard(a, finalAIPlayerNr == 2 ? 1 : 2);

                        //set the image
                        gameMethods.setImage((ImageView) view.board[a].getChildren().get(0),Player_Image, Player_color);


                        gameMethods.animateMoves(view.board[a]).setOnFinished(event1 -> {


                            //check for winner
                            if (model.board.getWinner() != 0) {
                                if (model.board.getWinner() != 3) {
                                    setChatMessage("You win with your move in field: ( " + ((a + 1) % 3f == 0 ? 3 : ((a + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((a + 1) / 3f) + ") congrats!");
                                    userdata.setWinGames();
                                }else{
                                    setChatMessage("It's a tie!");
                                }
                                setWinner(model.board.getWinner());
                                gameMethods.setWinnerStroke(model.board,view);
                                return;
                            }else{
                                setChatMessage("You have played in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");

                            }

                            int p = model.AiTurn(finalAIPlayerNr);
                            model.board.populateBoard(p,finalAIPlayerNr);

                            gameMethods.setImage((ImageView) view.board[p].getChildren().get(0),AI_image, AI_color);

                            gameMethods.animateMoves(view.board[p]);

                            System.out.println(model.board.toString());

                            //check for winner
                            if (model.board.getWinner() != 0) {
                                setWinner(model.board.getWinner());
                                userdata.setLoseGames();

                                gameMethods.setWinnerStroke(model.board,view);
                                if (model.board.getWinner() != 3) {

                                    setChatMessage("The Computer wins with his move in field: ( " + ((p + 1) % 3f == 0 ? 3 : ((p + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((p + 1) / 3f) + ")");
                                }else{
                                    setChatMessage("It's a tie!");
                                }
                                return;
                            }else{
                                setChatMessage("The Computer has played in field ( "+ ((p+1)%3f==0?3:((p+1)%3f==2?2:1))+", "+(int)Math.ceil((p+1)/3f)+")");

                            }
                        });

                    } else {
                        //Play against human
                        if (turn[0]) {
                            turn[0] = false;
                            //populate the gameboard
                            model.board.populateBoard(a, 1);

                            //set the image
                            gameMethods.setImage((ImageView) view.board[a].getChildren().get(0),Player_Image, Player_color);

                            gameMethods.animateMoves(view.board[a]);
                            setChatMessage("Player 1 has played in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");

                            //check for winner
                            if (model.board.getWinner() != 0) {
                                if (model.board.getWinner() != 3) {

                                    setChatMessage("Player 1 wins with his move in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");

                                }else{
                                    setChatMessage("It's a tie!");
                                }
                                setWinner(model.board.getWinner());
                                gameMethods.setWinnerStroke(model.board,view);
                                return;
                            }
                        } else {
                            turn[0] = true;
                            //populate the gameboard
                            model.board.populateBoard(a, 2);

                            //set the image
                            gameMethods.setImage((ImageView) view.board[a].getChildren().get(0),AI_image, AI_color);
                            gameMethods.animateMoves(view.board[a]);
                            setChatMessage("Player 2 has played in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");
                            //check for winner
                            if (model.board.getWinner() != 0) {
                                if (model.board.getWinner() != 3) {

                                    setChatMessage("Player 1 wins with his move in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");

                                }else{
                                    setChatMessage("It's a tie!");
                                }
                                setWinner(model.board.getWinner());
                                gameMethods.setWinnerStroke(model.board,view);
                                return;
                            }
                        }


                    }


                }else{
                    HBox pane = new HBox();
                    pane.setMinSize(600, 600);
                    pane.setMaxSize(600, 600);
                    pane.setAlignment(Pos.CENTER);

                    ImageView IV = new ImageView();
                    IV.setFitHeight(300);
                    IV.setFitWidth(300);
                    IV.setPreserveRatio(true);
                    IV.setImage(new Image("/img/invalid.png"));
                    pane.getChildren().add(IV);

                    view.gamePane.getChildren().add(pane);


                    FadeTransition ft1 = new FadeTransition(Duration.millis(400), pane);
                    ft1.setFromValue(0.0);
                    ft1.setToValue(1.0);

                    FadeTransition ft2 = new FadeTransition(Duration.millis(400), pane);
                    ft2.setFromValue(1.0);
                    ft2.setToValue(0.0);


                    SequentialTransition st = new SequentialTransition();
                    st.getChildren().addAll(ft1, ft2);

                    ScaleTransition str = new ScaleTransition(Duration.millis(800), pane);
                    str.setByX(1.1f);
                    str.setByY(1.1f);
                    str.setAutoReverse(true);


                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().addAll(st, str);
                    pt.play();

                    pt.setOnFinished(event1 -> {
                        view.gamePane.getChildren().remove(1);
                    });

                }

            });
        }


    }

    private void setWinner(int winner) {
        if (winner == AIPlayerNr) {
            HBox pane = new HBox();
            pane.setMinSize(600, 600);
            pane.setMaxSize(600, 600);

            ImageView IV = new ImageView();
            IV.setFitHeight(600);
            IV.setFitWidth(600);
            IV.setPreserveRatio(true);
            IV.setImage(new Image("/img/face.gif"));
            pane.getChildren().add(IV);

            // OST: https://www.youtube.com/watch?v=Zuw_O5MU5CE
            Thread one = new Thread() {
                public void run() {
                    int i = 0;
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;

                    Platform.runLater(() -> {
                        view.gamePane.getChildren().add(pane);
                        FadeTransition ft1 = new FadeTransition(Duration.millis(700), pane);
                        ft1.setFromValue(0.0);
                        ft1.setToValue(1.0);
                        ft1.play();

                        // http://soundbible.com/2054-Evil-Laugh-Male-9.html
                        MediaPlayer mediaPlayer;

                        Media sound =null;

                        try {
                            sound = new Media(getClass().getResource("/music/laugh.mp3").toURI().toString());
                        } catch (NullPointerException | MediaException e){
                            sound = new Media(Paths.get("/music/laugh.mp3").toUri().toString());


                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.setCycleCount(0);

                        if(userdata.getSoundOn())
                        mediaPlayer.play();
                    });
                }
            };
            one.start();

            Thread two = new Thread() {
                public void run() {
                    int i = 0;
                    while (i < 5) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    Platform.runLater(() -> {

                        createDialog();
                    });
                }
            };

            two.start();


        } else {
            Thread one = new Thread() {
                public void run() {
                    int i = 0;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;

                    Platform.runLater(() -> {


                        createDialog();


                    });
                }
            };

            one.start();

        }


    }

    private void createDialog() {

        if (userdata.getWins()>=3 && !userdata.getUnlockedFigures()[(figures_name.SUN.ordinal())]){
            createUnlockedDialog(figures_name.SUN);
            return;
        }
        if (userdata.getWins()>=10 && !userdata.getUnlockedFigures()[(figures_name.DIAMOND.ordinal())]){
            createUnlockedDialog(figures_name.DIAMOND);
            return;
        }

        if (userdata.getLoses()>=10 && !userdata.getUnlockedFigures()[(figures_name.SKULL.ordinal())]){
            createUnlockedDialog(figures_name.SKULL);
            return;
        }

        if (userdata.getLoses()>=3 && !userdata.getUnlockedFigures()[(figures_name.MOON.ordinal())]){
            createUnlockedDialog(figures_name.MOON);
            return;
        }

        view.mainPane.getChildren().add(DialogCreator.alertDialog("Do you want to play again?",
                "YES", event -> {
                    Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    offline_game gamemodel = new offline_game();
                    TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                    TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView,hardness,AI_HUMAN,Whostarts);


                }, "NO", new EventHandler() {
            @Override
            public void handle(Event event) {

                MainMenuModel model = new MainMenuModel();
                MainMenuView view2 = new MainMenuView((Stage) view.gamePane.getScene().getWindow());
                MainMenuController controller = new MainMenuController(model, view2, null);
            }
        }));
    }

    private void createUnlockedDialog(figures_name fig) {
        view.mainPane.getChildren().add(DialogCreator.unlockedDialog(fig,"YES!",event -> {

            userdata.unlockFigure(fig);
            userdata.change_selected_figure(fig);
                    Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    offline_game gamemodel = new offline_game();
                    TicTacToeView gameView = new TicTacToeView(stageTheEventSourceNodeBelongs);
                    TicTacToeController game_1_controller = new TicTacToeController(gamemodel, gameView,hardness,AI_HUMAN,Whostarts);




                },"no",event -> {
            userdata.unlockFigure(fig);

            MainMenuModel model = new MainMenuModel();
            MainMenuView view2 = new MainMenuView((Stage) view.gamePane.getScene().getWindow());
            MainMenuController controller = new MainMenuController(model, view2, null);

                }
        ));


    }


    public void setChatMessage(String message){

        Label lbl = new Label(message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
        view.chatRow.getChildren().add(lbl);
    }
}
