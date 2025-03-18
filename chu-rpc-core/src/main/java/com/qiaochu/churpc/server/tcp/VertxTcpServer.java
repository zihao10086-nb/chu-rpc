package com.qiaochu.churpc.server.tcp;

import com.qiaochu.churpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {


    private byte[] handleRequest(byte[] requestData){
        //编写请求逻辑，根据requestData返回响应数据
        return "hello,client".getBytes();

    }
    @Override
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();
        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(socket -> {
            //处理连接
            socket.handler(buffer -> {
                //处理接收到的字节数组
                byte[] requestData = buffer.getBytes();
                //进行自定义的处理逻辑
                byte[] responseData = handleRequest(requestData);
                //发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });
        //启动服务并监听特定端口
        server.listen(port, result -> {
            if (result.succeeded()){
                System.out.println("TCP Server started on port " + port);
            }else {
                System.out.println("TCP Server start failed"+result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
