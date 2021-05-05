package com.ensate.chatapp.client;

import java.io.EOFException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.interact.*;

public class Client {
    private static ClientConnection conn;
    private static String username;

    public static void setUsername(String accname) {
        username = accname;
    }

    public static void setConnection () throws IOException, InterruptedException {
        conn = new ClientConnection();
    }

    public static void sendRequest(Request req) throws IOException, InterruptedException {
        conn.send(req);
    }

    public static Response getResponse() throws IOException {
        try {
            Object obj = conn.receive();
            if (obj instanceof Response) 
                return (Response) obj; 
        } catch (EOFException | ClassNotFoundException  e) {
            System.out.println("Server own");
        }
        return new RespEmpty();
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

    public static void sendMessage (String sendTo, String message) throws IOException {
        conn.send(new ReqMessage(username, sendTo, message));
    }
}
