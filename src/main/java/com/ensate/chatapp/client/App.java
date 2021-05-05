package com.ensate.chatapp.client;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application { 
    private static Stage window;
    private static HashMap<String, Scene> scenes = new HashMap<>();
    private static String curr;

    public static boolean currentScene (String scene) {
        return curr == scene;
    }

    @Override
    public void start (Stage primaryStage) throws Exception { 
        Client.setConnection();
        System.out.println("Connected");
        window = primaryStage;
        window.setResizable(false);

        window.setScene(new Scene(loadFXML("login")));
        curr = "login";

        window.show();
    }
 
    public static void switchScene(String sceneNameFxml) {
        try {
            window.setScene(new Scene(loadFXML(sceneNameFxml)));
            curr = sceneNameFxml;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Parent loadFXML(String fxml) throws Exception {
        return FXMLLoader
                .load(App.class
                .getResource("/com/ensate/chatapp/client/resources/" + fxml + ".fxml"));
    } 

    public static void main(String[] args) throws IOException, InterruptedException { 
        launch(args);
    } 
}
