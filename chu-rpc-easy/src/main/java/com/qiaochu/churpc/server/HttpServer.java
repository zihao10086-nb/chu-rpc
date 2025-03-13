package com.qiaochu.churpc.server;

/**
 * HTTP 服务器接口
 */
public interface HttpServer {

    /**
     * 启动 HTTP 服务器
     * @param port
     */
    void doStart(int port);
}
