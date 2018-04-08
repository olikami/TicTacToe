package view;

import controller.MainMenuController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Intro;
import model.MainMenuModel;

public class IntroView {

    public IntroView(Stage primaryStage, Intro modelview) {


        Pane mainPane = new Pane();
        ImageView IV3 = new ImageView();
        IV3.setX(0);
        IV3.setY(0);
        IV3.setImage(new Image("/res/img/intro.gif"));
        IV3.setFitHeight(610);
        IV3.setFitWidth(1210);
        IV3.setPreserveRatio(true);
        mainPane.setMinSize(1200,600);
        mainPane.setMaxSize(1200,600);
        mainPane.getChildren().add(IV3);

        mainPane.setOnMouseClicked(event -> {


            MainMenuModel model = new MainMenuModel();
            MainMenuView view = new MainMenuView(primaryStage);
            MainMenuController controller = new MainMenuController(model, view,modelview.mediaPlayer);
            modelview.one.stop();
        });
        Scene scene = new Scene(mainPane, 1200,
                600);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
