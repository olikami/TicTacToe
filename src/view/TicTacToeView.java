package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToeView {

    public StackPane gamePane = new StackPane();
    public HBox[] board = new HBox[9];
    public HBox mainPane = new HBox();
    public StackPane chatPane = new StackPane();
    public VBox chatRow = new VBox();

    public TicTacToeView(Stage primaryStage){

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
        rows.setStyle("-fx-background-image: url('/res/img/grid.png'); " +
                "-fx-background-position: center center; "+
                "-fx-background-size: cover, auto;");




        chatPane.getChildren().add(chatRow);
        chatPane.setMinSize(600,600);
        chatPane.setMaxSize(600,600);
        chatPane.setStyle("-fx-background-color: #000000; ");

        gamePane.getChildren().addAll(rows);
        gamePane.setMinSize(600,600);
        gamePane.setMaxSize(600,600);

        mainPane.getChildren().addAll(gamePane,chatPane);

        Scene scene = new Scene(mainPane, 1200,
                600);
        primaryStage.setTitle("TicTacToe - Offline");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
