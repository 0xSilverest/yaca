package com.ensate.chatapp.utils;

import com.ensate.chatapp.utils.idea.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by krzysztofkaczor on 3/9/15.
 */
public class Runner {
    public static byte[] genKey() { 
        Random rand = new Random();
        byte[] keys = new byte[8];
        for (int i = 0; i < 8; i++) {
            keys[i] = (byte) rand.nextInt(0x0080);
        }
        return keys;
    }

    public static Idea initIdea(byte[] keys) {

        Byte[] bs = new Byte[8];
        for (int i = 0; i<8 ; i++)
            bs[i] = keys[i];
        
        IdeaKey key = new IdeaKey();
        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(bs).forEach(k -> key.setK(i.getAndIncrement(), k.byteValue()));
            
        System.out.println("Input key: " + key.toString());
    
        return new Idea(key);
    }

    public static List<IdeaBlock> encrypt(Idea idea, String stringToConvert) {
        System.out.println("Input string: " + stringToConvert);
        System.out.println("Input blocks:");
        StringToIdeaBlockConverter stringToIdeaBlockConverter = new StringToIdeaBlockConverter();
        List<IdeaBlock> blocks = stringToIdeaBlockConverter.convert(stringToConvert);
        for(IdeaBlock block : blocks) {
            System.out.println(block.toHexString());
        }

        //encrypting
        System.out.println("Encrypted blocks:");
        List<IdeaBlock> encryptedBlocks = new ArrayList<IdeaBlock>();
        for(IdeaBlock blockToEncrypt : blocks) {
            IdeaBlock encryptedBlock = idea.encrypt(blockToEncrypt);

            encryptedBlocks.add(encryptedBlock);
            System.out.println(encryptedBlock.toHexString() + " ");
        }

        return encryptedBlocks;
    }

    public static String decrypt(Idea idea, List<IdeaBlock> encryptedBlocks) {
        //decryptings
        StringBuilder stringOutput = new StringBuilder();
        System.out.println("Decrypted blocks:");
        for(IdeaBlock blockToDecrypt : encryptedBlocks) {
            IdeaBlock decryptedBlock = idea.decrypt(blockToDecrypt);

            System.out.println(decryptedBlock.toHexString());
            stringOutput.append(decryptedBlock.getBitArray().toASCII());
        }

        System.out.println("Decrypted string: " + stringOutput.toString());
        return stringOutput.toString();
    }
}
