package com.ensate.chatapp.client.controller;

import java.io.File;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class LoginController implements Initializable {
    @FXML
    private Button exitBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField accName;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView imageView;

    @Override 
    public void initialize (URL url, ResourceBundle resources) {
        loginBtn.setOnAction((event) -> loginEvent());
        exitBtn.setOnAction((event) -> exitEvent());
        registerBtn.setOnAction(e -> {
            App.switchScene("register");
        });
        
        File file = new File("pics/logo.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        centerImage();

        List.of(accName,password).stream()
            .forEach(x ->
            x.setOnKeyPressed(
                EventCreator.create (
                    KeyCode.ENTER, 
                    () -> loginEvent())
                )
            );
    }

    public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    private void loginEvent() { 
        try {
            String acc = accName.getText();
            Client.login(acc, password.getText());
            if (Client.getResponse().getResponseType().equals(ResponseType.SUCC)) {
                Client.init(acc);
                App.switchScene("chatapp");
                Client.loadMessages();
                Client.loadGeneralChat();
            } else {
                Alert a = new Alert(AlertType.ERROR, "Error occured");
                a.show();
            }
        } catch (NoSuchAlgorithmException  
                | IOException
                | ClassNotFoundException e) {
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
