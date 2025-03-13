package com.qiaochu.churpc.server;


import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();
        //创建HTTP服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        //监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());
        //启动HTTP服务器,并监听指定端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("HTTP Server started on port " + port);
            }else {
                System.out.println("HTTP Server start failed: " + result.cause());
            }
        });

    }
}
