package com.ensate.chatapp.interact;

public class ReqEmpty extends Request {
    public ReqEmpty () {
        this.reqType = RequestType.EMPTY;
    }

    @Override
    public void send() {}
}
