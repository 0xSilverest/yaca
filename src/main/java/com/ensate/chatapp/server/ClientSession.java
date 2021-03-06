package com.ensate.chatapp.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import com.ensate.chatapp.interact.*;

public class ClientSession extends Thread {
    
    private final ServerConnection conn;
    private final Session session;
    private PublicKey pk;

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

            case BROADCASTFILE : {
                chatOp = new BroadcastFile((ReqSendFile) req);
                break;
            }

            case ASKLIST : {
                chatOp = new ListUsers((ReqList) req);
                break;
            }

            case DISC : 
                chatOp = new Disconnect(session.getUsername());
                break;

            case EXIT : 
                conn.shutdown();
                chatOp = new Disconnect(session.getUsername());
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
                } else if (obj instanceof MessPubKey) {
                    pk = ((MessPubKey) obj).getKey();
                    Server.addKey(session.getUsername(), pk);                     
                    Server.getConnectedList()
                        .forEach(x -> {
                                if (!x.equals(session.getUsername())) {
                                    try { 
                                        Server.createSymAndSend(session.getUsername(), x); 
                                    } catch (Exception e) {
                                        e.printStackTrace(); 
                                    }
                                    
                                }
                            }
                        );
                }
            } catch (EOFException | SocketException e) {
                System.out.println("Client " + session.getUsername() + " interrupted");
                query = new ReqExit();
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }                      

        try {
            menu(new ReqExit());
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }
}
