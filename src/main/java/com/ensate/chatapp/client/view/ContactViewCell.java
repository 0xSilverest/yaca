package com.ensate.chatapp.client.view;

import com.ensate.chatapp.client.Client;
import com.ensate.chatapp.client.model.Contact;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContactViewCell extends ListCell<Contact> {

    @Override
    public void updateItem(Contact user, boolean empty) {
       super.updateItem(user, empty);
       if (user != null) {
            getStylesheets().add("file:///home/silverest/Coding/Java/shut-app/src/main/java/com/ensate/chatapp/client/resources/contactList.css");
            Label lblConn = new Label("● " + (user.isConnected()?"Online":"Offline"));
            Label lblUsr = new Label(user.getUsername());
            Label lblUnread = new Label(Client.getUnreadFor(user.getUsername()) + "");

            lblUsr.setStyle("-fx-text-fill:#" + (user.isConnected()?"000":"8e8b8e") + ";"  
                          + "-fx-font-weight:bold;");
            lblConn.setStyle("-fx-text-fill:#" + (user.isConnected()?"47a835":"8e8b8e") + ";");
            
            VBox boxyBoi = new VBox(lblUsr, lblConn);
            HBox root = new HBox(boxyBoi);
            if (!lblUnread.getText().equals("0")) {
                root.getChildren().add(lblUnread);
            }
            setGraphic(root);
       }
    }
}
