package model;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import model.player.figures_name;

import java.io.IOException;


public class DialogCreator {

    public static HBox vanillaDialog(String Message, String SmallMessage,Boolean remove){


        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxSize(1200,200);
        mainContainer.setMinSize(1200,200);

        VBox balken = new VBox();
        balken.setAlignment(Pos.CENTER);
        balken.setMaxSize(1200,200);
        balken.setMinSize(1200,200);
        balken.setStyle("-fx-background-color: rgba(35,35,35,0.9); ");

        Label lbl = new Label(Message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 90));

        Label lbl2 = new Label(SmallMessage);
        lbl2.setTextFill(Color.WHITE);
        lbl2.setFont(Font.font("ARIAL", FontWeight.BOLD, 40));

        balken.getChildren().addAll(lbl,lbl2);

        mainContainer.getChildren().addAll(balken);

        //Animate it



        FadeTransition ft1 = new FadeTransition(Duration.millis(800), balken);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);

        ScaleTransition str = new ScaleTransition(Duration.millis(800), balken);
        str.setFromX(1.1f);
        str.setFromY(1.1f);
        str.setToX(1f);
        str.setToY(1f);
        str.setAutoReverse(false);


        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft1, str);
        pt.play();


        TranslateTransition TT2 = new TranslateTransition(Duration.millis(800), lbl);

        TT2.setFromX(600);
        TT2.setFromY(00);

        TT2.setToX(0f);
        TT2.setToY(0);

        TT2.play();




        TranslateTransition TT = new TranslateTransition(Duration.millis(800), lbl2);

        TT.setFromX(-600);
        TT.setFromY(00);

        TT.setToX(0f);
        TT.setToY(0);
        TT.play();


        FadeTransition ft2 = new FadeTransition(Duration.millis(300), balken);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.0);




        Thread dialog_thread = new Thread() {
            public void run() {
                try {

                    int i =0;
                    while (i<3) {
                        Thread.sleep(1000);
                        i++;
                    }

                    Platform.runLater(ft2::play);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        TT.setOnFinished(e ->{
            dialog_thread.start();

        });



        return mainContainer;
    }

    public static HBox alertDialog(String Message, String btn1_msg, EventHandler button1, String btn2_msg, EventHandler button2){


        //Create dialog
        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxSize(1200,600);
        mainContainer.setMinSize(1200,600);

        VBox balken = new VBox();
        balken.setAlignment(Pos.CENTER);
        balken.setMaxSize(1200,200);
        balken.setMinSize(1200,200);
        balken.setStyle("-fx-background-color: rgba(35,35,35,0.9); ");

        Label lbl = new Label(Message);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 70));

        HBox buttonArea = new HBox();
        buttonArea.setAlignment(Pos.CENTER);
        buttonArea.setSpacing(50);
        buttonArea.setMaxSize(1200,50);
        buttonArea.setMinSize(1200,50);

        Label btn1_lbl = new Label(btn1_msg);
        btn1_lbl.setTextFill(Color.WHITE);
        btn1_lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));

        HBox btn1 = new HBox();
        btn1.getChildren().add(btn1_lbl);
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,button1);


        Label btn2_lbl = new Label(btn2_msg);
        btn2_lbl.setTextFill(Color.WHITE);
        btn2_lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));

        HBox btn2 = new HBox();
        btn2.getChildren().add(btn2_lbl);
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED,button2);
        buttonArea.getChildren().addAll(btn1,btn2);

        balken.getChildren().addAll(lbl,buttonArea);

        mainContainer.getChildren().addAll(balken);


        //Animate it



        FadeTransition ft1 = new FadeTransition(Duration.millis(800), balken);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);

        ScaleTransition str = new ScaleTransition(Duration.millis(800), balken);
        str.setFromX(1.1f);
        str.setFromY(1.1f);
        str.setToX(1f);
        str.setToY(1f);
        str.setAutoReverse(false);


        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft1, str);
        pt.play();


        TranslateTransition TT2 = new TranslateTransition(Duration.millis(800), lbl);

        TT2.setFromX(600);
        TT2.setFromY(00);

        TT2.setToX(0f);
        TT2.setToY(0);

        TT2.play();




        TranslateTransition TT = new TranslateTransition(Duration.millis(800), buttonArea);

        TT.setFromX(-600);
        TT.setFromY(00);

        TT.setToX(0f);
        TT.setToY(0);

        TT.play();





        return mainContainer;
    }



    public static HBox unlockedDialog( figures_name figure,String btn1_msg, EventHandler button1, String btn2_msg, EventHandler button2){


        //Create dialog
        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxSize(1200,600);
        mainContainer.setMinSize(1200,600);

        VBox balken = new VBox();
        balken.setAlignment(Pos.CENTER);
        balken.setMaxSize(1200,200);
        balken.setMinSize(1200,200);
        balken.setStyle("-fx-background-color: rgba(35,35,35,0.9); ");

        Label lbl = new Label("You unlocked the "+figure +" !");
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 60));

        ImageView img = new ImageView();
        img.setFitWidth(50);
        img.setFitHeight(50);
        img.setImage(new Image("/img/"+figure+"/"+figure+".png"));

        Label lbl2 = new Label("Do you want to try it out?");
        lbl2.setTextFill(Color.WHITE);
        lbl2.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));

        HBox buttonArea = new HBox();
        buttonArea.setAlignment(Pos.CENTER);
        buttonArea.setSpacing(50);
        buttonArea.setMaxSize(1200,50);
        buttonArea.setMinSize(1200,50);

        Label btn1_lbl = new Label(btn1_msg);
        btn1_lbl.setTextFill(Color.WHITE);
        btn1_lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));

        HBox btn1 = new HBox();
        btn1.getChildren().add(btn1_lbl);
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,button1);


        Label btn2_lbl = new Label(btn2_msg);
        btn2_lbl.setTextFill(Color.WHITE);
        btn2_lbl.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));

        HBox btn2 = new HBox();
        btn2.getChildren().add(btn2_lbl);
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED,button2);
        buttonArea.getChildren().addAll(btn1,btn2);

        balken.getChildren().addAll(lbl,img,lbl2,buttonArea);

        mainContainer.getChildren().addAll(balken);


        //Animate it



        FadeTransition ft1 = new FadeTransition(Duration.millis(800), balken);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);

        ScaleTransition str = new ScaleTransition(Duration.millis(800), balken);
        str.setFromX(1.1f);
        str.setFromY(1.1f);
        str.setToX(1f);
        str.setToY(1f);
        str.setAutoReverse(false);


        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft1, str);
        pt.play();


        TranslateTransition TT2 = new TranslateTransition(Duration.millis(800), lbl);
        TT2.setFromX(600);
        TT2.setFromY(00);
        TT2.setToX(0f);
        TT2.setToY(0);
        TT2.play();

        TranslateTransition TT3 = new TranslateTransition(Duration.millis(800), lbl2);
        TT3.setFromX(600);
        TT3.setFromY(00);
        TT3.setToX(0f);
        TT3.setToY(0);
        TT3.play();





        TranslateTransition TT = new TranslateTransition(Duration.millis(800), buttonArea);

        TT.setFromX(-600);
        TT.setFromY(00);

        TT.setToX(0f);
        TT.setToY(0);

        TT.play();




        return mainContainer;
    }


}
