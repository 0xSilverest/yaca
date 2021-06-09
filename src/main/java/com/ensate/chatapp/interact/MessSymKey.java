package com.ensate.chatapp.interact;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.ensate.chatapp.utils.RSA;
import com.ensate.chatapp.utils.idea.Idea;

public class MessSymKey implements Message {
    private byte[] keys;
    private String username;

    public MessSymKey(byte[] keys, PublicKey pk, String username) throws Exception {
        this.keys = RSA.encrypt(pk, keys);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getKey(PrivateKey sk) throws Exception {
         return RSA.decrypt(sk, keys);
    }
}
