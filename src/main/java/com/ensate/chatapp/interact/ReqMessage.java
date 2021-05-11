package com.ensate.chatapp.interact;

public class ReqMessage extends Request {
    private String sentFrom;
    private String sendTo;
    private String message;
    
    public ReqMessage(RequestType reqType, String sentFrom, String sendTo, String message) {
        this.reqType  = reqType;
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
