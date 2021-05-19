package com.ensate.chatapp.client.model;

import java.io.*;
import java.time.LocalDateTime;

public class FileMessage extends UserMessage {
    private final String fileName;
    private final byte[] file;
    private final FileType fileType;

    public FileMessage(LocalDateTime t, String sender, String message, String fileName, byte[] file) {
        super(t, sender, message);
        this.fileName = fileName;
        this.file = file;
        
        String[] sp = fileName.split("\\."); 
        fileType = FileType.parse(sp[sp.length - 1]);
    }
    
    public FileType getFileType() {
        return fileType;
    }

    public String getFileName () {
        return fileName;
    }

    public void makeFile(String dir) {
        File path = new File(dir + "/" + fileName);
 
        try { 
            OutputStream os = new FileOutputStream(path);
            os.write(file);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    } 
}
