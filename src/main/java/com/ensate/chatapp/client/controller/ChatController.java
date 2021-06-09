package com.ensate.chatapp.client.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
import com.ensate.chatapp.client.model.*;
import com.ensate.chatapp.client.view.*;

public class ChatController implements Initializable {
    public static void updateList() {
        Platform.runLater(() -> {
            connectedList.setAll(Client.getOnlineUsers());
        });
    }

    public static void updateChat() {
        Platform.runLater(() -> {
            messages.clear();
            if (groupSelected) {
                messages
                    .addAll(Client.getGeneralChat());
            } else {
                messages
                    .addAll(Client.getChatLogFor(currentSelectedContact));    
            }
        });
    }

    @FXML
    private ListView<Contact> connectedView;

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
    private Button groupChatBtn;

    private static boolean groupSelected=true;
    private static ObservableList<Contact> connectedList = FXCollections.observableArrayList();
    private static ObservableList<UserMessage> messages = FXCollections.observableArrayList();
    private static String currentSelectedContact;

    private void sendMessageEvent() {
        String msg = message.getText();
        if (!msg.isBlank()) {
            try {
                if (!groupSelected) {
                    Client.sendMessage(LocalDateTime.now(), currentSelectedContact, msg);
                } else {
                    Client.broadcast(LocalDateTime.now(), msg);
                }
                message.clear();    
                updateChat();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        File selectedFile = App.callFileChooser();

        if (selectedFile != null) {
            try { 
                if (groupSelected) 
                    Client.broadcastFile(LocalDateTime.now(), selectedFile.getName(), Files.readAllBytes(Paths.get(selectedFile.getPath())));
                else
                    Client.sendFile(LocalDateTime.now(), currentSelectedContact, selectedFile.getName(), Files.readAllBytes(Paths.get(selectedFile.getPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        messagesView.setFocusTraversable( false );
        
        connectedView
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((listener) -> {
                groupSelected = false;
                if (connectedView.getSelectionModel().getSelectedItem() != null)
                    currentSelectedContact = connectedView.getSelectionModel().getSelectedItem().getUsername(); 
                else 
                    currentSelectedContact = "";
                updateChat();
            });

        connectedView.setItems(connectedList);
        connectedView.setCellFactory(param -> new ContactViewCell());

        messagesView.setItems(messages);
        messagesView.setCellFactory(param -> new MessageCellView());

        message.setOnKeyPressed(
                EventCreator.create (
                    KeyCode.ENTER, 
                    () -> sendMessageEvent()  
                )); 

        groupChatBtn.setOnAction((event) -> {
            connectedView.getSelectionModel().clearSelection();
            groupSelected = true;
        });

        sendFileBtn.setOnAction((event) -> sendFileEvent());

        sendBtn.setOnAction((event) -> sendMessageEvent());
        discBtn.setOnAction((event) -> disconnect());
    }
}
