package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import com.ensate.chatapp.client.Client;

public class ChatController implements Initializable {
    @FXML
    private ListView<String> connectedList;

    @FXML 
    private TextField message;

    @FXML
    private TextField search;

    @FXML
    private Set<String> onlineSet;

    @FXML
    private void disconnect(ActionEvent event) throws IOException {
        Client.exit();
        Platform.exit();
    }

    @FXML
    private void sendMessage(ActionEvent event) throws IOException {
        Client.sendMessage("test2", message.getText());
        message.clear();
    }

    public void updateList(Set<String> set) {
        ObservableList<String> connected = FXCollections.observableArrayList(set);
        connectedList.setItems(connected);
    }
    
    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        //Client.getOnlineList();
        //connectedList.getItems().setAll(onlineSet);
    }
}
