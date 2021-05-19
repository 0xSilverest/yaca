package com.ensate.chatapp.interact;

import java.time.LocalDateTime;

public class RespMessage extends Response {
    private final LocalDateTime t;
    private final String sender;
    private final String msg;

    public RespMessage (LocalDateTime t, String sender, String msg, ResponseType responseType) {
        this.t = t;
        this.sender = sender;
        this.msg = msg;
        this.responseType = responseType;
    }

    public LocalDateTime getTime() {
        return t;
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
