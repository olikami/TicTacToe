package controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import model.player.figures_name;
import model.player.userdata;
import view.MainMenuView;
import view.TicTacToeView;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

public class TicTacToeController {
    offline_game model;
    TicTacToeView view;
    private int AIPlayerNr =1;
    public TicTacToeController(offline_game model, TicTacToeView view) {
        this.model = model;
        this.view = view;

        Boolean againstComputer = false;
        final Boolean[] turn = {true};


        Random r = new Random();
        figures_name fn = figures_name.values()[r.nextInt(figures_name.values().length)];
        while(userdata.get_selected_figure()==fn)
            fn = figures_name.values()[r.nextInt(figures_name.values().length)];


        //todo color adjust
        Image AI_image = new Image("/res/img/"+fn+"/"+fn+".png");


        Image Player_Image = new Image("/res/img/"+userdata.get_selected_figure()+"/"+userdata.get_selected_figure()+".png");



        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Play TicTacToe");
        alert.setHeaderText("Play ticTacToe against:");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Computer");
        ButtonType buttonTypeTwo = new ButtonType("Other Player");
        ButtonType buttonTypeCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose "One"
            againstComputer = true;

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Play TicTacToe");
            alert.setHeaderText("Set strenghth of Computer");
            alert.setContentText("Choose your option.");

             buttonTypeOne = new ButtonType("Easy");
             buttonTypeTwo = new ButtonType("Medium");
             buttonTypeCancel = new ButtonType("Hard");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);


            result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                // ... user chose "One"
                model.AI.depthLimiter = 0;
            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Two"
                model.AI.depthLimiter =1;
            } else {
                // ... user chose CANCEL or closed the dialog
                model.AI.depthLimiter = 10;
            }

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Who should start?");
            alert.setContentText("OK = AI starts  CANCEL = You start");

            result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                ((ImageView)view.board[model.AiTurn(AIPlayerNr)].getChildren().get(0)).setImage(AI_image);
            } else {
                // ... user chose CANCEL or closed the dialog
                AIPlayerNr =2;

            }



        } else if (result.get() == buttonTypeTwo) {
            // ... user chose "Two"
        } else {
            // ... user chose CANCEL or closed the dialog
            System.exit(0);
        }


        for(int i = 0;i<view.board.length;i++) {
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
                        ((ImageView) view.board[a].getChildren().get(0)).setImage(Player_Image);

                    animateMoves(view.board[a]).setOnFinished(event1 -> {




                    //check for winner
                        if (model.board.getWinner() != 0) {
                            setWinner(model.board.getWinner());
                            setWinnerStroke();
                            return;
                        }

                        int p = model.AiTurn(finalAIPlayerNr);
                        ((ImageView) view.board[p].getChildren().get(0)).setImage(AI_image);

                        animateMoves(view.board[p]);

                        System.out.println(model.board.toString());

                        //check for winner
                        if (model.board.getWinner() != 0) {
                            setWinner(model.board.getWinner());
                            setWinnerStroke();
                            return;
                        }
                    });

                } else {
                    //Play against human
                    if (turn[0]) {
                        turn[0] = false;
                        //populate the gameboard
                        model.board.populateBoard(a, 1);

                        //set the image
                        ((ImageView) view.board[a].getChildren().get(0)).setImage(Player_Image);
                        animateMoves(view.board[a]);

                        //check for winner
                        if (model.board.getWinner() != 0) {
                            setWinner(model.board.getWinner());
                            setWinnerStroke();
                            return;
                        }
                    } else {
                        turn[0] = true;
                        //populate the gameboard
                        model.board.populateBoard(a, 2);

                        //set the image
                        ((ImageView) view.board[a].getChildren().get(0)).setImage(AI_image);
                        animateMoves(view.board[a]);
                        //check for winner
                        if (model.board.getWinner() != 0) {
                            setWinner(model.board.getWinner());
                            setWinnerStroke();
                            return;
                        }
                    }


                }


                }else{
                    HBox pane = new HBox();
                    pane.setMinSize(600,600);
                    pane.setMaxSize(600,600);
                    pane.setAlignment(Pos.CENTER);

                    ImageView IV = new ImageView();
                    IV.setFitHeight(300);
                    IV.setFitWidth(300);
                    IV.setPreserveRatio(true);
                    IV.setImage(new Image("/res/img/invalid.png"));
                    pane.getChildren().add(IV);

                    view.mainPane.getChildren().add(pane);


                    FadeTransition ft1 = new FadeTransition(Duration.millis(400), pane);
                    ft1.setFromValue(0.0);
                    ft1.setToValue(1.0);

                    FadeTransition ft2 = new FadeTransition(Duration.millis(400), pane);
                    ft2.setFromValue(1.0);
                    ft2.setToValue(0.0);


                    SequentialTransition st = new SequentialTransition();
                    st.getChildren().addAll(ft1,ft2);

                    ScaleTransition str = new ScaleTransition(Duration.millis(800), pane);
                    str.setByX(1.1f);
                    str.setByY(1.1f);
                    str.setAutoReverse(true);


                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().addAll(st,str);
                    pt.play();

                    pt.setOnFinished(event1 -> {
                        view.mainPane.getChildren().remove(1);
                    });

                }

            });
        }




    }

    private void setWinner(int winner) {
        if(winner==AIPlayerNr){
            HBox pane = new HBox();
            pane.setMinSize(600,600);
            pane.setMaxSize(600,600);

            ImageView IV = new ImageView();
            IV.setFitHeight(600);
            IV.setFitWidth(600);
            IV.setPreserveRatio(true);
            IV.setImage(new Image("/res/img/face.gif"));
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
                        view.mainPane.getChildren().add(pane);
                        FadeTransition ft1 = new FadeTransition(Duration.millis(700), pane);
                        ft1.setFromValue(0.0);
                        ft1.setToValue(1.0);
                        ft1.play();

                        // http://soundbible.com/2054-Evil-Laugh-Male-9.html
                        MediaPlayer mediaPlayer;
                        Media sound = new Media(Paths.get("src/res/music/laugh.mp3").toUri().toString());
                        mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.setCycleCount(0);
                        mediaPlayer.play();
                    });
                }
            };

            one.start();

            Thread two = new Thread() {
                public void run() {
                    int i = 0;
                    while(i <5){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    Platform.runLater(() -> {


                            MainMenuModel model;
                            //TicTacToeView view;
                            MainMenuView view;
                            MainMenuController controller;
                            model = new MainMenuModel();
                            view = new MainMenuView((Stage)pane.getScene().getWindow());
                            controller = new MainMenuController(model, view,null);





                    });
                }
            };

            two.start();


        }else{
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


                        MainMenuModel model;
                        //TicTacToeView view;
                        MainMenuView view2;
                        MainMenuController controller;
                        model = new MainMenuModel();
                        view2 = new MainMenuView((Stage)view.mainPane.getScene().getWindow());
                        controller = new MainMenuController(model, view2,null);

                    });
                }
            };

            one.start();

        }



    }

    private void setWinnerStroke() {
        if(model.board.getWinnerStroke()[0]==5)
            return;

        HBox pane = new HBox();
        pane.setMinSize(600,600);
        pane.setMaxSize(600,600);

        ImageView IV = new ImageView();
        IV.setFitHeight(200);
        IV.setFitWidth(600);
        IV.setPreserveRatio(true);
        IV.setImage(new Image("/res/img/stroke.png"));
        pane.getChildren().add(IV);

        TranslateTransition TT = new TranslateTransition(Duration.millis(400), pane);

        switch(model.board.getWinnerStroke()[0]){
            case 1:
                TT.setFromX(600);
                TT.setFromY(00);
                break;
            case 2:
                pane.setRotate(-90);
                TT.setFromX(00);
                TT.setFromY(600);
                break;
            case 3:
                pane.setRotate(45);
                TT.setFromX(600);
                TT.setFromY(600);
                pane.setAlignment(Pos.CENTER);
                break;
            case 4:
                pane.setRotate(-45);
                TT.setFromX(-600);
                TT.setFromY(600);
                pane.setAlignment(Pos.CENTER);
                break;
        }

        switch(model.board.getWinnerStroke()[1]){
            case 0: break;
            case 1:
                pane.setAlignment(Pos.CENTER);
                break;
            case 2:
                pane.setAlignment(Pos.BOTTOM_CENTER);
                break;
        }


            view.mainPane.getChildren().add(pane);

        TT.setToX(0f);
        TT.setToY(0);

        TT.play();
    }


    public ParallelTransition animateMoves(Pane pane){

        FadeTransition ft2 = new FadeTransition(Duration.millis(400), pane);
        ft2.setFromValue(0.0);
        ft2.setToValue(1.0);


        ScaleTransition str = new ScaleTransition(Duration.millis(500), pane);
        str.setFromX(1.1f);
        str.setFromY(1.1f);
        str.setToX(1f);
        str.setToY(1f);
        str.setAutoReverse(false);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft2,str);
        pt.play();
        return pt;
    }
}
