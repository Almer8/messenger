package com.example;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ClientWindowController implements Initializable {
    @FXML
    private Label usernameField;
    DataSingleton data = DataSingleton.getInstance();
    public void setTextToUsernameField(String text){
    usernameField.setText(text);    

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setTextToUsernameField(data.getUsername());
        
    }

    
}