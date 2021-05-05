package com.ensate.chatapp.client;

import java.io.IOException;

import com.ensate.chatapp.interact.*;

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
        try {
            parseReponse(Client.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
