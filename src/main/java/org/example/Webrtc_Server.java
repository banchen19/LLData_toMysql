package org.example;

import org.example.bds_sers.Message_Utils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Webrtc_Server extends WebSocketServer {


    public Webrtc_Server(int PORT) {
        super(new InetSocketAddress(PORT));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println(webSocket.getRemoteSocketAddress().toString()+"请求连接");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            Message_Utils.Message(webSocket,message);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("ws服务端启动完成");
    }
}


