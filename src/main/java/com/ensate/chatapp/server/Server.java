package com.ensate.chatapp.server;

import java.io.*;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashMap;

import com.ensate.chatapp.server.db.DBApi;

public class Server {
    private final static int PORT = 8949;
    private final static HashMap<String, ServerConnection> userSock = new HashMap<>(); 

    public static ServerConnection getSocketFor(String user) {
        return userSock.get(user);
    }

    public static void addSocketFor(String username, ServerConnection conn) {
        userSock.putIfAbsent(username, conn);
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
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
