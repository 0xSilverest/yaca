package com.ensate.chatapp.interact;

import java.time.LocalDateTime;

public class ReqMessage extends Request {
    private LocalDateTime t;
    private String sentFrom;
    private String sendTo;
    private String message;
    
    public ReqMessage(LocalDateTime t, RequestType reqType, String sentFrom, String sendTo, String message) {
        this.t = t;
        this.reqType  = reqType;
        this.sentFrom = sentFrom;
        this.sendTo   = sendTo;
        this.message  = message;
    }
    
    public LocalDateTime getTime() {
        return t;
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
}
