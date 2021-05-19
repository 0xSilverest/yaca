package com.ensate.chatapp.client.model;

import java.io.*;

public class Contact {
    private byte[] image;
    private String username;
    private Boolean connected;

    public Contact (String username) {
        //this.image = image;
        this.username = username;
        this.connected = false;
    }

    public void saveImg() { 
      File path = new File("tmp/imgs" + username + ".png");
 
      try { 
          OutputStream os = new FileOutputStream(path);
          os.write(image);
          os.close();
      } catch (Exception e) {
          e.printStackTrace();
      } 
    }
    
    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return connected;
    }

    public void online() {
        connected = true;
    }

    public void offline() {
        connected = false;
    }
} 
