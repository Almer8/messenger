package com.example;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import message.Message;
import message.MessageType;

public class ClientWindowController implements Initializable {
    @FXML
    private Label usernameField;
    @FXML
    private Button sendButton;
    @FXML private TextArea msg;
    @FXML private Button refreshButton;
    @FXML private ListView<String> usersList;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    DataSingleton data = DataSingleton.getInstance();
    List<String> users = new ArrayList<>();
    public void setTextToUsernameField(String text){
    usernameField.setText(text);

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setTextToUsernameField(data.getUsername());
        socket = data.getSocket();
        try {
            //out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.flush();
            objInput = new ObjectInputStream(socket.getInputStream());



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void refreshUsers() throws IOException, ClassNotFoundException {
        System.out.println(socket.getInetAddress().getHostAddress() + ":" +socket.getPort() + ":" + socket.getLocalPort());//

        objOutput.writeObject(new Message("GET_USERS", MessageType.SERVER));
        objOutput.flush();
        users = (List<String>) objInput.readObject();
        System.out.println(data.getUsername());
        users.removeIf(user -> user.equals(data.getUsername()));
        usersList.getItems().clear();
        if(users != null) {
            for (String user : users) {
                usersList.getItems().add(user);
            }
        }
    }

}