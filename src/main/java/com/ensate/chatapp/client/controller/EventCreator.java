package com.ensate.chatapp.client.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EventCreator {
    public static EventHandler<KeyEvent> create (KeyCode code, Runnable funcEvent) {
        return 
            (keyEvent) -> {
                if (keyEvent.getCode() == code) {
                    funcEvent.run();
                }
            };
    }
}
