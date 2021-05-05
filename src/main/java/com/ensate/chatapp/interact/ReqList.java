package com.ensate.chatapp.interact;

public class ReqList extends Request {
    private final String username;
    
    public ReqList (String username) {
        this.username = username;
        this.reqType = RequestType.ASKLIST;
    }

    public String getUsername () {
        return username;
    }

    @Override
    public void send(){

    }
}
