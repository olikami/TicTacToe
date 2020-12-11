package model;

import controller.MainMenuController;
import controller.OnlineController;

import model.net.Client;
import model.net.Server;
import model.player.OnlinePlayer;
import model.player.figuresName;
import model.player.userdata;

import view.MainMenuView;
import view.TicTacToeView;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * @author Emanuel Graf
 */
public class OnlineGame {

    /**
     * how many players are allowed in the game
     */
    public static final int MAX_PLAYERS = 2;
    /**
     * how many players are in the room
     */
    public static final int PLAYERS = 1;
    /**
     * Server object
     */
    public Server server = null;
    /**
     * Client object
     */
    public Client client = null;
    /**
     * Boolean to determine who has the beginning turn
     */
    Boolean whoStarts = true;
    /**
     * Board object
     */
    public Board board = new Board();
    /**
     * Boolean to evaluate whether it's the turn of the player of this game instance
     */
    public Boolean myTurn = false;
    /**
     * the avatar of the opponent
     */
    public Image opponentImage;
    /**
     * the own avatar
     */
    public Image playerImage;
    /**
     * the own colour
     */
    public Color playerColor;
    /**
     * the opponents colour
     */
    public Color opponentColor;
    /**
     * the current board of the opponent as array
     */
    int[] boardOfOpponent = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    /**
     * timeout for the game
     */
    int timeout = 0;
    /**
     * board check thread
     */
    Thread boardCheckThread = null;
    /**
     * Logger
     */
    final static Logger logger = Logger.getLogger(OnlineGame.class.getName());


    /**
     * Am I player 1 or 2 ?
     */
    public int iAmNumber = 2;

    /**
     * the view part of the MVC pattern of the game
     */
    TicTacToeView view;

    /**
     * list of the players in this game
     */
    public ArrayList<OnlinePlayer> players = null;


    /**
     * @param gameView     The game View
     * @param serverClient Object determining whether we are client or server
     * @param whoStarts    If true, this instance makes first move
     */
    public OnlineGame(TicTacToeView gameView, Object serverClient, Boolean whoStarts) {
        /**the view part of the MVC pattern of the game */
        this.view = gameView;

        /**checking who is starting */
        if (serverClient instanceof Server) {
            /**we are the server */
            startServerHandler(serverClient);

        } else {
            /**we are a client */
            startClientHandler(serverClient, whoStarts);
        }
        /**finished checking who is starting */


        /**if I can start, I'll do a move. */
        if (this.whoStarts) {
            iAmNumber = 1;
            myTurn = true;
            labelChatMessage(
                String.format(
                    "%s, please make your turn!",
                    players.get(iAmNumber).getName()
                )
            );
            aiMove();
        }
        /**else, I'll wait for my move */

        final Random rand = new Random();
        /**Set the Opponent Image */
        figuresName figures;
        if (players.get((server) == null ? 0 : 1).getFigure() == null) {
            figures = figuresName.values()[rand.nextInt(figuresName.values().length)];
            while (userdata.getSelectedFigure() == figures) {
                figures = figuresName.values()[rand.nextInt(figuresName.values().length)];
            }
        } else {
            figures = players.get((server) == null ? 0 : 1).getFigure();
        }
        opponentImage = new Image(
            String.format(
                "/img/%s/%s.png",
                figures,
                figures
            )
        );
        playerImage = new Image(
            String.format(
                "/img/%s/%s.png",
                    userdata.getSelectedFigure(),
                    userdata.getSelectedFigure()
            )
        );


        /**setting the color for the icons */
        final String[] neonColors = {
                "#000cff",
                "#2fa7e8",
                "#e1e832",
                "#41e831",
                "#30e8d2"
        };
        playerColor = gameMethods.hex2Rgb(neonColors[rand.nextInt(4)]);
        opponentColor = gameMethods.hex2Rgb(neonColors[rand.nextInt(4)]);
        while (opponentColor.equals(playerColor)) {
            opponentColor = gameMethods.hex2Rgb(neonColors[rand.nextInt(4)]);
        }


    }

    private void startClientHandler(Object serverClient, Boolean whoStarts) {

        this.client = (Client) serverClient;
        try {
            players = client.getOnlinePlayers();
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.toString());
        }
        /**If we are a client, we receive whether we start or not. */
        this.whoStarts = whoStarts;


        boardCheckThread = new Thread(() -> {
            /**do this while the game is running */
            while (!client.socket.isClosed()) {
                try {
                    sleep(1000);
                    /**received a board message from a "Check" request to the server */
                    final String[] msg = client.communication("check", "");
                    logger.log(Level.INFO, "client received: " + Arrays.toString(msg));

                    /**if the check is unsuccessful, close the game */
                    if (msg == null) {
                        closeGame();
                        boardCheckThread.stop();

                        /**if the check is successful, update the game */
                    } else if (msg[1].contains("[")) {

                        int index = 0;
                        /**format the received board */
                        final String[] boardString = msg[1]
                                .replace("[", "")
                                .replace("]", "")
                                .split(" ");
                        for (final String str : boardString) {
                            if ("".equals(str)) continue;
                            /**update the board of the opponent inside of our own client class */
                            boardOfOpponent[index++] = Integer.parseInt(str);
                        }

                    }
                    /**check whether everything is okay with the game */
                    Platform.runLater(this::boardCheck);

                    /**ACTUALIZE CHAT */
                    Platform.runLater(() -> view.chatRow.getChildren().clear());
                    try {
                        final ArrayList<String> strings = client.chat("");
                        for (final String k : strings) {
                            final Label lbl = new Label(k);
                            lbl.setTextFill(Color.WHITE);
                            lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                            /**Update UI */
                            Platform.runLater(() -> view.chatRow.getChildren().add(lbl));
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        logger.log(Level.SEVERE, e.toString());
                    }

                } catch (InterruptedException | IOException | ClassNotFoundException e) {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });
        boardCheckThread.start();


    }

    private void closeGame() {
        /**
         * Close the game and inform player that opponent has closed the connection.
         */
        Platform.runLater(() -> {

            labelChatMessage("Opponent has closed connection.");
            labelChatMessage("Click to go back to main menu").addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                if (server != null)
                    server.stop();
                else
                    client.stop();

                final MainMenuModel model = new MainMenuModel();
                final MainMenuView view2 = new MainMenuView((Stage) view.gamePane.getScene().getWindow());
                new MainMenuController(model, view2, null);

            });
            boardCheckThread.stop();

        });
    }

    private void startServerHandler(Object serverClient) {
        server = (Server) serverClient;

        /**getting the players from the server */
        players = server.players;

        /**Evaluating who is starting with a random int */
        final Random rand = new Random(System.currentTimeMillis());
        final boolean opponentStarts = Boolean.parseBoolean(rand.nextInt(2) == 1 ? "false" : "true");

        /**Sending a message via the server */
        ((Server) serverClient).payload1 = "ready," + opponentStarts;

        /**setting the boolean who is starting */
        whoStarts = !opponentStarts;

        /**Creating the board checking thread: */
        /**check every 500millis
         * if the board has changed, check if the change is valid
         * ( fraud detection ) */
        boardCheckThread = new Thread(() -> {
            /**while the server is not closed, do the loop */
            while (!server.server.isClosed()) {
                try {
                    if (server.payload1.contains("ready"))
                        sleep(2000);
                    sleep(1000);
                    /**getting the board of the opponent */
                    boardOfOpponent = server.boardInServerClass;
                    Platform.runLater(() -> {
                        timeout++;
                        /**check whether everything is alright with the received board */
                        boardCheck();
                        /**send board to opponent */
                        server.payload1 = Arrays.toString(board.getBoardAsArray()).replace(",", "");
                        /**What to do in case of a timeout */
                        if (timeout > 60) {

                            closeGame();
                            boardCheckThread.interrupt();

                        }
                    });

                    /**ACTUALIZE CHAT */
                    Platform.runLater(() -> view.chatRow.getChildren().clear());
                    for (final String s : server.CHAT) {
                        final Label lbl = new Label(s);
                        lbl.setTextFill(Color.WHITE);
                        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
                        /**update the UI */
                        Platform.runLater(() -> view.chatRow.getChildren().add(lbl));
                    }

                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.toString());

                }
            }
        });
        boardCheckThread.start();

    }

    public void boardCheck() {

        /**declare gameMethods object for best practices */
        final gameMethods gameMethods = new gameMethods();

        /**if (boardOfOpponent != board.getBoardAsArray()) { */
        /**what to do if the boards are not equal:
         * check if the other player has one move played in a valid field
         * (loop through the board */
        int moveCounter = 0;
        final int[] boardArray = board.getBoardAsArray();
        for (int i = 0; i < boardArray.length; i++) {
            if (boardArray[i] != boardOfOpponent[i] && boardOfOpponent[i] != 0) {
                /**that's a move! */
                moveCounter++;
                timeout = 0;
                final HBox boardPosition = view.board[i];
                final ImageView fieldOnBoard = (ImageView) boardPosition.getChildren().get(0);
                if (boardArray[i] != 0) {
                    /**it's not a valid move! CHEATER!!!! */
                    labelChatMessage("Other player is a cheater!");
                }
                if (boardArray[i] == 0) {
                    /**populating the board with the opponent move */
                    board.populateBoard(i, iAmNumber == 2 ? 1 : 2);
                    gameMethods.setImage(fieldOnBoard, opponentImage, opponentColor);
                    /**animate the move */
                    model.gameMethods.animateMoves(boardPosition);

                    /**check for winner */
                    final int decisiveField = (i + 1) % 3f == 0 ? 3 : ((i + 1) % 3f == 2 ? 2 : 1);
                    if (board.getWinner() != 0) {

                        setWinner();
                        if (board.getWinner() != 3) {

                            if (iAmNumber != board.getWinner())
                                myTurn = true;
                            labelChatMessage(
                                String.format(
                                        "%s wins with his move in field: (%s, %s)",
                                        players.get(board.getWinner()).getName(),
                                        decisiveField,
                                        (int) Math.ceil((i + 1) / 3f)
                                    )
                            );
                        } else {
                            if (server != null)
                                myTurn = true;
                            labelChatMessage("It's a tie!");
                        }
                        return;
                    } else {
                        labelChatMessage(
                            String.format(
                                "%s has played in field (%s, %S)",
                                players.get(iAmNumber == 1 ? 1 : 0).getName(),
                                    decisiveField,
                                    (int) Math.ceil((i + 1) / 3f)
                            )
                        );
                        labelChatMessage(
                            String.format(
                                    "%s, please make your turn!",
                                    players.get(iAmNumber == 1 ? 1 : 0).getName()
                            )
                        );

                        myTurn = true;

                        aiMove();

                    }
                }
            }
        }

        if (moveCounter > 1) {
            /**Other player has played more than 1 field */
            labelChatMessage("Other player is a cheater!");
        }
    }

    private void aiMove() {
        /** here we implement the ai move as a response */
        final AI aiModel = new AI();
        final int nextMove = aiModel.getNextMove(board, iAmNumber);


        /**declare gameMethods object for best practices */
        final gameMethods gameMethods = new gameMethods();

        if (server != null) {
            if (server.aiMode) {
                myTurn = false;
                final int[] boardArray = this.board.populateBoard(nextMove, iAmNumber);
                server.payload1 = Arrays.toString(boardArray).replace(",", "");
                /**set the image */
                gameMethods.setImage((ImageView) view.board[nextMove].getChildren().get(0), playerImage, playerColor);
                model.gameMethods.animateMoves(view.board[nextMove]);

                if (this.board.getWinner() != 0)
                    setWinner();
            }
        } else {
            if (client.aiMode) {
                myTurn = false;
                final int[] boardArray = this.board.populateBoard(nextMove, iAmNumber);
                try {
                    final String[] msg = client.communication(
                            "board",
                            Arrays.toString(boardArray).replace(",", "")
                    );
                    System.out.println(
                        String.format(
                            "Client receives: %s",
                            Arrays.toString(msg)
                        )
                    );

                    /**set the image */
                    gameMethods.setImage(
                            (ImageView) view.board[nextMove].getChildren().get(0), playerImage, playerColor
                    );
                    model.gameMethods.animateMoves(view.board[nextMove]);
                    if (this.board.getWinner() != 0)
                        setWinner();

                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.SEVERE, e.toString());
                }
            }

        }
    }


    /**
     * Update the UI and the game after declaring the winner
     */
    public void setWinner() {


        /**declare gameMethods object for best practices */


        if (iAmNumber != board.getWinner())
            myTurn = true;

        /**update UI */
        view.mainPane.getChildren().add(DialogCreator.alertDialog("Do you want to play again?",
                "YES", event -> {

                    /** The Stage where the Event Source Node belongs to */
                    final Stage sourceNode = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    playAgain(sourceNode);


                }, "NO", event -> {

                    if (server != null)
                        server.stop();
                    else
                        client.stop();

                    final MainMenuModel model = new MainMenuModel();
                    final MainMenuView view2 = new MainMenuView((Stage) view.gamePane.getScene().getWindow());
                    new MainMenuController(model, view2, null);
                }));


        if (server != null && server.aiMode || client != null && client.aiMode) {
            /** The Stage where the Event Source Node belongs to */
            final Stage sourceNode = (Stage) view.gamePane.getScene().getWindow();

            /**If the player chooses to play again, restart everything */
            playAgain(sourceNode);


        }

        /**todo online statistics */
        /**userdata.setLoseGames(); */

        model.gameMethods.setWinnerStroke(board, view);
    }

    /**
     * sourceNode is the Stage where the Event Source Node belongs to
     */
    private void playAgain(Stage sourceNode) {


        /**update the game UI and the internal game boards */
        if (server != null) {
            board = new Board();
            Arrays.fill(boardOfOpponent, 0);
            Arrays.fill(server.boardInServerClass, 0);
            view = new TicTacToeView(sourceNode);
            new OnlineController(OnlineGame.this, view);
            if (myTurn)
                labelChatMessage(
                    String.format(
                        "%s, please make your turn",
                        players.get(iAmNumber == 1 ? 1 : 0).getName()
                    )
                );
        } else {
            final Thread waitForServer = new Thread(() -> {
                Platform.runLater(() -> {

                    /**update UI */
                    view = new TicTacToeView(sourceNode);
                    view.mainPane.getChildren().add(
                            DialogCreator.vanillaDialog("Waiting for host", "...", false)
                    );
                });

                while (!Arrays.equals(boardOfOpponent, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0})) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, e.toString());
                    }
                }
                Arrays.fill(boardOfOpponent, 0);
                Platform.runLater(() -> {
                    /**update internal game board */
                    board = new Board();
                    view = new TicTacToeView(sourceNode);
                    new OnlineController(OnlineGame.this, view);
                    if (myTurn)
                        labelChatMessage(
                            String.format(
                                "%s, please make your turn!",
                                players.get(iAmNumber == 1 ? 1 : 0).getName()
                            )
                        );
                });
            });
            waitForServer.start();
        }


    }

    /**
     * populate the chat UI element with chat messages
     */
    public Label labelChatMessage(String message) {

        final Label lbl = new Label(message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 20));
        view.chatRow.getChildren().add(lbl);
        if (!"It's your turn!".equals(message))
            if (server != null)
                server.CHAT.add(message);
        return lbl;
    }
}
