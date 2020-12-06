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
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class online_game {

    //how many players are allowed in the game
    public static final int MAX_PLAYERS = 2;
    //how many players are in the room
    public static final int PLAYERS = 1;
    //Server object
    public Server server =null;
    //Client object
    public Client client = null;
    //Boolean to determine who has the beginning turn
    Boolean WhoStarts = true;
    //Board object
    public Board board = new Board();
    //Boolean to evaluate whether it's the turn of the player of this game instance
    public Boolean MyTurn = false;
    //the avatar of the opponent
    public Image Opponent_image;
    //the own avatar
    public Image Player_Image  ;
    //the own colour
    public Color Player_color  ;
    //the opponents colour
    public Color Opponent_color;
    //the current board of the opponent as array
    int[] board_of_opponent={0,0,0,0,0,0,0,0,0};
    //timeout for the game
    int timeout =0;
    //board check thread
    Thread boardcheck_thread=null;
    //Logger
    Logger logger = Logger.getLogger(online_game.class.getName());


    //Am I player 1 or 2 ?
    public int IAmNumber = 2;

    //the View part of the MVC pattern of the game
    TicTacToeView View;

    //list of the players in this game
    public ArrayList<OnlinePlayer> Players =null;


    public online_game(TicTacToeView gameView, Object SERVER_CLIENT, Boolean whostarts) {
        //the View part of the MVC pattern of the game
        this.View = gameView;

        //checking who is starting
        if (SERVER_CLIENT instanceof Server) {
            //we are the server
            server = (Server) SERVER_CLIENT;

            //getting the players from the server
            Players = server.players;

            //Evaluating who is starting with a random int
            final Random r = new Random(System.currentTimeMillis());
            final Boolean opponentStarts = Boolean.valueOf(r.nextInt(2) == 1 ? "false" : "true");

            //Sending a message via the server
            ((Server) SERVER_CLIENT).payload1 = "ready," + opponentStarts;

            //setting the boolean who is starting
            WhoStarts = !opponentStarts;

            //Creating the board checking thread:
            //check every 500millis if the board has changed, check if the change is valid ( fraud detection )
            boardcheck_thread = new Thread() {
                public void run() {
                    //while the server is not closed, do the loop
                    while (!server.server.isClosed()) {
                        try {
                            if(server.payload1.contains("ready"))
                                sleep(2000);
                            sleep(1000);
                            //getting the board of the opponent
                            board_of_opponent=server.board_in_serverClass;
                            Platform.runLater(() -> {
                                timeout++;
                                //check whether everything is alright with the received board
                                boardCheck();
                                //send board to opponent
                                server.payload1=Arrays.toString(board.getBoardAsArray()).replace(",","");
                                //What to do in case of a timeout
                                if(timeout>60){

                                    Platform.runLater(() -> {

                                        setChatMessage("Opponent has closed connection. ");
                                        setChatMessage("Click to go back to main menu").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {


                                            //Stop everything and return to the main menu
                                            if (server != null)
                                                server.stop();
                                            else
                                                client.stop();

                                            final MainMenuModel model = new MainMenuModel();
                                            final MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                                            final MainMenuController controller = new MainMenuController(model, view2, null);


                                        });
                                        boardcheck_thread.stop();

                                    });
                                    boardcheck_thread.interrupt();

                                }
                            });

                            //ACTUALIZE CHAT
                            Platform.runLater(() -> View.chatRow.getChildren().clear());
                            for(final String s : server.CHAT){
                                final Label lbl = new Label(s);
                                lbl.setTextFill(Color.WHITE);
                                lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                                //update the UI
                                Platform.runLater(() -> View.chatRow.getChildren().add(lbl));
                            }

                        } catch (InterruptedException e) {
                            logger.log(Level.SEVERE, e.toString());

                        }
                    }
                }
            };
            boardcheck_thread.start();



        } else {
            //we are a client
            this.client = (Client)SERVER_CLIENT;
            try {
                Players = client.getOnlinePlayers();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.toString());
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, e.toString());
            }
            //If we are a client, we receive wheter we start or not.
            WhoStarts = whostarts;


            boardcheck_thread = new Thread() {
                public void run() {
                    //do this while the game is running
                    while (!client.socket.isClosed()) {
                        try {
                            sleep(1000);
                            //received a board message from a "Check" request to the server
                            final String[] s =client.communication("check", "");
                            System.out.println("client received: "+ Arrays.toString(s));

                            //if the check is unsuccesfull, close the game
                            if(s==null){
                                Platform.runLater(() -> {

                                    setChatMessage("Opponent has closed connection. ");
                                    setChatMessage("Click to go back to main menu").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {


                                        if (server != null)
                                            server.stop();
                                        else
                                            client.stop();

                                        final MainMenuModel model = new MainMenuModel();
                                        final MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                                        final MainMenuController controller = new MainMenuController(model, view2, null);


                                    });
                                    boardcheck_thread.stop();

                                });
                                boardcheck_thread.stop();

                                //if the check is successfull, update the game
                            }else if(s[1].contains("[")){

                                int i =0;
                                //format the received board
                                final String[] boardString=s[1].replace("[","").replace("]","").split(" ");
                                for (final String str : boardString) {
                                    if (str.equals(""))continue;
                                    //update the board of the opponent inside of our own client class
                                    board_of_opponent[i++] = Integer.parseInt(str);
                                }

                            }
                            Platform.runLater(() -> {

                                //check whether everything is okay with the game
                                boardCheck();



                            });

                            //ACTUALIZE CHAT
                            Platform.runLater(() -> {View.chatRow.getChildren().clear();});
                            try {
                                final ArrayList<String> j = client.chat("");
                                for(final String k : j){
                                    final Label lbl = new Label(k);
                                    lbl.setTextFill(Color.WHITE);
                                    lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                                    //Update UI
                                    Platform.runLater(() -> {View.chatRow.getChildren().add(lbl);});
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                logger.log(Level.SEVERE, e.toString());
                            }

                        } catch (InterruptedException | IOException | ClassNotFoundException e) {
                            logger.log(Level.SEVERE, e.toString());
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
            aiMove();
        }
        //else, I'll wait for my move

        final Random r = new Random();
        //Set the Opponent Image
        figures_name fn;
        if(Players.get((server)==null?0:1).getFigure()==null) {
            fn = figures_name.values()[r.nextInt(figures_name.values().length)];
            while (userdata.get_selected_figure() == fn) {
                fn = figures_name.values()[r.nextInt(figures_name.values().length)];
            }
        }else{
            fn=Players.get((server)==null?0:1).getFigure();
        }
        Opponent_image       = new Image("/img/" + fn + "/" + fn + ".png");
        Player_Image           = new Image("/img/" + userdata.get_selected_figure() + "/" + userdata.get_selected_figure() + ".png");


        //setting the color for the icons
        final String[] neon_colors = {
                "#000cff",
                "#2fa7e8",
                "#e1e832",
                "#41e831",
                "#30e8d2"};
        Player_color      = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        Opponent_color    = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        while(Opponent_color.equals(Player_color)) {
            Opponent_color = gameMethods.hex2Rgb(neon_colors[r.nextInt(4)]);
        }




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
                    //animate the move
                    gameMethods.animateMoves(View.board[i]);

                //check for winner
                if (board.getWinner() != 0) {

                    setWinner();
                    if (board.getWinner() != 3) {

                        if (IAmNumber != board.getWinner())
                            MyTurn = true;
                        setChatMessage(Players.get(board.getWinner()).getName() + " wins with his move in field: ( " + ((i + 1) % 3f == 0 ? 3 : ((i + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((i + 1) / 3f) + ")");
                    } else {
                        if (server != null)
                            MyTurn = true;
                        setChatMessage("It's a tie!");
                    }
                    return;
                } else {
                    setChatMessage(Players.get(IAmNumber==1?1:0).getName() + " has played in field ( " + ((i + 1) % 3f == 0 ? 3 : ((i + 1) % 3f == 2 ? 2 : 1)) + ", " + (int) Math.ceil((i + 1) / 3f) + ")");
                    setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");


                    MyTurn = true;

                    aiMove();

                }
            }
            }
        }

                if (move_counter > 1) {
                    //Other player has played more than 1 field
                    setChatMessage("Other player is a cheater!");
                }
            }

    private void aiMove() {
        // here we implement the ai move as a response
        final AI AI = new AI();
        final int p = AI.getNextMove(board, IAmNumber);

        if (server != null) {
            if (server.AI_Mode) {
                MyTurn = false;
                final int[] i = board.populateBoard(p, IAmNumber);
                server.payload1 = Arrays.toString(i).replace(",", "");
                //set the image
                gameMethods.setImage((ImageView) View.board[p].getChildren().get(0), Player_Image, Player_color);
                gameMethods.animateMoves(View.board[p]);

                if (board.getWinner() != 0)
                    setWinner();
            }
        } else {
            if (client.AI_Mode) {
                MyTurn = false;
                final int[] i =board.populateBoard(p, IAmNumber);
                try {
                    final String[] s = client.communication("board", Arrays.toString(i).replace(",", ""));
                    System.out.println("Client receives: " + Arrays.toString(s));

                    //set the image
                    gameMethods.setImage((ImageView) View.board[p].getChildren().get(0), Player_Image, Player_color);
                    gameMethods.animateMoves(View.board[p]);
                    if (board.getWinner() != 0)
                        setWinner();

                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.SEVERE, e.toString());
                }
            }

        }
    }


    //Update the UI and the game after declaring the winner
    public void setWinner() {

        if (IAmNumber != board.getWinner())
            MyTurn = true;

        //update UI
        View.mainPane.getChildren().add(DialogCreator.alertDialog("Do you want to play again?",
                "YES", event -> {

                    // The Stage where the Event Source Node belongs to
                    final Stage sourceNode = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    playAgain(sourceNode);




                }, "NO", new EventHandler() {
                    @Override
                    public void handle(Event event) {

                        if (server != null)
                            server.stop();
                        else
                            client.stop();

                        final MainMenuModel model = new MainMenuModel();
                        final MainMenuView view2 = new MainMenuView((Stage) View.gamePane.getScene().getWindow());
                        final MainMenuController controller = new MainMenuController(model, view2, null);
                    }
                }));


        if(server!=null&&server.AI_Mode||client!=null&&client.AI_Mode){
            // The Stage where the Event Source Node belongs to
            final Stage sourceNode = (Stage) ((Node) View.gamePane).getScene().getWindow();

            //If the player chooses to play again, restart everything
            playAgain(sourceNode);



        }

        //todo online statistics
        //userdata.setLoseGames();

        gameMethods.setWinnerStroke(board, View);
    }

    // sourceNode is the Stage where the Event Source Node belongs to
    private void playAgain(Stage sourceNode){


        //update the game UI and the internal game boards
        if (server != null) {
            board = new Board();
            Arrays.fill(board_of_opponent, 0);
            Arrays.fill(server.board_in_serverClass, 0);
            View = new TicTacToeView(sourceNode);
            new OnlineController(online_game.this, View);
            if(MyTurn)
                setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");
        }
        else{
            final Thread wait_for_server = new Thread(() -> {
                Platform.runLater(() -> {

                    //update UI
                    View = new TicTacToeView(sourceNode);
                    View.mainPane.getChildren().add(DialogCreator.vanillaDialog("Waiting for host","...",false));

                });

                while (!Arrays.equals(board_of_opponent, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0})) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, e.toString());
                    }
                }
                Arrays.fill(board_of_opponent, 0);
                Platform.runLater(() -> {
                    //update internal game board
                    board = new Board();
                    View = new TicTacToeView(sourceNode);
                    OnlineController game_1_controller = new OnlineController(online_game.this,View);
                    if(MyTurn)
                        setChatMessage(Players.get(IAmNumber==1?1:0).getName()+", please make your turn!");


                });

            });
            wait_for_server.start();
        }



    }

    //populate the chat UI element with chat messages
    public Label setChatMessage(String message) {

        final Label lbl = new Label(message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
        View.chatRow.getChildren().add(lbl);
        if(!message.equals("It's your turn!"))
            if(server!=null)
                server.CHAT.add(message);
        return lbl;
    }
}
