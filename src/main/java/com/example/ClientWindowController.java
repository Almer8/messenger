package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import message.Message;
import message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientWindowController implements Initializable {
    @FXML private Label usernameField;
    @FXML private Button sendButton;
    @FXML private TextArea msg;
    @FXML private Button refreshButton;
    @FXML private ListView<String> usersList;
    @FXML private TextFlow messageViewArea;
    private static Socket socket;
    private List<Message> messageList;
    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;
    private int selectedIndex = -1;

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
            this.messageList = new ArrayList<>();
            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.flush();
            objInput = new ObjectInputStream(socket.getInputStream());
            objOutput.writeObject(data.getUsername());
            objOutput.flush();
            new Thread(this::listener).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void sendMessageToUser() {
        if (usersList.getSelectionModel().getSelectedIndex() >=0) {
            Message message = new Message(data.getUsername(), usersList.getSelectionModel().getSelectedItem(), msg.getText(), MessageType.MESSAGE);
            try {
                objOutput.writeObject(message);
                objOutput.flush();
                System.out.println("Message sent");
                messageList.add(message);
                msg.clear();
                if (usersList.getSelectionModel().getSelectedItem().equals(message.getReceiver())) {
                    Text sender = new Text("You: ");
                    Text text = new Text(message.getMessageText() + "\n");
                    sender.setStyle("-fx-font-weight: bold");
                    messageViewArea.getChildren().add(sender);
                    messageViewArea.getChildren().add(text);
                }


            } catch (IOException e) {
                System.out.println("Output exception");
            }
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
                    System.out.println("Message added to list:");
                    System.out.println("From " + input.getSender() + " with text " + input.getMessageText());
                    if(usersList.getSelectionModel().getSelectedItem() != null) {
                        if (usersList.getSelectionModel().getSelectedItem().equals(input.getSender())) {
                            Text sender = new Text(input.getSender() + ": ");
                            Text text = new Text(input.getMessageText() + "\n");
                            sender.setStyle("-fx-font-weight: bold");
                            Platform.runLater(() -> {
                                messageViewArea.getChildren().add(sender);
                                messageViewArea.getChildren().add(text);
                            });
                        }
                    }
                }
                if (input.getMessageType().equals(MessageType.SERVER)){
                    if(input.getMessageText().equals("GET_USERS")) {
                        users = (List<String>) objInput.readObject();
                        System.out.println(data.getUsername());
                        users.removeIf(user -> user.equals(data.getUsername()));
                        Platform.runLater(() ->{
                        usersList.getItems().clear();
                        if (users != null) {
                            for (String user : users) {
                                usersList.getItems().add(user);
                            }
                        }
                        });
                    }
                    if (input.getMessageText().equals("DUPLICATE_USER")){
                        usernameField.setTextFill(Color.RED);
                        usernameField.setText("This user already exists!\n Closing the connection...");
                        closeConnection();
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e){
            try {
            socket.close();
            }
            catch (Exception ex){
                System.out.println("Double socket closing?");//
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

    public void refreshMessages(MouseEvent mouseEvent) throws IOException {
        if(usersList.getSelectionModel().getSelectedIndex() != selectedIndex){
            selectedIndex = usersList.getSelectionModel().getSelectedIndex();
            String selectedItem = usersList.getSelectionModel().getSelectedItem();
            messageViewArea.getChildren().clear();

            for (Message message:messageList) {
                if(message.getSender().equals(data.getUsername()) && message.getReceiver().equals(selectedItem)){

                    Text sender = new Text("You: ");
                    Text text = new Text(message.getMessageText() + "\n");
                    sender.setStyle("-fx-font-weight: bold");
                    messageViewArea.getChildren().add(sender);
                    messageViewArea.getChildren().add(text);
                } else if(message.getSender().equals(selectedItem) && message.getReceiver().equals(data.getUsername())){
                    Text sender = new Text(message.getSender() + ": ");
                    Text text = new Text(message.getMessageText() + "\n");
                    sender.setStyle("-fx-font-weight: bold");
                    Platform.runLater(() -> {
                        messageViewArea.getChildren().add(sender);
                        messageViewArea.getChildren().add(text);
                    });
                }
            }
        }

    }
}