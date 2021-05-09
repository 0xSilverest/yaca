package com.ensate.chatapp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.ensate.chatapp.Connection; 

public class ClientConnection extends Connection {
    ClientConnection() throws IOException, InterruptedException {
        int port = 8949;
        socket = new Socket("localhost", port);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean isOn() {
        return !socket.isClosed();
    }
}
