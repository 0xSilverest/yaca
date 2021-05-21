package com.ensate.chatapp.interact;

import java.time.LocalDateTime;

public class RespSendFile extends RespMessage {
    private byte[] file;
    private String fileName;

    public static RespSendFile fromReq(ReqSendFile sendFile) {
        return new RespSendFile(
                sendFile.getTime(),
                sendFile.getSender(),
                sendFile.getMsg(),
                sendFile.getFileName(),
                sendFile.getFile(),
                sendFile.getReqType().equals(RequestType.SENDFILE)?
                    ResponseType.SENDFILE:
                    ResponseType.BROADCASTFILE);
    }

    public RespSendFile (LocalDateTime t, String sender, String msg, String fileName, byte[] file, ResponseType resp) {
        super (t, sender, msg, ResponseType.SENDFILE); 
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
