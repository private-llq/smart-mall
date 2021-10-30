package com.jsy.basic.util;

import java.awt.print.Book;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class StreamUtils {

    /**
     * 序列化,List
     */
    public static <T> boolean writeObject(List<T> list, List<T> obj) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(obj);
//        byte[] array2 = byteArrayOutputStream.toByteArray();
        T[] array = (T[]) list.toArray();
        objectOutputStream.writeObject(array);

//        T[] array = (T[]) list.toArray();
//        try(ObjectOutputStream out = new ObjectOutputStream(new OutputStream))
//        {
//            out.writeObject(array);
//            out.flush();
//            return true;
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }

    /**
     * 反序列化,List
     */
    public static <E> List<E> readObjectForList(File file)
    {
        E[] object;
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file)))
        {
            object = (E[]) out.readObject();
            return Arrays.asList(object);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
}