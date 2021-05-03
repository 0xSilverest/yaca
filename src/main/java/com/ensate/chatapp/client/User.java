package com.ensate.chatapp.client;

import java.security.NoSuchAlgorithmException;

import com.ensate.chatapp.utils.*;

public class User {
    private String username;
    private String password;

    User(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = StringUtils.encrypt(password);
    }
}
