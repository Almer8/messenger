package com.example;

import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField ip_field;
    @FXML
    private Label error_label;
    @FXML
    String getIPField(){
        
        return ip_field.getText();
    }

    @FXML
    Socket createSocket() throws Exception{
        try{
        Socket socket = new Socket(getIPField(), 8081);
        return socket;
    }
    catch (java.net.UnknownHostException e){
        System.out.println(e);
        error_label.setVisible(true);;
    }
    return null;
    }
    
    
}
