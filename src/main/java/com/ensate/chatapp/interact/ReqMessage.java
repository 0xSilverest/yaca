package com.ensate.chatapp.interact;

public class ReqMessage extends Request {
    private String sentFrom;
    private String sendTo;
    private String message;
    
    public ReqMessage(String sentFrom, String sendTo, String message) {
        this.reqType  = RequestType.SENDMES;
        this.sentFrom = sentFrom;
        this.sendTo   = sendTo;
        this.message  = message;
    }
    
    public String getSendTo() {
        return sendTo;
    }

    public String getSender() {
        return sentFrom;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public void send(){
    }
}
