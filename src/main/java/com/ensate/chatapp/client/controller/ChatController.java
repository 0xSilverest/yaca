package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.interact.ReqList;

public class ChatController implements Initializable {
    @FXML
    private ListView<String> connectedView;

    @FXML
    private ListView<String> messagesView;

    @FXML 
    private TextField message;

    @FXML
    private TextField search;

    @FXML
    private Button sendBtn;

    @FXML
    private Button discBtn; 

    private static ObservableList<String> connectedList = FXCollections.observableArrayList();
    private ObservableList<String> messages = FXCollections.observableArrayList(List.of("this", "that", "other guy"));
    private String currentSelectedContact;

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        sendBtn.setOnAction((event) -> sendMessageEvent());
        discBtn.setOnAction((event) -> disconnect());

        message.setOnKeyPressed((keyEvent) -> 
                EventCreator.create (
                    KeyCode.ENTER, 
                    () -> sendMessageEvent()
                    )
                ); 

        connectedView
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((listener) -> {
                currentSelectedContact = connectedView.getSelectionModel().getSelectedItem();
            });

        connectedView.setItems(connectedList);
    
        messagesView.setItems(messages);
    }

    private void sendMessageEvent() {
        try {
            Client.sendMessage(currentSelectedContact, message.getText());
            message.clear();    
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

    public static void updateList() {
        Platform.runLater(() -> connectedList.setAll(Client.getOnlineUsers())); 
    }

    //public static void updateChatLog() {
    //    Platform.runLater(() -> messages.setAll(CLient.loadMessages()));
    //} 
}
