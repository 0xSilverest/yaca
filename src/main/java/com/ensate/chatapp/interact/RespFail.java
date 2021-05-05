package com.ensate.chatapp.interact;

public class RespFail extends Response {
    String reason;

    public RespFail (String reason) {
        responseType = ResponseType.FAIL;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
