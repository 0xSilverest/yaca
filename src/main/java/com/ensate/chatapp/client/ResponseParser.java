package com.ensate.chatapp.client;

import java.io.IOException;

import com.ensate.chatapp.interact.*;

import javafx.application.Platform;

public class ResponseParser extends Thread {
    public static void parseReponse(Response resp) {
        switch (resp.getResponseType()) {
            case MESSAGE:
                //TODO add unread
                RespMessage respMsg = (RespMessage) resp;
                new Thread(() -> Client.updateChatLog(respMsg.getSender(), UserMessage.retrieve(respMsg))).start();
                break;

            case BROADCAST: 
                new Thread(() -> 
                        Client
                        .updateGroupChat(
                            UserMessage.retrieve((RespMessage) resp))
                        ).start();
                break;

            case SENDFILE:
                //TODO call TextArea and give hyperlink for file
                break;

            case UPDATELIST:
                new Thread(() -> Client.updateOnlineUsers(((RespUpdateList) resp).getLoggedIns())).start();
                break;

            case SUCC:
                System.out.println("success");
                break;

            case FAIL: 
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
