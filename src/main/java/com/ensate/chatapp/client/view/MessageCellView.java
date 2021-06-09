package com.ensate.chatapp.client.view;

import java.io.File;

import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.client.model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MessageCellView extends ListCell<UserMessage> {
    ListCell<UserMessage> cell = new ListCell<>();
    VBox root = new VBox();
    
    @Override
    public void updateItem(UserMessage msg, boolean empty) {
       super.updateItem(msg, empty);
       if (empty) {
            setGraphic(null);
       } else if (msg != null) {
            getStylesheets().add("file:///home/silverest/Coding/Java/shut-app/src/main/java/com/ensate/chatapp/client/resources/contactList.css");

            Label lbl = new Label(msg.getSender().equals(Client.getUsername())?"You":msg.getSender());
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            lbl.setPadding(new Insets(0, 0, 2, 0));

            Bubble b = new Bubble(msg); 

            VBox box = new VBox(lbl, b);
            
            if (msg instanceof FileMessage) 
                checkType((FileMessage) msg, box);

            setAlignment(msg.getSender().equals(Client.getUsername())?Pos.CENTER_LEFT:Pos.CENTER_RIGHT);
            box.setAlignment(msg.getSender().equals(Client.getUsername())?Pos.CENTER_LEFT:Pos.CENTER_RIGHT);
            setGraphic(box); 
       }
    }

    private void checkType (FileMessage flMsg, VBox box) {
        flMsg.makeFile("tmp/");
        String filePath = new File("tmp/"+flMsg.getFileName()).toURI().toString();
        switch(flMsg.getFileType()) {
            case IMAGE:
                Image img = new Image(filePath);
                ImageView imgVw = new ImageView(img); 
                imgVw.setFitWidth(400);
                imgVw.setPreserveRatio(true);
                imgVw.setCache(true);
                box.getChildren().add(imgVw);
                break;
    
            case VIDEO:
                break;
            
            case DOC:
                break;
            
            case AUDIO:
                AudioPlayer audioPlayer = new AudioPlayer(filePath);
                box.getChildren().add(audioPlayer);
                break;
    
            case OTHER:
                break;
        }
    }

}
