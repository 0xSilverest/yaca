package com.ensate.chatapp.client;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.ensate.chatapp.client.controller.ChatController;
import com.ensate.chatapp.client.model.*;
import com.ensate.chatapp.interact.*;
import com.ensate.chatapp.utils.*;
import com.ensate.chatapp.utils.idea.IdeaBlock;

public class Client {
    private static ClientConnection conn;
    private static String username;
    private static List<Contact> onlineUsers = new ArrayList<>();
    private static HashMap<String, ArrayList<UserMessage>> chatLog = new HashMap<>();
    private static HashMap<String, byte[]> sessionKeys = new HashMap<>();
    private static List<UserMessage> groupChat = new ArrayList<>();
    private static PrivateKey sk;
    private static PublicKey pk;
    
    public static void init (String accName) throws NoSuchAlgorithmException, IOException {
        username = accName;
        generateAsymKeys();
        sendKey();
        new ResponseParser().start();
    }

    private static void addSessionKey(MessSymKey messKey) {
        try {
            sessionKeys.putIfAbsent(messKey.getUsername(), messKey.getKey(sk));  
            System.out.println(new String(messKey.getKey(sk)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateAsymKeys () throws NoSuchAlgorithmException {
        KeyGen keyGen = new KeyGen(1024);
        keyGen.createKeys();
        sk = keyGen.getPrivateKey();
        pk = keyGen.getPublicKey();
    }

    public static void loadMessages (HashMap<String, ArrayList<UserMessage>> loadedChatLog) {
        chatLog = loadedChatLog ;
    }
    
    public static void loadGeneralChat (List<UserMessage> general) {
        groupChat = general;
    }

    public static void updateOnlineUsers(Set<String> onlines) {
        List<Contact> cpList = List.copyOf(onlineUsers);
        cpList.forEach(x -> {
            if (onlines.contains(x.getUsername())) {
                onlineUsers.remove(x);
                onlineUsers.add(0, x);
                x.online();
            } else {
                x.offline();
                sessionKeys.remove(x.getUsername());
            }
        });
        ChatController.updateList();
    }

    public static void updateUsers(Set<Contact> users) {
        onlineUsers = new ArrayList<>(users);
        ChatController.updateList();
    }

    public static byte[] getKey (String username) {
        return sessionKeys.get(username);
    } 

    public static void updateChatLog(String k, UserMessage msg) {
        if (!chatLog.containsKey(k))
            chatLog.put(k, new ArrayList<UserMessage>(List.of(msg)));
        else chatLog.get(k).add(msg);
        ChatController.updateChat();
    }; 

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
    
    public static void loadGeneralChat() throws IOException, ClassNotFoundException {
        Object obj = FileUtils.deserialize("general.log");
        if (obj != null && obj instanceof ArrayList<?>) {
            Client.loadGeneralChat((ArrayList<UserMessage>) obj);
            ChatController.updateChat();
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

    public static void sendRequest(Request req) throws IOException{

        conn.send(req);
    }

    public static void sendFile(LocalDateTime t, String sendTo, String fileName, byte[] fileBytes) throws IOException {
        updateChatLog(sendTo, new FileMessage(t, username, "placeholder", fileName, fileBytes));
        sendRequest (new ReqSendFile(t, username, sendTo, "placeholder".getBytes(), fileName, fileBytes, RequestType.SENDFILE));
    }

    public static void broadcastFile(LocalDateTime t, String fileName, byte[] fileBytes) throws IOException {
        updateGroupChat(new FileMessage(t, username, "placeholder", fileName, fileBytes));
        sendRequest (new ReqSendFile(t, username, "", "placeholder".getBytes(), fileName, fileBytes, RequestType.BROADCASTFILE));
    }

    public static String getUsername() {
        return username;
    }

    public static void disconnect() {
        try {
            sendRequest(new ReqDisconnect());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Response getResponse() throws IOException {
        try {
            Object obj = conn.receive();
            if (obj instanceof Response) 
                return (Response) obj; 
            else if (obj instanceof MessSymKey) {
                addSessionKey((MessSymKey) obj);
            }
        } catch (EOFException | ClassNotFoundException  e) {}
        return new RespEmpty();
    }

    public static void login (String account, String password) throws IOException, NoSuchAlgorithmException {
        sendRequest(new ReqAcc(account, password, RequestType.LOGIN));
    }

    public static void exit () throws IOException {
        if (conn.isOn()) {  
            sendRequest(new ReqExit());
            FileUtils.serialize(chatLog, username + ".log");
            FileUtils.serialize(groupChat, "general.log");
            conn.shutdown();
        }
    }

    public static void sendMessage (LocalDateTime t, String sendTo, String message) throws IOException {
        List<IdeaBlock> encwypt = Runner.encrypt(Runner.initIdea(sessionKeys.get(sendTo)) ,message); 
        sendRequest(new ReqMessage(t, RequestType.SENDMES, username, sendTo, encwypt));
        updateChatLog(sendTo, new UserMessage(t, username, message));
    }

    public static void broadcast (LocalDateTime t, String message) throws IOException {
        sendRequest(new ReqMessage(t, RequestType.BROADCAST, username, "", message.getBytes()));
    }

    public static void askForList() {
        try {
            sendRequest(new ReqList(username));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRegistration (String accname, String email, String password) throws IOException, NoSuchAlgorithmException {
        sendRequest(new ReqAcc(accname, password, RequestType.REG));
    }

    public static void sendKey () throws IOException {
        conn.send(new MessPubKey(pk));
    }
}
