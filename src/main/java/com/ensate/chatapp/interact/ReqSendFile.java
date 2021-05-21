package com.ensate.chatapp.interact;

import java.time.LocalDateTime;

public class ReqSendFile extends ReqMessage {
    private final String fileName;
    private final byte[] file;

    public ReqSendFile (LocalDateTime t, String sentFrom, String sendTo, String message, String fileName, byte[] file, RequestType requestType) {
        super (t, requestType, sentFrom, sendTo, message);
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFile() {
        return file;
    }
}
