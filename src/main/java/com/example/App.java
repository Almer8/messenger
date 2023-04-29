package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    
    }
    @Override
    public void stop(){
    ClientWindowController.closeConnection();
    }
    
    public static void main(String[] args) {
        launch();
    }
    

}