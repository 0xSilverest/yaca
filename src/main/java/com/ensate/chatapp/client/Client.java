package com.ensate.chatapp.client;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private static ClientConnection conn;

    public static void main (String[] args) throws IOException, InterruptedException {
        conn = new ClientConnection();
        
        final Scanner in = new Scanner(System.in);

        String query = "";

        Runnable messaging = () -> {
            String msg = "";
            while (!msg.equals("/exit")) {
                msg = in.nextLine();
                try {
                    conn.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                conn.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Thread receivingThread = new Thread (() -> {
            while (conn.isOn()) {
                try {
                    String str = conn.getMessage();
                    if(str.length() > 0)
                        System.out.println(str);
                } catch (Exception ignored) {}
            }
        });

        receivingThread.start();
        messaging.run(); 
        in.close();
    }
}
