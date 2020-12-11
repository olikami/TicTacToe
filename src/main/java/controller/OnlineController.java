package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.gameMethods;
import model.OnlineGame;
import view.TicTacToeView;

import java.io.IOException;
import java.util.Arrays;

public class OnlineController {
    OnlineGame model;
    TicTacToeView view;
    int IAmNumber;


    public OnlineController(OnlineGame gamemodel, TicTacToeView gameView) {
        model = gamemodel;
        view = gameView;

        IAmNumber = model.iAmNumber;




        for (int i = 0; i < view.board.length; i++) {
            int a = i;
            Color finalOpponent_color = model.opponentColor;
            view.board[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                //Check if it's a possible move
                if (model.board.PlaceIsEmpty(a) && !model.board.isLocked() && model.myTurn) {
                     model.myTurn =false;


                    //populate the gameboard
                        int[] mi =model.board.populateBoard(a, IAmNumber);

                        if(model.server !=null){
                            //todo model.server.board_in_server=model.board.getBoardAsArray();
                            model.server.payload1 = Arrays.toString(mi).replace(",","");
                        }else{
                            try {
                                System.out.println("Client sends: "+Arrays.toString(mi).replace(",",""));
                                String[] s = model.client.communication("board",Arrays.toString(mi).replace(",",""));

                                System.out.println("Client receives: "+Arrays.toString(s));

                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        //set the image
                        gameMethods.setImage((ImageView) view.board[a].getChildren().get(0),model.playerImage, model.playerColor);
                        gameMethods.animateMoves(view.board[a]).setOnFinished(event1 -> {


                            //check for winner
                            if (model.board.getWinner() != 0) {
                                model.setWinner();
                                if (model.board.getWinner() != 3) {
                                    model.labelChatMessage("You win with your move in field: ( " + ((a + 1) % 3f == 0 ? 3 : ((a + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((a + 1) / 3f) + ") congrats!");

                                    // TODO: 15.04.2018 setting onlinestats
                                    //userdata.setWinGames();
                                }else{
                                    model.labelChatMessage("It's a tie!");
                                }
                                //todo setting winner
                                //setWinner(model.board.getWinner());
                                gameMethods.setWinnerStroke(model.board,view);
                                return;
                            }else{
                               model.labelChatMessage(model.players.get(IAmNumber-1).getName()+" has played in field ( "+ ((a+1)%3f==0?3:((a+1)%3f==2?2:1))+", "+(int)Math.ceil((a+1)/3f)+")");

                            }



                        });



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


     //send chat
        view.chatInput.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                if(model.server!=null){
                    model.server.CHAT.add(view.chatInput.getText());
                    view.chatInput.clear();

                }else{
                    try {
                        model.client.chat(view.chatInput.getText());
                        view.chatInput.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


}
