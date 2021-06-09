package com.ensate.chatapp.client;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class App extends Application { 
    private static Stage window;
    private static String curr;

    public static boolean currentScene (String scene) {
        return curr == scene;
    }

    @Override
    public void start (Stage primaryStage) throws Exception { 
        Client.setConnection();
        System.out.println("Connected");
        window = primaryStage;
        window.setTitle("ChatApp");
        window.setResizable(false);

        window.setScene(new Scene(loadFXML("login")));
        curr = "login";

        window.show();
    }

    public static File callFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to send");
        return fileChooser.showOpenDialog(window);
    }
    
    public static File callDirChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select dir to save in");
        return dirChooser.showDialog(window);
    }

    public static void switchScene(String sceneNameFxml) {
        try {
            window.setScene(new Scene(loadFXML(sceneNameFxml)));
            curr = sceneNameFxml;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alert(String err) {
        Alert a = new Alert (AlertType.ERROR, err);
        a.show();
    }

    private static Parent loadFXML(String fxml) throws Exception {
        return FXMLLoader
                .load(App.class
                .getResource("/com/ensate/chatapp/client/resources/" + fxml + ".fxml"));
    } 

    public static void main(String[] args) throws IOException, InterruptedException { 
        launch(args);
    }
}
