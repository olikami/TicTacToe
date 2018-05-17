package model;

import controller.MainMenuController;
import controller.OnlineController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.net.Client;
import model.net.Server;
import model.player.OnlinePlayer;
import model.player.figures_name;
import model.player.userdata;
import view.MainMenuView;
import view.TicTacToeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Thread.sleep;

public class online_game {

    public static int MAX_PLAYERS = 2;
    public static int CURRENT_PLAYERS_IN_ROOM = 1;
    public Server server =null;
    public Client client = null;
    Boolean WhoStarts = true;
    public Board board = new Board();
    public Boolean MyTurn = false;
    public Image Opponent_image;
    public Image Player_Image  ;
    public Color Player_color  ;
    public Color Opponent_color;
    int[] board_of_opponent={0,0,0,0,0,0,0,0,0};
    int timeout =0;
    Thread boardcheck_thread=null;


    //Am I player 1 or 2 ?
    public int IAmNumber = 2;

    TicTacToeView View;

    public ArrayList<OnlinePlayer> Players =null;


    public online_game(TicTacToeView gameView, Object SERVER_CLIENT, Boolean whostarts) {
        this.View = gameView;
        //checking who is starting
        if (SERVER_CLIENT instanceof Server) {
            server = ((Server) SERVER_CLIENT);

            Players = server.players;
            Random r = new Random(System.currentTimeMillis());
            Boolean doesTheOpponentStart = (Boolean.valueOf(r.nextInt((2)) == 1 ? "false" : "true"));

            ((Server) SERVER_CLIENT).payload1 = "ready," + doesTheOpponentStart;
            WhoStarts = !doesTheOpponentStart;

            //Creating the board checking thread:
            //check every 500millis if the board has changed, check if the change is valid ( fraud detection )
            boardcheck_thread = new Thread() {
                public void run() {
                    while (!server.server.isClosed()) {
                        try {
                            if(server.payload1.contains("ready"))
                                sleep(2000);
                            sleep(1000);
                            board_of_opponent=server.board_in_serverClass;
                            Platform.runLater(() -> {
                                timeout++;
                                boardCheck();
                                server.payload1=(Arrays.toString(board.getBoardAsArray()).replace(",",""));
                                if(timeout>60){

                                    Platform.runLater(() -> {

                                        setChatMessage("Opponent has closed connection. ");
                                        setChatMessage("Click to go back to main menu").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {


                                            if ((server != null))
                                                server.stop();
                                            else
                                                client.stop();

                                            MainMenuModel model = new MainMenuModel();
                                            MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                                            MainMenuController controller = new MainMenuController(model, view2, null);


                                        });
                                        boardcheck_thread.stop();

                                    });
                                    boardcheck_thread.interrupt();

                                }
                            });

                            //ACTUALIZE CHAT
                            Platform.runLater(() -> View.chatRow.getChildren().clear());
                            for(String s : server.CHAT){
                                Label lbl = new Label(s);
                                lbl.setTextFill(Color.WHITE);
                                lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                                Platform.runLater(() -> View.chatRow.getChildren().add(lbl));
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            boardcheck_thread.start();



        } else {
            //we are a client
            this.client = ((Client)SERVER_CLIENT);
            try {
                Players = client.getOnlinePlayers();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            WhoStarts = whostarts;


            boardcheck_thread = new Thread() {
                public void run() {
                    while (!client.socket.isClosed()) {
                        try {
                            sleep(1000);
                            String[] s =client.communication("check", "");
                            System.out.println("client received: "+ Arrays.toString(s));
                            if(s==null){

                                Platform.runLater(() -> {

                                    setChatMessage("Opponent has closed connection. ");
                                    setChatMessage("Click to go back to main menu").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {


                                        if ((server != null))
                                            server.stop();
                                        else
                                            client.stop();

                                        MainMenuModel model = new MainMenuModel();
                                        MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                                        MainMenuController controller = new MainMenuController(model, view2, null);


                                    });
                                    boardcheck_thread.stop();

                                });
                                boardcheck_thread.stop();

                            }else if(s[1].contains("[")){

                                int i =0;
                                String[] boardString=((s[1].replace("[","")).replace("]","")).split(" ");
                                for (String str : boardString) {
                                    if (str.equals(""))continue;
                                    board_of_opponent[i++] = Integer.parseInt(str);
                                }

                            }
                            Platform.runLater(() -> {

                                boardCheck();



                            });

                            //ACTUALIZE CHAT
                            Platform.runLater(() -> {View.chatRow.getChildren().clear();});
                            try {
                                ArrayList<String> j = client.chat("");
                                for(String k : j){
                                    Label lbl = new Label(k);
                                    lbl.setTextFill(Color.WHITE);
                                    lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                                    Platform.runLater(() -> {View.chatRow.getChildren().add(lbl);});
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        } catch (InterruptedException | IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            boardcheck_thread.start();


        }
        //finished checking who is starting


        //if I can start, I'll do a move.
        if (WhoStarts) {
            IAmNumber = 1;
            MyTurn = true;
            setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");
            AImove();
        }
        //else, I'll wait for my move

        Random r = new Random();
        //Set the Opponent Image
        figures_name fn;
        if(Players.get((server)==null?0:1).getFigure()==null) {
            fn = figures_name.values()[r.nextInt(figures_name.values().length)];
            while (userdata.get_selected_figure() == fn)
                fn = figures_name.values()[r.nextInt(figures_name.values().length)];
        }else{
            fn=Players.get((server)==null?0:1).getFigure();
        }
        Opponent_image       = new Image("/img/" + fn + "/" + fn + ".png");
        Player_Image           = new Image("/img/" + userdata.get_selected_figure() + "/" + userdata.get_selected_figure() + ".png");


        //setting the color for the icons
        String[] neon_colors = {
                "#000cff",
                "#2fa7e8",
                "#e1e832",
                "#41e831",
                "#30e8d2"};
        Player_color      = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        Opponent_color    = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        while(Opponent_color.equals(Player_color))
            Opponent_color = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);




    }

    public void boardCheck() {

        //if (board_of_opponent != board.getBoardAsArray()) {
        //what to do if the boards are not equal: check if the other player has one move played in a valid field (loop through the board
        int move_counter = 0;
        for (int i = 0; i < board.getBoardAsArray().length; i++) {
            if (board.getBoardAsArray()[i] != board_of_opponent[i] && board_of_opponent[i] != 0) {
                //thats a move!
                move_counter++;
                timeout = 0;
                if (board.getBoardAsArray()[i] != 0) {
                    //it's not a valid move! CHEATER!!!!
                    setChatMessage("Other player is a cheater!");
                }
                if (board.getBoardAsArray()[i] == 0) {
                    //populating the board with the opponent move
                    board.populateBoard(i, IAmNumber == 2 ? 1 : 2);
                    gameMethods.setImage((ImageView) View.board[i].getChildren().get(0), Opponent_image, Opponent_color);
                    gameMethods.animateMoves(View.board[i]);

                //check for winner
                if (board.getWinner() != 0) {

                    setWinner();
                    if (board.getWinner() != 3) {

                        if (IAmNumber != board.getWinner())
                            MyTurn = true;
                        setChatMessage(Players.get(board.getWinner()).getName() + " wins with his move in field: ( " + ((i + 1) % 3f == 0 ? 3 : ((i + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((i + 1) / 3f) + ")");
                    } else {
                        if ((server != null))
                            MyTurn = true;
                        setChatMessage("It's a tie!");
                    }
                    return;
                } else {
                    setChatMessage(Players.get(IAmNumber==1?1:0).getName() + " has played in field ( " + ((i + 1) % 3f == 0 ? 3 : ((i + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((i + 1) / 3f) + ")");
                    setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");


                    MyTurn = true;

                    AImove();

                }
            }
            }
        }

                if (move_counter > 1) {
                    //Other player has played more than 1 field
                    setChatMessage("Other player is a cheater!");
                }
            }

    private void AImove() {
        // here we implement the ai move as a response
        AI AI = new AI();
        int p = AI.getNextMove(board, IAmNumber);

        if ((server != null)) {
            if (server.AI_Mode) {
                MyTurn = false;
                board.populateBoard(p, IAmNumber);
                server.payload1 = Arrays.toString(board.getBoardAsArray()).replace(",", "");
                //set the image
                gameMethods.setImage((ImageView) View.board[p].getChildren().get(0), Player_Image, Player_color);
                gameMethods.animateMoves(View.board[p]);

                if (board.getWinner() != 0)
                    setWinner();
            }
        } else {
            if (client.AI_Mode) {
                MyTurn = false;
                board.populateBoard(p, IAmNumber);
                try {
                    String[] s = client.communication("board", Arrays.toString(board.getBoardAsArray()).replace(",", ""));
                    System.out.println("Client receives: " + Arrays.toString(s));

                    //set the image
                    gameMethods.setImage((ImageView) View.board[p].getChildren().get(0), Player_Image, Player_color);
                    gameMethods.animateMoves(View.board[p]);
                    if (board.getWinner() != 0)
                        setWinner();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void setWinner() {

        if (IAmNumber != board.getWinner())
            MyTurn = true;

        View.mainPane.getChildren().add(DialogCreator.alertDialog("Do you want to play again?",
                "YES", event -> {

                    Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    playAgain(stageTheEventSourceNodeBelongs);




                }, "NO", new EventHandler() {
                    @Override
                    public void handle(Event event) {

                        if ((server != null))
                            server.stop();
                        else
                            client.stop();

                        MainMenuModel model = new MainMenuModel();
                        MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                        MainMenuController controller = new MainMenuController(model, view2, null);
                    }
                }));


        if((server!=null&&server.AI_Mode)||(client!=null&&client.AI_Mode)){
            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) View.gamePane).getScene().getWindow();

            playAgain(stageTheEventSourceNodeBelongs);



        }

        //todo online statistics
        //userdata.setLoseGames();

        gameMethods.setWinnerStroke(board, View);
    }

    private void playAgain(Stage stageTheEventSourceNodeBelongs){


        if (server != null) {
            board = new Board();
            Arrays.fill(board_of_opponent, 0);
            Arrays.fill(server.board_in_serverClass, 0);
            View = new TicTacToeView(stageTheEventSourceNodeBelongs);
            new OnlineController(online_game.this, View);
            if(MyTurn)
                setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");
        }
        else{
            Thread wait_for_server = new Thread(() -> {
                Platform.runLater(() -> {


                    View = new TicTacToeView(stageTheEventSourceNodeBelongs);
                    View.mainPane.getChildren().add(DialogCreator.vanillaDialog("Waiting for host","...",false));

                });

                while (!Arrays.equals(board_of_opponent, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0})) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Arrays.fill(board_of_opponent, 0);
                Platform.runLater(() -> {
                    board = new Board();
                    View = new TicTacToeView(stageTheEventSourceNodeBelongs);
                    OnlineController game_1_controller = new OnlineController(online_game.this,View);
                    if(MyTurn)
                        setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");


                });

            });
            wait_for_server.start();
        }



    }

    public Label setChatMessage(String message) {

        Label lbl = new Label(message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
        View.chatRow.getChildren().add(lbl);
        if(!message.equals("It's your turn!"))
            if(server!=null)
                server.CHAT.add(message);
        return lbl;
    }
}
