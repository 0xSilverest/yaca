package com.ensate.chatapp.client;

import com.ensate.chatapp.interact.RespMessage;

public class UserMessage {
    private final String sender;
    private final String message;

    public static UserMessage retrieve (RespMessage respMsg) {
        return new UserMessage(respMsg.getSender(), respMsg.getMsg());
    }

    public UserMessage (String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public String toString() {
        return sender + ":\n " + message;
    }    
}

