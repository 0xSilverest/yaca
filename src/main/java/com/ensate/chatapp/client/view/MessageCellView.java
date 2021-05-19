package com.ensate.chatapp.client.view;

import java.io.File;

import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.model.*;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
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

            Bubble b = new Bubble(msg); 

            setAlignment(msg.getSender().equals("you")?Pos.CENTER_LEFT:Pos.CENTER_RIGHT);
            setGraphic(b);
       }
    }
}
