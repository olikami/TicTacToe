package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IntroView {

    public IntroView(Stage primaryStage) {


        Pane mainPane = new Pane();
        ImageView IV3 = new ImageView();
        IV3.setX(0);
        IV3.setY(0);
        IV3.setImage(new Image("/res/img/intro.gif"));
        IV3.setFitHeight(610);
        IV3.setFitWidth(610);
        IV3.setPreserveRatio(true);
        mainPane.setMinSize(600,600);
        mainPane.setMaxSize(600,600);
        mainPane.getChildren().add(IV3);

        Scene scene = new Scene(mainPane, 600,
                600);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
