package com.qiaochu.churpc.serializer;

import java.io.*;


/**
 * JDK序列化器
 */
public class JdkSerializer implements Serializer {
    /**
     * 序列化对象
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
