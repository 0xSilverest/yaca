package com.ensate.chatapp.interact;

public class RespMessage extends Response {
    private final String sender;
    private final String msg;

    public RespMessage (String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
        this.responseType = ResponseType.MESSAGE;
    }

    public String getSender() {
        return sender;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return sender + ": " + msg;
    }
}
