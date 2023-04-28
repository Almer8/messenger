package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    private TextField ip_field;
    @FXML
    private TextField usernameField;
    @FXML
    private Label error_label;
    @FXML
    private Button okButton;
    DataSingleton data = DataSingleton.getInstance();
    @FXML
    String getIP(){
        String[] parts = ip_field.getText().split(":");
        try {
            return parts[0];
            
        } catch (Exception e) {
            error_label.setText("Incorrect IP address!"); 
        }
        return null;
    }
    Integer getPort() throws Exception{
        String[] parts = ip_field.getText().split(":");
        try{
        return Integer.parseInt(parts[1]);
    }
    catch(Exception e){
        error_label.setText("Incorrect port!");
    }
    return null;

    }
    @FXML
    String getUsernameField(){
        return usernameField.getText();
    }
    

    @FXML
    Socket createSocket() throws Exception{
        
        try{
        Socket socket = new Socket(getIP(), getPort());
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        out.println(getUsernameField());
        out.println("\r");


        data.setUsername(getUsernameField());
        data.setSocket(socket);
        Stage stage = (Stage) okButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
        stage.setTitle("Messenger");
        stage.setScene(new Scene(root));



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
