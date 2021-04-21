package com.ensate.chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    protected Socket socket;

    public String getMessage() throws IOException {
        return inputStream.readUTF();
    }

    public void sendMessage(String msg) throws IOException {
        outputStream.writeUTF(msg);
    }

    public void shutdown() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
