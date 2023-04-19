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
        
        try{
        Socket socket = new Socket(getIPField(), 8081);
        data.setUsername(getUsernameField());
        
        
        return socket;
    }



    catch (java.net.UnknownHostException e){
        System.out.println(e);
        error_label.setText("There is no server with this IP address!");
    }
    catch(java.net.ConnectException e){
        error_label.setText("Connection refused!"); 
    }
    catch(java.net.SocketException e){
        error_label.setText("Network is unreachable!");
    }
    catch(Exception e){
        System.out.println(e);
    }
    
    return null;
    }
    
    
}
