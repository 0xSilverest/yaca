package com.ensate.chatapp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.ensate.chatapp.Connection; 

public class ClientConnection extends Connection {
    ClientConnection() throws IOException, InterruptedException {
        int port = 8949;
        socket = new Socket("localhost", port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println(inputStream.readUTF());
    }

    public boolean isOn() {
        return !socket.isClosed();
    }
}
