package com.ensate.chatapp.interact;

import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.utils.StringUtils;

public class ReqAcc extends Request {
    private final String username;
    private final String pw;

    public ReqAcc (String username, String pw, RequestType type) throws NoSuchAlgorithmException {
        this.username = username;
        this.pw = StringUtils.encrypt(pw);
        this.reqType = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPw() {
        return pw;
    }
}
