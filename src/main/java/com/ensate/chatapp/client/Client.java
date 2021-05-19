package com.ensate.chatapp.client;

import java.io.EOFException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.ensate.chatapp.client.controller.ChatController;
import com.ensate.chatapp.client.model.*;
import com.ensate.chatapp.interact.*;
import com.ensate.chatapp.utils.*;

public class Client {
    private static ClientConnection conn;
    private static String username;
    private static List<Contact> onlineUsers = new ArrayList<>();
    private static HashMap<String, ArrayList<UserMessage>> chatLog = new HashMap<>();
    private static List<UserMessage> groupChat = new ArrayList<>();

    public static void loadMessages (HashMap<String, ArrayList<UserMessage>> loadedChatLog) {
        chatLog = loadedChatLog ;
    }

    public static void updateOnlineUsers(Set<String> onlines) {
        List<Contact> cpList = List.copyOf(onlineUsers);
        cpList.forEach(x -> {
            if (onlines.contains(x.getUsername())) {
                onlineUsers.remove(x);
                onlineUsers.add(0, x);
                x.online();
            } else x.offline();
        });
        ChatController.updateList();
    }

    public static void updateUsers(Set<Contact> users) {
        onlineUsers = new ArrayList<>(users);
        onlineUsers.forEach(System.out::println);
        ChatController.updateList();
    }

    public static void updateChatLog(String k, UserMessage msg) {
        if (!chatLog.containsKey(k))
            chatLog.put(k, new ArrayList<UserMessage>(List.of(msg)));
        else chatLog.get(k).add(msg);
        ChatController.updateChat();
    } 

    public static long getUnreadFor(String username) {
        return 
            getChatLogFor(username)
            .stream()
            .filter(x -> !x.isRead())
            .count();
    }
 
    public static void updateGroupChat(UserMessage msg) {
       groupChat.add(msg);
       ChatController.updateChat();
    }

    public static Response getResponse() throws IOException {
        try {
            Object obj = conn.receive();

            if (obj instanceof Response) 
                return (Response) obj; 
        } catch (EOFException | ClassNotFoundException  e) {}
        return new RespEmpty();
    }

    public static void login (String account, String password) throws IOException, NoSuchAlgorithmException {
        conn.send(new ReqAcc(account, password, RequestType.LOGIN));
    }

    public static void exit () throws IOException {
        if (conn.isOn()) {  
            conn.send(new ReqExit());
            FileUtils.serialize(chatLog, username + ".log");
            conn.shutdown();
        }
    }

    public static void sendMessage (LocalDateTime t, String sendTo, String message) throws IOException {
        conn.send(new ReqMessage(t, RequestType.SENDMES, username, sendTo, message));
        updateChatLog(sendTo, new UserMessage(t, "you", message));
    }

    public static void broadcast (LocalDateTime t, String message) throws IOException {
        conn.send(new ReqMessage(t, RequestType.BROADCAST, username, "", message));
    }

    public static void askForList() {
        try {
            sendRequest(new ReqList(username));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Contact> getOnlineUsers() {
        return onlineUsers;
    }

    public static List<UserMessage> getGeneralChat() {
        return groupChat;
    }

    public static ArrayList<UserMessage> getChatLogFor(String contact) {
        if (chatLog.containsKey(contact)) {
            ArrayList<UserMessage> cs = chatLog.get(contact);
            cs.sort((x, y) -> x.getTime().compareTo(y.getTime()));
            cs.forEach(UserMessage::seen);
            return cs;
        } else return new ArrayList<UserMessage>();
    }
    
    public static void loadMessages() throws IOException, ClassNotFoundException {
        Object obj = FileUtils.deserialize(username + ".log");
        if (obj != null && obj instanceof HashMap<?, ?>) {
            Client.loadMessages((HashMap<String,ArrayList<UserMessage>>) obj);
        }
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

    public static void sendFile(LocalDateTime t, String sendTo, String fileName, byte[] fileBytes) throws IOException, InterruptedException {
        updateChatLog(sendTo, new FileMessage(t, "you", "placeholder", fileName, fileBytes));
        conn.send(new ReqSendFile(t, username, sendTo, "placeholder", fileName, fileBytes));
    }

    public static String getUsername() {
        return username;
    }
}
