package com.ensate.chatapp.client;

import java.io.IOException;
import java.util.stream.*;
import com.ensate.chatapp.interact.*;
import javafx.application.Platform;
import com.ensate.chatapp.client.model.*;

public class ResponseParser extends Thread {
    public static void parseReponse(Response resp) {
        switch (resp.getResponseType()) {
            case MESSAGE:
                //TODO add unread
                RespMessage respMsg = (RespMessage) resp;
                Client.updateChatLog(respMsg.getSender(), UserMessage.retrieve(respMsg));
                break;

            case BROADCAST: 
                Client
                .updateGroupChat(
                    UserMessage.retrieve((RespMessage) resp));
                break;

            case SENDFILE:
                RespSendFile respF = (RespSendFile) resp;
                Client.updateChatLog(
                        respF.getSender(), 
                        new FileMessage(
                            respF.getTime(), 
                            respF.getSender(), 
                            respF.getMsg(), 
                            respF.getFileName(), 
                            respF.getFile()));
                break;

            case UPDATELIST:
                
                Client
                .updateOnlineUsers(
                    ((RespUpdateList) resp)
                    .getLoggedIns());
                break;

            case USERSLIST:
                Client.updateUsers(
                    ((RespUpdateList) resp).
                    getLoggedIns()
                    .stream()
                    .map(Contact::new)
                    .collect(Collectors.toSet())
                    );
                Client.askForList();
                break;
                
            case SUCC:
                System.out.println("success");
                break;

            case FAIL: 
                System.out.println(((RespFail) resp).getReason());
                break;

            case EMPTY:
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Response rsp = Client.getResponse();
                new Thread(() -> parseReponse(rsp)).start();;
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
