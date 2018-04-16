package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.DialogCreator;



public class TicTacToeView {

    public StackPane gamePane = new StackPane();
    public HBox[] board = new HBox[9];
    public StackPane mainPane = new StackPane();
    public BorderPane chatPane = new BorderPane();
    public VBox chatRow = new VBox();
    public TextField chatInput = new TextField();

    public TicTacToeView(Stage primaryStage){

        HBox game_chatContainer = new HBox();


        for(int i = 0;i<board.length;i++) {

            ImageView IV = new ImageView();
            IV.setFitHeight(180);
            IV.setFitWidth(180);
            IV.setPreserveRatio(true);
            board[i]= new HBox();
            board[i].setMinSize(200,200);
            board[i].setMaxSize(200,200);
            board[i].setAlignment(Pos.CENTER);
            board[i].getChildren().add(IV);

        }

        HBox row1 = new HBox();
        row1.setMinSize(600,200);
        row1.setMaxSize(600,200);
        row1.getChildren().addAll(board[0],board[1],board[2]);

        HBox row2 = new HBox();
        row2.setMinSize(600,200);
        row2.setMaxSize(600,200);
        row2.getChildren().addAll(board[3],board[4],board[5]);

        HBox row3 = new HBox();
        row3.setMinSize(600,200);
        row3.setMaxSize(600,200);
        row3.getChildren().addAll(board[6],board[7],board[8]);

        VBox rows = new VBox();
        rows.getChildren().addAll(row1,row2,row3);
        rows.setMinSize(600,600);
        rows.setMaxSize(600,600);
        rows.setStyle("-fx-background-image: url('/img/grid.png'); " +
                "-fx-background-position: center center; "+
                "-fx-background-size: cover, auto;");



        chatPane.setCenter(chatRow);
        chatPane.setMinSize(600,600);
        chatPane.setMaxSize(600,600);
        chatPane.setStyle("-fx-background-color: #000000; ");

        chatInput.setFont(Font.font("ARIAL", FontWeight.BOLD, 70));
        chatInput.setPromptText("Type in message");
        chatInput.setMinSize(600,100);
        chatInput.setMaxSize(600,100);
        chatPane.setBottom(chatInput);


        gamePane.getChildren().addAll(rows);
        gamePane.setMinSize(600,600);
        gamePane.setMaxSize(600,600);

        game_chatContainer.getChildren().addAll(gamePane,chatPane);
        mainPane.setMinSize(1200,600);
        mainPane.setMaxSize(1200,600);
        mainPane.getChildren().add(game_chatContainer);

        Scene scene = new Scene(mainPane, 1200,
                600);
        primaryStage.setTitle("TicTacToe - Offline");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
