package model;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import view.TicTacToeView;

public class gameMethods {


    //some generic methods to use while playing tictactoe


    public static ParallelTransition animateMoves(Pane pane) {

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
        pt.getChildren().addAll(ft2, str);
        pt.play();
        return pt;
    }


    public static void setWinnerStroke(Board board, TicTacToeView view) {
        if (board.getWinnerStroke()[0] == 5)
            return;

        HBox pane = new HBox();
        pane.setMinSize(600, 600);
        pane.setMaxSize(600, 600);

        ImageView IV = new ImageView();
        IV.setFitHeight(200);
        IV.setFitWidth(600);
        IV.setPreserveRatio(true);
        IV.setImage(new Image("/img/stroke.png"));
        pane.getChildren().add(IV);

        TranslateTransition TT = new TranslateTransition(Duration.millis(400), pane);

        switch (board.getWinnerStroke()[0]) {
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

        switch (board.getWinnerStroke()[1]) {
            case 0:
                break;
            case 1:
                pane.setAlignment(Pos.CENTER);
                break;
            case 2:
                pane.setAlignment(Pos.BOTTOM_CENTER);
                break;
        }


        view.gamePane.getChildren().add(pane);

        TT.setToX(0f);
        TT.setToY(0);

        TT.play();
    }


    //https://stackoverflow.com/questions/4129666/how-to-convert-hex-to-rgb-using-java

    /**
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    public static Color hex2Rgb(String colorStr) {

        Double d = (double)(Integer.valueOf(colorStr.substring(1, 3), 16)/255d);
        return new Color((double)(Integer.valueOf(colorStr.substring(1, 3), 16))/255d,
                (double)(Integer.valueOf(colorStr.substring(3, 5), 16))/255d,
                (double)(Integer.valueOf(colorStr.substring(5, 7), 16))/255d,1);
    }

    public void setImage(ImageView IV, Image img,Color color){

        IV.setImage(img);

        ImageView IVclip = new ImageView(img);
        IVclip.setFitHeight(180);
        IVclip.setFitWidth(180);

        IV.setClip(IVclip);


        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);

        Blend blush = new Blend(
                BlendMode.MULTIPLY,
                monochrome,
                new ColorInput(
                        0,
                        0,
                        180,
                        180,
                        color
                )
        );


        IV.setEffect(blush);
        IV.setCache(true);
        IV.setCacheHint(CacheHint.SPEED);

    }


}
