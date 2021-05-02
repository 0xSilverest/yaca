package com.ensate.chatapp.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.utils.StringUtils;

public class ClientSession extends Thread {
    
    private final ServerConnection conn;
    private final Session session;

    ClientSession (ServerConnection conn) {
        this.conn = conn;
        this.session = new Session();
    }

    private void menu (String query) throws IOException, NoSuchAlgorithmException {
        String[] strArr = query.split("\\s+", 2);
        String choice = strArr[0];
        String input = strArr.length > 1 ? strArr[1] : "";
    
        ChatOp chatop = null;

        switch (choice) {
            case "/register" : {
                    String[] data = input.split("\\s+");
                    if (data.length >= 2) {
                        chatop = new Register(data[0], StringUtils.encrypt(data[1])); 
                        conn.sendMessage(chatop.execute());
                    } else {
                        conn.sendMessage("Login/Password missing");
                    }
                    break;
                }
            case "/login" : {
                    String[] data = input.split("\\s+");
                    if (data.length >= 2) {
                        chatop = new Authentificate(data[0], StringUtils.encrypt(data[1]));
                        String response = chatop.execute();
                        if (response.equals("Connected succesfully")) {
                            conn.sendMessage(response);
                            session.assign(StringUtils.randomGen(), data[0]);
                            System.out.println(data[0]);
                        } else {
                            conn.sendMessage(response);
                        }
                        Server.addSocketFor(data[0], conn);
                    } else {
                        conn.sendMessage("Login/Password missing");
                    }
                    break;
                }
            case "/chat" : {
                    String[] data = input.split("\\s+", 2);
                    if (session.isAssigned()) { 
                        chatop = new SendMessage(conn, session.getUsername(), Server.getSocketFor(data[0]), data[1]);
                        chatop.execute();
                    } else {
                        conn.sendMessage("Error not connected");
                    }
                    break;
                }
            case "/broadcast" : {
                    if (session.isAssigned()) {
                        chatop = new Broadcast(conn, session.getUsername(), input);
                        chatop.execute();
                    } else {
                        conn.sendMessage("Error not connected");
                    }
                    break;
                }
            case "/disconnect" : {
                    this.session.close();
                    break;
                }
            case "/exit" :
                conn.shutdown();
                break;

            default :
                
        }

    }

    @Override
    public void run() {
        try {
            conn.sendMessage("Server: authorize or register");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String query = "";

        while (!query.equals("/exit")) {
            try {
                query = conn.getMessage();
                menu(query);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }                      
    }
}
