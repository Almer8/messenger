package com.example;

import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class LoginController {
    @FXML
    private TextField ip_field;
    @FXML
    private TextField usernameField;
    @FXML
    private Label error_label;
    DataSingleton data = DataSingleton.getInstance();
    @FXML
    String getIPField(){
        
        return ip_field.getText();
    }
    @FXML
    String getUsernameField(){
        return usernameField.getText();
    }
    

    @FXML
    Socket createSocket() throws Exception{
        //TODO: move to try section later
        data.setUsername(getUsernameField());
        try{
        Socket socket = new Socket(getIPField(), 8081);
        App.setRoot("ClientWindow");
        App.changeStageResizableProperty();
        return socket;
    }
    catch (java.net.UnknownHostException e){
        System.out.println(e);
        error_label.setVisible(true);
        App.changeStageResizableProperty(); //temp
        App.setRoot("ClientWindow"); //temp
    }
    
    return null;
    }
    
    
}
