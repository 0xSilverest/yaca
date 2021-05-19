package com.ensate.chatapp.interact;

public class RespEmpty extends Response {
    public RespEmpty() {
        responseType = ResponseType.EMPTY;
        status = RespStatus.SUCCESS;    
    }
}
