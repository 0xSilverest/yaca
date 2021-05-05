package com.ensate.chatapp.interact;

public class RespMessage extends Response {
    private final String sender;
    private final String msg;

    public RespMessage (String sender, String msg) {
        this.sender = sender;
        this.msg = msg;
        this.responseType = ResponseType.MESSAGE;
    }

    @Override
    public String toString() {
        return sender + ": " + msg;
    }
}
