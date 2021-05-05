package com.ensate.chatapp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.ensate.chatapp.interact.*;

public class Connection {
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    protected Socket socket;

    public Message receive() throws IOException, ClassNotFoundException {
        Object obj = ois.readObject();
        if (obj instanceof Message)
            return (Message) obj;
        return null;
    }

    public void send(Message msg) throws IOException {
        oos.writeObject(msg);
    }

    public void shutdown() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
}
