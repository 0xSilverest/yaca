package com.ensate.chatapp.client.model;

import java.util.List;

public enum FileType {
    IMAGE, VIDEO, DOC, AUDIO, OTHER;

    private static List<String> vidTypes = List.of("amv", "mp4", "avi", "flv", "wmv");
    private static List<String> imgTypes = List.of("jpg", "png", "gif", "jpeg");
    private static List<String> textTypes = List.of("txt", "docx", "pdf", "csv");
    private static List<String> audioTypes = List.of("mp3", "wav", "wv", "flac");

    public static FileType parse (String typeStr) {
        if (vidTypes.contains(typeStr)) {
            return VIDEO;
        } 
        
        if (imgTypes.contains(typeStr)) {
            return IMAGE;
        } 

        if (textTypes.contains(typeStr)) {
            return DOC;
        } 

        if (audioTypes.contains(typeStr)) {
            return AUDIO;
        }

        return OTHER;
    }
}
