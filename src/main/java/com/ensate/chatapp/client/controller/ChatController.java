package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import com.ensate.chatapp.client.Client;

public class ChatController implements Initializable {

    @FXML 
    TextField message;

    @FXML
    TextField search;

    @FXML
    private void disconnect(ActionEvent event) throws IOException {
        Client.exit();
        Platform.exit();
    }

    @FXML
    private void sendMessage(ActionEvent event) throws IOException {
        System.out.println(message.getText());
    }

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        
    }
}
