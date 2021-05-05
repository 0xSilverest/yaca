package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import com.ensate.chatapp.interact.*;
import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.client.ResponseParser;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    @FXML
    private Button exitBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField accName;

    @FXML
    private PasswordField password;

    @FXML
    private void onLoginReq(ActionEvent event) throws NoSuchAlgorithmException, IOException, ClassNotFoundException { 
        String acc = accName.getText();
        Client.login(acc, password.getText());
        if (Client.getResponse().getResponseType().equals(ResponseType.SUCC)) {
            Client.setUsername(acc);
            new ResponseParser().start();
            App.switchScene("chatapp");
        }
    }

    @FXML
    private void onExitReq(ActionEvent event) throws IOException{
        Client.exit();
        Platform.exit();
    }

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
       
    }
}
