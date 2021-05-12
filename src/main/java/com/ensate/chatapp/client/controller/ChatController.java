package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.client.UserMessage;

public class ChatController implements Initializable {
    public static void updateList() {
        Platform.runLater(() -> connectedList.setAll(Client.getOnlineUsers()));
    }

    public static void updateChat() {
        Platform.runLater(() -> {
            if (groupSelected) {
                messages
                    .setAll(Client.getGeneralChat());
            } else {
                messages
                    .setAll(Client.getChatLogFor(currentSelectedContact));    
            }
        });
    }

    @FXML
    private ListView<String> connectedView;

    @FXML
    private ListView<UserMessage> messagesView;

    @FXML
    private TextField message;

    @FXML
    private TextField search;

    @FXML
    private Button sendBtn;

    @FXML
    private Button discBtn;

    @FXML
    private Button generalBtn;
   
    @FXML 
    private Button sendFileBtn;

    @FXML
    private Button groupeChatBtn;

    private static boolean groupSelected=true;
    private static ObservableList<String> connectedList = FXCollections.observableArrayList();
    private static ObservableList<UserMessage> messages = FXCollections.observableArrayList();
    private static String currentSelectedContact;
    
    private void sendMessageEvent() {
        try {
            if (!groupSelected) {
                Client.sendMessage(currentSelectedContact, message.getText());
            } else {
                Client.broadcast(message.getText());
            }
            message.clear();    
            updateChat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void disconnect() {
        try {
            Client.exit();
            Platform.exit();    
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileEvent() {
        App.callFileChooser();
    }

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        connectedView
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((listener) -> {
                groupSelected = false;
                currentSelectedContact = connectedView.getSelectionModel().getSelectedItem(); 
                System.out.println(currentSelectedContact);
                updateChat();
            });

        connectedView.setItems(connectedList);
    
        messagesView.setItems(messages);

        message.setOnKeyPressed(
                EventCreator.create (
                    KeyCode.ENTER, 
                    () -> sendMessageEvent()  
                )); 

        groupeChatBtn.setOnAction((event) -> {
            connectedView.getSelectionModel().clearSelection();
            groupSelected = true;
            updateChat();
        });

        sendFileBtn.setOnAction((event) -> sendFileEvent());

        sendBtn.setOnAction((event) -> sendMessageEvent());
        discBtn.setOnAction((event) -> disconnect());
    }
}
