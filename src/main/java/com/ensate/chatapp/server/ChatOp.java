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
                return "Connected succesfully";
            else return "Wrong entry";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error occured";
    }
}

class SendMessage implements ChatOp {
    private final ServerConnection sentFrom;
    private final ServerConnection sendTo;
    private final String msg;

    SendMessage (ServerConnection sentFrom, ServerConnection sendTo, String msg) {
        this.sentFrom = sentFrom;
        this.sendTo = sendTo;
        this.msg = msg;
    }

    @Override
    public String execute() {
        try {
            sendTo.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();   
        }
        return ""; 
    }
}

