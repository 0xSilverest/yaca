package com.ensate.chatapp.interact;

import java.util.HashSet;
import java.util.Set;

public class RespUpdateList extends Response {
    private Set<String> loggedIns = new HashSet<>();

    public RespUpdateList (Set<String> loggedIns, ResponseType rt) {
        this.loggedIns = loggedIns;
        this.responseType = rt;
    }

    public Set<String> getLoggedIns() {
        return loggedIns;
    }
}
