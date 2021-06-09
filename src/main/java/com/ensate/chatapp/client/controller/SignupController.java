package com.ensate.chatapp.client.controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class SignupController implements Initializable {
    @FXML
    private TextField accName;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField password;
    
    @FXML
    private PasswordField password1;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;
 
    private void registerEvent() {
        try {
            Client.sendRegistration(accName.getText(), emailField.getText(), password.getText());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        confirmBtn.setOnAction(e -> registerEvent());

        cancelBtn.setOnAction(e -> App.switchScene("login"));
    }
}
