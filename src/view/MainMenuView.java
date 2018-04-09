package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.player.figures;

public class MainMenuView {


    public StackPane mainPane = new StackPane();
    public VBox row2;
    public VBox row3;
    public VBox row1;
    public Pane background = new Pane();


    public MainMenuView(Stage primaryStage) {

        VBox rows_container = new VBox();
        rows_container.setAlignment(Pos.CENTER);
        row1 = new VBox();
        row1.setMinSize(600, 200);
        row1.setMaxSize(600, 200);
        row1.setAlignment(Pos.CENTER);

        ImageView IV3 = new ImageView("/res/img/title.gif");

        IV3.setFitHeight(200);
        IV3.setFitWidth(600);
        IV3.setPreserveRatio(true);
        Pane imageHolder3= new Pane();
        imageHolder3.setMinSize(600,200);
        imageHolder3.setMaxSize(600,200);
        imageHolder3.getChildren().add(IV3);

        row1.getChildren().add(imageHolder3);

        row2 = new VBox();
        row2.setMinSize(600, 200);
        row2.setMaxSize(600, 200);

        row2.setAlignment(Pos.CENTER);

        ImageView offline_btn = new ImageView("/res/img/play/play.png");
        row2.getChildren().addAll(offline_btn);

        row3 = new VBox();
        row3.setMinSize(600, 200);
        row3.setMaxSize(600, 200);
        row3.setAlignment(Pos.CENTER);




        rows_container.getChildren().addAll(row1, row2, row3);
        rows_container.setStyle("-fx-background-color: rgba(0,0,0,0.42);");

        ImageView IV = new ImageView();
        IV.setImage(new Image("/res/img/cross.png"));
        IV.setFitHeight(200);
        IV.setFitWidth(200);
        IV.setPreserveRatio(true);
        Pane imageHolder= new Pane();
        imageHolder.setMinSize(200,200);
        imageHolder.setMaxSize(200,200);
        imageHolder.getChildren().add(IV);

        ImageView IV2 = new ImageView();
        IV2.setImage(new Image("/res/img/circle.png"));
        IV2.setFitHeight(200);
        IV2.setFitWidth(200);
        IV2.setPreserveRatio(true);
        Pane imageHolder2= new Pane();
        imageHolder2.setMinSize(200,200);
        imageHolder2.setMaxSize(200,200);
        imageHolder2.getChildren().add(IV2);


        background.setMinSize(600, 600);
        background.setMaxSize(600, 600);
        background.getChildren().addAll(imageHolder,imageHolder2);

        ImageView IV4= new ImageView();
        IV4.setImage(new Image("/res/img/grid.gif"));
        IV4.setFitHeight(600);
        IV4.setFitWidth(600);
        IV4.setPreserveRatio(true);

        mainPane.setStyle("-fx-background-color: rgb(0,0,0);");
        mainPane.getChildren().addAll(IV4,background,rows_container);
        mainPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainPane, 1200,
                600);
        primaryStage.setTitle("TicTacToe - Offline");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}