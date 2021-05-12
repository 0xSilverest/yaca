package com.ensate.chatapp.client;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

    public static void callFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to send");
        File selectedFile = fileChooser.showOpenDialog(window);

    }

    public static void switchScene(String sceneNameFxml) {
        try {
            window.setScene(new Scene(loadFXML(sceneNameFxml)));
            curr = sceneNameFxml;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
