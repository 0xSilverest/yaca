package com.ensate.chatapp.interact;

import java.security.PublicKey;

public class MessPubKey implements Message {
    private PublicKey key;

    public MessPubKey(PublicKey key) {
        this.key = key;
    }
    
    public PublicKey getKey() {
        return key;
    }
}
