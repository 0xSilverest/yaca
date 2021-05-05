package com.ensate.chatapp.interact;

public class RespSendFile extends Response {
    private String sender;
    private byte[] file;
    private int size;

    public static RespSendFile fromReq(ReqSendFile sendFile) {
        return RespSendFile(sendFile.getsender(), sendFile.getFile());
    }

    public RespSendFile (String sender, byte[] file) {
        this.sender = sender;
        this.file = file;
        this.size = file.length;
        this.responseType = ResponseType.SENDFILE; 
    }
}
