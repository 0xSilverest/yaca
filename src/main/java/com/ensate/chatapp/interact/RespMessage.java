package com.ensate.chatapp.interact;

import java.time.LocalDateTime;
import java.util.List;

import com.ensate.chatapp.utils.idea.IdeaBlock;

public class RespMessage extends Response {
    private final LocalDateTime t;
    private final String sender;
    private final List<IdeaBlock> msg;

    public RespMessage (LocalDateTime t, String sender, List<IdeaBlock> msg, ResponseType responseType) {
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

    public List<IdeaBlock> getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return sender + ": " + msg;
    }
}
