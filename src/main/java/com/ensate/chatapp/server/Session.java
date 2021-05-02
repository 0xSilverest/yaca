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

    public String getUsername () {
        return username;
    }

    public String getId () {
        return id;
    } 

    public boolean isAssigned () {
        return id != null && username != null;
    }

    public void close() {
        this.id = null;
        this.username = null;
    }
}
