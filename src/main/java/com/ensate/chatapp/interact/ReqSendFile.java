package com.ensate.chatapp.interact;

public class ReqSendFile extends Request {
    private String sentFrom;
    private String sendTo;
    private byte[] file;

    public ReqSendFile (String sentFrom, String sendTo, byte[] file) {
        this.sentFrom = sentFrom;
        this.sendTo = sendTo;
        this.file = file;
        this.reqType = RequestType.SENDFILE;
    }

    public byte[] getFile() {
        return file;
    }

    public String getSendTo() {
        return sendTo;
    }

    public String getSender() {
        return sentFrom;
    }

    @Override
    public void send(){

    }
}
