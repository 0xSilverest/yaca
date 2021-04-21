package com.ensate.chatapp.server;

import java.io.*;
import java.net.*;
import com.ensate.chatapp.Connection;

public class ServerConnection extends Connection {
    public ServerConnection(ServerSocket server) throws IOException {
        socket = server.accept();
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        this.sendMessage("Connected succesfully!");
    }
}
