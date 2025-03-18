package com.qiaochu.churpc.protocol;

/**
 * 协议常量
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH =12;
    /**
     * 消息魔数
     */
    byte MESSAGE_MAGIC=0x1;
    /**
     * 协议版本号
     */
    byte MESSAGE_VERSION=0x1;
}
