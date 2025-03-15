package com.qiaochu.churpc.serializer;

import com.qiaochu.churpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工厂类
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
