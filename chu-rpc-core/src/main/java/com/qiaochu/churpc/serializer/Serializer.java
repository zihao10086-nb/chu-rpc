package com.qiaochu.churpc.serializer;

import java.io.IOException;

/**
 * 序列化器接口
 */
public interface Serializer {


    /**
     * 序列化对象
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
