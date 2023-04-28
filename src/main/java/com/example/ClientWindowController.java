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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    @FXML private TextFlow messageViewArea;
    private static Socket socket;
    private List<Message> messageList;
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
            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.flush();
            objInput = new ObjectInputStream(socket.getInputStream());

            new Thread(this::listener).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void refreshUsers() throws IOException, ClassNotFoundException {
        System.out.println(socket.getInetAddress().getHostAddress() + ":" +socket.getPort() + ":" + socket.getLocalPort());//

        objOutput.writeObject(new Message(MessageType.SERVER, "GET_USERS"));
        objOutput.flush();
    }

    public void listener(){

        while (!socket.isClosed()){
        try {
            Message input = (Message) objInput.readObject();

            if(input != null) {
                if (input.getMessageType().equals(MessageType.MESSAGE)) {
                    messageList.add(input);
                    if (usersList.getSelectionModel().getSelectedItem().equals(input.getSender())) {
                        Text text = new Text(input.getMessageText() + "\n\n");
                        messageViewArea.getChildren().add(text);
                    }
                }
                if (input.getMessageType().equals(MessageType.SERVER)){
                    if(input.getMessageText().equals("GET_USERS")) {
                        users = (List<String>) objInput.readObject();
                        System.out.println(data.getUsername());
                        users.removeIf(user -> user.equals(data.getUsername()));
                        usersList.getItems().clear();
                        if (users != null) {
                            for (String user : users) {
                                usersList.getItems().add(user);
                            }
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e){
            try {
            socket.close();
            }
            catch (Exception ex){
                System.out.println(ex);
            }

        }
        }

    }
public static void closeConnection(){
        try {
        socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
}

}