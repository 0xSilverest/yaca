package com.ensate.chatapp.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.Utils.StringUtils;

public class ClientSession extends Thread {
    
    private final ServerConnection conn;

    ClientSession (ServerConnection conn) {
        this.conn = conn;
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
                        conn.sendMessage(chatop.execute());
                        Server.addSocketFor(data[0], conn);
                    } else {
                        conn.sendMessage("Login/Password missing");
                    }
                    break;
                }
            case "/chat" : {
                    String[] data = input.split("\\s+", 2);
                    chatop = new SendMessage(conn, Server.getSocketFor(data[0]), data[1]);
                    chatop.execute();
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
