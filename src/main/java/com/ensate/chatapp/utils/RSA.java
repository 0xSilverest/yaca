package com.ensate.chatapp.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    enum KeyTypes {
        PK, SK;
    }

    public static PrivateKey retrieveSK (byte[] key) {
       return (PrivateKey) retrieveKey(key, KeyTypes.SK);
    }
    
    public static PublicKey retrievePK (byte[] key) {
       return (PublicKey) retrieveKey(key, KeyTypes.PK);
    }

    private static Key retrieveKey (byte[] key, KeyTypes t) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            EncodedKeySpec kSpec = new X509EncodedKeySpec(key);
            switch (t) {
                case PK:
                    return kf.generatePublic(kSpec);

                case SK:
                    return kf.generatePrivate(kSpec);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(PublicKey pk, byte[] data) 
            throws InvalidKeySpecException, NoSuchAlgorithmException,
                InvalidKeyException, NoSuchPaddingException, 
                BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, pk);
        return encryptCipher.doFinal(data);
    }

    public static byte[] decrypt(PrivateKey sk, byte[] encryptedData)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException
            , NoSuchPaddingException, NoSuchAlgorithmException, IOException 
            , ClassNotFoundException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, sk);
        return decryptCipher.doFinal(encryptedData);
    }
}

