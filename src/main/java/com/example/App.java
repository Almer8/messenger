package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    
    }
    static void changeStageResizableProperty(){
        if(stage.resizableProperty().get()){
            stage.setResizable(false);
        }
        else{
            stage.setResizable(true);
        }
    }

    public static void main(String[] args) {
        launch();
    }
    

}