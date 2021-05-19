package com.ensate.chatapp.client.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ensate.chatapp.interact.RespMessage;

public class UserMessage implements Serializable {

    private final LocalDateTime t;
    private final String sender;
    private final String message;
    private boolean read;

    public static UserMessage retrieve (RespMessage respMsg) {
        return new UserMessage(respMsg.getTime(), respMsg.getSender(), respMsg.getMsg());
    }

    public UserMessage (LocalDateTime t, String sender, String message) {
        this.t = t;
        this.sender = sender;
        this.message = message.replaceAll("(.{69})", "$1\n");
        this.read = false;
    }

    public LocalDateTime getTime() {
        return t;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public void seen () {
        read = true;
    }

    @Override
    public String toString() {
        return sender + ":\n " + message;
    }    
}

