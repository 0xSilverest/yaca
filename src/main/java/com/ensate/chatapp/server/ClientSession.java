package com.ensate.chatapp.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.interact.*;
import com.ensate.chatapp.utils.StringUtils;

public class ClientSession extends Thread {
    
    private final ServerConnection conn;
    private final Session session;

    ClientSession (ServerConnection conn) {
        this.conn = conn;
        this.session = new Session();
    }

    private void menu (Request req) throws IOException, NoSuchAlgorithmException {
        ChatOp chatOp = new DefChatOp();

        switch (req.getReqType()) {
            case REG : {
                chatOp = new Register((ReqAcc) req, conn);
                break;
            }

            case LOGIN : {
                chatOp = new Authentificate((ReqAcc) req, conn, session);
                break;
            }

            case SENDMES : {
                chatOp = new SendMessage((ReqMessage) req);
                break;
            }

            case SENDFILE : {
                chatOp = new SendFile((ReqSendFile) req);
                break;
            }

            case BROADCAST : {
                chatOp = new Broadcast((ReqMessage) req);
                break;
            }

            case ASKLIST : {
                chatOp = new ListUsers((ReqList) req);
                break;
            }

            case DISC : 
                break;

            case EXIT :
                conn.shutdown();
                break;

            case EMPTY :
                chatOp = new DefChatOp();
                break;
        }

        chatOp.execute();

    }

    @Override
    public void run() {
        Request query = new ReqEmpty();

        while (!query.getReqType().equals(RequestType.EXIT)) {
            try {
                Object obj = conn.receive();
                if (obj instanceof Request) {
                    query = (Request) obj;
                    menu(query);
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }                      
    }
}
