package com.ensate.chatapp.utils;

import java.io.*;

public class FileUtils {

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException, IOException {
        if (new File(fileName).exists()) {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object loadedObject = ois.readObject();
            ois.close();
            fis.close();
            return loadedObject;
        } 
        new File(fileName).createNewFile();
        return null;
    }

    public static void serialize(Object obj, String fileName) throws IOException{
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.flush();
        fos.close();
    }
}
