package com.ensate.chatapp.interact;

import java.io.*;

public class RespSendFile extends Response {
    private String sender;
    private byte[] file;

    public static RespSendFile fromReq(ReqSendFile sendFile) {
        return new RespSendFile(sendFile.getSender(), sendFile.getFile());
    }

    public RespSendFile (String sender, byte[] file) {
        this.sender = sender;
        this.file = file;
        this.responseType = ResponseType.SENDFILE; 
    }
   
    public void downloadFile() {
        File path = new File("/home/silverest/demo.txt");
 
        try { 
            OutputStream os = new FileOutputStream(path);
            os.write(file);
            System.out.println("Write bytes to file.");
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
