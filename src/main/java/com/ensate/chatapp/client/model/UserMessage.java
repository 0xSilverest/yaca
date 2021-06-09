package com.ensate.chatapp.client.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import com.ensate.chatapp.interact.RespMessage;
import com.ensate.chatapp.utils.Runner;

public class UserMessage implements Serializable {

    private final LocalDateTime t;
    private final String sender;
    private final String message;
    private boolean read;

    /*public static UserMessage retrieve (RespMessage respMsg) {
        return new UserMessage(respMsg.getTime(), respMsg.getSender(), respMsg.getMsg());
    }*/

    public static UserMessage retrieve (RespMessage respMsg, byte[] symKey) {
        return new UserMessage(respMsg.getTime(), respMsg.getSender(), Runner.decrypt(Runner.initIdea(symKey), respMsg.getMsg()));
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

