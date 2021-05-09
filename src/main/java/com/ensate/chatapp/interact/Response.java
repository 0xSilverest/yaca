package com.ensate.chatapp.interact;

public abstract class Response implements Message {
    protected ResponseType responseType;
    protected RespStatus status;

    public ResponseType getResponseType() {
        return responseType;
    }

    public RespStatus getStatus() {
        return status;
    }
}

