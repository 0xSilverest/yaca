package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

import com.ensate.chatapp.interact.*;
import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.client.ResponseParser;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController implements Initializable {
    @FXML
    private Button exitBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField accName;

    @FXML
    private PasswordField password;
 
    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        loginBtn.setOnAction((event) -> loginEvent());
        exitBtn.setOnAction((event) -> exitEvent());

        List.of(accName,password).stream().forEach(x ->
            x.setOnKeyPressed(
                EventCreator.create (
                    KeyCode.ENTER, 
                    () -> loginEvent())
                )
            );
    }

    private void loginEvent() { 
        try {
        String acc = accName.getText();
        Client.login(acc, password.getText());
        if (Client.getResponse().getResponseType().equals(ResponseType.SUCC)) {
            Client.setUsername(acc);
            new ResponseParser().start();
            App.switchScene("chatapp");
        }
        } catch (NoSuchAlgorithmException 
                | IOException e) {
            e.printStackTrace();
        }
    }

    private void exitEvent() { 
        try {
            Client.exit();
            Platform.exit(); 
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
