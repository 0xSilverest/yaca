package com.ensate.chatapp.server;

import java.io.IOException;
import java.sql.SQLException;

import com.ensate.chatapp.server.db.*;
import com.ensate.chatapp.utils.StringUtils;
import com.ensate.chatapp.interact.*;

@FunctionalInterface
public interface ChatOp { 
    void execute();
}
 
class Register implements ChatOp {
    private final String username;
    private final String pw;
    private final ServerConnection conn;

    Register (ReqAcc req, ServerConnection conn) {
        this.username = req.getUsername();
        this.pw = req.getPw();
        this.conn = conn;
    }

    @Override
    public void execute() {
        try {
            if (UserDb.createUser(username, pw))
                conn.send(new RespSucc());
            else   
                conn.send(new RespFail("Username already in use"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

class Authentificate implements ChatOp {
    private final ServerConnection conn;
    private final String username;
    private final String pw;
    private final Session session;

    Authentificate (ReqAcc req, ServerConnection conn, Session session) {
        this.username = req.getUsername();
        this.pw = req.getPw();
        this.conn = conn;
        this.session = session;
    }

    @Override
    public void execute() {
        try {
            if (UserDb.authentificate(username, pw)) {
                System.out.println(username);
                conn.send(new RespSucc());
                session.assign(StringUtils.randomGen(), username);
                Server.addSocketFor(username, conn);
            } else if (Server.isConnected(username)) 
                conn.send(new RespFail("User already connected."));
            else 
                conn.send(new RespFail("Wrong username/password"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

class SendMessage implements ChatOp {
    private final ReqMessage req;

    SendMessage (ReqMessage req) {
        this.req = req;
    }

    @Override
    public void execute() {
        try {
            Server
                .getSocketFor(req.getSendTo())
                .send(new RespMessage(req.getSender(), req.getMsg()));
            System.out.println(req);            
        } catch (IOException e) {
            e.printStackTrace();   
        }
         
    }
}

class Broadcast implements ChatOp {
    private final ReqMessage req;

    Broadcast (ReqMessage req) {
        this.req = req;
    }

    @Override
    public void execute () {
        Server.sendMessage(new RespMessage(req.getSender(), req.getMsg()));
        System.out.println(req);        
    }
}

class ListUsers implements ChatOp {
    private final ReqList req;

    ListUsers (ReqList req) {
        this.req = req;
    }

    @Override
    public void execute () {
        try {
            Server
                .getSocketFor(req.getUsername())
                .send(new RespUpdateList(Server.getConnectedList())); 
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}

class SendFile implements ChatOp {
    private final ReqSendFile req;

    SendFile (ReqSendFile req) {
        this.req = req;
    } 

    @Override
    public void execute () {
        try {
            Server.getSocketFor(req.getSendTo()).send(RespSendFile.fromReq(req));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DefChatOp implements ChatOp {
    @Override
    public void execute () {}
}
