package com.ensate.chatapp.client;

import java.io.EOFException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ensate.chatapp.client.controller.ChatController;
import com.ensate.chatapp.interact.*;

public class Client {
    private static ClientConnection conn;
    private static String username;
    private static List<String> onlineUsers = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> chatLog = new HashMap<>();

    public static List<String> getOnlineUsers() {
        return onlineUsers;
    }

    public static void updateOnlineUsers(Set<String> users) {
        onlineUsers = new ArrayList<>(users
                                        .stream()
                                        .filter(u -> !username.equals(u))
                                        .collect(Collectors.toList()));
        ChatController.updateList();
    }

    public static void updateChatLog(String k, String msg) {
        if (!chatLog.containsKey(k))
            chatLog.put(k, new ArrayList<String>(List.of(msg)));
        else chatLog.get(k).add(msg);
        ChatController.updateChat();
    } 

    public static ArrayList<String> getChatLogFor(String contact) {
        if (chatLog.containsKey(contact))
            return chatLog.get(contact);
        else return new ArrayList<String>();
    }
    
    public static HashMap<String, ArrayList<String>> loadMessages() {
        return chatLog;
    }

    public static void shutdown() throws IOException {
        conn.shutdown();
    }

    public static boolean isOn () {
        return conn.isOn();
    }

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
            System.out.println("Server down");
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
        updateChatLog(sendTo, message);
    }

    public static void askForList() {
        try {
            sendRequest(new ReqList(username));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
