package com.ensate.chatapp.client;

import java.io.IOException;

import com.ensate.chatapp.interact.*;

import javafx.application.Platform;

public class ResponseParser extends Thread {
    public static void parseReponse(Response resp) {
        switch (resp.getResponseType()) {
            case MESSAGE:
                //TODO call TextArea and fill it with the message received 
                RespMessage respMsg = (RespMessage) resp;
                System.out.println(respMsg);
                break;

            case SENDFILE:
                //TODO call TextArea and give hyperlink for file
                break;

            case UPDATELIST:
                //TODO call ListView and update it
                new Thread(() -> Client.updateOnlineUsers(((RespUpdateList) resp).getLoggedIns())).start();
                break;

            case SUCC:
                //TODO Use for Login and Registration
                System.out.println("success");
                break;

            case FAIL:
                //TODO Use for Login and Registration
                System.out.println(((RespFail) resp).getReason());
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                parseReponse(Client.getResponse());
            } catch (IOException e) {
                try {
                    Client.shutdown();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Platform.exit();
            }
        }
    }
}
