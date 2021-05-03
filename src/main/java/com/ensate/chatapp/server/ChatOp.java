package com.ensate.chatapp.server;

import java.io.IOException;
import java.sql.SQLException;

import com.ensate.chatapp.server.db.*;

@FunctionalInterface
public interface ChatOp { 
    String execute();
}
 
class Register implements ChatOp {
    private final String username;
    private final String pw;

    Register (String username, String pw) {
        this.username = username;
        this.pw = pw;
    }

    @Override
    public String execute() {
        try {
            if (UserDb.createUser(username, pw))
                return "User registered succesfully";
            else  return "Username already taken";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error occured";
    }
}

class Authentificate implements ChatOp {
    private final String username;
    private final String pw;

    Authentificate (String username, String pw) {
        this.username = username;
        this.pw = pw;
    }

    @Override
    public String execute() {
        try {
            if (UserDb.authentificate(username, pw))
                return "200:Connected succesfully";
            else if (Server.isConnected(username)) 
                return "300:Account already in use";
            else return "300:Wrong entry";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "400:Error occured";
    }
}

class SendMessage implements ChatOp {
    private final ServerConnection sentFrom;
    private final String name;
    private final ServerConnection sendTo;
    private final String msg;

    SendMessage (ServerConnection sentFrom, String name, ServerConnection sendTo, String msg) {
        this.sentFrom = sentFrom;
        this.name = name;
        this.sendTo = sendTo;
        this.msg = msg;
    }

    @Override
    public String execute() {
        try {
            sentFrom.sendMessage("you: " + msg);
            sendTo.sendMessage(name + ": " + msg);
        } catch (IOException e) {
            e.printStackTrace();   
        }
        return ""; 
    }
}

class Broadcast implements ChatOp {
    private final ServerConnection sender;
    private final String name;
    private final String message;

    Broadcast (ServerConnection sentFrom, String name, String message) {
        this.sender = sentFrom;
        this.name = name;
        this.message = message;
    }

    @Override
    public String execute () {
        Server.sendMessage(name + ": " + message);
        return "";
    }
}

class ListUsers implements ChatOp {
    private final ServerConnection conn;
    
    ListUsers (ServerConnection conn) {
        this.conn = conn;
    }

    @Override
    public String execute () {
        
    }
}
