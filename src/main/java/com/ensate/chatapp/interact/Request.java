package com.ensate.chatapp.interact;

public abstract class Request implements Message {
    protected RequestType reqType;

    public RequestType getReqType() {
        return reqType;
    }
}

