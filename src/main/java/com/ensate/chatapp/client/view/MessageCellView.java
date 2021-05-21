package com.ensate.chatapp.client.view;

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
        switch(flMsg.getFileType()) {
            case IMAGE:
                flMsg.makeFile("/home/silverest/tmp");
                Image img = new Image("file:///home/silverest/tmp/"+flMsg.getFileName());
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
                flMsg.makeFile("/home/silverest/tmp");
                //Media media = new Media(new File("file:///home/silverest/tmp/"+flMsg.getFileName()).toURI().toString());
                //MediaPlayer mediaPlayer = new MediaPlayer(media);  
                break;
    
            case OTHER:
                break;
        }
    }

}
