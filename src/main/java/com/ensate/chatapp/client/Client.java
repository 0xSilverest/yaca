package com.ensate.chatapp.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.interact.*;

public class Client {
    private static ClientConnection conn;
    
    public static void setConnection () throws IOException, InterruptedException {
        conn = new ClientConnection();
    }

    public static void sendRequest(Request req) throws IOException, InterruptedException {
        conn.send(req);
    }

    public static Response getResponse() throws IOException, ClassNotFoundException {
        Object obj = conn.receive();
        if (obj instanceof Response) 
            return (Response) obj;
        else return new RespEmpty();
    }

    public static void login (String account, String password) throws IOException, NoSuchAlgorithmException {
        conn.send(new ReqAcc(account, password, RequestType.LOGIN));
    }

    public static void exit () throws IOException {
        if (conn.isOn()) {  
            conn.send(new ReqExit());
            conn.shutdown();
        }
    }
    
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
}
