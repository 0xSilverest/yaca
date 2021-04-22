package com.ensate.chatapp.server;

class Session {
    private String id;
    private String username;

    Session () {}

    Session (String id, String username) {
        assign(id, username);
    }

    public void assign(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public void close() {
        this.id = "";
        this.username = "";
    }
}
