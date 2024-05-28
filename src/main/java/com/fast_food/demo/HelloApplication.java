package com.fast_food.demo;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.InitialScene;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try{
            InitialScene ie = new InitialScene();
            ie.start(stage);
        } catch (Exception e) {e.printStackTrace();}
    }


    public static void main(String[] args) {
        launch();
    }
}