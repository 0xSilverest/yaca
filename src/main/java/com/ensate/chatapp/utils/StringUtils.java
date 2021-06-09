package com.ensate.chatapp.utils;

import java.math.BigInteger; 
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.Random; 
import java.nio.charset.Charset;

public class StringUtils {
    public static byte[] toSHA256(String input) throws NoSuchAlgorithmException { 
        return MessageDigest
            .getInstance("SHA-256")
            .digest(input.getBytes(StandardCharsets.UTF_8)); 
    }
    
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash); 
  
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
  
        while (hexString.length() < 32) { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
    }

    public static String encrypt(String str) throws NoSuchAlgorithmException {
       return toHexString(toSHA256(str)); 
    }

    public static String randomGen() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
