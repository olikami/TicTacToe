
import controller.MainMenuController;
import controller.TicTacToeController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Intro;
import model.MainMenuModel;
import model.offline_game;
import view.IntroView;
import view.MainMenuView;
import view.TicTacToeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

import static javafx.application.Application.launch;

public class Main extends Application {



    Intro model;
    IntroView view;


    public static void main(String[] args) {
        launch(args);
        System.exit(1);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {



        model = new Intro(primaryStage);
        view = new IntroView(primaryStage,model);
        //garbage collector takes care of the objects. no need to destroy them

    }
}
