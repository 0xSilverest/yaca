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
    
    @Override
    public void start (Stage primaryStage) throws Exception { 
        Client.setConnection();
        System.out.println("Connected");
        window = primaryStage;
        window.setResizable(false);

        scenes.put("login", new Scene(loadFXML("login.fxml")));
        scenes.put("chatapp", new Scene(loadFXML("app.fxml")));

        window.setScene(scenes.get("login"));
        window.show();
    }
 
    public static void switchScene(String sceneName) {
        if (scenes.containsKey(sceneName))
            window.setScene(scenes.get(sceneName)); 
    }

    public Parent loadFXML(String fxml) throws Exception {
        return FXMLLoader
                .load(getClass()
                .getResource("/com/ensate/chatapp/client/resources/" + fxml));
    } 

    public static void main(String[] args) throws IOException, InterruptedException { 
        launch(args);
    } 
}
