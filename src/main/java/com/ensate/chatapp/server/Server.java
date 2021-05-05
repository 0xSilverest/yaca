package com.ensate.chatapp.server;

import java.io.*;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.ensate.chatapp.interact.RespMessage;
import com.ensate.chatapp.interact.Response;
import com.ensate.chatapp.server.db.DBApi;

public class Server {
    private final static int PORT = 8949;
    private final static HashMap<String, ServerConnection> userSock = new HashMap<>(); 

    public static ServerConnection getSocketFor(String user) {
        return userSock.get(user);
    }

    public static boolean hasSocketFor(String user) {
        return userSock.containsKey(user);
    }

    public static void addSocketFor(String username, ServerConnection conn) {
        userSock.putIfAbsent(username, conn);
    }

    public static void broadcast (Response resp) {
        userSock.values().forEach(x -> {
            try {
               x.send(resp);    
            } catch (IOException e) {}
        });
    }

    public static Set<String> getConnectedList() {
        return new HashSet<String>(userSock.keySet());
    }

    public static boolean isConnected(String username) {
        return userSock.containsKey(username);
    }

    public static void removeUser(String username) {
        userSock.remove(username);
    }
    public static void main(String[] args)
            throws IOException, SQLException, ClassNotFoundException {
        ServerSocket server = new ServerSocket(PORT, 50);
        server.setReuseAddress(true);
        DBApi.startDB();
        System.out.println("Server started!");
        
        while (true) {
            try {
                ServerConnection conn = new ServerConnection(server);
                new ClientSession(conn).start(); 
            } catch (IOException e) {
                server.close();
                break;
            }
        }
    } 
}
