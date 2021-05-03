package com.ensate.chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected Socket socket;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;

    public String getMessage() throws IOException {
        return dis.readUTF();
    }

    public void sendMessage(String msg) throws IOException {
        dos.writeUTF(msg);
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
    }

    public void shutdown() throws IOException {
        dis.close();
        dos.close();
        oos.close();
        ois.close();
        socket.close();
    }
}
